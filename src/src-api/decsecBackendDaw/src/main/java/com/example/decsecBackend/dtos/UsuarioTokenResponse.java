package com.example.decsecBackend.dtos;

public class UsuarioTokenResponse {
    private UsuarioDTO usuario;
    private String token;

    public UsuarioTokenResponse(UsuarioDTO usuario, String token) {
        this.usuario = usuario;
        this.token = token;
    }

    // Getters y Setters
    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
