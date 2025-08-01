/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadegestiondelibrosbibliioteca.model.service;

/**
 *
 * @author gian_
 */
import com.mycompany.sistemadegestiondelibrosbibliioteca.config.DatabaseConfig;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao.LibroDAO;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.dto.LibroDTO;
import com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity.Libro;
import java.sql.SQLException;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

/**
 * LibroService - Lógica de negocio con JDBC
 * Actualizado para usar tu estructura existente
 */
public class LibroService {

    private LibroDAO libroDAO;

    public LibroService() {
        this.libroDAO = new LibroDAO();
        // Asegurar que la conexión esté inicializada
        try {
            DatabaseConfig.inicializar();
        } catch (SQLException e) {
            System.err.println("❌ Error inicializando base de datos: " + e.getMessage());
        }
    }

    /**
     * Obtener libro por ID
     */
    public LibroDTO obtenerLibroPorId(Long id) {
        // Validaciones
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser positivo");
        }

        try {
            // Buscar con JDBC
            Optional<Libro> libroOpt = libroDAO.findById(id);

            if (!libroOpt.isPresent()) {
                throw new RuntimeException("Libro no encontrado con ID: " + id + " (Error 404)");
            }

            Libro libro = libroOpt.get();

            // Convertir a DTO
            return convertirADTO(libro);

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
        // Validaciones
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

            // Guardar con JDBC
            Libro libroGuardado = libroDAO.save(nuevoLibro);

            // Convertir a DTO
            return convertirADTO(libroGuardado);

        } catch (Exception e) {
            if (e instanceof IllegalArgumentException || e instanceof RuntimeException) {
                throw e;
            }
            throw new RuntimeException("Error guardando en la base de datos: " + e.getMessage());
        }
    }

    /**
     * Obtener todos los libros
     */
    public List<LibroDTO> obtenerTodosLosLibros() {
        try {
            List<Libro> libros = libroDAO.findAll();
            List<LibroDTO> librosDTO = new ArrayList<>();

            for (Libro libro : libros) {
                librosDTO.add(convertirADTO(libro));
            }

            return librosDTO;

        } catch (Exception e) {
            throw new RuntimeException("Error listando libros: " + e.getMessage());
        }
    }

    /**
     * Contar libros
     */
    public int contarLibros() {
        try {
            return libroDAO.count();
        } catch (Exception e) {
            throw new RuntimeException("Error contando libros: " + e.getMessage());
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