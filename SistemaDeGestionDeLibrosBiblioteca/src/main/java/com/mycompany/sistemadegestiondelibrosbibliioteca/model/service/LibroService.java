/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadegestiondelibrosbibliioteca.model.service;

/**
 *
 * @author gian_
 */
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao.LibroDAO;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.dto.LibroDTO;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity.Libro;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * LibroService - Capa de lógica de negocio
 * Contiene TODAS las validaciones y reglas de negocio
 * Convierte entre Entity y DTO
 * Maneja todas las excepciones de negocio
 */
public class LibroService {
    private LibroDAO libroDAO;

    public LibroService() {
        this.libroDAO = new LibroDAO();
    }

    public LibroDTO obtenerLibroPorId(Long id) {
        // ID no puede ser nulo
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        // ID debe ser positivo
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        // Buscar en la base de datos
        Optional<Libro> libroOpt = libroDAO.findById(id);

        //  Libro debe existir (Error 404)
        if (libroOpt.isEmpty()) {
            throw new RuntimeException("Libro no encontrado con ID: " + id + " (Error 404)");
        }
        Libro libro = libroOpt.get();

        // Convertir Entity a DTO (ocultar campo disponible)
        return convertirADTO(libro);
    }

    public List<LibroDTO> obtenerTodosLosLibros() {
        Map<Long, Libro> todosLosLibros = libroDAO.findAll();
        List<LibroDTO> librosDTO = new ArrayList<>();

        for (Libro libro : todosLosLibros.values()) {
            librosDTO.add(convertirADTO(libro));
        }

        return librosDTO;
    }

    public LibroDTO agregarLibro(String titulo, String autor, String anoPublicacionStr) {
        // Ejecutar TODAS las validaciones de negocio
        validarTitulo(titulo);
        validarAutor(autor);
        validarAnoPublicacion(anoPublicacionStr); // Validar como String

        // Convertir a Integer (ya validado)
        Integer anoPublicacion = Integer.parseInt(anoPublicacionStr);

        // Crear nueva entidad
        Libro nuevoLibro = new Libro();
        nuevoLibro.setTitulo(titulo.trim());
        nuevoLibro.setAutor(autor.trim());
        nuevoLibro.setAnoPublicacion(anoPublicacion);
        nuevoLibro.setDisponible(true); // Por defecto disponible

        // Guardar en la base de datos
        Libro libroGuardado = libroDAO.save(nuevoLibro);

        // Convertir a DTO para retornar
        return convertirADTO(libroGuardado);
    }

    private void validarTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }

        if (titulo.trim().length() < 2) {
            throw new IllegalArgumentException("El título debe tener al menos 2 caracteres");
        }

        if (titulo.trim().length() > 200) {
            throw new IllegalArgumentException("El título no puede exceder 200 caracteres");
        }
    }
    
    private void validarAutor(String autor) {
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor no puede estar vacío");
        }

        if (autor.trim().length() < 2) {
            throw new IllegalArgumentException("El autor debe tener al menos 2 caracteres");
        }

        if (autor.trim().length() > 100) {
            throw new IllegalArgumentException("El autor no puede exceder 100 caracteres");
        }

        // El autor no debe contener números
        if (autor.matches(".*\\d.*")) {
            throw new IllegalArgumentException("El autor no puede contener números");
        }

        // El autor solo debe contener letras, espacios y algunos caracteres especiales
        if (!autor.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s\\.\\-']+$")) {
            throw new IllegalArgumentException("El autor solo puede contener letras, espacios, puntos, guiones y apostrofes");
        }
    }

    private void validarAnoPublicacion(String anoPublicacionStr) {
        // VALIDACIÓN 1: No nulo o vacío
        if (anoPublicacionStr == null || anoPublicacionStr.trim().isEmpty()) {
            throw new IllegalArgumentException("El año de publicación no puede estar vacío");
        }

        // VALIDACIÓN 2: No contiene letras
        if (anoPublicacionStr.matches(".*[a-zA-Z].*")) {
            throw new IllegalArgumentException("El año de publicación no puede contener letras");
        }

        // VALIDACIÓN 3: Es número válido
        Integer anoPublicacion;
        try {
            anoPublicacion = Integer.parseInt(anoPublicacionStr.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El año de publicación debe ser un número válido");
        }

        // VALIDACIÓN 4: Número positivo
        if (anoPublicacion <= 0) {
            throw new IllegalArgumentException("El año de publicación debe ser válido");
        }

        // VALIDACIÓN 5: No futuro
        int anoActual = java.time.Year.now().getValue();
        if (anoPublicacion > anoActual) {
            throw new IllegalArgumentException("El año de publicación no puede ser futuro");
        }

        // VALIDACIÓN 6: Rango razonable
        if (anoPublicacion < 1000) {
            throw new IllegalArgumentException("El año de publicación debe ser desde el año 1000");
        }
    }

    private LibroDTO convertirADTO(Libro libro) {
        return new LibroDTO(
                libro.getId(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getAnoPublicacion()
                // Nota: NO se incluye el campo 'disponible'
        );
    }
}