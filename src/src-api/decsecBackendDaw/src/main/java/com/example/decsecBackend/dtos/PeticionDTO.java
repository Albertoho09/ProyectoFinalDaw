package com.example.decsecBackend.dtos;

import com.example.decsecBackend.modelo.Estado;
import com.example.decsecBackend.modelo.Imagen;
import com.example.decsecBackend.modelo.Peticion;
import com.example.decsecBackend.modelo.Usuario;
import lombok.Data;

/**
 * PeticionDTO
 */

@Data
public class PeticionDTO {

    private Long id;

    private String usuarioReceptorNick;

    private String usuarioEmisorNick;

    private Imagen usuarioEmisorFoto;

    private Estado estado;

    public PeticionDTO(Peticion peti) {
        this.id = peti.getId();
        this.usuarioReceptorNick = peti.getUsuarioReceptor().getNick();
        this.usuarioEmisorNick = peti.getUsuarioEmisor().getNick();
        this.usuarioEmisorFoto = peti.getUsuarioEmisor().getFoto();
        this.estado = peti.getEstado();
    }
}