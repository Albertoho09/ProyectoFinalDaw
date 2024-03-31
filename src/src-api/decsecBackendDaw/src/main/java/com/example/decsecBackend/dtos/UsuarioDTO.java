package com.example.decsecBackend.dtos;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.example.decsecBackend.modelo.Role;
import com.example.decsecBackend.modelo.Usuario;

import lombok.Data;

@Data
public class UsuarioDTO {

	private String nick;

	private String nombre;

	private String apellidos;

	private String email;

	private LocalDate fechaNac;

	private int npublicaciones;

	private Set<Role> roles = new HashSet<>();

	public UsuarioDTO(Usuario usu) {
		this.nick = usu.getNick();
		this.nombre = usu.getNombre();
		this.apellidos = usu.getApellidos();
		this.email = usu.getEmail();
		this.fechaNac = usu.getFechaNac();
		this.roles = usu.getRoles();
		this.npublicaciones = usu.getPublicaciones().size();
	}

}
