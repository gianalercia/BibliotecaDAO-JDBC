package com.mycompany.sistemadegestiondelibrosbibliioteca.model.dao;

import com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity.Libro;
import java.util.List;
import java.util.Optional;

// ILibroDAO - Interfaz para el patrón DAO
// Define las operaciones CRUD estándar
// Interfaz separa contrato de implementación
public interface ILibroDAO {

    // create - Crear nuevo libro
    Libro create(Libro libro);

    // read - Buscar libro por ID
    Optional<Libro> read(Long id);

    // update - Actualizar libro existente
    Libro update(Libro libro);

    // delete - Eliminar libro por ID
    boolean delete(Long id);

    // read all - Obtener todos los libros
    List<Libro> readAll();

    // exists - Verificar si existe libro con título y autor
    boolean exists(String titulo, String autor);
}