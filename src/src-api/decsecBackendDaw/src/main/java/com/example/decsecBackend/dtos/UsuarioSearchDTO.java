package com.example.decsecBackend.dtos;

import com.example.decsecBackend.modelo.Imagen;

import lombok.Data;

@Data
public class UsuarioSearchDTO {
	private String nick;
    private Imagen fotoperfil;
    
    public UsuarioSearchDTO(String nick, Imagen fotoperfil) {
        this.nick = nick;
        this.fotoperfil = fotoperfil;
    }
}
