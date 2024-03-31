package com.example.decsecBackend.servicios;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.decsecBackend.dtos.UsuarioDTO;
import com.example.decsecBackend.modelo.Usuario;

public interface UsuarioServicio {
	List<UsuarioDTO> listarTodosUsuariosDTO();

	List<Usuario> listarTodosUsuarios();

	Usuario crearUsuario(Usuario usuario);

	Usuario obtenerUsuario(Long id);

	void borrarUsuario(Long id);

	UserDetailsService userDetailsService();

	Boolean existePorEmail(String email);

	Boolean existePorId(Long id);

	Usuario encontrarPorEmail(String email);

	Usuario actualizarUsuario(Long id, Map<String, Object> updates);

	Boolean usuarioPrivado(String email);
}
