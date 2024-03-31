package com.example.decsecBackend.controladores;

import com.example.decsecBackend.modelo.Role;
import com.example.decsecBackend.modelo.Usuario;
import com.example.decsecBackend.serviciosImpl.UsuarioServicioImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ControladorUsuarioTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioServicioImpl usuarioServicio;

    @Test
    @WithMockUser(roles = "USER")
    public void testListarUsuariosPorIdUsuarioComun() throws Exception {
        Long idUsuarioExistente = 1L;

        // Configura el comportamiento del servicio mock
        when(usuarioServicio.existePorId(idUsuarioExistente)).thenReturn(true);
        when(usuarioServicio.obtenerUsuario(idUsuarioExistente)).thenReturn(new Usuario(/* ... */));

        // Realiza la solicitud y verifica la respuesta
        mockMvc.perform(get("/" + idUsuarioExistente))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.nombre").exists())  // Ajusta según la estructura de tu objeto Usuario
               .andExpect(jsonPath("$.rol").doesNotExist());  // Ajusta según la estructura de tu objeto Usuario
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testListarUsuariosPorIdAdmin() throws Exception {
        

        // Configura el comportamiento del servicio mock
        when(usuarioServicio.existePorId(1L)).thenReturn(true);


        // Realiza la solicitud y verifica la respuesta
        mockMvc.perform(get("/api/v1/users/" + 1))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.nombre").exists())  // Ajusta según la estructura de tu objeto Usuario
               .andExpect(jsonPath("$.rol").exists())  // Ajusta según la estructura de tu objeto Usuario
               .andExpect(jsonPath("$.rol").value("ADMIN"));  // Ajusta según la estructura de tu objeto Usuario
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testListarUsuariosPorIdUsuarioComunNoExistente() throws Exception {
        Long idUsuarioNoExistente = 99L;

        // Configura el comportamiento del servicio mock
        when(usuarioServicio.existePorId(idUsuarioNoExistente)).thenReturn(false);

        // Realiza la solicitud y verifica la respuesta
        mockMvc.perform(get("/api/v1/users/" + idUsuarioNoExistente))
               .andExpect(status().isNotFound())
               .andExpect(content().contentType(MediaType.TEXT_PLAIN))
               .andExpect(content().string("El usuario con id:" + idUsuarioNoExistente + " no existe"));
    }

    @SuppressWarnings("null")
    @Test
    @WithMockUser(roles = "USER")
    void testListarUsuariosPorId() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNick("testUser");
        usuario.setNombre("John");
        usuario.setApellidos("Doe");
        usuario.setFechaNac(LocalDate.of(1990, 1, 1));
        usuario.setPassword("password");
        usuario.setPrivado(false);
        usuario.setEmail("test@example.com");
        usuario.setRoles(Collections.singleton(Role.ROLE_USER));

        Long usuarioId = usuario.getId();

        usuarioServicio.crearUsuario(usuario);

        when(usuarioServicio.existePorId(usuarioId)).thenReturn(true);
        when(usuarioServicio.obtenerUsuario(usuarioId)).thenReturn(usuario);

        mockMvc.perform(get("/api/v1/users/", usuarioId))
                .andExpect(jsonPath("nick").value(usuario.getNick()))
                .andExpect(jsonPath("roles[0]").value("ROLE_USER"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testBorrarUsuarioPorId() throws Exception {
        Long usuarioId = 1L;

        when(usuarioServicio.existePorId(usuarioId)).thenReturn(true);
        usuarioServicio.borrarUsuario(usuarioId);
        when(usuarioServicio.existePorId(usuarioId)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/users/{id}", usuarioId))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario con id:" + usuarioId + " borrado correctamente"));
    }
}
