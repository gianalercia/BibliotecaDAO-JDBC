package com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao;

import com.mycompany.sistemadegestiondelibrosbibliioteca.config.DatabaseConfig;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity.Libro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibroDAOImpl implements ILibroDAO {

    /**
     * CREATE - Insertar nuevo libro
     */
    @Override
    public Libro create(Libro libro) {
        // Sanitizar datos
        String tituloSanitizado = sanitizarTexto(libro.getTitulo());
        String autorSanitizado = sanitizarTexto(libro.getAutor());

        // Validar duplicados
        if (exists(tituloSanitizado, autorSanitizado)) {
            throw new RuntimeException("El libro '" + tituloSanitizado + "' de " + autorSanitizado + " ya existe");
        }

        String sql = "INSERT INTO libros (titulo, autor, ano_publicacion, disponible) VALUES (?, ?, ?, ?)";

        System.out.println("🔧 DAO CREATE: Insertando libro...");
        System.out.println("📝 SQL: " + sql);
        System.out.println("📚 Datos: " + tituloSanitizado + " - " + autorSanitizado + " (" + libro.getAnoPublicacion() + ")");

        try (Connection conn = DatabaseConfig.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Parámetros sanitizados
            pstmt.setString(1, tituloSanitizado);
            pstmt.setString(2, autorSanitizado);
            pstmt.setInt(3, libro.getAnoPublicacion());
            pstmt.setBoolean(4, libro.getDisponible() != null ? libro.getDisponible() : true);

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        libro.setId(rs.getLong(1));
                        libro.setTitulo(tituloSanitizado);
                        libro.setAutor(autorSanitizado);
                        System.out.println("✅ CREATE exitoso - ID generado: " + libro.getId());
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error en CREATE: " + e.getMessage());
            throw new RuntimeException("Error al crear libro: " + e.getMessage());
        }

        return libro;
    }

    /**
     * READ - Buscar libro por ID
     */
    @Override
    public Optional<Libro> read(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }

        String sql = "SELECT id, titulo, autor, ano_publicacion, disponible FROM libros WHERE id = ?";

        System.out.println("🔍 DAO READ: Buscando libro ID " + id);
        System.out.println("📝 SQL: " + sql);

        try (Connection conn = DatabaseConfig.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Libro libro = mapearResultSet(rs);
                    System.out.println("✅ READ exitoso - Libro encontrado: " + libro.getTitulo());
                    return Optional.of(libro);
                } else {
                    System.out.println("❌ READ - Libro no encontrado");
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ Error en READ: " + e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * UPDATE - Actualizar libro existente
     */
    @Override
    public Libro update(Libro libro) {
        if (libro.getId() == null) {
            throw new IllegalArgumentException("ID requerido para actualizar");
        }

        // Sanitizar datos
        String tituloSanitizado = sanitizarTexto(libro.getTitulo());
        String autorSanitizado = sanitizarTexto(libro.getAutor());

        String sql = "UPDATE libros SET titulo = ?, autor = ?, ano_publicacion = ?, disponible = ? WHERE id = ?";

        System.out.println("📝 DAO UPDATE: Actualizando libro ID " + libro.getId());
        System.out.println("📝 SQL: " + sql);
        System.out.println("📚 Nuevos datos: " + tituloSanitizado + " - " + autorSanitizado);

        try (Connection conn = DatabaseConfig.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tituloSanitizado);
            pstmt.setString(2, autorSanitizado);
            pstmt.setInt(3, libro.getAnoPublicacion());
            pstmt.setBoolean(4, libro.getDisponible() != null ? libro.getDisponible() : true);
            pstmt.setLong(5, libro.getId());

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                libro.setTitulo(tituloSanitizado);
                libro.setAutor(autorSanitizado);
                System.out.println("✅ UPDATE exitoso - Libro actualizado");
            } else {
                System.out.println("❌ UPDATE - Libro no encontrado para actualizar");
                throw new RuntimeException("Libro no encontrado para actualizar");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error en UPDATE: " + e.getMessage());
            throw new RuntimeException("Error al actualizar libro: " + e.getMessage());
        }

        return libro;
    }

    /**
     * DELETE - Eliminar libro por ID
     */
    @Override
    public boolean delete(Long id) {
        if (id == null || id <= 0) {
            return false;
        }

        String sql = "DELETE FROM libros WHERE id = ?";

        System.out.println("🗑️ DAO DELETE: Eliminando libro ID " + id);
        System.out.println("📝 SQL: " + sql);

        try (Connection conn = DatabaseConfig.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("✅ DELETE exitoso - Libro eliminado");
                return true;
            } else {
                System.out.println("❌ DELETE - Libro no encontrado");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error en DELETE: " + e.getMessage());
            return false;
        }
    }

    /**
     * READ ALL - Obtener todos los libros
     */
    @Override
    public List<Libro> readAll() {
        String sql = "SELECT id, titulo, autor, ano_publicacion, disponible FROM libros ORDER BY id";
        List<Libro> libros = new ArrayList<>();

        System.out.println("📋 DAO READ ALL: Obteniendo todos los libros");
        System.out.println("📝 SQL: " + sql);

        try (Connection conn = DatabaseConfig.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Libro libro = mapearResultSet(rs);
                libros.add(libro);
            }

            System.out.println("✅ READ ALL exitoso - " + libros.size() + " libros encontrados");

        } catch (SQLException e) {
            System.err.println("❌ Error en READ ALL: " + e.getMessage());
        }

        return libros;
    }

    /**
     * EXISTS - Verificar si existe libro con título y autor
     */
    @Override
    public boolean exists(String titulo, String autor) {
        String tituloSanitizado = sanitizarTexto(titulo);
        String autorSanitizado = sanitizarTexto(autor);

        String sql = "SELECT 1 FROM libros WHERE LOWER(TRIM(titulo)) = LOWER(TRIM(?)) AND LOWER(TRIM(autor)) = LOWER(TRIM(?))";

        try (Connection conn = DatabaseConfig.getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tituloSanitizado);
            pstmt.setString(2, autorSanitizado);

            try (ResultSet rs = pstmt.executeQuery()) {
                boolean existe = rs.next();
                if (existe) {
                    System.out.println("⚠️ DUPLICADO DETECTADO: " + tituloSanitizado + " - " + autorSanitizado);
                }
                return existe;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error verificando duplicados: " + e.getMessage());
            return false;
        }
    }

    // ========================================================================
    // MÉTODOS AUXILIARES
    // ========================================================================

    /**
     * Mapear ResultSet a objeto Libro
     */
    private Libro mapearResultSet(ResultSet rs) throws SQLException {
        Libro libro = new Libro();
        libro.setId(rs.getLong("id"));
        libro.setTitulo(rs.getString("titulo"));
        libro.setAutor(rs.getString("autor"));
        libro.setAnoPublicacion(rs.getInt("ano_publicacion"));
        libro.setDisponible(rs.getBoolean("disponible"));
        return libro;
    }

    /**
     * Sanitizar texto de entrada
     * Elimina caracteres peligrosos y normaliza
     */
    private String sanitizarTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return "";
        }

        return texto.trim()
                .replaceAll("\\s+", " ")              // Espacios múltiples → uno solo
                .replaceAll("[<>\"'&]", "")           // Caracteres peligrosos
                .replaceAll("(?i)(script|select|insert|update|delete|drop|create|alter)", ""); // SQL keywords
    }
}