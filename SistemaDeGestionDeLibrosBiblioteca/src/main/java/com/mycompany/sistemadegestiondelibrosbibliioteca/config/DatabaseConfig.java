package com.mycompany.sistemadegestiondelibrosbibliioteca.config;

import java.io.File;
import java.sql.*;

/**
 * DatabaseConfig - Configuración JDBC con SQLite
 *
 * CARACTERÍSTICAS:
 * - Validación si BD ya existe
 * - Creación automática si no existe
 * - Inicialización segura
 * - Gestión de conexión singleton
 */
public class DatabaseConfig {

    private static final String DB_FILE = "biblioteca.db";
    private static final String URL = "jdbc:sqlite:" + DB_FILE;

    private static Connection conexion = null;
    private static boolean inicializado = false;

    /**
     * Inicializar base de datos
     * Verifica si existe, si no la crea
     */
    public static void inicializar() throws SQLException {
        if (inicializado && conexion != null && !conexion.isClosed()) {
            System.out.println("ℹ️ Base de datos ya inicializada");
            return;
        }

        // Verificar si la BD ya existe
        boolean bdExiste = verificarBaseDatos();

        System.out.println("🔌 Inicializando conexión JDBC...");

        try {
            // Cargar driver SQLite
            Class.forName("org.sqlite.JDBC");

            // Establecer conexión
            conexion = DriverManager.getConnection(URL);
            System.out.println("✅ Conexión JDBC establecida");
            System.out.println("📁 Archivo BD: " + DB_FILE);

            if (!bdExiste) {
                System.out.println("🆕 Base de datos nueva - Creando estructura...");
                crearEstructura();
            } else {
                System.out.println("📊 Base de datos existente - Verificando estructura...");
                verificarEstructura();
            }

            inicializado = true;

        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver SQLite no encontrado", e);
        }
    }

    /**
     * Verificar si el archivo de BD existe
     */
    private static boolean verificarBaseDatos() {
        File dbFile = new File(DB_FILE);
        boolean existe = dbFile.exists() && dbFile.length() > 0;

        if (existe) {
            System.out.println("📋 Base de datos encontrada: " + DB_FILE + " (" + dbFile.length() + " bytes)");
        } else {
            System.out.println("🆕 Base de datos no existe - Se creará: " + DB_FILE);
        }

        return existe;
    }

    /**
     * Crear estructura completa de la BD
     */
    private static void crearEstructura() throws SQLException {
        String sql = """
            CREATE TABLE libros (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT NOT NULL,
                autor TEXT NOT NULL,
                ano_publicacion INTEGER NOT NULL,
                disponible BOOLEAN DEFAULT 1,
                fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
                UNIQUE(titulo, autor)
            )
        """;

        System.out.println("🏗️ Creando tabla 'libros'...");
        System.out.println("📝 SQL: " + sql.replaceAll("\\s+", " ").trim());

        try (Statement stmt = conexion.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Tabla 'libros' creada exitosamente");

            // Crear índices para optimización
            crearIndices();
        }
    }

    /**
     * Verificar estructura existente
     */
    private static void verificarEstructura() throws SQLException {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='libros'";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                System.out.println("✅ Tabla 'libros' verificada");
                mostrarEstadisticas();
            } else {
                System.out.println("⚠️ Tabla 'libros' no encontrada - Creando...");
                crearEstructura();
            }
        }
    }

    /**
     * Crear índices para optimización
     */
    private static void crearIndices() throws SQLException {
        String[] indices = {
                "CREATE INDEX IF NOT EXISTS idx_titulo ON libros(titulo)",
                "CREATE INDEX IF NOT EXISTS idx_autor ON libros(autor)",
                "CREATE INDEX IF NOT EXISTS idx_titulo_autor ON libros(titulo, autor)"
        };

        try (Statement stmt = conexion.createStatement()) {
            for (String indice : indices) {
                stmt.execute(indice);
            }
            System.out.println("🔍 Índices creados para optimización");
        }
    }

    /**
     * Mostrar estadísticas de la BD
     */
    private static void mostrarEstadisticas() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM libros";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                int total = rs.getInt("total");
                System.out.println("📊 Libros en BD: " + total);
            }
        }
    }

    /**
     * Obtener conexión activa
     */
    public static Connection getConexion() throws SQLException {
        if (!inicializado || conexion == null || conexion.isClosed()) {
            inicializar();
        }
        return conexion;
    }

    /**
     * Cerrar conexión
     */
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("🔒 Conexión JDBC cerrada");
                System.out.println("💾 Datos persistidos en: " + DB_FILE);
                inicializado = false;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error cerrando conexión: " + e.getMessage());
        }
    }

    /**
     * Verificar si la conexión está activa
     */
    public static boolean estaConectado() {
        try {
            return conexion != null && !conexion.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}