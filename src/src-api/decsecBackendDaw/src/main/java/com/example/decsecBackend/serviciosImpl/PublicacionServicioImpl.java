package com.example.decsecBackend.serviciosImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public List<PublicacionDTO> listarPublicaciones() {
        return repositorioPublicacion.findAll().stream()
                .sorted((p1, p2) -> p2.getFechaPublicacion().compareTo(p1.getFechaPublicacion()))
                .map(publicacion -> new PublicacionDTO(publicacion))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PublicacionDTO> listarPublicacionesUsuario(String email) {
        return servicioUsuario.encontrarPorEmail(email).getPublicaciones().stream()
                .sorted((p1, p2) -> p2.getFechaPublicacion().compareTo(p1.getFechaPublicacion()))
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
    public List<PublicacionDTO> listarPublicacionesdFeed(Long id, int dias) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(dias);
        return repositorioPublicacion.encontrarPublicacionesRecientesPorUsuario(id, sevenDaysAgo);
    }

    @SuppressWarnings("null")
    @Override
    public void borrarPublicacion(Long id) {
        Publicacion publi = repositorioPublicacion.findById(id)
                .orElseThrow(() -> new NotFoundException("Publicacion no encontrada"));
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
    public void megusta(Long publicacionId, Long usuarioId) {
        // Buscar la publicación por su ID
        Publicacion publicacion = repositorioPublicacion.findById(publicacionId)
        .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        // Buscar al usuario por su ID
        Usuario usuario = servicioUsuario.obtenerUsuario(usuarioId);

        // Agregar el usuario a la lista de "me gusta" de la publicación
        publicacion.getUsuariosQueDieronMeGusta().add(usuario);

        // Guardar la publicación para actualizar la tabla intermedia
        repositorioPublicacion.save(publicacion);

        System.out.println("ME GUSTA");
    }

    @Override
    public void noMegusta(Long publicacionId, Long usuarioId) {
        // Buscar la publicación por su ID
        Publicacion publicacion = repositorioPublicacion.findById(publicacionId)
        .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        // Buscar al usuario por su ID
        Usuario usuario = servicioUsuario.obtenerUsuario(usuarioId);

        // Eliminar el usuario de la lista de "me gusta" de la publicación
        publicacion.getUsuariosQueDieronMeGusta().remove(usuario);

        // Guardar la publicación para actualizar la tabla intermedia
        repositorioPublicacion.save(publicacion);

        System.out.println("NO ME GUSTA");
    }

    @Override
    public Boolean pertenecePublicacion(Long id, String email) {

        List<Publicacion> publicaciones = servicioUsuario.encontrarPorEmail(email).getPublicaciones();

        return publicaciones.stream()
                .anyMatch(objeto -> objeto.getId() == id);

    }

    public PublicacionDTO crearPublicacion(PublicacionDTOrequest publi, Map<String, MultipartFile> imagenes,
            Usuario usuario) throws IOException {
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
        publicacion.setFechaPublicacion(LocalDateTime.now());
        publicacion.setUsuario(usuario);
        return new PublicacionDTO(repositorioPublicacion.save(publicacion));
    }

}
