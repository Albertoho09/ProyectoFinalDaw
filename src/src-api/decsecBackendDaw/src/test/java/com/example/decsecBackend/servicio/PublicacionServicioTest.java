package com.example.decsecBackend.servicio;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.decsecBackend.dtos.PublicacionDTO;
import com.example.decsecBackend.modelo.Publicacion;
import com.example.decsecBackend.modelo.Usuario;
import com.example.decsecBackend.repositorios.PublicacionRepositorio;
import com.example.decsecBackend.serviciosImpl.PublicacionServicioImpl;
import com.example.decsecBackend.serviciosImpl.UsuarioServicioImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PublicacionServicioTest {

    @Mock
    private PublicacionRepositorio repositorioPublicacion;

    @Mock
    private UsuarioServicioImpl servicioUsuario;

    @InjectMocks
    private PublicacionServicioImpl servicioPublicacion;

    @Test
    public void testListarPublicaciones() {
        // Configura el comportamiento del repositorio de publicaciones mock para devolver una lista de publicaciones
        when(repositorioPublicacion.findAll()).thenReturn(Arrays.asList(
                new Publicacion(/* Datos de la publicación 1 */),
                new Publicacion(/* Datos de la publicación 2 */)
        ));

        // Realiza la llamada al método y verifica que se devuelva una lista de PublicacionDTO
        List<PublicacionDTO> publicacionesDTO = servicioPublicacion.listarPublicaciones();

        assertEquals(2, publicacionesDTO.size());
        // Agrega más aserciones según la estructura de tu PublicacionDTO
    }

    @Test
    public void testListarPublicacionesUsuario() {
        String emailUsuario = "usuario@dominio.com";
        // Configura el comportamiento del servicio de usuario mock para devolver un usuario existente por email
        when(servicioUsuario.encontrarPorEmail(emailUsuario)).thenReturn(new Usuario(/* Datos del usuario existente */));

        // Configura el comportamiento del servicio de usuario mock para devolver una lista de publicaciones del usuario
        when(servicioUsuario.encontrarPorEmail(emailUsuario).getPublicaciones()).thenReturn(Arrays.asList(
                new Publicacion(/* Datos de la publicación 1 */),
                new Publicacion(/* Datos de la publicación 2 */)
        ));

        // Realiza la llamada al método y verifica que se devuelva una lista de PublicacionDTO
        List<PublicacionDTO> publicacionesDTO = servicioPublicacion.listarPublicacionesUsuario(emailUsuario);

        assertEquals(2, publicacionesDTO.size());
        // Agrega más aserciones según la estructura de tu PublicacionDTO
    }

    @Test
    public void testListarPublicacionPorId() {
        Long idPublicacionExistente = 1L;
        Publicacion publi = new Publicacion();
        
        // Configura el comportamiento del repositorio de publicaciones mock para devolver una publicación existente por id
        when(repositorioPublicacion.findById(idPublicacionExistente)).thenReturn(Optional.of(publi));

        // Realiza la llamada al método y verifica que se devuelva una PublicacionDTO
        PublicacionDTO publicacionDTO = servicioPublicacion.listarPublicacionPorId(idPublicacionExistente);

        assertNotNull(publicacionDTO);
        // Agrega más aserciones según la estructura de tu PublicacionDTO
    }

    @Test
    public void testBorrarPublicacion() {
        Long idPublicacionExistente = 1L;

        // Configura el comportamiento del repositorio de publicaciones mock para devolver una publicación existente por id
        when(repositorioPublicacion.findById(idPublicacionExistente)).thenReturn(Optional.of(new Publicacion(/* Datos de la publicación existente */)));

        // Realiza la llamada al método y verifica que no se lance ninguna excepción
        assertDoesNotThrow(() -> servicioPublicacion.borrarPublicacion(idPublicacionExistente));
    }

    @Test
    public void testActualizarPublicacion() {
        Long idPublicacionExistente = 1L;

        // Configura el comportamiento del repositorio de publicaciones mock para devolver una publicación existente por id
        when(repositorioPublicacion.findById(idPublicacionExistente)).thenReturn(Optional.of(new Publicacion(/* Datos de la publicación existente */)));

        // Crea un mapa con actualizaciones
        Map<String, Object> updates = Map.of("comentarioUsuario", "Nuevo comentario");

        // Realiza la llamada al método y verifica que se devuelva una PublicacionDTO
        PublicacionDTO publicacionActualizada = servicioPublicacion.actualizarPublicacion(idPublicacionExistente, updates);

        assertNotNull(publicacionActualizada);
        // Agrega más aserciones según la estructura de tu PublicacionDTO
    }

    @Test
    public void testExistePorId() {
        Long idPublicacionExistente = 1L;

        // Configura el comportamiento del repositorio de publicaciones mock para el caso de existencia por id
        when(repositorioPublicacion.existsById(idPublicacionExistente)).thenReturn(true);

        assertTrue(servicioPublicacion.existePorId(idPublicacionExistente));
    }

    @Test
    public void testNoExistePorId() {
        Long idPublicacionNoExistente = 99L;

        // Configura el comportamiento del repositorio de publicaciones mock para el caso de no existencia por id
        when(repositorioPublicacion.existsById(idPublicacionNoExistente)).thenReturn(false);

        assertFalse(servicioPublicacion.existePorId(idPublicacionNoExistente));
    }

    @Test
    public void testMegusta() {
        Long idPublicacionExistente = 1L;

        // Realiza la llamada al método y verifica que no se lance ninguna excepción
        assertDoesNotThrow(() -> servicioPublicacion.megusta(idPublicacionExistente));
    }

    @Test
    public void testNoMegusta() {
        Long idPublicacionExistente = 1L;

        // Realiza la llamada al método y verifica que no se lance ninguna excepción
        assertDoesNotThrow(() -> servicioPublicacion.noMegusta(idPublicacionExistente));
    }

    @Test
    public void testPertenecePublicacion() {
        String emailUsuario = "usuario@dominio.com";
        Long idPublicacionExistente = 1L;

        // Configura el comportamiento del servicio de usuario mock para devolver un usuario existente por email
        when(servicioUsuario.encontrarPorEmail(emailUsuario)).thenReturn(new Usuario(/* Datos del usuario existente */));

        // Configura el comportamiento del servicio de usuario mock para devolver una lista de publicaciones del usuario
        when(servicioUsuario.encontrarPorEmail(emailUsuario).getPublicaciones()).thenReturn(Arrays.asList(
                new Publicacion(/* Datos de la publicación 1 */),
                new Publicacion(/* Datos de la publicación 2 */)
        ));

        assertTrue(servicioPublicacion.pertenecePublicacion(idPublicacionExistente, emailUsuario));
    }

    @Test
    public void testNoPertenecePublicacion() {
        String emailUsuario = "usuario@dominio.com";
        Long idPublicacionNoExistente = 99L;

        // Configura el comportamiento del servicio de usuario mock para devolver un usuario existente por email
        when(servicioUsuario.encontrarPorEmail(emailUsuario)).thenReturn(new Usuario(/* Datos del usuario existente */));

        // Configura el comportamiento del servicio de usuario mock para devolver una lista vacía de publicaciones del usuario
        when(servicioUsuario.encontrarPorEmail(emailUsuario).getPublicaciones()).thenReturn(Arrays.asList());

        assertFalse(servicioPublicacion.pertenecePublicacion(idPublicacionNoExistente, emailUsuario));
    }
}
