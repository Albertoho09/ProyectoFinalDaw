package com.example.decsecBackend.controladores;

import java.util.Map;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.decsecBackend.errores.NotFoundException; // Añadir esta línea
import com.example.decsecBackend.dtos.SigninRequest;
import com.example.decsecBackend.dtos.UsuarioDTO;
import com.example.decsecBackend.dtos.UsuarioTokenResponse;
import com.example.decsecBackend.modelo.Role;
import com.example.decsecBackend.modelo.Usuario;
import com.example.decsecBackend.seguridad.JwtAuthenticationResponse;
import com.example.decsecBackend.serviciosImpl.UsuarioServicioImpl;
import com.example.decsecBackend.serviciosImpl.AuthenticationService;
import com.example.decsecBackend.serviciosImpl.JwtServiceImpl;

@CrossOrigin(origins = { "http://localhost:4200" }) // Habilita el acceso a recursos de origen cruzado
@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/v1/users") // Mapea las solicitudes a esta ruta
public class UserController {
	@Autowired // Inyecta el servicio de autenticación
	AuthenticationService authenticationService;
	@Autowired // Inyecta el servicio de usuarios
	private UsuarioServicioImpl usuarioservice;

	@GetMapping // Mapea una solicitud GET
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')") // Requiere autenticación con roles específicos
	public ResponseEntity<?> listarUsuarios(
			@AuthenticationPrincipal Usuario usuario) { // Obtiene el usuario autenticado
		if (usuario.getRoles().contains(Role.ROLE_USER)) { // Si el usuario es un usuario normal
			return ResponseEntity.ok(usuarioservice.listarTodosUsuariosDTO()); // Devuelve la lista de usuarios como DTO
		} else { // Si el usuario es administrador
			return ResponseEntity.ok(usuarioservice.listarTodosUsuarios()); // Devuelve la lista de usuarios sin DTO
		}
	}

	@GetMapping("/search") // Mapea una solicitud GET para búsqueda
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')") // Requiere autenticación con roles específicos
	public ResponseEntity<?> listarUsuariosSearch(
			@AuthenticationPrincipal Usuario usuario) { // Obtiene el usuario autenticado
		return ResponseEntity.ok(usuarioservice.listarTodosUsuariosSearchDTO()); // Devuelve la lista de usuarios para búsqueda como DTO
	}

	@GetMapping("/{id}") // Mapea una solicitud GET para un usuario específico
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')") // Requiere autenticación con roles específicos
	public ResponseEntity<?> listarUsuariosPorId(@PathVariable Long id, // Obtiene el id del usuario
			@AuthenticationPrincipal Usuario usuario) { // Obtiene el usuario autenticado
		if (usuario.getRoles().contains(Role.ROLE_ADMIN)) { // Si el usuario es administrador
			if (usuarioservice.existePorId(id)) { // Verifica si el usuario existe
				return ResponseEntity.ok(usuarioservice.obtenerUsuario(id)); // Devuelve el usuario
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con id:" + id + " no existe"); // Devuelve un mensaje de error si no existe
		} else { // Si el usuario es un usuario normal
			if (usuarioservice.existePorId(id)) { // Verifica si el usuario existe
				return ResponseEntity.ok(new UsuarioDTO(usuarioservice.obtenerUsuario(id))); // Devuelve el usuario como DTO
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con id:" + id + " no existe"); // Devuelve un mensaje de error si no existe
		}
	}

	@GetMapping("/token") // Mapea una solicitud GET para obtener un usuario por token
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')") // Requiere autenticación con roles específicos
	public ResponseEntity<?> obtenerPorToken(@AuthenticationPrincipal Usuario usuario) { // Obtiene el usuario autenticado
		if (usuario.getRoles().contains(Role.ROLE_ADMIN)) { // Si el usuario es administrador
			if (usuarioservice.existePorEmail(usuario.getEmail())) { // Verifica si el usuario existe por email
				return ResponseEntity.ok(usuarioservice.encontrarPorEmail(usuario.getEmail())); // Devuelve el usuario
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("El usuario con email:" + usuario.getEmail() + " no existe"); // Devuelve un mensaje de error si no existe
		} else { // Si el usuario es un usuario normal
			if (usuarioservice.existePorEmail(usuario.getEmail())) { // Verifica si el usuario existe por email
				return ResponseEntity.ok(new UsuarioDTO(usuarioservice.encontrarPorEmail(usuario.getEmail()))); // Devuelve el usuario como DTO
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("El usuario con email:" + usuario.getEmail() + " no existe"); // Devuelve un mensaje de error si no existe
		}
	}

	@DeleteMapping // Mapea una solicitud DELETE para borrar el usuario autenticado
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')") // Requiere autenticación con roles específicos
	public ResponseEntity<?> borrarMiUsuario(
			@AuthenticationPrincipal Usuario usuario) { // Obtiene el usuario autenticado
		usuarioservice.borrarUsuario(usuario.getId()); // Borra el usuario
		return ResponseEntity.status(HttpStatus.OK)
				.body("Tu usuario con correo " + usuario.getEmail() + " ha sido eliminado"); // Devuelve un mensaje de confirmación
	}

	@DeleteMapping("/{email}") // Mapea una solicitud DELETE para borrar un usuario por email
	@PreAuthorize("hasRole('ROLE_ADMIN')") // Requiere autenticación con el rol de administrador
	public ResponseEntity<?> borrarUsuarioPorEmail(@PathVariable(required = true) String email, // Obtiene el email del usuario a borrar
			@AuthenticationPrincipal Usuario usuario) { // Obtiene el usuario autenticado
		if (usuarioservice.existePorEmail(email)) { // Verifica si el usuario existe
			Usuario usu = usuarioservice.encontrarPorEmail(email); // Encuentra el usuario por email
			usuarioservice.borrarUsuario(usu.getId()); // Borra el usuario
			return ResponseEntity.ok(Map.of("mensaje", "Usuario con email:" + email + " borrado correctamente")); // Devuelve un mensaje de confirmación
		} else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "El usuario no existe")); // Devuelve un mensaje de error si no existe
		}
	}

	@PatchMapping("/{id}") // Mapea una solicitud PATCH para actualizar un usuario específico
	@PreAuthorize("hasRole('ROLE_ADMIN')") // Requiere autenticación con el rol de administrador
	public ResponseEntity<?> actualizarParcialmenteOtroUsuario(@PathVariable(required = false) Long id, // Obtiene el id del usuario a actualizar
			@AuthenticationPrincipal Usuario usuario, @RequestBody Map<String, Object> updates) { // Obtiene el usuario autenticado y los datos de actualización
		if (usuarioservice.existePorId(id)) { // Verifica si el usuario existe
			return ResponseEntity.status(HttpStatus.OK).body(usuarioservice.actualizarUsuario(id, updates)); // Actualiza el usuario y devuelve los datos
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con id:" + id + " no existe"); // Devuelve un mensaje de error si no existe
		}
	}

	@PatchMapping // Mapea una solicitud PATCH para actualizar el usuario autenticado
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')") // Requiere autenticación con roles específicos
	public ResponseEntity<?> actualizarParcialmenteMiUsuario(
			@AuthenticationPrincipal Usuario usuario, @RequestBody Map<String, Object> updates) { // Obtiene el usuario autenticado y los datos de actualización
		try {
			// Llamada al servicio para actualizar el usuario
			Usuario resultadoUsuario = usuarioservice.actualizarUsuario(usuario.getId(), updates);

			// Convertir el usuario actualizado en un DTO
			UsuarioDTO resultado = new UsuarioDTO(resultadoUsuario);

			// Crear la respuesta con el usuario y el token
			UsuarioTokenResponse respuesta = new UsuarioTokenResponse(resultado,
					authenticationService.signupEdit(resultadoUsuario).getToken());

			// Devolver los datos con código 200
			return ResponseEntity.status(HttpStatus.OK).body(respuesta);

		} catch (Exception e) {
			// En caso de cualquier excepción, retornar un código 400 (Bad Request) con el
			// mensaje de error
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
		}
	}

	@PatchMapping("/actualizarMedia") // Mapea una solicitud PATCH para actualizar la media del usuario autenticado
	@PreAuthorize("hasRole('ROLE_USER')") // Requiere autenticación con el rol de usuario
	public ResponseEntity<?> actualizarParcialmenteMiUsuarioMedia(
			@AuthenticationPrincipal Usuario usuario, 
			@RequestPart(value = "imagen", required = false) MultipartFile imagen,
            @RequestPart(value = "banner", required = false) MultipartFile banner) {
				try {
					UsuarioDTO respuesta = usuarioservice.actualizarMedia(usuario.getId(), imagen, banner);
					// Devolver los datos con código 200
					return ResponseEntity.status(HttpStatus.OK).body(respuesta);
		
				} catch (Exception e) {
					// En caso de cualquier excepción, retornar un código 400 (Bad Request) con el
					// mensaje de error
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
				}
	}


	@GetMapping("/nick/{nick}") // Mapea una solicitud GET para obtener un usuario por nick
	public ResponseEntity<?> devolverUsuarioNick(@PathVariable String nick, // Obtiene el nick del usuario
			@AuthenticationPrincipal Usuario usuario) { // Obtiene el usuario autenticado
		try {
			return ResponseEntity.ok(usuarioservice.obtenerPorNick(nick)); // Devuelve el usuario
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Devuelve un mensaje de error si no existe
		}
	}

}
