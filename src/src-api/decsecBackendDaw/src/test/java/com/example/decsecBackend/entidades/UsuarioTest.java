package com.example.decsecBackend.entidades;

import com.example.decsecBackend.modelo.Role;
import com.example.decsecBackend.modelo.Usuario;
import com.example.decsecBackend.repositorios.UsuarioRepositorio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class UsuarioTest {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Test
    void testCrearUsuario() {
        // Crear un usuario de prueba
        Usuario usuario = new Usuario();
        usuario.setNick("testUser");
        usuario.setNombre("John");
        usuario.setApellidos("Doe");
        usuario.setFechaNac(LocalDate.of(1990, 1, 1));
        usuario.setPassword("password");
        usuario.setPrivado(false);
        usuario.setEmail("test@example.com");

        // Agregar roles
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        usuario.setRoles(roles);

        // Guardar el usuario en la base de datos
        usuarioRepositorio.save(usuario);

        // Recuperar el usuario de la base de datos
        Usuario usuarioRecuperado = usuarioRepositorio.findByEmail("test@example.com").orElse(null);

        // Verificar que la información se haya guardado correctamente
        assert usuarioRecuperado != null;
        assertEquals("testUser", usuarioRecuperado.getNick());
        assertEquals("John", usuarioRecuperado.getNombre());
        assertEquals("Doe", usuarioRecuperado.getApellidos());
        assertEquals(LocalDate.of(1990, 1, 1), usuarioRecuperado.getFechaNac());
        assertEquals("password", usuarioRecuperado.getPassword());
        assertEquals(false, usuarioRecuperado.getPrivado());
        assertEquals("test@example.com", usuarioRecuperado.getEmail());
        assertEquals(1, usuarioRecuperado.getRoles().size());
        assertEquals("ROLE_USER", usuarioRecuperado.getRoles().iterator().next().name());
    }

    @Test
    void testModificarUsuario() {
        // Crear un usuario de prueba
        Usuario usuario = new Usuario();
        usuario.setNick("testUser");
        usuario.setNombre("John");
        usuario.setApellidos("Doe");
        usuario.setFechaNac(LocalDate.of(1990, 1, 1));
        usuario.setPassword("password");
        usuario.setPrivado(false);
        usuario.setEmail("testModificar@example.com");

        // Agregar roles
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        usuario.setRoles(roles);

        // Guardar el usuario en la base de datos
        usuarioRepositorio.save(usuario);

        // Modificar el usuario
        usuario.setNombre("Jane");
        usuarioRepositorio.save(usuario);

        // Recuperar el usuario después de la modificación
        Usuario usuarioModificado = usuarioRepositorio.findByEmail("testModificar@example.com").orElse(null);

        // Verificar que el usuario se haya modificado correctamente
        assertNotNull(usuarioModificado);
        assertEquals("testUser", usuarioModificado.getNick());
        assertEquals("Jane", usuarioModificado.getNombre());
        // Otras verificaciones...
    }

    @SuppressWarnings("null")
    @Test
    void testBorrarUsuario() {
        // Crear un usuario de prueba
        Usuario usuario = new Usuario();
        usuario.setNick("testUser");
        usuario.setNombre("John");
        usuario.setApellidos("Doe");
        usuario.setFechaNac(LocalDate.of(1990, 1, 1));
        usuario.setPassword("password");
        usuario.setPrivado(false);
        usuario.setEmail("testBorrar@example.com");

        // Agregar roles
        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        usuario.setRoles(roles);

        // Guardar el usuario en la base de datos
        usuarioRepositorio.save(usuario);

        // Borrar el usuario
        usuarioRepositorio.deleteById(usuario.getId());

        // Intentar recuperar el usuario después de la eliminación
        Usuario usuarioBorrado = usuarioRepositorio.findByEmail("testBorrar@example.com").orElse(null);

        // Verificar que el usuario se haya borrado correctamente
        assertNull(usuarioBorrado);
    }

}
