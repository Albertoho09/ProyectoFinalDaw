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
    private PublicacionRepositorio repositorioPublicacion; // Inyección de dependencia del repositorio de publicaciones
    @Autowired
    private UsuarioServicioImpl servicioUsuario; // Inyección de dependencia del servicio de usuarios
    @Autowired
    private ImagenRepositorio repositorioImagen; // Inyección de dependencia del repositorio de imágenes

    @Override
    public List<PublicacionDTO> listarPublicaciones(Usuario usuario) {
        // Retorna una lista de DTO de publicaciones ordenadas por fecha de publicación descendente
        return repositorioPublicacion.findAll().stream()
                .sorted((p1, p2) -> p2.getFechaPublicacion().compareTo(p1.getFechaPublicacion()))
                .map(publicacion -> new PublicacionDTO(publicacion, usuario))
                .collect(Collectors.toList());
    }

    @Override
    public List<PublicacionDTO> listarPublicacionesUsuario(String email, Usuario usuario) {
        // Retorna una lista de DTO de publicaciones del usuario especificado, ordenadas por fecha de publicación descendente
        return servicioUsuario.encontrarPorEmail(email).getPublicaciones().stream()
                .sorted((p1, p2) -> p2.getFechaPublicacion().compareTo(p1.getFechaPublicacion()))
                .map(publicacion -> new PublicacionDTO(publicacion, usuario))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("null")
    @Override
    public PublicacionDTO listarPublicacionPorId(Long id, Usuario usuario) {
        // Retorna un DTO de publicación por su ID, lanzando una excepción si no se encuentra
        return new PublicacionDTO(repositorioPublicacion.findById(id)
                .orElseThrow(() -> new NotFoundException("Publicacion no encontrada")), usuario);
    }

    @Override
    public List<PublicacionDTO> listarPublicacionesdFeed(Long id, int dias, Usuario usuario) {
        // Calcula la fecha de hace 'dias' días atrás
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(dias);
        // Encuentra publicaciones recientes del usuario especificado
        List<Publicacion> publicacionesRecientes = repositorioPublicacion.encontrarPublicacionesRecientesPorUsuario(id, sevenDaysAgo);
        // Retorna una lista de DTO de publicaciones recientes
        return publicacionesRecientes.stream()
                .map(publicacion -> new PublicacionDTO(publicacion, usuario))
                .collect(Collectors.toList());
    }

    @Override
    public void borrarPublicacion(Long id) {
        // Elimina una publicación por su ID, lanzando una excepción si no se encuentra
        Publicacion publi = repositorioPublicacion.findById(id)
                .orElseThrow(() -> new NotFoundException("Publicacion no encontrada"));
        repositorioPublicacion.delete(publi);
    }

    @SuppressWarnings("null")
    @Override
    public PublicacionDTO actualizarPublicacion(Long id, Map<String, Object> updates, Usuario usuario) {

        // Encuentra la publicación por su ID, lanzando una excepción si no se encuentra
        Publicacion publi = repositorioPublicacion.findById(id)
                .orElseThrow(() -> new NotFoundException("Publicacion no encontrada"));

        // Actualiza los campos de la publicación según los updates proporcionados
        updates.forEach((campo, valor) -> {
            switch (campo) {
                case "comentarioUsuario":
                    publi.setComentarioUsuario(valor.toString());
                    break;

                default:
                    break;
            }
        });

        // Retorna un DTO de la publicación actualizada
        return new PublicacionDTO(repositorioPublicacion.save(publi), usuario);
    }

    @SuppressWarnings("null")
    @Override
    public Boolean existePorId(Long id) {
        // Verifica si una publicación existe por su ID
        return repositorioPublicacion.existsById(id);
    }

    @Override
    public void megusta(Long publicacionId, Long usuarioId) {
        // Busca la publicación por su ID, lanzando una excepción si no se encuentra
        Publicacion publicacion = repositorioPublicacion.findById(publicacionId)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        // Busca al usuario por su ID
        Usuario usuario = servicioUsuario.obtenerUsuario(usuarioId);

        // Agrega el usuario a la lista de "me gusta" de la publicación
        publicacion.getUsuariosQueDieronMeGusta().add(usuario);

        // Guarda la publicación para actualizar la tabla intermedia
        repositorioPublicacion.save(publicacion);

        System.out.println("ME GUSTA");
    }

    @Override
    public void noMegusta(Long publicacionId, Long usuarioId) {
        // Busca la publicación por su ID, lanzando una excepción si no se encuentra
        Publicacion publicacion = repositorioPublicacion.findById(publicacionId)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        // Busca al usuario por su ID
        Usuario usuario = servicioUsuario.obtenerUsuario(usuarioId);

        // Elimina el usuario de la lista de "me gusta" de la publicación
        publicacion.getUsuariosQueDieronMeGusta().remove(usuario);

        // Guarda la publicación para actualizar la tabla intermedia
        repositorioPublicacion.save(publicacion);

        System.out.println("NO ME GUSTA");
    }

    @Override
    public Boolean pertenecePublicacion(Long id, String email) {

        // Encuentra el usuario por su email
        List<Publicacion> publicaciones = servicioUsuario.encontrarPorEmail(email).getPublicaciones();

        // Verifica si el usuario tiene una publicación específica
        return publicaciones.stream()
                .anyMatch(objeto -> objeto.getId() == id);

    }

    public PublicacionDTO crearPublicacion(PublicacionDTOrequest publi, Map<String, MultipartFile> imagenes,
            Usuario usuario) throws IOException {
        // Inicializa una lista de imágenes
        List<Imagen> listImagenes = new ArrayList<>();
        // Crea una nueva publicación
        Publicacion publicacion = new Publicacion();
        // Si hay imágenes, las procesa y las agrega a la publicación
        if (imagenes != null) {
            for (Map.Entry<String, MultipartFile> entry : imagenes.entrySet()) {
                MultipartFile imagen = entry.getValue();
                // Crea una nueva imagen y la guarda
                Imagen img = new Imagen(imagen.getOriginalFilename(), imagen.getContentType(), imagen.getBytes());
                repositorioImagen.save(img);
                listImagenes.add(img);
            }
            publicacion.setImagenes(listImagenes);
        }
        // Configura los campos de la publicación
        publicacion.setComentarioUsuario(publi.getComentarioUsuario());
        publicacion.setFechaPublicacion(LocalDateTime.now());
        publicacion.setUsuario(usuario);
        // Retorna un DTO de la publicación creada
        return new PublicacionDTO(repositorioPublicacion.save(publicacion), usuario);
    }

    public List<PublicacionDTO> listarPublicacionesConMeGusta(String email, Usuario usuario) {
        // Encuentra el usuario por su email
        // Retorna una lista de DTO de publicaciones que el usuario ha marcado como "me gusta", ordenadas por fecha de publicación descendente
        return servicioUsuario.encontrarPorEmail(email).getPublicacionesConMeGusta().stream()
                .sorted((p1, p2) -> p2.getFechaPublicacion().compareTo(p1.getFechaPublicacion()))
                .map(publicacion -> new PublicacionDTO(publicacion, usuario))
                .collect(Collectors.toList());
    }

}
