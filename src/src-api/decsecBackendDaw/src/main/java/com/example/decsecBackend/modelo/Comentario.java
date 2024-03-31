package com.example.decsecBackend.modelo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comentario;

    private LocalDateTime hora = LocalDateTime.now();

    @ManyToOne
    @ElementCollection(fetch = FetchType.EAGER, targetClass = Usuario.class)
    @JoinColumn(name = "publicacion_id", nullable = false)
    @JsonManagedReference
    private Publicacion publicacion;

    @ManyToOne
    @ElementCollection(fetch = FetchType.EAGER, targetClass = Usuario.class)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonManagedReference
    private Usuario usuario;

}
