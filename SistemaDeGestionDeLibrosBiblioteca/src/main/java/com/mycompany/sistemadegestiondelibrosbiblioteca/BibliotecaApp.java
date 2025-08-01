/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistemadegestiondelibrosbiblioteca;

/**
 *
 * @author gian_
 */
import com.mycompany.sistemadegestiondelibrosbibliioteca.config.DatabaseConfig;
import com.mycompany.sistemadegestiondelibrosbibliioteca.controller.LibroController;
import com.mycompany.sistemadegestiondelibrosbibliioteca.view.BibliotecaView;

/**
 * BibliotecaApp - Aplicación principal
 * Demuestra JDBC + DAO + SQLite con tu estructura MVC
 *
 * @author gian_
 */
public class BibliotecaApp {

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("    📚 SISTEMA DE GESTIÓN DE LIBROS - JDBC + DAO");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("🏗️  Arquitectura: MVC (Model-View-Controller)");
        System.out.println("💾 Base de datos: SQLite (archivo biblioteca.db)");
        System.out.println("🔗 Conectividad: JDBC");
        System.out.println("📋 Patrón: DAO (Data Access Object)");
        System.out.println("═══════════════════════════════════════════════════════\n");

        BibliotecaView view = new BibliotecaView();

        try {
            // 1. Inicializar base de datos SQLite con JDBC
            System.out.println("🔄 INICIALIZANDO SISTEMA...");
            DatabaseConfig.inicializar();
            DatabaseConfig.mostrarEstadoBaseDatos();
            System.out.println();

            // 2. Crear controlador MVC
            LibroController controller = new LibroController(view);
            System.out.println("✅ Controlador MVC inicializado");
            System.out.println("✅ Sistema listo para usar\n");

            // 3. Ejecutar demostraciones JDBC + DAO
            ejecutarDemostracionesJDBC(controller, view);

            // 4. Sistema interactivo (en este trabajo no lo uso)
            // view.ejecutarSistemaInteractivo(controller);

        } catch (Exception e) {
            System.err.println("❌ Error en la aplicación: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cerrar conexión JDBC
            DatabaseConfig.cerrarConexion();
        }
    }

    /**
     * Ejecutar demostraciones de JDBC + DAO
     */
    private static void ejecutarDemostracionesJDBC(LibroController controller, BibliotecaView view) {
        System.out.println("🧪 EJECUTANDO DEMOSTRACIONES JDBC + DAO");
        System.out.println("═══════════════════════════════════════════");

        // Demostración 1: INSERT - Agregar libros
        mostrarDemo(1, "INSERT con JDBC - Agregar libros");
        controller.agregarLibro("El Quijote", "Miguel de Cervantes", "1605");
        controller.agregarLibro("1984", "George Orwell", "1949");
        controller.agregarLibro("Cien años de soledad", "Gabriel García Márquez", "1967");

        // Demostración 2: SELECT - Buscar libro por ID
        mostrarDemo(2, "SELECT con JDBC - Buscar libro existente");
        controller.obtenerLibro(1L);

        // Demostración 3: SELECT - Libro no encontrado
        mostrarDemo(3, "SELECT con JDBC - Libro no encontrado");
        controller.obtenerLibro(999L);

        // Demostración 4: Validaciones - ID nulo
        mostrarDemo(4, "Validaciones - ID nulo");
        controller.obtenerLibro(null);

        // Demostración 5: Validaciones - Datos inválidos
        mostrarDemo(5, "Validaciones - Título vacío");
        controller.agregarLibro("", "Autor Test", "2023");

        // Demostración 6: SELECT ALL - Listar todos los libros
        mostrarDemo(6, "SELECT ALL con JDBC - Listar todos");
        controller.listarTodosLosLibros();

        // Demostración 7: Agregar libro con año inválido
        mostrarDemo(7, "Validaciones - Año futuro");
        controller.agregarLibro("Libro del Futuro", "Autor Futurista", "2050");

        // Demostración 8: Agregar libro con año como texto
        mostrarDemo(8, "Validaciones - Año con letras");
        controller.agregarLibro("Libro Test", "Autor Test", "año2023");

        System.out.println("✅ DEMOSTRACIONES COMPLETADAS");
        System.out.println("═══════════════════════════════════════════");
        System.out.println("🎯 CONCEPTOS DEMOSTRADOS:");
        System.out.println("   ✅ Conexión JDBC a SQLite");
        System.out.println("   ✅ Creación de tabla con SQL");
        System.out.println("   ✅ INSERT con PreparedStatement");
        System.out.println("   ✅ SELECT con WHERE");
        System.out.println("   ✅ SELECT ALL sin WHERE");
        System.out.println("   ✅ Auto-generated keys (AUTOINCREMENT)");
        System.out.println("   ✅ Mapeo ResultSet → Objeto");
        System.out.println("   ✅ DAO encapsulando JDBC");
        System.out.println("   ✅ Validaciones de negocio");
        System.out.println("   ✅ Manejo de errores SQL");
        System.out.println("   ✅ Persistencia en archivo SQLite");
        System.out.println();

        mostrarResumenFinal();
    }

    /**
     * Mostrar encabezado de demostración
     */
    private static void mostrarDemo(int numero, String descripcion) {
        System.out.println("🔬 DEMO " + numero + ": " + descripcion);
        System.out.println("─────────────────────────────────────────────────────");
    }

    /**
     * Mostrar resumen final
     */
    private static void mostrarResumenFinal() {
        System.out.println("📋 RESUMEN TÉCNICO:");
        System.out.println("═══════════════════════════════════════════");
        System.out.println("🔧 JDBC (Java Database Connectivity):");
        System.out.println("   • API estándar para conectar Java con BD");
        System.out.println("   • Connection, PreparedStatement, ResultSet");
        System.out.println("   • Ejecuta SQL desde Java");
        System.out.println();
        System.out.println("📋 DAO (Data Access Object):");
        System.out.println("   • Patrón que encapsula acceso a datos");
        System.out.println("   • Separa lógica de negocio de persistencia");
        System.out.println("   • Operaciones CRUD estándar");
        System.out.println();
        System.out.println("💾 SQLite:");
        System.out.println("   • Base de datos real en archivo");
        System.out.println("   • Sin servidor, fácil de usar");
        System.out.println("   • SQL completo soportado");
        System.out.println();
        System.out.println("🏗️  Arquitectura MVC:");
        System.out.println("   • Model: Entity + DTO + DAO + Service");
        System.out.println("   • View: Presentación e interacción");
        System.out.println("   • Controller: Coordinación de flujo");
        System.out.println();
        System.out.println("💾 Archivo generado: biblioteca.db");
        System.out.println("🎯 Datos persistentes entre ejecuciones");
    }
}