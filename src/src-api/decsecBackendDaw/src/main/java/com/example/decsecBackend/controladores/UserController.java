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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    AuthenticationService authenticationService;
	@Autowired
	private UsuarioServicioImpl usuarioservice;

	@GetMapping
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> listarUsuarios(
			@AuthenticationPrincipal Usuario usuario) {
		if (usuario.getRoles().contains(Role.ROLE_USER)) {
			return ResponseEntity.ok(usuarioservice.listarTodosUsuariosDTO());
		} else {
			return ResponseEntity.ok(usuarioservice.listarTodosUsuarios());
		}
	}

	@GetMapping("/search")
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> listarUsuariosSearch(
			@AuthenticationPrincipal Usuario usuario) {
		return ResponseEntity.ok(usuarioservice.listarTodosUsuariosSearchDTO());
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> listarUsuariosPorId(@PathVariable Long id,
			@AuthenticationPrincipal Usuario usuario) {
		if (usuario.getRoles().contains(Role.ROLE_ADMIN)) {
			if (usuarioservice.existePorId(id)) {
				return ResponseEntity.ok(usuarioservice.obtenerUsuario(id));
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con id:" + id + " no existe");
		} else {
			if (usuarioservice.existePorId(id)) {
				return ResponseEntity.ok(new UsuarioDTO(usuarioservice.obtenerUsuario(id)));
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con id:" + id + " no existe");
		}
	}

	@GetMapping("/token")
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> obtenerPorToken(@AuthenticationPrincipal Usuario usuario) {
		if (usuario.getRoles().contains(Role.ROLE_ADMIN)) {
			if (usuarioservice.existePorEmail(usuario.getEmail())) {
				return ResponseEntity.ok(usuarioservice.encontrarPorEmail(usuario.getEmail()));
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("El usuario con email:" + usuario.getEmail() + " no existe");
		} else {
			if (usuarioservice.existePorEmail(usuario.getEmail())) {
				return ResponseEntity.ok(new UsuarioDTO(usuarioservice.encontrarPorEmail(usuario.getEmail())));
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("El usuario con email:" + usuario.getEmail() + " no existe");
		}
	}

	@DeleteMapping
	@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> borrarMiUsuario(
			@AuthenticationPrincipal Usuario usuario) {
		usuarioservice.borrarUsuario(usuario.getId());
		return ResponseEntity.status(HttpStatus.OK)
				.body("Tu usuario con correo " + usuario.getEmail() + " ha sido eliminado");
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> borrarUsuarioPorId(@PathVariable Long id,
			@AuthenticationPrincipal Usuario usuario) {
		if (usuarioservice.existePorId(id)) {
			usuarioservice.borrarUsuario(id);
			return ResponseEntity.status(HttpStatus.OK).body("Usuario con id:" + id + " borrado correctamente");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con id:" + id + " no existe");
		}
	}

	@PatchMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> actualizarParcialmenteOtroUsuario(@PathVariable(required = false) Long id,
			@AuthenticationPrincipal Usuario usuario, @RequestBody Map<String, Object> updates) {
		if (usuarioservice.existePorId(id)) {
			return ResponseEntity.status(HttpStatus.OK).body(usuarioservice.actualizarUsuario(id,
					updates));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El usuario con id:" + id + " no existe");
		}
	}

@PatchMapping
@PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
public ResponseEntity<?> actualizarParcialmenteMiUsuario(
        @AuthenticationPrincipal Usuario usuario, @RequestBody Map<String, Object> updates) {
    try {
        // Llamada al servicio para actualizar el usuario
        Usuario resultadoUsuario = usuarioservice.actualizarUsuario(usuario.getId(), updates);

        // Convertir el usuario actualizado en un DTO
        UsuarioDTO resultado = new UsuarioDTO(resultadoUsuario);

        // Crear la respuesta con el usuario y el token
        UsuarioTokenResponse respuesta = new UsuarioTokenResponse(resultado, authenticationService.signupEdit(resultadoUsuario).getToken());

        // Devolver los datos con código 200
        return ResponseEntity.status(HttpStatus.OK).body(respuesta);

    } catch (Exception e) {
        // En caso de cualquier excepción, retornar un código 400 (Bad Request) con el mensaje de error
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
    }
}


	@GetMapping("/nick/{nick}")
	public ResponseEntity<?> devolverUsuarioNick(@PathVariable String nick,
			@AuthenticationPrincipal Usuario usuario) {
		try {
			return ResponseEntity.ok(usuarioservice.obtenerPorNick(nick));
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

}
