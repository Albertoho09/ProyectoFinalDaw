package com.example.decsecBackend.controladores;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.decsecBackend.dtos.PublicacionDTOrequest;
import com.example.decsecBackend.errores.NotFoundException;
import com.example.decsecBackend.modelo.Role;
import com.example.decsecBackend.modelo.Usuario;
import com.example.decsecBackend.serviciosImpl.PublicacionServicioImpl;
import com.example.decsecBackend.serviciosImpl.UsuarioServicioImpl;
import com.google.gson.Gson;

@CrossOrigin(origins = { "http://localhost:4200" }) // Habilita el acceso a recursos de origen cruzado
@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/v1/publicaciones") // Mapea las solicitudes a esta ruta
public class PublicacionController {
    private static final Logger logger = LoggerFactory.getLogger(PublicacionController.class); // Inicializa el logger para registros

    @Autowired // Inyecta el servicio de publicaciones
    private PublicacionServicioImpl publicacionService;

    @Autowired // Inyecta el servicio de usuarios
    private UsuarioServicioImpl usuarioService;

    @GetMapping() // Mapea una solicitud GET para listar publicaciones
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')") // Requiere autenticación con roles específicos
    public ResponseEntity<?> listarPublicaciones(@RequestParam(required = false) String email, // Parámetro opcional para filtrar por email
            @RequestParam(required = false) Boolean megusta, // Parámetro opcional para filtrar por "me gusta"
            @AuthenticationPrincipal Usuario usuario) { // Obtiene el usuario autenticado

        if (usuario.getRoles().contains(Role.ROLE_ADMIN)) { // Si el usuario es administrador
            if (email != null) {
                logger.info("##### LISTANDO PUBLICACIONES USUARIO (ADMINISTRADOR) #####");
                return ResponseEntity.ok(publicacionService.listarPublicacionesUsuario(email, usuario)); // Devuelve las publicaciones del usuario especificado
            } else {
                logger.info("##### LISTANDO PUBLICACIONES (ADMINISTRADOR) #####");
                return ResponseEntity.ok(publicacionService.listarPublicaciones(usuario)); // Devuelve todas las publicaciones
            }
        } else { // Si el usuario es un usuario normal
            if (email != null) {
                if (usuarioService.usuarioPrivado(usuario.getId(), email)) { // Verifica si el usuario es privado
                    logger.info("##### PUBLICACIONES NO DISPONIBLE USUARIO PRIVADO (USUARIO) #####");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN) // Devuelve un código de estado 403 (Forbidden)
                            .body("Publicaciones no disponibles, usuario privado.");
                } else {
                    logger.info("##### LISTANDO PUBLICACIONES USUARIO (USUARIO) #####");
                    if (megusta != null) {
                        return ResponseEntity.ok(publicacionService.listarPublicacionesConMeGusta(email, usuario)); // Devuelve las publicaciones con "me gusta"
                    }
                    return ResponseEntity.ok(publicacionService.listarPublicacionesUsuario(email, usuario)); // Devuelve las publicaciones del usuario especificado
                }
            } else {
                logger.info("##### LISTANDO PUBLICACIONES PROPIAS (USUARIO) #####");
                return ResponseEntity.ok(publicacionService.listarPublicacionesUsuario(usuario.getEmail(), usuario)); // Devuelve las publicaciones del usuario autenticado
            }
        }
    }

    @GetMapping("/publicacionesFeed") // Mapea una solicitud GET para el feed de publicaciones
    @PreAuthorize("hasRole('ROLE_USER')") // Requiere autenticación con el rol de usuario
    public ResponseEntity<?> listarPublicacionesFeed(@AuthenticationPrincipal Usuario usuario, // Obtiene el usuario autenticado
            @RequestParam(required = true) int dias) { // Parámetro requerido para el número de días
        try {
            logger.info("##### LISTANDO PUBLICACIONES FEED (USUARIO) #####");
            return ResponseEntity.ok(publicacionService.listarPublicacionesdFeed(usuario.getId(), dias, usuario)); // Devuelve el feed de publicaciones
        } catch (Exception e) {
            logger.error("Error al listar publicaciones feed: {}", e.getMessage()); // Registra el error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // Devuelve un código de estado 500 (Internal Server Error)
                    .body("Error al listar las publicaciones.");
        }
    }

    @GetMapping("/publicacionesMeGusta") // Mapea una solicitud GET para las publicaciones con "me gusta"
    @PreAuthorize("hasRole('ROLE_USER')") // Requiere autenticación con el rol de usuario
    public ResponseEntity<?> listarPublicacionesMegusta(@AuthenticationPrincipal Usuario usuario, // Obtiene el usuario autenticado
            @RequestParam(required = true) int dias) { // Parámetro requerido para el número de días
        try {
            logger.info("##### LISTANDO PUBLICACIONES FEED (USUARIO) #####");
            return ResponseEntity.ok(publicacionService.listarPublicacionesdFeed(usuario.getId(), dias, usuario)); // Devuelve el feed de publicaciones
        } catch (Exception e) {
            logger.error("Error al listar publicaciones feed: {}", e.getMessage()); // Registra el error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // Devuelve un código de estado 500 (Internal Server Error)
                    .body("Error al listar las publicaciones.");
        }
    }

    @PostMapping // Mapea una solicitud POST para crear una publicación
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')") // Requiere autenticación con roles específicos
    public ResponseEntity<?> crearPubli(@RequestPart("publicacion") String publicacion, // Parte de la solicitud para la publicación
            @RequestParam(required = false) Map<String, MultipartFile> imagenes, // Parámetro opcional para las imágenes
            @AuthenticationPrincipal Usuario usuario) { // Obtiene el usuario autenticado
        PublicacionDTOrequest publi;
        try {
            publi = new Gson().fromJson(publicacion, PublicacionDTOrequest.class); // Convierte la cadena JSON a un objeto PublicacionDTOrequest
            return ResponseEntity.ok(publicacionService.crearPublicacion(publi, imagenes, usuario)); // Crea la publicación y devuelve la respuesta
        } catch (IOException e) {
            System.out.println(e.getMessage()); // Imprime el mensaje de error
            return ResponseEntity.badRequest().body(e.getMessage()); // Devuelve un código de estado 400 (Bad Request) con el mensaje de error
        }
    }

    @PatchMapping("/{id}") // Mapea una solicitud PATCH para actualizar parcialmente una publicación
    @PreAuthorize("hasRole('ROLE_USER')") // Requiere autenticación con el rol de usuario
    public ResponseEntity<?> actualizarParcialmente(@PathVariable(required = true) Long id, // Parámetro requerido para el id de la publicación
            @AuthenticationPrincipal Usuario usuario, @RequestBody Map<String, Object> updates) { // Obtiene el usuario autenticado y los datos de actualización

        if (publicacionService.pertenecePublicacion(id, usuario.getEmail())) { // Verifica si la publicación pertenece al usuario autenticado
            logger.info("##### ACTUALIZANDO PUBLICACION (USUARIO) #####");
            return ResponseEntity.ok(publicacionService.actualizarPublicacion(id, updates, usuario)); // Actualiza la publicación y devuelve la respuesta
        } else {
            logger.info("##### ACTUALIZANDO PUBLICACION FALLIDA (USUARIO) #####");
            return ResponseEntity.badRequest().body("La publicacion que quieres editar no te pertenece"); // Devuelve un mensaje de error
        }

    }

    @DeleteMapping("/{id}") // Mapea una solicitud DELETE para borrar una publicación
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')") // Requiere autenticación con roles específicos
    public ResponseEntity<?> borrarPublicacion(@PathVariable(required = true) Long id, // Parámetro requerido para el id de la publicación
            @AuthenticationPrincipal Usuario usuario) { // Obtiene el usuario autenticado
        try {
            if (usuario.getRoles().contains(Role.ROLE_ADMIN)) { // Si el usuario es administrador
                publicacionService.borrarPublicacion(id); // Borra la publicación
                return ResponseEntity.ok(Map.of("mensaje", "Publicacion borrada exitosamente")); // Devuelve un mensaje de confirmación
            } else { // Si el usuario es un usuario normal
                if (publicacionService.pertenecePublicacion(id, usuario.getEmail())) { // Verifica si la publicación pertenece al usuario autenticado
                    publicacionService.borrarPublicacion(id); // Borra la publicación
                    return ResponseEntity.ok(Map.of("mensaje", "Publicacion borrada exitosamente")); // Devuelve un mensaje de confirmación
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE) // Devuelve un código de estado 406 (Not Acceptable)
                            .body(Map.of("error", "La publicacion no te pertence")); // Devuelve un mensaje de error
                }
            }
    
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND) // Devuelve un código de estado 404 (Not Found)
                    .body(Map.of("error", "La publicacion no existe")); // Devuelve un mensaje de error
        }
    }
    

    @PostMapping("darmegusta/{id}") // Mapea una solicitud POST para dar "me gusta" a una publicación
    public ResponseEntity<?> darMegusta(@PathVariable(required = true) Long id, // Parámetro requerido para el id de la publicación
            @AuthenticationPrincipal Usuario usuario) { // Obtiene el usuario autenticado
        publicacionService.megusta(id, usuario.getId()); // Registra el "me gusta"
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Me gusta registrado con éxito"); // Crea un mensaje de respuesta
        return ResponseEntity.ok(respuesta); // Devuelve la respuesta
    }

    @PostMapping("quitarmegusta/{id}") // Mapea una solicitud POST para quitar "me gusta" a una publicación
    public ResponseEntity<?> quitarMegusta(@PathVariable(required = true) Long id, // Parámetro requerido para el id de la publicación
            @AuthenticationPrincipal Usuario usuario) { // Obtiene el usuario autenticado
        publicacionService.noMegusta(id, usuario.getId()); // Quita el "me gusta"
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Me gusta registrado con éxito"); // Crea un mensaje de respuesta
        return ResponseEntity.ok(respuesta); // Devuelve la respuesta
    }

}
