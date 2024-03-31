package com.example.decsecBackend.entidades;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.decsecBackend.modelo.Comentario;
import com.example.decsecBackend.modelo.Publicacion;
import com.example.decsecBackend.modelo.Role;
import com.example.decsecBackend.modelo.Usuario;
import com.example.decsecBackend.repositorios.ComentarioRepositorio;
import com.example.decsecBackend.repositorios.PublicacionRepositorio;
import com.example.decsecBackend.repositorios.UsuarioRepositorio;

import jakarta.transaction.Transactional;

@SpringBootTest
public class ComentarioTest {

    @Autowired
    private ComentarioRepositorio comentarioRepositorio;

    @Autowired
    private UsuarioRepositorio usarioRepositorio;

    @Autowired
    private PublicacionRepositorio publicacionRepositorio;

    @SuppressWarnings("null")
    @Test
    @Transactional
    public void testGuardarComentario() {
        
        Comentario comentario = new Comentario();
        comentario.setComentario("Este es un comentario de prueba");
        comentario.setHora(LocalDateTime.now());

        Usuario usuario = new Usuario();
        usuario.setNick("testComentarioCrear");
        usuario.setNombre("John");
        usuario.setApellidos("Doe");
        usuario.setFechaNac(LocalDate.of(1990, 1, 1));
        usuario.setPassword("password");
        usuario.setPrivado(false);
        usuario.setEmail("testCrearComentario@example.com");

        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        usuario.setRoles(roles);

        Publicacion publicacion = new Publicacion();
        publicacion.setComentarioUsuario("Comentario de prueba");
        publicacion.setUsuario(usuario);
        
        comentario.setPublicacion(publicacion);
        comentario.setUsuario(usuario);

        usarioRepositorio.save(usuario);
        publicacionRepositorio.save(publicacion);
        comentarioRepositorio.save(comentario);

        Comentario comentarioRecuperado = comentarioRepositorio.findById(comentario.getId()).orElse(null);

        assertNotNull(comentarioRecuperado);
        assertEquals("Este es un comentario de prueba", comentarioRecuperado.getComentario());
    }
}
