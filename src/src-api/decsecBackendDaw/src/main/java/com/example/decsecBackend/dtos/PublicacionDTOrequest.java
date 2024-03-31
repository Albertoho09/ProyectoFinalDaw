package com.example.decsecBackend.dtos;

import lombok.Data;

@Data
public class PublicacionDTOrequest {
    private byte[] foto;

    private String comentarioUsuario;
}
