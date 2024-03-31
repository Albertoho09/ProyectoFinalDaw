package com.example.decsecBackend.dtos;

import java.time.LocalDateTime;

import com.example.decsecBackend.modelo.Comentario;

import lombok.Data;

@Data
public class ComentarioDTO {

    private Long id;

    private String comentario;

    private LocalDateTime hora;

    private String nickUsuario;

    public ComentarioDTO(Comentario comentario) {
        this.id = comentario.getId();
        this.comentario = comentario.getComentario();
        this.hora = comentario.getHora();
        this.nickUsuario = comentario.getUsuario().getNick();
    }
}
