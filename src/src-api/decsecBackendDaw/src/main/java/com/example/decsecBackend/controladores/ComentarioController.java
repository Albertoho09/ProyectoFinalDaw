package com.example.decsecBackend.controladores;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.decsecBackend.dtos.ComentarioDTOrequest;
import com.example.decsecBackend.errores.NotFoundException;
import com.example.decsecBackend.modelo.Comentario;
import com.example.decsecBackend.modelo.Role;
import com.example.decsecBackend.modelo.Usuario;
import com.example.decsecBackend.serviciosImpl.ComentarioServicioImpl;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = {"http://localhost:4200"}) // Habilita el acceso a recursos de origen cruzado
@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/v1/comentarios") // Mapea las solicitudes a esta ruta
public class ComentarioController {

    @Autowired
    private ComentarioServicioImpl comentarioServicio; // Inyecta el servicio de comentarios

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')") // Requiere autenticación con roles específicos
    public ResponseEntity<?> listarTodosComentarios(@AuthenticationPrincipal Usuario usuario) {
        // Determina si el usuario autenticado es administrador o usuario para mostrar comentarios
        if (usuario.getRoles().contains(Role.ROLE_ADMIN)) {
            return ResponseEntity.ok(comentarioServicio.listarComentarios()); // Devuelve todos los comentarios si es administrador
        } else {
            return ResponseEntity.ok(comentarioServicio.listarMisComentarios(usuario.getEmail())); // Devuelve los comentarios del usuario si es usuario
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')") // Requiere autenticación con roles específicos
    public ResponseEntity<?> listarComentariosPublicacion(@PathVariable(required = true) Long id,
            @AuthenticationPrincipal Usuario usuario) {
        // Devuelve los comentarios de una publicación específica
        return ResponseEntity.ok(comentarioServicio.listarComentariosPublicacion(id));
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')") // Requiere autenticación con roles específicos
    public ResponseEntity<?> crearComentario(@RequestBody ComentarioDTOrequest comentario,
            @PathVariable(required = true) Long id,
            @AuthenticationPrincipal Usuario usuario) {
        // Crea un nuevo comentario
        Comentario coment = new Comentario();
        coment.setComentario(comentario.getComentario());
        // Devuelve el comentario creado con código de estado HTTP 201
        return ResponseEntity.status(HttpStatus.CREATED).body(
                comentarioServicio.crearComentario(coment, usuario.getEmail(), id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')") // Requiere autenticación con roles específicos
    public ResponseEntity<?> actualizarComentario(@RequestBody ComentarioDTOrequest comentario,
    @PathVariable(required = true) Long id, @AuthenticationPrincipal Usuario usuario) {
        try {
            // Verifica si el comentario pertenece al usuario autenticado
            if (comentarioServicio.comentarioPerteneceAUsuario(id, usuario.getEmail())) {
                // Actualiza el comentario y devuelve el resultado
                return ResponseEntity.status(HttpStatus.CREATED).body(comentarioServicio.actualizarComentario(comentario.getComentario(), id));
            }else{
                // Devuelve un mensaje de error si el comentario no pertenece al usuario
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("El comentario no te pertence");
            }
        } catch (NotFoundException e) {
            // Maneja la excepción si el comentario no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El comentario no existe");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')") // Requiere autenticación con roles específicos
    public ResponseEntity<?> borraComentario(@PathVariable(required = true)Long id, @AuthenticationPrincipal Usuario usuario) {
        try {
            // Determina si el usuario autenticado es administrador o usuario para borrar comentarios
            if (usuario.getRoles().contains(Role.ROLE_ADMIN)) {
                // Borra el comentario si es administrador
                comentarioServicio.borrarComentario(id);
                return ResponseEntity.ok(Map.of("mensaje", "Comentario borrado exitosamente"));
            }else{
                // Verifica si el comentario pertenece al usuario autenticado si es usuario
                if (comentarioServicio.comentarioPerteneceAUsuario(id, usuario.getEmail())) {
                    // Borra el comentario si pertenece al usuario
                    comentarioServicio.borrarComentario(id);
                    return ResponseEntity.ok(Map.of("mensaje", "Comentario borrado exitosamente"));
                }else{
                    // Devuelve un mensaje de error si el comentario no pertenece al usuario
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(Map.of("error", "El comentario no te pertence"));
                }
            }
        } catch (NotFoundException e) {
            // Maneja la excepción si el comentario no existe
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "El comentario no existe"));
        }
    }
}
