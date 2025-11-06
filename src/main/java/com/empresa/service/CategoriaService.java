package main.java.com.empresa.service;

import java.sql.SQLException;
import java.util.List;

import main.java.com.empresa.dao.CategoriaDAO;
import main.java.com.empresa.model.Categoria;
import main.java.com.empresa.util.LogUtil;

public class CategoriaService {

    private final CategoriaDAO categoriaDAO;

    public CategoriaService() {
        this.categoriaDAO = new CategoriaDAO();
    }

    public void crearCategoria(Categoria categoria) {
        try {
            categoriaDAO.insertar(categoria);
            LogUtil.info("Categoría creada: " + categoria.getNombre());
        } catch (SQLException e) {
            LogUtil.error("Error creando categoría: " + e.getMessage());
        }
    }

    public List<Categoria> obtenerTodas() {
        try {
            return categoriaDAO.obtenerTodas();
        } catch (SQLException e) {
            LogUtil.error("Error obteniendo categorías: " + e.getMessage());
            return List.of();
        }
    }

    public void actualizarCategoria(Categoria categoria) {
        try {
            categoriaDAO.actualizar(categoria);
            LogUtil.info("Categoría actualizada: " + categoria.getId());
        } catch (SQLException e) {
            LogUtil.error("Error actualizando categoría: " + e.getMessage());
        }
    }

    public void eliminarCategoria(int id) {
        try {
            categoriaDAO.eliminar(id);
            LogUtil.info("Categoría eliminada: " + id);
        } catch (SQLException e) {
            LogUtil.error("Error eliminando categoría: " + e.getMessage());
        }
    }
}
