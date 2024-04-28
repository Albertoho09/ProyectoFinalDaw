package com.example.decsecBackend.dtos;

import java.time.LocalDate;
import java.util.List;

import com.example.decsecBackend.modelo.Imagen;
import com.example.decsecBackend.modelo.Publicacion;

import lombok.Data;

/**
 * PublicacionDTO
 */

@Data
public class PublicacionDTO {

    private Long id;

    private List<Imagen> imagenes;

    private String comentarioUsuario;

    private int megusta = 0;

    private LocalDate fechaPublicacion;

    private String emailUsuario;

    public PublicacionDTO(Publicacion publi) {
        this.id = publi.getId();
        this.imagenes = publi.getImagenes();
        this.comentarioUsuario = publi.getComentarioUsuario();
        this.megusta = publi.getMegusta();
        this.fechaPublicacion = publi.getFechaPublicacion();
        this.emailUsuario = publi.getUsuario().getEmail();
    }
}