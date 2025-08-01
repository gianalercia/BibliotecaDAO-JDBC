/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadegestiondelibrosbibliioteca.model.dto;

/**
 *
 * @author gian_
 */

public class LibroDTO {
    private Long id;
    private String titulo;
    private String autor;
    private Integer anoPublicacion;
    // El campo 'disponible' está OCULTO

    /**
     * Constructor
     */
    public LibroDTO(Long id, String titulo, String autor, Integer anoPublicacion) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacion = anoPublicacion;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getAutor() {
        return autor;
    }
    
    public void setAutor(String autor) {
        this.autor = autor;
    }
    
    public Integer getAnoPublicacion() {
        return anoPublicacion;
    }
    
    public void setAnoPublicacion(Integer anoPublicacion) {
        this.anoPublicacion = anoPublicacion;
    }
    
    @Override
    public String toString() {
        return "LibroDTO{" + 
                "id=" + id + 
                ", titulo='" + titulo + '\'' + 
                ", autor='" + autor + '\'' + 
                ", anoPublicacion=" + anoPublicacion + 
                '}';
    }
}