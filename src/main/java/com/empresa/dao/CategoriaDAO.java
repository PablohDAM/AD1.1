package main.java.com.empresa.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import main.java.com.empresa.db.DBConnection;
import main.java.com.empresa.model.Categoria;

public class CategoriaDAO {

    public void insertar(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categoria (nombre) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNombre());
            stmt.executeUpdate();
        }
    }

    public List<Categoria> obtenerTodas() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id, nombre FROM categoria";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Categoria c = new Categoria(
                        rs.getInt("id"),
                        rs.getString("nombre")
                );
                categorias.add(c);
            }
        }
        return categorias;
    }

    public void actualizar(Categoria categoria) throws SQLException {
        String sql = "UPDATE categoria SET nombre = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNombre());
            stmt.setInt(2, categoria.getId());
            stmt.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
    
    public int obtenerOCrearCategoria(String nombreCategoria) throws SQLException {
        int categoriaId = -1;

        String sqlSelect = "SELECT id FROM categoria WHERE nombre = ?";
        String sqlInsert = "INSERT INTO categoria (nombre) VALUES (?)";

        try (Connection conn = DBConnection.getConnection()) {
            // Buscar categoría
            try (PreparedStatement psSelect = conn.prepareStatement(sqlSelect)) {
                psSelect.setString(1, nombreCategoria);
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id");  // Si existe, retornar id
                    }
                }
            }

            // Si no existe, insertar
            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                psInsert.setString(1, nombreCategoria);
                psInsert.executeUpdate();

                try (ResultSet rsKeys = psInsert.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        categoriaId = rsKeys.getInt(1); // Obtener id generado
                    }
                }
            }
        }

        return categoriaId;
    }
    
}
