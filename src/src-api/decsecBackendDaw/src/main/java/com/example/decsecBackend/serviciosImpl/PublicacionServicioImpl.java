package com.example.decsecBackend.serviciosImpl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.decsecBackend.dtos.PublicacionDTO;
import com.example.decsecBackend.errores.NotFoundException;
import com.example.decsecBackend.modelo.Publicacion;
import com.example.decsecBackend.modelo.Usuario;
import com.example.decsecBackend.repositorios.PublicacionRepositorio;
import com.example.decsecBackend.servicios.PublicacionServicio;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PublicacionServicioImpl implements PublicacionServicio {

    @Autowired
    private PublicacionRepositorio repositorioPublicacion;
    @Autowired
    private UsuarioServicioImpl servicioUsuario;

    @SuppressWarnings("null")
    @Override
    public PublicacionDTO crearPublicacion(Map<String, Object> datos, String email) {
        Usuario usu = servicioUsuario.encontrarPorEmail(email);
        Publicacion publicacion = new Publicacion();
        publicacion.setComentarioUsuario(datos.get("comentarioUsuario").toString());
        publicacion.setUsuario(usu);
        return new PublicacionDTO(repositorioPublicacion.save(publicacion));
    }

    @Override
    public List<PublicacionDTO> listarPublicaciones() {
        return repositorioPublicacion.findAll().stream()
                .map(publicacion -> new PublicacionDTO(publicacion))
                .collect(Collectors.toList());
    }

    @Override
    public List<PublicacionDTO> listarPublicacionesUsuario(String email) {
        return servicioUsuario.encontrarPorEmail(email).getPublicaciones().stream()
                .map(publicacion -> new PublicacionDTO(publicacion))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("null")
    @Override
    public PublicacionDTO listarPublicacionPorId(Long id) {
        return new PublicacionDTO(repositorioPublicacion.findById(id)
                .orElseThrow(() -> new NotFoundException("Publicacion no encontrada")));
    }

    @Override
    public List<Publicacion> listarPublicacionesdFeed(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'listarPublicacionesdFeed'");
    }

    @SuppressWarnings("null")
    @Override
    public void borrarPublicacion(Long id) {
        Publicacion publi = repositorioPublicacion.findById(id).orElseThrow(() -> new NotFoundException("Publicacion no encontrada"));
        repositorioPublicacion.delete(publi);
    }

    @SuppressWarnings("null")
    @Override
    public PublicacionDTO actualizarPublicacion(Long id, Map<String, Object> updates) {

        Publicacion publi = repositorioPublicacion.findById(id)
                .orElseThrow(() -> new NotFoundException("Publicacion no encontrada"));

        updates.forEach((campo, valor) -> {
            switch (campo) {
                case "comentarioUsuario":
                    publi.setComentarioUsuario(valor.toString());
                    break;

                default:
                    break;
            }
        });

        return new PublicacionDTO(repositorioPublicacion.save(publi));
    }

    @SuppressWarnings("null")
    @Override
    public Boolean existePorId(Long id) {
        return repositorioPublicacion.existsById(id);
    }

    @Override
    public void megusta(Long id) {
        repositorioPublicacion.meGusta(id);
    }

    @Override
    public void noMegusta(Long id) {
        repositorioPublicacion.noMeGusta(id);
    }

    @Override
    public Boolean pertenecePublicacion(Long id, String email) {

        List<Publicacion> publicaciones = servicioUsuario.encontrarPorEmail(email).getPublicaciones();

        return publicaciones.stream()
                .anyMatch(objeto -> objeto.getId() == id);

    }

}
