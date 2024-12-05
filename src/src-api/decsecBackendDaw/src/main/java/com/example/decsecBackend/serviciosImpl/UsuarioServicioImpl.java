package com.example.decsecBackend.serviciosImpl;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import com.example.decsecBackend.dtos.PeticionDTO;
import com.example.decsecBackend.dtos.UsuarioDTO;
import com.example.decsecBackend.dtos.UsuarioSearchDTO;
import com.example.decsecBackend.errores.NotFoundException;
import com.example.decsecBackend.modelo.Estado;
import com.example.decsecBackend.modelo.Imagen;
import com.example.decsecBackend.modelo.Usuario;
import com.example.decsecBackend.repositorios.ImagenRepositorio;
import com.example.decsecBackend.repositorios.PeticionRepositorio;
import com.example.decsecBackend.repositorios.UsuarioRepositorio;
import com.example.decsecBackend.servicios.UsuarioServicio;

@Service
public class UsuarioServicioImpl implements UsuarioServicio {

	@Autowired
	private UsuarioRepositorio repositorio;
	@Autowired
	private PeticionRepositorio peticionRepositorio;
	@Autowired
	private ImagenRepositorio imagenRepositorio;

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@SuppressWarnings("null")
	@Override
	public Usuario crearUsuario(Usuario usuario) {
		return repositorio.save(usuario);
	}

	@Override
	public Usuario obtenerUsuario(Long id) {
		return repositorio.findById(id).orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
	}

	@SuppressWarnings("null")
	@Override
	public void borrarUsuario(Long id) {
		repositorio.deleteById(id);
	}

	@Override
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			@Override
			public UserDetails loadUserByUsername(String username) {
				return repositorio.findByEmail(username)
						.orElseThrow(() -> new UsernameNotFoundException("User not found"));
			}
		};
	}

	@Override
	public List<UsuarioDTO> listarTodosUsuariosDTO() {
		return repositorio.findAll().stream()
				.map(usuario -> new UsuarioDTO(usuario))
				.collect(Collectors.toList());
	}

	@Override
	public List<UsuarioSearchDTO> listarTodosUsuariosSearchDTO() {
		return repositorio.findAll().stream()
				.map(usuario -> new UsuarioSearchDTO(usuario.getNick(), usuario.getFoto()))
				.collect(Collectors.toList());
	}

	@Override
	public List<Usuario> listarTodosUsuarios() {
		return repositorio.findAll();
	}

	@Override
	public Boolean existePorEmail(String email) {
		return repositorio.existsByEmail(email);
	}

	@Override
	public Usuario encontrarPorEmail(String email) {
		return repositorio.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
	}

	@Override
	public Boolean existePorId(Long id) {
		return repositorio.existsById(id);
	}

	@Override
	public Usuario actualizarUsuario(Long id, Map<String, Object> updates) {
		Usuario usu = obtenerUsuario(id);

		updates.forEach((campo, valor) -> {
			switch (campo) {
				case "nombre":
					usu.setNombre((String) valor);
					break;
				case "apellidos":
					usu.setApellidos((String) valor);
					break;
				case "fechaNac":
					OffsetDateTime fechaOffset = OffsetDateTime.parse(valor.toString());
					LocalDate fechaLocal = fechaOffset.toLocalDate(); // Convert to LocalDate
					usu.setFechaNac(fechaLocal); // Now you can pass the LocalDate
					break;
				case "nick":
					Usuario usuEjemploNick = repositorio.findByNick((String) valor).orElse(null);
					if (usuEjemploNick == null) {
						usu.setNick((String) valor);
					} else {
						if (usuEjemploNick.getId() == id) {
							usu.setNick((String) valor);
						} else {
							throw new RuntimeException("El nick ya está en uso.");
						}
					}
					break;
				case "email":
					Usuario usuEjemploEmail = repositorio.findByEmail((String) valor).orElse(null);
					if (usuEjemploEmail == null) {
						usu.setEmail((String) valor);
					} else {
						if (usuEjemploEmail.getId() == id) {
							usu.setEmail((String) valor);
						} else {
							throw new RuntimeException("El Email ya está en uso.");
						}
					}
					break;
				case "password":
					if (!passwordEncoder.matches((String) valor, usu.getPassword())) {
						throw new RuntimeException("La contraseña no es correcta.");
					}
					usu.setPassword(passwordEncoder.encode((String) updates.get("passwordNew")));
					break;
				case "privado":
					usu.setPrivado((valor instanceof Boolean) ? (Boolean) valor : false);
					break;
			}
		});

		return crearUsuario(usu);
	}

	@Override
	public Boolean usuarioPrivado(Long idUsuarioEmisor, String email) {
		Usuario usu = repositorio.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

		if (usu.getPrivado() && !usu.getEmail().equals(email)) {
			return peticionRepositorio.existsByUsuarioEmisorIdAndUsuarioReceptorId(idUsuarioEmisor, idUsuarioEmisor) &&
					peticionRepositorio.encontrarPeticion(idUsuarioEmisor, idUsuarioEmisor).getEstado() != Estado.ACEPTADO;
		}
		return false;
	}

	@Override
	public UsuarioDTO obtenerPorNick(String nick) {
		return new UsuarioDTO(
				repositorio.findByNick(nick)
						.orElseThrow(() -> new NotFoundException("Usuario con nick '" + nick + "' no encontrado")));
	}

	@Override
	public Boolean existePorNick(String nick) {
		UsuarioDTO usuario = new UsuarioDTO(repositorio.findByNick(nick)
				.orElseThrow(() -> new NotFoundException("Usuario con nick '" + nick + "' no existe")));
		return existePorEmail(usuario.getEmail());
	}

	@Override
	public UsuarioDTO actualizarMedia(Long id, MultipartFile imagen, MultipartFile banner) {
		try {
			Usuario usu = obtenerUsuario(id);
			if (imagen != null) {
				Imagen img = new Imagen(imagen.getOriginalFilename(), imagen.getContentType(), imagen.getBytes());
				imagenRepositorio.save(img);
				usu.setFoto(img);
			}
			if (banner != null) {
				Imagen bannerimg = new Imagen(banner.getOriginalFilename(), banner.getContentType(), banner.getBytes());
				imagenRepositorio.save(bannerimg);
				usu.setBanner(bannerimg);
			}
			return new UsuarioDTO(crearUsuario(usu));
		} catch (IOException e) {
			throw new RuntimeException("Error al procesar la imagen: " + e.getMessage()); // Manejo de excepción
		}
	};

}
