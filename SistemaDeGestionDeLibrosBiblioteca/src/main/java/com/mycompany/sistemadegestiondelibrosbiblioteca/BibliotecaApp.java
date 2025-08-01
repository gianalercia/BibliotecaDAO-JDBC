/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistemadegestiondelibrosbiblioteca;

/**
 *
 * @author gian_
 */
import com.mycompany.sistemadegestiondelibrosbibliioteca.controller.LibroController;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao.LibroDAO;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity.Libro;
import com.mycompany.sistemadegestiondelibrosbibliioteca.view.BibliotecaView;
import com.mycompany.sistemadegestiondelibrosbibliioteca.config.DatabaseConfig;


/**
 * BibliotecaApp - Aplicación principal
 *
 * ENDPOINTS REST:
 * GET  /libros/{id}  - Obtener DTO de libro por ID
 * POST /libros       - Crear nuevo libro
 */
public class BibliotecaApp {

    public static void main(String[] args) {
        System.out.println("=== DEMOSTRACIÓN JDBC + DAO + SQLite ===");
        System.out.println("Base de datos: SQLite (archivo .db)\n");

        try {
            // Inicializar conexión JDBC y crear BD SQLite
            DatabaseConfig.inicializar();
            DatabaseConfig.mostrarEstadoBaseDatos();
            System.out.println();

            // Crear DAO (usa JDBC internamente)
            LibroDAO libroDAO = new LibroDAO();

            // === DEMOSTRACIONES ===

            // 1. INSERT con JDBC + SQLite
            System.out.println("1️⃣ INSERTANDO LIBROS (INSERT SQL en SQLite)");
            Libro libro1 = new Libro("El Quijote", "Cervantes", 1605);
            Libro libro2 = new Libro("1984", "George Orwell", 1949);
            Libro libro3 = new Libro("Cien años de soledad", "García Márquez", 1967);

            libroDAO.guardar(libro1);
            libroDAO.guardar(libro2);
            libroDAO.guardar(libro3);
            System.out.println("✅ Libros insertados en SQLite\n");

            // 2. SELECT con JDBC + SQLite
            System.out.println("2️⃣ BUSCANDO LIBROS (SELECT SQL en SQLite)");
            Libro encontrado = libroDAO.buscarPorId(1);
            if (encontrado != null) {
                System.out.println("📖 Encontrado: " + encontrado);
            }

            Libro noEncontrado = libroDAO.buscarPorId(999);
            if (noEncontrado == null) {
                System.out.println("❌ Libro ID 999 no existe en SQLite");
            }
            System.out.println();

            // 3. COUNT con JDBC + SQLite
            System.out.println("3️⃣ CONTANDO REGISTROS (COUNT SQL en SQLite)");
            int total = libroDAO.contarTodos();
            System.out.println("📊 Total de libros en SQLite: " + total + "\n");

            // 4. SELECT ALL con JDBC + SQLite
            System.out.println("4️⃣ LISTANDO TODOS (SELECT ALL SQL en SQLite)");
            for (Libro libro : libroDAO.listarTodos()) {
                System.out.println("📚 " + libro);
            }
            System.out.println();

            // 5. UPDATE con JDBC + SQLite
            System.out.println("5️⃣ ACTUALIZANDO LIBRO (UPDATE SQL en SQLite)");
            if (encontrado != null) {
                encontrado.setTitulo("Don Quijote de la Mancha");
                libroDAO.actualizar(encontrado);
                System.out.println("📝 Libro actualizado: " + libroDAO.buscarPorId(1));
            }
            System.out.println();

            // 6. DELETE con JDBC + SQLite
            System.out.println("6️⃣ ELIMINANDO LIBRO (DELETE SQL en SQLite)");
            boolean eliminado = libroDAO.eliminar(2);
            if (eliminado) {
                System.out.println("🗑️ Libro ID 2 eliminado de SQLite");
                System.out.println("📊 Libros restantes: " + libroDAO.contarTodos());
            }
            System.out.println();

            System.out.println("🎯 DEMOSTRACIÓN COMPLETADA");
            System.out.println("💾 Todos los datos están guardados en: biblioteca.db");

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConfig.cerrarConexion();
        }
    }
}