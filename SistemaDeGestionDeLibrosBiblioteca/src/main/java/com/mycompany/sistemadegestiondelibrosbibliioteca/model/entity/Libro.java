/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistemadegestiondelibrosbibliioteca.model.entity;

/**
 *
 * @author gian_
 */
/**
 * Entity Libro - Representa la tabla de base de datos
 */
public class Libro {
    private Long id;
    private String titulo;
    private String autor;
    private Integer anoPublicacion;
    private Boolean disponible;
    

    public Libro() {
    }
    public Libro(Long id, String titulo, String autor, Integer anoPublicacion, Boolean disponible) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacion = anoPublicacion;
        this.disponible = disponible;
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
    
    public Boolean getDisponible() {
        return disponible;
    }
    
    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
    
    @Override
    public String toString() {
        return "Libro{" + 
                "id=" + id + 
                ", titulo='" + titulo + '\'' + 
                ", autor='" + autor + '\'' + 
                ", anoPublicacion=" + anoPublicacion + 
                ", disponible=" + disponible + 
                '}';
    }
}