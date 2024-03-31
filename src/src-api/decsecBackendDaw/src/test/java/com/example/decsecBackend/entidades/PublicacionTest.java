package com.example.decsecBackend.entidades;

import com.example.decsecBackend.modelo.Publicacion;
import com.example.decsecBackend.modelo.Usuario;
import com.example.decsecBackend.repositorios.PublicacionRepositorio;
import com.example.decsecBackend.repositorios.UsuarioRepositorio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class PublicacionTest {

    @Autowired
    private PublicacionRepositorio publicacionRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Test
    void testCrearPublicacion() {
        // Crear un usuario de prueba
        Usuario usuario = new Usuario();
        usuario.setNick("testUser");
        usuario.setNombre("John");
        usuario.setApellidos("Doe");
        usuario.setFechaNac(LocalDate.of(1990, 1, 1));
        usuario.setPassword("password");
        usuario.setPrivado(false);
        usuario.setEmail("testCrear@example.com");

        usuarioRepositorio.save(usuario);

        // Crear una nueva publicación
        Publicacion publicacion = new Publicacion();
        publicacion.setComentarioUsuario("Nueva publicación de prueba");
        publicacion.setUsuario(usuario);

        // Guardar la publicación en la base de datos
        publicacionRepositorio.save(publicacion);

        // Recuperar la publicación de la base de datos
        @SuppressWarnings("null")
        Publicacion publicacionCreada = publicacionRepositorio.findById(publicacion.getId()).orElse(null);

        // Verificar que la publicación se haya creado correctamente
        assertNotNull(publicacionCreada);
        assertEquals("Nueva publicación de prueba", publicacionCreada.getComentarioUsuario());
        // Otras verificaciones...
    }

    @Test
    void testModificarPublicacion() {
        // Crear un usuario de prueba
        Usuario usuario = new Usuario();
        usuario.setNick("testUser");
        usuario.setNombre("John");
        usuario.setApellidos("Doe");
        usuario.setFechaNac(LocalDate.of(1990, 1, 1));
        usuario.setPassword("password");
        usuario.setPrivado(false);
        usuario.setEmail("testNuevo@example.com");

        usuarioRepositorio.save(usuario);

        // Crear una publicación de prueba
        Publicacion publicacion = new Publicacion();
        publicacion.setComentarioUsuario("Comentario original");
        publicacion.setUsuario(usuario);

        publicacionRepositorio.save(publicacion);

        // Modificar la publicación
        publicacion.setComentarioUsuario("Comentario modificado");
        publicacionRepositorio.save(publicacion);

        // Recuperar la publicación después de la modificación
        @SuppressWarnings("null")
        Publicacion publicacionModificada = publicacionRepositorio.findById(publicacion.getId()).orElse(null);

        // Verificar que la publicación se haya modificado correctamente
        assertNotNull(publicacionModificada);
        assertEquals("Comentario modificado", publicacionModificada.getComentarioUsuario());
        // Otras verificaciones...
    }

    @SuppressWarnings("null")
    @Test
    void testBorrarPublicacion() {
        // Crear un usuario de prueba
        Usuario usuario = new Usuario();
        usuario.setNick("testUser");
        usuario.setNombre("John");
        usuario.setApellidos("Doe");
        usuario.setFechaNac(LocalDate.of(1990, 1, 1));
        usuario.setPassword("password");
        usuario.setPrivado(false);
        usuario.setEmail("testBorrar@example.com");

        usuarioRepositorio.save(usuario);

        // Crear una publicación de prueba
        Publicacion publicacion = new Publicacion();
        publicacion.setComentarioUsuario("Comentario de prueba");
        publicacion.setUsuario(usuario);

        publicacionRepositorio.save(publicacion);

        // Borrar la publicación
        publicacionRepositorio.deleteById(publicacion.getId());

        // Intentar recuperar la publicación después de la eliminación
        Publicacion publicacionBorrada = publicacionRepositorio.findById(publicacion.getId()).orElse(null);

        // Verificar que la publicación se haya borrado correctamente
        assertNull(publicacionBorrada);
    }
}
