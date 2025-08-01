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

/**
 * BibliotecaApp - Demostración JDBC
 */
public class BibliotecaApp {

    public static void main(String[] args) {
        try {
            DatabaseConfig.inicializar();

            // Insertar libros de ejemplo
            ILibroDAO dao = new LibroDAOImpl();

            // Libro 1
            Libro libro1 = new Libro();
            libro1.setTitulo("It");
            libro1.setAutor("Stephen King");
            libro1.setAnoPublicacion(1986);
            libro1.setDisponible(true);

            try {
                dao.create(libro1);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }

            // Libro 2
            Libro libro2 = new Libro();
            libro2.setTitulo("Choque de reyes");
            libro2.setAutor("R.R. Martin");
            libro2.setAnoPublicacion(1998);
            libro2.setDisponible(true);

            try {
                dao.create(libro2);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }

            // Libro 3
            Libro libro3 = new Libro();
            libro3.setTitulo("Colorado Kid");
            libro3.setAutor("Stephen King");
            libro3.setAnoPublicacion(2002);
            libro3.setDisponible(true);

            try {
                dao.create(libro3);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            DatabaseConfig.cerrarConexion();
        }
    }
}