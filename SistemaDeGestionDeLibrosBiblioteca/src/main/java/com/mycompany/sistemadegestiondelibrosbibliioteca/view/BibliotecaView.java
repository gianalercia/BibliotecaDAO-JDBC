/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadegestiondelibrosbibliioteca.view;

/**
 *
 * @author gian_
 */
import com.mycompany.sistemadegestiondelibrosbibliioteca.controller.LibroController;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.dto.LibroDTO;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * BibliotecaView - Capa de presentación MVC
 * Maneja interfaz de usuario y presentación de datos
 */
public class BibliotecaView {
    private Scanner scanner;

    public BibliotecaView() {
        this.scanner = new Scanner(System.in);
    }
    /**
     * Muestra un libro encontrado
     */
    public void mostrarLibroEncontrado(LibroDTO libro) {
        System.out.println("HTTP 200 OK");
        System.out.println("Libro encontrado:");
        System.out.println("ID: " + libro.getId());
        System.out.println("Título: " + libro.getTitulo());
        System.out.println("Autor: " + libro.getAutor());
        System.out.println("Año: " + libro.getAnoPublicacion());
        System.out.println();
    }
    /**
     * Muestra un libro creado
     */
    public void mostrarLibroCreado(LibroDTO libro) {
        System.out.println("HTTP 201 CREATED");
        System.out.println("Libro creado exitosamente:");
        System.out.println("ID: " + libro.getId());
        System.out.println("Título: " + libro.getTitulo());
        System.out.println("Autor: " + libro.getAutor());
        System.out.println("Año: " + libro.getAnoPublicacion());
        System.out.println();
    }

    public void mostrarListaLibros(List<LibroDTO> libros) {
        System.out.println("HTTP 200 OK");
        System.out.println("Lista de todos los libros:");
        System.out.println("Total de libros: " + libros.size());
        System.out.println();

        if (libros.isEmpty()) {
            System.out.println("No hay libros en el sistema.");
        } else {
            for (LibroDTO libro : libros) {
                System.out.println("ID: " + libro.getId() +
                        " | Título: " + libro.getTitulo() +
                        " | Autor: " + libro.getAutor() +
                        " | Año: " + libro.getAnoPublicacion());
            }
        }
        System.out.println();
    }

    /**
     * Muestra errores HTTP
     */
    public void mostrarError(int codigoHttp, String mensaje) {
        String nombreEstado = obtenerNombreEstado(codigoHttp);
        System.out.println("HTTP " + codigoHttp + " " + nombreEstado);
        System.out.println("Mensaje: " + mensaje);
        System.out.println();
    }

    /**
     * Ejecuta el sistema interactivo
     */
    public void ejecutarSistemaInteractivo(LibroController controller) {
        boolean continuar = true;

        while (continuar) {
            try {
                mostrarMenu();
                int opcion = leerOpcion();

                switch (opcion) {
                    case 1:
                        ejecutarBusquedaLibro(controller);
                        break;
                    case 2:
                        ejecutarAgregarLibro(controller);
                        break;
                    case 3:
                        ejecutarListarLibros(controller);
                        break;
                    case 4:
                        continuar = false;
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opción inválida. Seleccione 1-4");
                        break;
                }

                if (continuar && opcion >= 1 && opcion <= 3) {
                    System.out.println("Presione Enter para continuar...");
                    scanner.nextLine();
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    /**
     * Buscar libro por ID - GET /libros/{id}
     */
    private void ejecutarBusquedaLibro(LibroController controller) {
        System.out.println("=== Buscar libro por ID ===");

        try {
            System.out.print("Ingrese el ID del libro: ");
            String input = scanner.nextLine().trim();

            Long id = null;
            if (!input.isEmpty()) {
                try {
                    id = Long.parseLong(input);
                } catch (NumberFormatException e) {
                    mostrarError(400, "ID inválido. Debe ser un número entero.");
                    return;
                }
            }
            // El Controller maneja Model y View
            controller.obtenerLibro(id);

        } catch (Exception e) {
            mostrarError(500, "Error al procesar la búsqueda: " + e.getMessage());
        }
    }
    /**
     * Agregar nuevo libro - POST /libros
     */
    private void ejecutarAgregarLibro(LibroController controller) {
        System.out.println("=== Agregar nuevo libro ===");

        try {
            System.out.print("Ingrese el título: ");
            String titulo = scanner.nextLine().trim();

            System.out.print("Ingrese el autor: ");
            String autor = scanner.nextLine().trim();

            System.out.print("Ingrese el año de publicación: ");
            String anoStr = scanner.nextLine().trim();

            System.out.println("Datos a guardar:");
            System.out.println("Título: " + titulo);
            System.out.println("Autor: " + autor);
            System.out.println("Año: " + anoStr);

            System.out.print("¿Confirma? (s/n): ");
            String confirmacion = scanner.nextLine().trim().toLowerCase();

            if (confirmacion.equals("s") || confirmacion.equals("si")) {
                // El Controller maneja Model y View
                // Pasamos el año como String para que Service valide
                controller.agregarLibro(titulo, autor, anoStr);
            } else {
                System.out.println("Operación cancelada.");
            }

        } catch (Exception e) {
            mostrarError(500, "Error al procesar datos: " + e.getMessage());
        }
    }

    /**
     * Muestra el menú principal
     */
    private void mostrarMenu() {
        System.out.println("\n=== Sistema de Gestión de Libros ===");
        System.out.println("1. Buscar libro por ID");
        System.out.println("2. Agregar nuevo libro");
        System.out.println("3. Mostrar todos los libros");
        System.out.println("4. Salir");
        System.out.print("Seleccione una opción: ");
    }

    /**
     * Lee opción del usuario
     */
    private int leerOpcion() {
        try {
            int opcion = scanner.nextInt();
            scanner.nextLine();
            return opcion;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }

    /**
     * Obtiene nombre del estado HTTP
     */
    private String obtenerNombreEstado(int codigo) {
        switch (codigo) {
            case 200: return "OK";
            case 201: return "CREATED";
            case 400: return "BAD REQUEST";
            case 404: return "NOT FOUND";
            case 500: return "INTERNAL SERVER ERROR";
            default: return "UNKNOWN";
        }
    }

    private void ejecutarListarLibros(LibroController controller) {
        System.out.println("=== Listar todos los libros ===");

        // El Controller maneja Model y View
        controller.listarTodosLosLibros();
    }
}