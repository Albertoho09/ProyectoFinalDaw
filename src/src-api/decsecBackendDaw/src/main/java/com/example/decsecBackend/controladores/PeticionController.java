package com.example.decsecBackend.controladores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.decsecBackend.modelo.Usuario;
import com.example.decsecBackend.serviciosImpl.PeticionServicioImpl;
import com.example.decsecBackend.serviciosImpl.UsuarioServicioImpl;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api/v1/peticiones")
public class PeticionController {
    private static final Logger logger = LoggerFactory.getLogger(PublicacionController.class);

    @Autowired
    private PeticionServicioImpl peticionService;

    @Autowired
    private UsuarioServicioImpl usuarioService;

    @GetMapping("/{emailReceptor}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> obtenerPeticion(@PathVariable(required = true) String emailReceptor,
            @AuthenticationPrincipal Usuario usuario){
            return ResponseEntity.ok(peticionService.obtenerPeticion(usuario.getId(), emailReceptor));
    }
}
