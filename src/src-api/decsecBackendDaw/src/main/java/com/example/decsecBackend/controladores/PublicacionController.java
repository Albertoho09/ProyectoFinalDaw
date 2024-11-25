package com.example.decsecBackend.controladores;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.decsecBackend.dtos.PublicacionDTOrequest;
import com.example.decsecBackend.errores.NotFoundException;
import com.example.decsecBackend.modelo.Role;
import com.example.decsecBackend.modelo.Usuario;
import com.example.decsecBackend.serviciosImpl.PublicacionServicioImpl;
import com.example.decsecBackend.serviciosImpl.UsuarioServicioImpl;
import com.google.gson.Gson;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api/v1/publicaciones")
public class PublicacionController {
    private static final Logger logger = LoggerFactory.getLogger(PublicacionController.class);

    @Autowired
    private PublicacionServicioImpl publicacionService;

    @Autowired
    private UsuarioServicioImpl usuarioService;

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public ResponseEntity<?> listarPublicaciones(@RequestParam(required = false) String email,
            @AuthenticationPrincipal Usuario usuario) {

        if (usuario.getRoles().contains(Role.ROLE_ADMIN)) {
            if (email != null) {
                logger.info("##### LISTANDO PUBLICACIONES USUARIO (ADMINISTRADOR) #####");
                return ResponseEntity.ok(publicacionService.listarPublicacionesUsuario(email));
            } else {
                logger.info("##### LISTANDO PUBLICACIONES (ADMINISTRADOR) #####");
                return ResponseEntity.ok(publicacionService.listarPublicaciones());
            }
        } else {
            if (email != null) {
                if (usuarioService.usuarioPrivado(email)) {
                    logger.info("##### PUBLICACIONES NO DISPONIBLE USUARIO PRIVADO (USUARIO) #####");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("Publicaciones no disponibles, usuario privado.");
                } else {
                    logger.info("##### LISTANDO PUBLICACIONES USUARIO (USUARIO) #####");
                    return ResponseEntity.ok(publicacionService.listarPublicacionesUsuario(email));
                }
            } else {
                logger.info("##### LISTANDO PUBLICACIONES PROPIAS (USUARIO) #####");
                return ResponseEntity.ok(publicacionService.listarPublicacionesUsuario(usuario.getEmail()));
            }
        }
    }

    @GetMapping("/publicacionesFeed")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> listarPublicacionesFeed(@AuthenticationPrincipal Usuario usuario) {
        try {
            logger.info("##### LISTANDO PUBLICACIONES FEED (USUARIO) #####");
            return ResponseEntity.ok(publicacionService.listarPublicacionesdFeed(usuario.getId()));
        } catch (Exception e) {
            logger.error("Error al listar publicaciones feed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al listar las publicaciones.");
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public ResponseEntity<?> crearPubli(@RequestPart("publicacion") String publicacion,
            @RequestParam(required = false) Map<String, MultipartFile> imagenes,
            @AuthenticationPrincipal Usuario usuario) {
        PublicacionDTOrequest publi;
        try {
            publi = new Gson().fromJson(publicacion, PublicacionDTOrequest.class);
            return ResponseEntity.ok(publicacionService.crearPublicacion(publi, imagenes, usuario));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> actualizarParcialmente(@PathVariable(required = true) Long id,
            @AuthenticationPrincipal Usuario usuario, @RequestBody Map<String, Object> updates) {

        if (publicacionService.pertenecePublicacion(id, usuario.getEmail())) {
            logger.info("##### ACTUALIZANDO PUBLICACION (USUARIO) #####");
            return ResponseEntity.ok(publicacionService.actualizarPublicacion(id, updates));
        } else {
            logger.info("##### ACTUALIZANDO PUBLICACION FALLIDA (USUARIO) #####");
            return ResponseEntity.badRequest().body("La publicacion que quieres editar no te pertenece");
        }

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public ResponseEntity<?> borrarPublicacion(@PathVariable(required = true) Long id,
            @AuthenticationPrincipal Usuario usuario) {
        try {
            if (usuario.getRoles().contains(Role.ROLE_ADMIN)) {
                publicacionService.borrarPublicacion(id);
                return ResponseEntity.status(HttpStatus.OK).body("Publicacion borrada exitosamente");
            } else {
                if (publicacionService.pertenecePublicacion(id, usuario.getEmail())) {
                    publicacionService.borrarPublicacion(id);
                    return ResponseEntity.status(HttpStatus.OK).body("Publicacion borrada exitosamente");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("La publicacion no te pertence");
                }
            }

        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La publicacion no existe");
        }
    }

    @PostMapping("darmegusta/{id}")
    public ResponseEntity<?> darMegusta(@PathVariable(required = true) Long id,
            @AuthenticationPrincipal Usuario usuario) {
        publicacionService.megusta(id);
        return ResponseEntity.status(HttpStatus.OK).body("Has indicado que te gusta la publicacion");
    }

    @PostMapping("quitarmegusta/{id}")
    public ResponseEntity<?> quitarMegusta(@PathVariable(required = true) Long id,
            @AuthenticationPrincipal Usuario usuario) {
        publicacionService.noMegusta(id);
        return ResponseEntity.status(HttpStatus.OK).body("Has indicado que no te gusta la publicacion");
    }

}
