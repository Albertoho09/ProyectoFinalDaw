package com.example.decsecBackend.dtos;

import com.example.decsecBackend.modelo.Estado;
import com.example.decsecBackend.modelo.Peticion;
import com.example.decsecBackend.modelo.Usuario;
import lombok.Data;

/**
 * PeticionDTO
 */

@Data
public class PeticionDTO {

    private Long id;

    private Usuario usuarioEmisor;

    private Usuario usuarioReceptor;

    private Estado estado;

    public PeticionDTO(Peticion peti) {
        this.id = peti.getId();
        this.usuarioEmisor = peti.getUsuarioEmisor();
        this.usuarioReceptor = peti.getUsuarioReceptor();
        this.estado = peti.getEstado();
    }
}