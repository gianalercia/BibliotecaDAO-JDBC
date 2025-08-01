/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao;

/**
 *
 * @author gian_
 */
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity.Libro;
import com.mycompany.sistemadegestiondelibrosbibliioteca.config.DatabaseConfig
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * LibroDAO - Data Access Object
 */
/**
 * LibroDAO - Data Access Object para SQLite
 *
 * ¿QUÉ ES DAO?
 * - Patrón de diseño que encapsula el acceso a datos
 * - Separa la lógica de negocio de la persistencia
 * - Define operaciones CRUD: Create, Read, Update, Delete
 *
 * ¿CÓMO USA JDBC?
 * - DAO define QUÉ operaciones hacer (guardar, buscar, etc.)
 * - JDBC define CÓMO acceder a SQLite
 * - DAO usa JDBC internamente para ejecutar SQL
 */
public class LibroDAO {

    /**
     * OPERACIÓN: CREATE (Guardar libro)
     * SQL: INSERT INTO libros
     * JDBC: PreparedStatement para evitar SQL injection
     * SQLite: AUTOINCREMENT para ID automático
     */
    public void guardar(Libro libro) throws SQLException {
        String sql = "INSERT INTO libros (titulo, autor, año) VALUES (?, ?, ?)";

        System.out.println("🔧 DAO ejecutando INSERT en SQLite...");
        System.out.println("SQL: " + sql);

        // PreparedStatement: JDBC seguro con parámetros
        try (PreparedStatement pstmt = DatabaseConfig.getConexion().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {

            // Establecer parámetros (evita SQL injection)
            pstmt.setString(1, libro.getTitulo());
            pstmt.setString(2, libro.getAutor());
            pstmt.setInt(3, libro.getAño());

            // Ejecutar INSERT en SQLite
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                // Obtener ID generado automáticamente por SQLite
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        libro.setId(rs.getInt(1));
                        System.out.println("✅ JDBC: Libro guardado en SQLite con ID " + libro.getId());
                    }
                }
            }
        }
    }

    /**
     * OPERACIÓN: READ (Buscar por ID)
     * SQL: SELECT con WHERE
     * JDBC: PreparedStatement + ResultSet
     * SQLite: Búsqueda por PRIMARY KEY
     */
    public Libro buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, titulo, autor, año FROM libros WHERE id = ?";

        System.out.println("🔍 DAO ejecutando SELECT en SQLite...");
        System.out.println("SQL: " + sql + " [id=" + id + "]");

        try (PreparedStatement pstmt = DatabaseConfig.getConexion().prepareStatement(sql)) {

            // Parámetro seguro
            pstmt.setInt(1, id);

            // Ejecutar SELECT en SQLite
            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    // Mapear ResultSet a objeto Libro
                    Libro libro = new Libro();
                    libro.setId(rs.getInt("id"));
                    libro.setTitulo(rs.getString("titulo"));
                    libro.setAutor(rs.getString("autor"));
                    libro.setAño(rs.getInt("año"));

                    System.out.println("✅ JDBC: Libro encontrado en SQLite");
                    return libro;
                } else {
                    System.out.println("❌ JDBC: Libro no encontrado en SQLite");
                    return null;
                }
            }
        }
    }

    /**
     * OPERACIÓN: READ ALL (Listar todos)
     * SQL: SELECT sin WHERE + ORDER BY
     * JDBC: Statement + ResultSet
     * SQLite: Orden por ID ascendente
     */
    public List<Libro> listarTodos() throws SQLException {
        String sql = "SELECT id, titulo, autor, año FROM libros ORDER BY id";
        List<Libro> libros = new ArrayList<>();

        System.out.println("📋 DAO ejecutando SELECT ALL en SQLite...");
        System.out.println("SQL: " + sql);

        try (Statement stmt = DatabaseConfig.getConexion().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Libro libro = new Libro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getInt("año")
                );
                libros.add(libro);
            }

            System.out.println("✅ JDBC: " + libros.size() + " libros encontrados en SQLite");
        }

        return libros;
    }

    /**
     * OPERACIÓN: COUNT (Contar registros)
     * SQL: SELECT COUNT(*)
     * JDBC: Statement + ResultSet
     * SQLite: Función de agregación COUNT
     */
    public int contarTodos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM libros";

        System.out.println("🔢 DAO ejecutando COUNT en SQLite...");
        System.out.println("SQL: " + sql);

        try (Statement stmt = DatabaseConfig.getConexion().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("✅ JDBC: Total de registros en SQLite = " + count);
                return count;
            }
        }

        return 0;
    }

    /**
     * OPERACIÓN: UPDATE (Actualizar libro)
     * SQL: UPDATE con WHERE
     * JDBC: PreparedStatement
     * SQLite: Actualización por PRIMARY KEY
     */
    public void actualizar(Libro libro) throws SQLException {
        String sql = "UPDATE libros SET titulo = ?, autor = ?, año = ? WHERE id = ?";

        System.out.println("📝 DAO ejecutando UPDATE en SQLite...");
        System.out.println("SQL: " + sql + " [id=" + libro.getId() + "]");

        try (PreparedStatement pstmt = DatabaseConfig.getConexion().prepareStatement(sql)) {
            pstmt.setString(1, libro.getTitulo());
            pstmt.setString(2, libro.getAutor());
            pstmt.setInt(3, libro.getAño());
            pstmt.setInt(4, libro.getId());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("✅ JDBC: Libro actualizado en SQLite");
            } else {
                System.out.println("❌ JDBC: Libro no encontrado para actualizar");
            }
        }
    }

    /**
     * OPERACIÓN: DELETE (Eliminar por ID)
     * SQL: DELETE con WHERE
     * JDBC: PreparedStatement
     * SQLite: Eliminación por PRIMARY KEY
     */
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM libros WHERE id = ?";

        System.out.println("🗑️ DAO ejecutando DELETE en SQLite...");
        System.out.println("SQL: " + sql + " [id=" + id + "]");

        try (PreparedStatement pstmt = DatabaseConfig.getConexion().prepareStatement(sql)) {
            pstmt.setInt(1, id);

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("✅ JDBC: Libro eliminado de SQLite");
                return true;
            } else {
                System.out.println("❌ JDBC: Libro no encontrado para eliminar");
                return false;
            }
        }
    }
}