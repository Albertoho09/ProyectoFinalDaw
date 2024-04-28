package com.example.decsecBackend.dtos;

import java.util.Date;

import lombok.Data;

@Data
public class SignUpRequest {
	private String nick;
	
	private String nombre;
	
	private String apellidos;
	
    private String email;

	private Date fechaNac;

    private String password;
    
	private Boolean privado;
}
