package main.java.com.empresa.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import main.java.com.empresa.db.DBConnection;
import main.java.com.empresa.model.Producto;

public class ProductoDAO {

	public void insertarProducto(Producto producto) throws SQLException {
	    String sql = "INSERT INTO producto (nombre, categoria_id, stock, precio) VALUES (?, ?, ?, ?)";
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, producto.getNombre());
	        stmt.setInt(2, producto.getCategoriaId());
	        stmt.setInt(3, producto.getStock());
	        stmt.setDouble(4, producto.getPrecio());
	        stmt.executeUpdate();
	    }
	}


    public List<Producto> obtenerTodos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id, nombre, categoria_id, precio, stock FROM producto";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto p = new Producto(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getInt("categoria_id"),
                    rs.getDouble("precio"),
                    rs.getInt("stock")
                );
                productos.add(p);
            }
        }
        return productos;
    }

    public void actualizarProducto(Producto producto) throws SQLException {
        String sql = "UPDATE producto SET nombre = ?, categoria_id = ?, stock = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setInt(2, producto.getCategoriaId());
            stmt.setInt(3, producto.getStock());
            stmt.setInt(4, producto.getId_producto());
            stmt.executeUpdate();
        }
    }

    public void eliminarProducto(int id) throws SQLException {
        String sql = "DELETE FROM producto WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
