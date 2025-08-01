package com.mycompany.sistemadegestiondelibrosbibliioteca.config;

import java.sql.*;

/**
 * DatabaseConfig - Configuración JDBC con SQLite
 *
 * JDBC (Java Database Connectivity):
 * - API estándar de Java para conectar con bases de datos
 * - Permite ejecutar consultas SQL desde Java
 * - SQLite: Base de datos en archivo, sin servidor
 */
public class DatabaseConfig {

    // Configuración para SQLite (crea archivo biblioteca.db)
    private static final String URL = "jdbc:sqlite:biblioteca.db";

    private static Connection conexion = null;

    /**
     * Inicializar conexión JDBC y crear base de datos SQLite
     */
    public static void inicializar() throws SQLException {
        System.out.println("🔌 Conectando a SQLite con JDBC...");

        try {
            // 1. Cargar driver JDBC de SQLite
            Class.forName("org.sqlite.JDBC");

            // 2. Establecer conexión usando JDBC
            // SQLite crea automáticamente el archivo si no existe
            conexion = DriverManager.getConnection(URL);
            System.out.println("✅ Conexión JDBC establecida");
            System.out.println("📁 Base de datos: biblioteca.db (archivo creado)");

            // 3. Crear tabla usando SQL
            crearTabla();

        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver SQLite no encontrado", e);
        }
    }

    /**
     * Crear tabla libros usando JDBC y SQL
     */
    private static void crearTabla() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS libros (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT NOT NULL,
                autor TEXT NOT NULL,
                año INTEGER NOT NULL
            )
        """;

        System.out.println("🏗️ Creando tabla con SQL...");
        System.out.println("SQL: " + sql.replace("\n", " ").trim());

        // Statement: Objeto JDBC para ejecutar SQL
        try (Statement stmt = conexion.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Tabla 'libros' creada/verificada en SQLite");
        }
    }

    /**
     * Obtener conexión JDBC activa
     */
    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            throw new SQLException("Conexión no inicializada");
        }
        return conexion;
    }

    /**
     * Verificar si la base de datos existe y tiene datos
     */
    public static void mostrarEstadoBaseDatos() throws SQLException {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='libros'";

        try (Statement stmt = conexion.createStatement();
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
                System.out.println("💾 Datos persistidos en: biblioteca.db");
            }
        } catch (SQLException e) {
            System.err.println("Error cerrando conexión: " + e.getMessage());
        }
    }
}