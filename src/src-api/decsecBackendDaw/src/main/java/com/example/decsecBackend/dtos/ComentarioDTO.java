package com.example.decsecBackend.dtos;

import java.time.LocalDateTime;

import com.example.decsecBackend.modelo.Comentario;
import com.example.decsecBackend.modelo.Imagen;

import lombok.Data;

@Data
public class ComentarioDTO {

    private Long id;

    private String comentario;

    private LocalDateTime hora;

    private String nickUsuario;

    private Imagen fotoPerfil;

    public ComentarioDTO(Comentario comentario) {
        this.id = comentario.getId();
        this.comentario = comentario.getComentario();
        this.hora = comentario.getHora();
        this.nickUsuario = comentario.getUsuario().getNick();
        this.fotoPerfil = comentario.getUsuario().getFoto();
    }
}
