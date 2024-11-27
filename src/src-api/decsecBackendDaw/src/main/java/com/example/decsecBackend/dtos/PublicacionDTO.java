package com.example.decsecBackend.dtos;
import java.time.LocalDateTime;
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

    private int megusta;

    private LocalDateTime fechaPublicacion;

    private String nick;

    private Imagen fotoPerfil;

    private Boolean isliked;

    public PublicacionDTO(Publicacion publi) {
        this.id = publi.getId();
        this.imagenes = publi.getImagenes();
        this.comentarioUsuario = publi.getComentarioUsuario();
        this.megusta = publi.getUsuariosQueDieronMeGusta() != null ? publi.getUsuariosQueDieronMeGusta().size() : 0;
        this.fechaPublicacion = publi.getFechaPublicacion();
        this.nick = publi.getUsuario().getNick();
        this.fotoPerfil = publi.getUsuario().getFoto();
        this.isliked = publi.getUsuariosQueDieronMeGusta() != null && publi.getUsuariosQueDieronMeGusta().stream().anyMatch(u -> u.getId().equals(publi.getUsuario().getId())) ? true : false;
    }
}