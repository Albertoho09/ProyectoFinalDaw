package com.example.decsecBackend.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Imagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String tipo;
    @Column(length = 50000000)
    private byte[] datos;
    
    public Imagen() {
    }

    public Imagen(String nombre, String tipo, byte[] datos) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.datos = datos;
    }
    
}
