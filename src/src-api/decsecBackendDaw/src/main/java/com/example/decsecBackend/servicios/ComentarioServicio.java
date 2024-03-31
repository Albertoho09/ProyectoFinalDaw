package com.example.decsecBackend.servicios;

import java.util.List;

import com.example.decsecBackend.dtos.ComentarioDTO;
import com.example.decsecBackend.modelo.Comentario;

public interface ComentarioServicio {

    List<ComentarioDTO> listarComentarios();

    List<ComentarioDTO> listarComentariosPublicacion(Long id);

    List<ComentarioDTO> listarMisComentarios(String email);

    ComentarioDTO crearComentario(Comentario comentario, String emailUsuario, Long idPubli);

    ComentarioDTO actualizarComentario(String nuevoComentario, Long idComentario);

    void borrarComentario(Long idComentario);

    public boolean comentarioPerteneceAUsuario(Long IdComentario, String email);

}
