/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadegestiondelibrosbibliioteca.model.service;

/**
 *
 * @author gian_
 */
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao.ILibroDAO;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao.LibroDAOImpl;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.dto.LibroDTO;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity.Libro;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

/**
 * LibroService - Capa de lógica de negocio
 * Actualizado para usar el nuevo DAO CRUD con interfaz
 */
public class LibroService {

    private ILibroDAO libroDAO;

    public LibroService() {
        this.libroDAO = new LibroDAOImpl();
    }

    /**
     * Obtener libro por ID
     */
    public LibroDTO obtenerLibroPorId(Long id) {
        // Validaciones básicas
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser positivo");
        }

        try {
            Optional<Libro> libroOpt = libroDAO.read(id);

            if (!libroOpt.isPresent()) {
                throw new RuntimeException("Libro no encontrado con ID: " + id + " (Error 404)");
            }

            return convertirADTO(libroOpt.get());

        } catch (Exception e) {
            if (e instanceof IllegalArgumentException || e instanceof RuntimeException) {
                throw e;
            }
            throw new RuntimeException("Error accediendo a la base de datos: " + e.getMessage());
        }
    }

    /**
     * Agregar nuevo libro
     */
    public LibroDTO agregarLibro(String titulo, String autor, String anoPublicacionStr) {
        // Validaciones de negocio
        validarTitulo(titulo);
        validarAutor(autor);
        Integer anoPublicacion = validarAnoPublicacion(anoPublicacionStr);

        try {
            // Crear entidad
            Libro nuevoLibro = new Libro();
            nuevoLibro.setTitulo(titulo.trim());
            nuevoLibro.setAutor(autor.trim());
            nuevoLibro.setAnoPublicacion(anoPublicacion);
            nuevoLibro.setDisponible(true);

            // Usar DAO CRUD
            Libro libroGuardado = libroDAO.create(nuevoLibro);

            return convertirADTO(libroGuardado);

        } catch (Exception e) {
            if (e instanceof IllegalArgumentException || e instanceof RuntimeException) {
                throw e;
            }
            throw new RuntimeException("Error guardando libro: " + e.getMessage());
        }
    }

    /**
     * Actualizar libro existente
     */
    public LibroDTO actualizarLibro(Long id, String titulo, String autor, String anoPublicacionStr) {
        // Validaciones
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para actualizar");
        }

        validarTitulo(titulo);
        validarAutor(autor);
        Integer anoPublicacion = validarAnoPublicacion(anoPublicacionStr);

        try {
            // Verificar que existe
            Optional<Libro> existente = libroDAO.read(id);
            if (!existente.isPresent()) {
                throw new RuntimeException("Libro no encontrado para actualizar");
            }

            // Actualizar datos
            Libro libro = existente.get();
            libro.setTitulo(titulo.trim());
            libro.setAutor(autor.trim());
            libro.setAnoPublicacion(anoPublicacion);

            // Usar DAO CRUD
            Libro actualizado = libroDAO.update(libro);

            return convertirADTO(actualizado);

        } catch (Exception e) {
            if (e instanceof IllegalArgumentException || e instanceof RuntimeException) {
                throw e;
            }
            throw new RuntimeException("Error actualizando libro: " + e.getMessage());
        }
    }

    /**
     * Eliminar libro por ID
     */
    public boolean eliminarLibro(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para eliminar");
        }

        try {
            return libroDAO.delete(id);
        } catch (Exception e) {
            throw new RuntimeException("Error eliminando libro: " + e.getMessage());
        }
    }

    /**
     * Obtener todos los libros
     */
    public List<LibroDTO> obtenerTodosLosLibros() {
        try {
            List<Libro> libros = libroDAO.readAll();
            List<LibroDTO> librosDTO = new ArrayList<>();

            for (Libro libro : libros) {
                librosDTO.add(convertirADTO(libro));
            }

            return librosDTO;

        } catch (Exception e) {
            throw new RuntimeException("Error listando libros: " + e.getMessage());
        }
    }

    // ========================================================================
    // MÉTODOS PRIVADOS DE VALIDACIÓN
    // ========================================================================

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
    }

    private Integer validarAnoPublicacion(String anoPublicacionStr) {
        if (anoPublicacionStr == null || anoPublicacionStr.trim().isEmpty()) {
            throw new IllegalArgumentException("El año de publicación no puede estar vacío");
        }

        Integer anoPublicacion;
        try {
            anoPublicacion = Integer.parseInt(anoPublicacionStr.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El año de publicación debe ser un número válido");
        }

        if (anoPublicacion <= 0) {
            throw new IllegalArgumentException("El año de publicación debe ser válido");
        }

        int anoActual = java.time.Year.now().getValue();
        if (anoPublicacion > anoActual) {
            throw new IllegalArgumentException("El año de publicación no puede ser futuro");
        }

        if (anoPublicacion < 1000) {
            throw new IllegalArgumentException("El año de publicación debe ser desde el año 1000");
        }

        return anoPublicacion;
    }

    /**
     * Convertir Entity a DTO
     */
    private LibroDTO convertirADTO(Libro libro) {
        return new LibroDTO(
                libro.getId(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getAnoPublicacion()
        );
    }
}