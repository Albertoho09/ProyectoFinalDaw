package com.example.decsecBackend.dtos;

import java.util.Date;

public class SignUpRequest {
	private String nick;
	
	private String nombre;
	
	private String apellidos;
	
    private String email;

	private Date fechaNac;

    private String password;
    
    private byte[] fotoperfil;

    
    
	public byte[] getFotoperfil() {
		return fotoperfil;
	}

	public void setFotoperfil(byte[] fotoperfil) {
		this.fotoperfil = fotoperfil;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getFechaNac() {
		return fechaNac;
	}

	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
    
}
