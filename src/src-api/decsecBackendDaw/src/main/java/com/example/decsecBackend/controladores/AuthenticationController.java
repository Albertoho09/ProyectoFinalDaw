package com.example.decsecBackend.controladores;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.decsecBackend.dtos.*;
import com.example.decsecBackend.seguridad.JwtAuthenticationResponse;
import com.example.decsecBackend.serviciosImpl.AuthenticationService;
import com.example.decsecBackend.serviciosImpl.UsuarioServicioImpl;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;

// Esta anotación indica que esta clase es un controlador REST
@RestController
// Esta anotación mapea las solicitudes a esta ruta
@RequestMapping("/api/v1/auth")
// Esta anotación habilita el acceso a recursos de origen cruzado
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:4200" })
public class AuthenticationController {
    // Inyección de dependencias para el servicio de autenticación
    @Autowired
    AuthenticationService authenticationService;
    // Inyección de dependencias para el servicio de usuarios
    @Autowired
    private UsuarioServicioImpl usuarioservice;

    // Método para el registro de usuarios
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestPart("usuario") String usuario,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen,
            @RequestPart(value = "banner", required = false) MultipartFile banner) {
        SignUpRequest request;
        try {
            // Conversión de la cadena JSON a un objeto SignUpRequest
            request = new Gson().fromJson(usuario, SignUpRequest.class);
            // Llamada al servicio de autenticación para el registro
            return ResponseEntity.ok(authenticationService.signup(request, imagen, banner));
        } catch (IOException e) {
            // Manejo de errores de conversión
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Método para el inicio de sesión
    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
        // Llamada al servicio de autenticación para el inicio de sesión
        return ResponseEntity.ok(authenticationService.signin(request));
    }

    // Método para validar si un email ya está registrado
    @GetMapping("/validar-email")
    public Boolean postMethodName(@RequestParam String email) {
        // Verificación si el email ya está registrado
        if (usuarioservice.existePorEmail(email)) {
            return true;
        }
        return false;
    }

}
