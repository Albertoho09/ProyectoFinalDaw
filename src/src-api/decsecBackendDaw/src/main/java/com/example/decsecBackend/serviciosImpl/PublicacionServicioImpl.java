package com.example.decsecBackend.serviciosImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.decsecBackend.dtos.PublicacionDTO;
import com.example.decsecBackend.dtos.PublicacionDTOrequest;
import com.example.decsecBackend.errores.NotFoundException;
import com.example.decsecBackend.modelo.Imagen;
import com.example.decsecBackend.modelo.Publicacion;
import com.example.decsecBackend.modelo.Usuario;
import com.example.decsecBackend.repositorios.ImagenRepositorio;
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
    @Autowired
    private ImagenRepositorio repositorioImagen;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);


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

    public PublicacionDTO crearPublicacion(PublicacionDTOrequest publi, Map<String, MultipartFile> imagenes, Usuario usuario) throws IOException {
        List<Imagen> listImagenes = new ArrayList<>();
        Publicacion publicacion = new Publicacion();
        if (imagenes != null) {
            for (Map.Entry<String, MultipartFile> entry : imagenes.entrySet()) {
                MultipartFile imagen = entry.getValue();
                Imagen img = new Imagen(imagen.getOriginalFilename(), imagen.getContentType(), imagen.getBytes());
                repositorioImagen.save(img);
                listImagenes.add(img);
            }
            publicacion.setImagenes(listImagenes);
        }
        publicacion.setComentarioUsuario(publi.getComentarioUsuario());
        publicacion.setFechaPublicacion(LocalDate.now());
        publicacion.setUsuario(usuario);
        return new PublicacionDTO(repositorioPublicacion.save(publicacion));
    }

}
