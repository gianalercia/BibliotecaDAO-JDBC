/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadegestiondelibrosbibliioteca.controller;

import com.mycompany.sistemadegestiondelibrosbibliioteca.model.service.LibroService;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.dto.LibroDTO;
import com.mycompany.sistemadegestiondelibrosbibliioteca.view.BibliotecaView;

import java.util.List;

/**
 * LibroController - Controlador MVC
 * Coordina entre Model (LibroService) y View (BibliotecaView)
 * Maneja endpoints REST y peticiones HTTP
 *
 * @author gian_
 */
public class LibroController {
    private LibroService libroService;  // Referencia al MODEL
    private BibliotecaView view;         // Referencia a la VIEW

    public LibroController(BibliotecaView view) {
        this.libroService = new LibroService();
        this.view = view;
    }

    /**
     * Endpoint: GET /libros/{id}
     */
    public void obtenerLibro(Long id) {
        try {
            // Coordina con el MODEL para obtener los datos
            // TODAS las validaciones están en el Service
            LibroDTO libro = libroService.obtenerLibroPorId(id);

            // Coordina con la VIEW para mostrar la respuesta exitosa
            view.mostrarLibroEncontrado(libro);

        } catch (IllegalArgumentException e) {
            // Error 400 - Bad Request (datos inválidos)
            view.mostrarError(400, "Datos inválidos: " + e.getMessage());

        } catch (RuntimeException e) {
            if (e.getMessage().contains("404")) {
                // Error 404 - Not Found
                view.mostrarError(404, "Libro no encontrado");
            } else {
                // Error 500 - Internal Server Error
                view.mostrarError(500, "Error interno del servidor");
            }
        }
    }

    public void listarTodosLosLibros() {
        try {
            // Coordina con el MODEL para obtener todos los libros
            List<LibroDTO> libros = libroService.obtenerTodosLosLibros();

            // Coordina con la VIEW para mostrar la lista
            view.mostrarListaLibros(libros);

        } catch (Exception e) {
            // Error 500 - Internal Server Error
            view.mostrarError(500, "Error al obtener la lista de libros: " + e.getMessage());
        }
    }
    /**
     * Endpoint: POST /libros
     * Maneja la petición de creación de nuevo libro
     * Coordina Model y View sin contener lógica de negocio
     */
    public void agregarLibro(String titulo, String autor, String anoPublicacionStr) {
        try {
            // Coordina con el MODEL para crear el libro
            // TODAS las validaciones están en el Service
            LibroDTO nuevoLibro = libroService.agregarLibro(titulo, autor, anoPublicacionStr);

            // Coordina con la VIEW para mostrar la respuesta exitosa (201 Created)
            view.mostrarLibroCreado(nuevoLibro);

        } catch (IllegalArgumentException e) {
            // Error 400 - Bad Request (datos inválidos)
            view.mostrarError(400, "Datos inválidos: " + e.getMessage());

        } catch (Exception e) {
            // Error 500 - Internal Server Error
            view.mostrarError(500, "Error interno del servidor: " + e.getMessage());
        }
    }
}