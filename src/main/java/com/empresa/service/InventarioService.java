package main.java.com.empresa.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.com.empresa.dao.ProductoDAO;
import main.java.com.empresa.model.Producto;
import main.java.com.empresa.util.CSVUtil;
import main.java.com.empresa.util.LogUtil;

public class InventarioService {

    private final ProductoDAO productoDAO;

    public InventarioService() {
        this.productoDAO = new ProductoDAO();
    }

    // === CRUD ===
    public void crearProducto(Producto producto) {
        try {
            productoDAO.insertarProducto(producto);
            LogUtil.info("Producto creado: " + producto.getNombre());
        } catch (SQLException e) {
            LogUtil.error("Error creando producto: " + e.getMessage());
        }
    }

    public List<Producto> obtenerTodos() {
        try {
            return productoDAO.obtenerTodos();
        } catch (SQLException e) {
            LogUtil.error("Error obteniendo productos: " + e.getMessage());
            return List.of();
        }
    }

    public void actualizarProducto(Producto producto) {
        try {
            productoDAO.actualizarProducto(producto);
            LogUtil.info("Producto actualizado: " + producto.getId_producto());
        } catch (SQLException e) {
            LogUtil.error("Error actualizando producto: " + e.getMessage());
        }
    }

    public void eliminarProducto(int id) {
        try {
            productoDAO.eliminarProducto(id);
            LogUtil.info("Producto eliminado: " + id);
        } catch (SQLException e) {
            LogUtil.error("Error eliminando producto: " + e.getMessage());
        }
    }

    // === CSV Import ===
    public void importarProductosDesdeCSV(String filePath) {
        List<Producto> productos = CSVUtil.leerProductos(filePath);

        for (Producto p : productos) {
            try {
                productoDAO.insertarProducto(p);
                LogUtil.info("Producto importado desde CSV: " + p.getNombre());
            } catch (SQLException e) {
                LogUtil.error("Error importando producto " + p.getNombre() + ": " + e.getMessage());
            }
        }
    }

    // === Filtro para exportación JSON ===
    public List<Producto> obtenerProductosConStockMenorA(int umbral) {
        List<Producto> resultado = new ArrayList<>();
        try {
            List<Producto> todos = productoDAO.obtenerTodos();
            for (Producto p : todos) {
                if (p.getStock() < umbral) {
                    resultado.add(p);
                }
            }
        } catch (SQLException e) {
            LogUtil.error("Error filtrando productos con bajo stock: " + e.getMessage());
        }
        return resultado;
    }
}
