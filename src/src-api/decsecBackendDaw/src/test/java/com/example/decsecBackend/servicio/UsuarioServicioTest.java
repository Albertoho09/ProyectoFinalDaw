package com.example.decsecBackend.servicio;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.decsecBackend.dtos.UsuarioDTO;
import com.example.decsecBackend.errores.NotFoundException;
import com.example.decsecBackend.modelo.Role;
import com.example.decsecBackend.modelo.Usuario;
import com.example.decsecBackend.repositorios.UsuarioRepositorio;
import com.example.decsecBackend.serviciosImpl.UsuarioServicioImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UsuarioServicioTest {

    @Mock
    private UsuarioRepositorio repositorio;

    @InjectMocks
    private UsuarioServicioImpl servicio;

    @Test
    public void testCrearUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNick("testUser");
        usuario.setNombre("John");
        usuario.setApellidos("Doe");
        usuario.setFechaNac(LocalDate.of(1990, 1, 1));
        usuario.setPassword("password");
        usuario.setPrivado(false);
        usuario.setEmail("test@example.com");
        usuario.setRoles(Collections.singleton(Role.ROLE_USER));

        when(repositorio.save(any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioCreado = servicio.crearUsuario(usuario);

        assertEquals(usuario, usuarioCreado);
    }

    @Test
    public void testObtenerUsuarioExistente() {
        Long idUsuarioExistente = 12L;
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(idUsuarioExistente);
        usuarioMock.setNick("testUser12");
        usuarioMock.setNombre("John");
        usuarioMock.setApellidos("Doe");
        usuarioMock.setFechaNac(LocalDate.of(1990, 1, 1));
        usuarioMock.setPassword("password");
        usuarioMock.setPrivado(false);
        usuarioMock.setEmail("test12@example.com");
        usuarioMock.setRoles(Collections.singleton(Role.ROLE_USER));

        when(repositorio.findById(anyLong())).thenReturn(java.util.Optional.of(usuarioMock));

        Usuario usuarioObtenido = servicio.obtenerUsuario(idUsuarioExistente);

        assertEquals(usuarioMock, usuarioObtenido);
    }

    @Test
    public void testObtenerUsuarioNoExistente() {
        Long idUsuarioNoExistente = 99L;

        when(repositorio.findById(anyLong())).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> servicio.obtenerUsuario(idUsuarioNoExistente));
    }

    @Test
    public void testBorrarUsuario() {
        Long idUsuarioExistente = 1L;

        // Configura el comportamiento del repositorio mock para la eliminación
        when(repositorio.existsById(idUsuarioExistente)).thenReturn(true);

        // Realiza la llamada al método y verifica que no se lance ninguna excepción
        assertDoesNotThrow(() -> servicio.borrarUsuario(idUsuarioExistente));
    }

    @Test
    public void testListarTodosUsuariosDTO() {
        // Configura el comportamiento del repositorio mock para devolver una lista de usuarios
        when(repositorio.findAll()).thenReturn(Arrays.asList(
                new Usuario(/* Datos del usuario 1 */),
                new Usuario(/* Datos del usuario 2 */)
        ));

        // Realiza la llamada al método y verifica que se devuelva una lista de UsuarioDTO
        List<UsuarioDTO> usuariosDTO = servicio.listarTodosUsuariosDTO();

        assertEquals(2, usuariosDTO.size());
        // Agrega más aserciones según la estructura de tu UsuarioDTO
    }

    @Test
    public void testListarTodosUsuarios() {
        // Configura el comportamiento del repositorio mock para devolver una lista de usuarios
        when(repositorio.findAll()).thenReturn(Arrays.asList(
                new Usuario(/* Datos del usuario 1 */),
                new Usuario(/* Datos del usuario 2 */)
        ));

        // Realiza la llamada al método y verifica que se devuelva una lista de Usuario
        List<Usuario> usuarios = servicio.listarTodosUsuarios();

        assertEquals(2, usuarios.size());
        // Agrega más aserciones según la estructura de tu Usuario
    }

    @Test
    public void testExistePorEmail() {
        String emailExistente = "usuario@dominio.com";
        String emailNoExistente = "noexiste@dominio.com";

        // Configura el comportamiento del repositorio mock para los casos de existencia y no existencia por email
        when(repositorio.existsByEmail(emailExistente)).thenReturn(true);
        when(repositorio.existsByEmail(emailNoExistente)).thenReturn(false);

        assertTrue(servicio.existePorEmail(emailExistente));
        assertFalse(servicio.existePorEmail(emailNoExistente));
    }

    @Test
    public void testEncontrarPorEmail() {
        String emailExistente = "usuario@dominio.com";
        Usuario usuarioExistente = new Usuario(/* Datos del usuario existente */);

        // Configura el comportamiento del repositorio mock para devolver un usuario existente por email
        when(repositorio.findByEmail(emailExistente)).thenReturn(Optional.of(usuarioExistente));

        // Realiza la llamada al método y verifica que se devuelva el usuario existente
        Usuario usuarioEncontrado = servicio.encontrarPorEmail(emailExistente);

        assertNotNull(usuarioEncontrado);
        assertEquals(usuarioExistente, usuarioEncontrado);
    }

    @Test
    public void testEncontrarPorEmailNoExistente() {
        String emailNoExistente = "noexiste@dominio.com";

        // Configura el comportamiento del repositorio mock para un usuario que no existe por email
        when(repositorio.findByEmail(emailNoExistente)).thenReturn(Optional.empty());

        // Realiza la llamada al método y verifica que se lance IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> servicio.encontrarPorEmail(emailNoExistente));
    }

    @Test
    public void testExistePorId() {
        Long idExistente = 1L;
        Long idNoExistente = 99L;

        // Configura el comportamiento del repositorio mock para los casos de existencia y no existencia por id
        when(repositorio.existsById(idExistente)).thenReturn(true);
        when(repositorio.existsById(idNoExistente)).thenReturn(false);

        assertTrue(servicio.existePorId(idExistente));
        assertFalse(servicio.existePorId(idNoExistente));
    }

}
