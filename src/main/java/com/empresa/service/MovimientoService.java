package main.java.com.empresa.service;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.com.empresa.db.DBConnection;
import main.java.com.empresa.util.LogUtil;

public class MovimientoService {

    /**
     * Registra un movimiento de inventario
     * @param productoId ID del producto
     * @param tipo "ENTRADA" o "SALIDA"
     * @param cantidad cantidad del movimiento
     */
    public void registrarMovimiento(int productoId, String tipo, int cantidad) {
        // Validar tipo
        if (!tipo.equals("ENTRADA") && !tipo.equals("SALIDA")) {
            LogUtil.error("Tipo de movimiento inválido: " + tipo);
            return;
        }

        // Validar cantidad
        if (cantidad <= 0) {
            LogUtil.warn("Cantidad de movimiento inválida (<=0) para producto " + productoId);
            return;
        }

        String sql = "INSERT INTO movimiento (producto_id, tipo, cantidad) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productoId);
            stmt.setString(2, tipo);
            stmt.setInt(3, cantidad);

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                LogUtil.info("Movimiento registrado: productoId=" + productoId + ", tipo=" + tipo + ", cantidad=" + cantidad);
            } else {
                LogUtil.warn("No se registró movimiento para productoId=" + productoId);
            }

        } catch (SQLException e) {
            LogUtil.error("Error registrando movimiento para productoId=" + productoId + ": " + e.getMessage());
        }
    }

    /**
     * Registra el stock inicial de un producto como ENTRADA
     * @param productoId ID del producto
     * @param stock cantidad inicial
     */
    public void registrarStockInicial(int productoId, int stock) {
        if (stock > 0) {
            registrarMovimiento(productoId, "ENTRADA", stock);
        } else {
            LogUtil.warn("Stock inicial cero para productoId=" + productoId);
        }
    }

    public void registrarEntrada(int idProducto, int cantidad) {
        String insertMovimiento = "INSERT INTO movimiento (producto_id, tipo, cantidad) VALUES (?, 'ENTRADA', ?)";
        String updateStock = "UPDATE producto SET stock = stock + ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // 1️ Insertamos movimiento
            try (PreparedStatement stmtMov = conn.prepareStatement(insertMovimiento)) {
                stmtMov.setInt(1, idProducto);
                stmtMov.setInt(2, cantidad);
                stmtMov.executeUpdate();
            }

            // 2️ Actualizamos stock
            try (PreparedStatement stmtStock = conn.prepareStatement(updateStock)) {
                stmtStock.setInt(1, cantidad);
                stmtStock.setInt(2, idProducto);
                stmtStock.executeUpdate();
            }

            conn.commit();
            LogUtil.info("Entrada registrada. Producto " + idProducto + " +" + cantidad);

        } catch (SQLException e) {
            LogUtil.error("Error registrando entrada: " + e.getMessage());
        }
    }

    public void registrarSalida(int idProducto, int cantidad) {
        String insertMovimiento = "INSERT INTO movimiento (producto_id, tipo, cantidad) VALUES (?, 'SALIDA', ?)";
        // Usamos GREATEST() para asegurar que el stock no quede negativo.
        String updateStock = "UPDATE producto SET stock = GREATEST(stock - ?, 0) WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // 1️ Validar parámetros
            if (cantidad <= 0) {
                LogUtil.warn("Cantidad inválida (" + cantidad + ") para salida de producto " + idProducto);
                return;
            }

            // 2️ Verificar stock disponible
            if (!hayStockSuficiente(conn, idProducto, cantidad)) {
                LogUtil.warn("Stock insuficiente para producto " + idProducto);
                conn.rollback();
                return;
            }

            // 3️ Registrar movimiento
            try (PreparedStatement stmtMov = conn.prepareStatement(insertMovimiento)) {
                stmtMov.setInt(1, idProducto);
                stmtMov.setInt(2, cantidad);
                stmtMov.executeUpdate();
            }

            // 4️ Actualizar stock de forma segura
            try (PreparedStatement stmtStock = conn.prepareStatement(updateStock)) {
                stmtStock.setInt(1, cantidad);
                stmtStock.setInt(2, idProducto);
                stmtStock.executeUpdate();
            }

            conn.commit();
            LogUtil.info("Salida registrada correctamente. Producto " + idProducto + " - Cantidad: " + cantidad);

        } catch (SQLException e) {
            LogUtil.error("Error registrando salida: " + e.getMessage());
            try {
                // rollback seguro si hubo error después de desactivar autoCommit
                DBConnection.getConnection().rollback();
            } catch (SQLException ex) {
                LogUtil.error("Error realizando rollback: " + ex.getMessage());
            }
        }
    }


    // === MÉTODO AUXILIAR ===
    private boolean hayStockSuficiente(Connection conn, int idProducto, int cantidad) throws SQLException {
        String sql = "SELECT stock FROM producto WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int stockActual = rs.getInt("stock");
                    return stockActual >= cantidad;
                }
            }
        }
        return false;
    }
}
