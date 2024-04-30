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

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;
	@Autowired
	private UsuarioServicioImpl usuarioservice;
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestPart("usuario") String usuario,
                                                            @RequestPart(value = "imagen", required = false) MultipartFile imagen) {
        SignUpRequest request;
        try {
            request = new Gson().fromJson(usuario, SignUpRequest.class);
            return ResponseEntity.ok(authenticationService.signup(request, imagen));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }

    @GetMapping("/validar-email")
	public Boolean postMethodName(@RequestParam String email) {		
		if (usuarioservice.existePorEmail(email)) {
			return true;
		}
		return false;
	}
	
}
