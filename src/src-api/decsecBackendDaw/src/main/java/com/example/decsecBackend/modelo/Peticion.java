package com.example.decsecBackend.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Peticion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_emisor_id")
    private Usuario usuarioEmisor;

    @ManyToOne
    @JoinColumn(name = "usuario_receptor_id")
    private Usuario usuarioReceptor;

    @Enumerated(EnumType.STRING)
    private Estado estado;
}
