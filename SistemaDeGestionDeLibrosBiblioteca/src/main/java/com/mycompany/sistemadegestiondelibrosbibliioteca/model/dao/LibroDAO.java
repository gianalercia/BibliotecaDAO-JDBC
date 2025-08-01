/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao;

/**
 *
 * @author gian_
 */
import com.mycompany.sistemadegestiondelibrosbibliioteca.config.DatabaseConfig;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity.Libro;
import java.sql.*;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

/**
 * LibroDAO - Data Access Object con JDBC y SQLite
 * Corregido para funcionar con la estructura existente
 */
public class LibroDAO {

    /**
     * Buscar libro por ID usando JDBC
     */
    public Optional<Libro> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        String sql = "SELECT id, titulo, autor, ano_publicacion, disponible FROM libros WHERE id = ?";

        System.out.println("🔍 DAO ejecutando SELECT en SQLite...");
        System.out.println("SQL: " + sql + " [id=" + id + "]");

        try (Connection conn = DatabaseConfig.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Libro libro = new Libro();
                    libro.setId(rs.getLong("id"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setAutor(rs.getString("autor"));
                    libro.setAnoPublicacion(rs.getInt("ano_publicacion")); // Usar el método correcto
                    libro.setDisponible(rs.getBoolean("disponible"));

                    System.out.println("✅ JDBC: Libro encontrado en SQLite");
                    return Optional.of(libro);
                } else {
                    System.out.println("❌ JDBC: Libro no encontrado en SQLite");
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error JDBC en findById: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Guardar libro usando JDBC
     */
    public Libro save(Libro libro) {
        if (libro.getId() == null) {
            return insertarNuevoLibro(libro);
        } else {
            return actualizarLibro(libro);
        }
    }

    /**
     * Insertar nuevo libro
     */
    private Libro insertarNuevoLibro(Libro libro) {
        String sql = "INSERT INTO libros (titulo, autor, ano_publicacion, disponible) VALUES (?, ?, ?, ?)";

        System.out.println("🔧 DAO ejecutando INSERT en SQLite...");
        System.out.println("SQL: " + sql);

        try (Connection conn = DatabaseConfig.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, libro.getTitulo());
            pstmt.setString(2, libro.getAutor());
            pstmt.setInt(3, libro.getAnoPublicacion());
            pstmt.setBoolean(4, libro.getDisponible());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        libro.setId(rs.getLong(1));
                        System.out.println("✅ JDBC: Libro guardado en SQLite con ID " + libro.getId());
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error JDBC en save: " + e.getMessage());
            throw new RuntimeException("Error al guardar libro: " + e.getMessage());
        }

        return libro;
    }

    /**
     * Actualizar libro existente
     */
    private Libro actualizarLibro(Libro libro) {
        String sql = "UPDATE libros SET titulo = ?, autor = ?, ano_publicacion = ?, disponible = ? WHERE id = ?";

        System.out.println("📝 DAO ejecutando UPDATE en SQLite...");
        System.out.println("SQL: " + sql + " [id=" + libro.getId() + "]");

        try (Connection conn = DatabaseConfig.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, libro.getTitulo());
            pstmt.setString(2, libro.getAutor());
            pstmt.setInt(3, libro.getAnoPublicacion());
            pstmt.setBoolean(4, libro.getDisponible());
            pstmt.setLong(5, libro.getId());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("✅ JDBC: Libro actualizado en SQLite");
            } else {
                System.out.println("❌ JDBC: Libro no encontrado para actualizar");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error JDBC en actualizar: " + e.getMessage());
            throw new RuntimeException("Error al actualizar libro: " + e.getMessage());
        }

        return libro;
    }

    /**
     * Contar total de libros
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM libros";

        System.out.println("🔢 DAO ejecutando COUNT en SQLite...");
        System.out.println("SQL: " + sql);

        try (Connection conn = DatabaseConfig.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("✅ JDBC: Total de registros en SQLite = " + count);
                return count;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error JDBC en count: " + e.getMessage());
        }

        return 0;
    }

    /**
     * Obtener todos los libros
     */
    public List<Libro> findAll() {
        String sql = "SELECT id, titulo, autor, ano_publicacion, disponible FROM libros ORDER BY id";
        List<Libro> libros = new ArrayList<>();

        System.out.println("📋 DAO ejecutando SELECT ALL en SQLite...");
        System.out.println("SQL: " + sql);

        try (Connection conn = DatabaseConfig.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Libro libro = new Libro();
                libro.setId(rs.getLong("id"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setAutor(rs.getString("autor"));
                libro.setAnoPublicacion(rs.getInt("ano_publicacion"));
                libro.setDisponible(rs.getBoolean("disponible"));
                libros.add(libro);
            }

            System.out.println("✅ JDBC: " + libros.size() + " libros encontrados en SQLite");

        } catch (SQLException e) {
            System.err.println("❌ Error JDBC en findAll: " + e.getMessage());
        }

        return libros;
    }
}