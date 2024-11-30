package com.example.decsecBackend.servicios;

import java.util.List;
import java.util.Map;

import com.example.decsecBackend.dtos.PublicacionDTO;

public interface PublicacionServicio {

    List<PublicacionDTO> listarPublicaciones();

    List<PublicacionDTO> listarPublicacionesUsuario(String email);

    PublicacionDTO listarPublicacionPorId(Long id);

    List<PublicacionDTO> listarPublicacionesdFeed(Long id, int dias);

    List<PublicacionDTO> listarPublicacionesConMeGusta(String email);

    void borrarPublicacion(Long id);

    PublicacionDTO actualizarPublicacion(Long id, Map<String, Object> updates);

    Boolean existePorId(Long id);

    void megusta(Long publicacionId, Long idUsuario);

    void noMegusta(Long publicacionId, Long idUsuario);

    Boolean pertenecePublicacion(Long id, String email);

}
