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
	private UsuarioRepositorio repositorio; // Inyección de dependencia del repositorio de usuarios
	@Autowired
	private PeticionRepositorio peticionRepositorio; // Inyección de dependencia del repositorio de peticiones
	@Autowired
	private ImagenRepositorio imagenRepositorio; // Inyección de dependencia del repositorio de imágenes

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Instancia de codificador de contraseñas

	@SuppressWarnings("null")
	@Override
	public Usuario crearUsuario(Usuario usuario) {
		return repositorio.save(usuario); // Guarda un nuevo usuario en la base de datos
	}

	@Override
	public Usuario obtenerUsuario(Long id) {
		return repositorio.findById(id).orElseThrow(() -> new NotFoundException("Usuario no encontrado")); // Busca un usuario por su ID y lanza una excepción si no se encuentra
	}

	@SuppressWarnings("null")
	@Override
	public void borrarUsuario(Long id) {
		repositorio.deleteById(id); // Elimina un usuario de la base de datos por su ID
	}

	@Override
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {
			 @Override
			public UserDetails loadUserByUsername(String username) {
				return repositorio.findByEmail(username)
						.orElseThrow(() -> new UsernameNotFoundException("User not found")); // Busca un usuario por su email y lanza una excepción si no se encuentra
			}
		};
	}

	@Override
	public List<UsuarioDTO> listarTodosUsuariosDTO() {
		return repositorio.findAll().stream()
				.map(usuario -> new UsuarioDTO(usuario))
				.collect(Collectors.toList()); // Convierte una lista de usuarios a una lista de DTO de usuarios
	}

	@Override
	public List<UsuarioSearchDTO> listarTodosUsuariosSearchDTO() {
		return repositorio.findAll().stream()
				.map(usuario -> new UsuarioSearchDTO(usuario.getNick(), usuario.getFoto()))
				.collect(Collectors.toList()); // Convierte una lista de usuarios a una lista de DTO de búsqueda de usuarios
	}

	@Override
	public List<Usuario> listarTodosUsuarios() {
		return repositorio.findAll(); // Retorna una lista de todos los usuarios
	}

	@Override
	public Boolean existePorEmail(String email) {
		return repositorio.existsByEmail(email); // Verifica si un usuario existe por su email
	}

	@Override
	public Usuario encontrarPorEmail(String email) {
		return repositorio.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("Invalid email or password.")); // Busca un usuario por su email y lanza una excepción si no se encuentra
	}

	@Override
	public Boolean existePorId(Long id) {
		return repositorio.existsById(id); // Verifica si un usuario existe por su ID
	}

	@Override
	public Usuario actualizarUsuario(Long id, Map<String, Object> updates) {
		Usuario usu = obtenerUsuario(id); // Obtiene el usuario a actualizar

		updates.forEach((campo, valor) -> {
			switch (campo) {
				case "nombre":
					usu.setNombre((String) valor); // Actualiza el nombre del usuario
					break;
				case "apellidos":
					usu.setApellidos((String) valor); // Actualiza los apellidos del usuario
					break;
				case "fechaNac":
					OffsetDateTime fechaOffset = OffsetDateTime.parse(valor.toString()); // Parsea el valor de la fecha de nacimiento a OffsetDateTime
					LocalDate fechaLocal = fechaOffset.toLocalDate(); // Convierte OffsetDateTime a LocalDate
					usu.setFechaNac(fechaLocal); // Actualiza la fecha de nacimiento del usuario
					break;
				case "nick":
					Usuario usuEjemploNick = repositorio.findByNick((String) valor).orElse(null); // Busca un usuario por el nuevo nick
					if (usuEjemploNick == null) {
						usu.setNick((String) valor); // Actualiza el nick del usuario si no está en uso
					} else {
						if (usuEjemploNick.getId() == id) {
							usu.setNick((String) valor); // Actualiza el nick del usuario si es el mismo usuario
						} else {
							throw new RuntimeException("El nick ya está en uso."); // Lanza una excepción si el nick ya está en uso
						}
					}
					break;
				case "email":
					Usuario usuEjemploEmail = repositorio.findByEmail((String) valor).orElse(null); // Busca un usuario por el nuevo email
					if (usuEjemploEmail == null) {
						usu.setEmail((String) valor); // Actualiza el email del usuario si no está en uso
					} else {
						if (usuEjemploEmail.getId() == id) {
							usu.setEmail((String) valor); // Actualiza el email del usuario si es el mismo usuario
						} else {
							throw new RuntimeException("El Email ya está en uso."); // Lanza una excepción si el email ya está en uso
						}
					}
					break;
				case "password":
					if (!passwordEncoder.matches((String) valor, usu.getPassword())) {
						throw new RuntimeException("La contraseña no es correcta."); // Lanza una excepción si la contraseña actual no coincide
					}
					usu.setPassword(passwordEncoder.encode((String) updates.get("passwordNew"))); // Actualiza la contraseña del usuario
					break;
				case "privado":
					usu.setPrivado((valor instanceof Boolean) ? (Boolean) valor : false); // Actualiza el estado de privacidad del usuario
					break;
			}
		});

		return crearUsuario(usu); // Guarda los cambios del usuario
	}

	@Override
	public Boolean usuarioPrivado(Long idUsuarioEmisor, String email) {
		Usuario usu = repositorio.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("Invalid email or password.")); // Busca el usuario receptor por email

		if (usu.getPrivado() && !usu.getEmail().equals(email)) {
			return peticionRepositorio.existsByUsuarioEmisorIdAndUsuarioReceptorId(idUsuarioEmisor, idUsuarioEmisor) &&
					peticionRepositorio.encontrarPeticion(idUsuarioEmisor, idUsuarioEmisor).getEstado() != Estado.ACEPTADO; // Verifica si el usuario es privado y si hay una petición pendiente
		}
		return false; // Retorna falso si el usuario no es privado o si la petición está aceptada
	}

	@Override
	public UsuarioDTO obtenerPorNick(String nick) {
		return new UsuarioDTO(
				repositorio.findByNick(nick)
						.orElseThrow(() -> new NotFoundException("Usuario con nick '" + nick + "' no encontrado"))); // Busca un usuario por su nick y devuelve un DTO
	}

	@Override
	public Boolean existePorNick(String nick) {
		UsuarioDTO usuario = new UsuarioDTO(repositorio.findByNick(nick)
				.orElseThrow(() -> new NotFoundException("Usuario con nick '" + nick + "' no existe"))); // Busca un usuario por su nick y devuelve un DTO
		return existePorEmail(usuario.getEmail()); // Verifica si el usuario existe por su email
	}

	@Override
	public UsuarioDTO actualizarMedia(Long id, MultipartFile imagen, MultipartFile banner) {
		try {
			Usuario usu = obtenerUsuario(id); // Obtiene el usuario a actualizar
			if (imagen != null) {
				Imagen img = new Imagen(imagen.getOriginalFilename(), imagen.getContentType(), imagen.getBytes()); // Crea una nueva imagen
				imagenRepositorio.save(img); // Guarda la imagen
				usu.setFoto(img); // Actualiza la foto del usuario
			}
			if (banner != null) {
				Imagen bannerimg = new Imagen(banner.getOriginalFilename(), banner.getContentType(), banner.getBytes()); // Crea una nueva imagen de banner
				imagenRepositorio.save(bannerimg); // Guarda la imagen de banner
				usu.setBanner(bannerimg); // Actualiza el banner del usuario
			}
			return new UsuarioDTO(crearUsuario(usu)); // Devuelve un DTO del usuario actualizado
		} catch (IOException e) {
			throw new RuntimeException("Error al procesar la imagen: " + e.getMessage()); // Manejo de excepción
		}
	};

}
