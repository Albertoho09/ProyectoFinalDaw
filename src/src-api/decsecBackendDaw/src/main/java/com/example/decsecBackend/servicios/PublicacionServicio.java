package com.example.decsecBackend.servicios;

import java.util.List;
import java.util.Map;

import com.example.decsecBackend.dtos.PublicacionDTO;
import com.example.decsecBackend.modelo.Publicacion;

public interface PublicacionServicio {

    List<PublicacionDTO> listarPublicaciones();

    List<PublicacionDTO> listarPublicacionesUsuario(String email);

    PublicacionDTO listarPublicacionPorId(Long id);

    List<Publicacion> listarPublicacionesdFeed(Long id);

    void borrarPublicacion(Long id);

    PublicacionDTO actualizarPublicacion(Long id, Map<String, Object> updates);

    Boolean existePorId(Long id);

    void megusta(Long id);

    void noMegusta(Long id);

    Boolean pertenecePublicacion(Long id, String email);

}
