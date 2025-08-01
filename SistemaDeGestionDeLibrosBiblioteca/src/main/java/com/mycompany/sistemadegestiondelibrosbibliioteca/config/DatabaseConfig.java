package com.mycompany.sistemadegestiondelibrosbibliioteca.config;

import java.io.File;
import java.sql.*;

/**
 * DatabaseConfig - Configuración JDBC con SQLite
 */
public class DatabaseConfig {

    private static final String DB_FILE = "biblioteca.db";
    private static final String URL = "jdbc:sqlite:" + DB_FILE;

    private static Connection conexion = null;
    private static boolean inicializado = false;

    /**
     * Inicializar base de datos
     */
    public static void inicializar() throws SQLException {
        if (inicializado && conexion != null && !conexion.isClosed()) {
            return;
        }

        // Verificar si la BD ya existe
        boolean bdExiste = verificarBaseDatos();

        try {
            // Cargar driver SQLite
            Class.forName("org.sqlite.JDBC");

            // Establecer conexión
            conexion = DriverManager.getConnection(URL);

            if (!bdExiste) {
                crearEstructura();
                System.out.println("Base de datos creada: " + DB_FILE);
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
        return dbFile.exists() && dbFile.length() > 0;
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

        try (Statement stmt = conexion.createStatement()) {
            stmt.execute(sql);
            crearIndices();
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
                inicializado = false;
            }
        } catch (SQLException e) {
            System.err.println("Error cerrando conexión: " + e.getMessage());
        }
    }
}