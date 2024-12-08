package com.example.decsecBackend.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.decsecBackend.modelo.Usuario;
import com.example.decsecBackend.serviciosImpl.PeticionServicioImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@CrossOrigin(origins = {"http://localhost:4200"}) // Habilita el acceso a recursos de origen cruzado
@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/api/v1/peticiones") // Mapea las solicitudes a esta ruta
public class PeticionController {

    @Autowired
    private PeticionServicioImpl peticionService; // Inyecta el servicio de peticiones

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_USER')") // Requiere autenticación con el rol de usuario
    public ResponseEntity<?> obtenerPeticion(@RequestParam(required = false) String emailReceptor,
            @AuthenticationPrincipal Usuario usuario){
                if(emailReceptor != null){
                    return ResponseEntity.ok(peticionService.obtenerPeticion(usuario.getId(), emailReceptor)); // Devuelve la petición específica
                }else{
                    return ResponseEntity.ok(peticionService.misPeticiones(usuario.getId())); // Devuelve las peticiones del usuario
                }
    }

    @GetMapping("/amigos")
    @PreAuthorize("hasRole('ROLE_USER')") // Requiere autenticación con el rol de usuario
    public ResponseEntity<?> obtenerPeticionAmigos(@AuthenticationPrincipal Usuario usuario){
        System.out.println(usuario.getNombre()); // Imprime el nombre del usuario
        return ResponseEntity.ok(peticionService.misAmigos(usuario.getId())); // Devuelve las peticiones de amigos
    }

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_USER')") // Requiere autenticación con el rol de usuario
    public ResponseEntity<?> emviarPeticion(@RequestParam(required = true) String emailReceptor,
            @AuthenticationPrincipal Usuario usuario){
            return ResponseEntity.ok(peticionService.enviarPeticion(usuario, emailReceptor)); // Envía una petición
    }

    @PutMapping("/cambiarEstado")
    @PreAuthorize("hasRole('ROLE_USER')") // Requiere autenticación con el rol de usuario
    public  ResponseEntity<?> cambiarEstado(
                                      @RequestParam Long id,
                                      @RequestParam String estado, @AuthenticationPrincipal Usuario usuario) {
        try {
            return ResponseEntity.ok(peticionService.cambiarEstado(id, estado)); // Cambia el estado de una petición
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // Maneja errores
        }
    }
}
