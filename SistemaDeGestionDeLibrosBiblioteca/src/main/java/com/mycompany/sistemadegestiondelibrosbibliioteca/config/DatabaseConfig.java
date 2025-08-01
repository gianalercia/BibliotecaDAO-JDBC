package com.mycompany.sistemadegestiondelibrosbibliioteca.config;

import java.sql.*;

/**
 * DatabaseConfig - Configuración JDBC con SQLite
 * Adaptado para tu estructura de proyecto
 */
public class DatabaseConfig {

    // Configuración para SQLite
    private static final String URL = "jdbc:sqlite:biblioteca.db";

    private static Connection conexion = null;
    private static boolean inicializado = false;

    /**
     * Inicializar conexión JDBC y crear base de datos SQLite
     */
    public static void inicializar() throws SQLException {
        // Evitar doble inicialización
        if (inicializado && conexion != null && !conexion.isClosed()) {
            return;
        }

        System.out.println("🔌 Conectando a SQLite con JDBC...");

        try {
            // Cargar driver JDBC de SQLite
            Class.forName("org.sqlite.JDBC");

            // Establecer conexión
            conexion = DriverManager.getConnection(URL);
            System.out.println("✅ Conexión JDBC establecida");
            System.out.println("📁 Base de datos: biblioteca.db");

            // Crear tabla
            crearTabla();

            inicializado = true;

        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver SQLite no encontrado", e);
        }
    }

    /**
     * Crear tabla libros
     */
    private static void crearTabla() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS libros (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT NOT NULL,
                autor TEXT NOT NULL,
                ano_publicacion INTEGER NOT NULL,
                disponible BOOLEAN DEFAULT true
            )
        """;

        System.out.println("🏗️ Creando tabla con SQL...");

        try (Statement stmt = conexion.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Tabla 'libros' creada/verificada en SQLite");
        }
    }

    /**
     * Obtener conexión JDBC
     */
    public static Connection getConexion() throws SQLException {
        // Si no está inicializada, inicializar automáticamente
        if (!inicializado || conexion == null || conexion.isClosed()) {
            inicializar();
        }
        return conexion;
    }

    /**
     * Mostrar estado de la base de datos
     */
    public static void mostrarEstadoBaseDatos() throws SQLException {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='libros'";

        try (Statement stmt = getConexion().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                System.out.println("📋 Tabla 'libros' existe en SQLite");

                // Contar registros
                try (ResultSet rsCount = stmt.executeQuery("SELECT COUNT(*) FROM libros")) {
                    if (rsCount.next()) {
                        int count = rsCount.getInt(1);
                        System.out.println("📊 Registros existentes: " + count);
                    }
                }
            } else {
                System.out.println("❌ Tabla 'libros' no existe");
            }
        }
    }

    /**
     * Cerrar conexión JDBC
     */
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("🔒 Conexión JDBC cerrada");
                System.out.println("💾 Datos persistidos en biblioteca.db");
                inicializado = false;
            }
        } catch (SQLException e) {
            System.err.println("Error cerrando conexión: " + e.getMessage());
        }
    }
}