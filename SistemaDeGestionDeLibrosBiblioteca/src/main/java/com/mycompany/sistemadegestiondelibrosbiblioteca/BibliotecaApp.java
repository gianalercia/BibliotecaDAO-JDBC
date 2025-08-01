/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistemadegestiondelibrosbiblioteca;

/**
 *
 * @author gian_
 */
import com.mycompany.sistemadegestiondelibrosbibliioteca.config.DatabaseConfig;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao.ILibroDAO;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao.LibroDAOImpl;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity.Libro;
import java.util.List;
import java.util.Optional;

/**
 * BibliotecaApp - Demostración CRUD con JDBC + DAO
 *
 * OPERACIONES DEMOSTRADAS:
 * - CREATE: INSERT de libros únicos
 * - READ: SELECT por ID
 * - UPDATE: UPDATE de libro existente
 * - DELETE: DELETE por ID
 * - READ ALL: SELECT de todos los libros
 *
 * @author gian_
 */
public class BibliotecaApp {

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("    📚 DEMOSTRACIÓN CRUD - JDBC + DAO + SQLite");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("🏗️  Patrón: DAO con interfaz");
        System.out.println("💾 Base de datos: SQLite");
        System.out.println("🔗 Conectividad: JDBC");
        System.out.println("🛡️  Sanitización: Activada");
        System.out.println("🚫 Duplicados: Validados");
        System.out.println("═══════════════════════════════════════════════════════\n");

        try {
            // Inicializar BD (verifica si existe o la crea)
            DatabaseConfig.inicializar();
            System.out.println();

            // DAO con interfaz (buenas prácticas)
            ILibroDAO libroDAO = new LibroDAOImpl();

            // === DEMOSTRACIÓN CRUD COMPLETA ===
            ejecutarDemostracionCRUD(libroDAO);

        } catch (Exception e) {
            System.err.println("❌ Error en la aplicación: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConfig.cerrarConexion();
        }
    }

    /**
     * Ejecutar demostración completa de CRUD
     */
    private static void ejecutarDemostracionCRUD(ILibroDAO dao) {
        System.out.println("🚀 INICIANDO DEMOSTRACIÓN CRUD");
        System.out.println("═══════════════════════════════════");

        // === 1. CREATE - Insertar libros ===
        mostrarOperacion("CREATE", "Insertar nuevos libros (validando duplicados)");

        Libro libro1 = new Libro();
        libro1.setTitulo("Don Quijote de la Mancha");
        libro1.setAutor("Miguel de Cervantes");
        libro1.setAnoPublicacion(1605);
        libro1.setDisponible(true);

        Libro libro2 = new Libro();
        libro2.setTitulo("1984");
        libro2.setAutor("George Orwell");
        libro2.setAnoPublicacion(1949);
        libro2.setDisponible(true);

        Libro libro3 = new Libro();
        libro3.setTitulo("Cien Años de Soledad");
        libro3.setAutor("Gabriel García Márquez");
        libro3.setAnoPublicacion(1967);
        libro3.setDisponible(true);

        // Insertar libros
        try {
            Libro creado1 = dao.create(libro1);
            System.out.println("📚 Libro creado: " + creado1.getTitulo() + " (ID: " + creado1.getId() + ")");

            Libro creado2 = dao.create(libro2);
            System.out.println("📚 Libro creado: " + creado2.getTitulo() + " (ID: " + creado2.getId() + ")");

            Libro creado3 = dao.create(libro3);
            System.out.println("📚 Libro creado: " + creado3.getTitulo() + " (ID: " + creado3.getId() + ")");

        } catch (Exception e) {
            System.out.println("⚠️ Error o duplicado: " + e.getMessage());
        }

        System.out.println();

        // === 2. READ - Buscar por ID ===
        mostrarOperacion("READ", "Buscar libro por ID");

        Optional<Libro> encontrado = dao.read(1L);
        if (encontrado.isPresent()) {
            Libro libro = encontrado.get();
            System.out.println("📖 Libro encontrado: " + libro.getTitulo() + " - " + libro.getAutor() + " (" + libro.getAnoPublicacion() + ")");
        } else {
            System.out.println("❌ Libro con ID 1 no encontrado");
        }

        // Buscar libro inexistente
        Optional<Libro> noEncontrado = dao.read(999L);
        if (!noEncontrado.isPresent()) {
            System.out.println("❌ Libro con ID 999 no existe (comportamiento esperado)");
        }

        System.out.println();

        // === 3. READ ALL - Listar todos ===
        mostrarOperacion("READ ALL", "Obtener todos los libros");

        List<Libro> todos = dao.readAll();
        System.out.println("📋 Total de libros en BD: " + todos.size());
        for (Libro libro : todos) {
            System.out.println("   📚 ID " + libro.getId() + ": " + libro.getTitulo() + " - " + libro.getAutor() + " (" + libro.getAnoPublicacion() + ")");
        }

        System.out.println();

        // === 4. UPDATE - Actualizar libro ===
        mostrarOperacion("UPDATE", "Actualizar libro existente");

        if (!todos.isEmpty()) {
            Libro paraActualizar = todos.get(0); // Primer libro
            String tituloOriginal = paraActualizar.getTitulo();

            paraActualizar.setTitulo("El Ingenioso Hidalgo Don Quijote de la Mancha");
            paraActualizar.setDisponible(false);

            try {
                Libro actualizado = dao.update(paraActualizar);
                System.out.println("📝 Libro actualizado:");
                System.out.println("   🔄 Título anterior: " + tituloOriginal);
                System.out.println("   📚 Título nuevo: " + actualizado.getTitulo());
                System.out.println("   📊 Disponible: " + actualizado.getDisponible());
            } catch (Exception e) {
                System.out.println("❌ Error actualizando: " + e.getMessage());
            }
        }

        System.out.println();

        // === 5. DELETE - Eliminar libro ===
        mostrarOperacion("DELETE", "Eliminar libro por ID");

        if (todos.size() >= 2) {
            Long idAEliminar = todos.get(1).getId(); // Segundo libro
            String tituloAEliminar = todos.get(1).getTitulo();

            boolean eliminado = dao.delete(idAEliminar);
            if (eliminado) {
                System.out.println("🗑️ Libro eliminado: " + tituloAEliminar + " (ID: " + idAEliminar + ")");

                // Verificar eliminación
                List<Libro> despuesDelete = dao.readAll();
                System.out.println("📊 Libros restantes: " + despuesDelete.size());
            } else {
                System.out.println("❌ No se pudo eliminar el libro");
            }
        }

        // Intentar eliminar libro inexistente
        boolean eliminadoInexistente = dao.delete(999L);
        if (!eliminadoInexistente) {
            System.out.println("❌ Libro ID 999 no existe para eliminar (comportamiento esperado)");
        }

        System.out.println();

        // === 6. Validación de duplicados ===
        mostrarOperacion("VALIDACIÓN", "Intentar insertar duplicado");

        Libro duplicado = new Libro();
        duplicado.setTitulo("Don Quijote de la Mancha");
        duplicado.setAutor("Miguel de Cervantes");
        duplicado.setAnoPublicacion(1605);
        duplicado.setDisponible(true);

        try {
            dao.create(duplicado);
            System.out.println("❌ ERROR: Se permitió duplicado (no debería pasar)");
        } catch (RuntimeException e) {
            System.out.println("✅ Duplicado rechazado correctamente: " + e.getMessage());
        }

        System.out.println();

        // === RESUMEN FINAL ===
        mostrarResumenFinal(dao);
    }

    /**
     * Mostrar encabezado de operación
     */
    private static void mostrarOperacion(String operacion, String descripcion) {
        System.out.println("🔧 " + operacion + ": " + descripcion);
        System.out.println("─────────────────────────────────────────────────────");
    }

    /**
     * Mostrar resumen final de la demostración
     */
    private static void mostrarResumenFinal(ILibroDAO dao) {
        System.out.println("📋 RESUMEN DE DEMOSTRACIÓN CRUD");
        System.out.println("═══════════════════════════════════════════");

        List<Libro> finales = dao.readAll();
        System.out.println("📊 Estado final de la base de datos:");
        System.out.println("   📚 Total de libros: " + finales.size());

        if (!finales.isEmpty()) {
            System.out.println("   📖 Libros en BD:");
            for (Libro libro : finales) {
                System.out.println("      • ID " + libro.getId() + ": " + libro.getTitulo());
            }
        }

    }
}