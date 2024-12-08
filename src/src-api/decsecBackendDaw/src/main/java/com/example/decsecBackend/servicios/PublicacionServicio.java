package com.example.decsecBackend.servicios;

import java.util.List;
import java.util.Map;

import com.example.decsecBackend.dtos.PublicacionDTO;
import com.example.decsecBackend.modelo.Usuario;

public interface PublicacionServicio {

    List<PublicacionDTO> listarPublicaciones(Usuario usuario);

    List<PublicacionDTO> listarPublicacionesUsuario(String email, Usuario usuario);

    PublicacionDTO listarPublicacionPorId(Long id, Usuario usuario);

    List<PublicacionDTO> listarPublicacionesdFeed(Long id, int dias, Usuario usuario);

    List<PublicacionDTO> listarPublicacionesConMeGusta(String email, Usuario usuario);

    void borrarPublicacion(Long id);

    PublicacionDTO actualizarPublicacion(Long id, Map<String, Object> updates, Usuario usuario);

    Boolean existePorId(Long id);

    void megusta(Long publicacionId, Long idUsuario);

    void noMegusta(Long publicacionId, Long idUsuario);

    Boolean pertenecePublicacion(Long id, String email);

}
