package com.example.decsecBackend.serviciosImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.decsecBackend.dtos.ComentarioDTO;
import com.example.decsecBackend.errores.NotFoundException;
import com.example.decsecBackend.modelo.Comentario;
import com.example.decsecBackend.modelo.Publicacion;
import com.example.decsecBackend.repositorios.ComentarioRepositorio;
import com.example.decsecBackend.repositorios.PublicacionRepositorio;
import com.example.decsecBackend.servicios.ComentarioServicio;
import com.example.decsecBackend.servicios.UsuarioServicio;

@Service
public class ComentarioServicioImpl implements ComentarioServicio {

    @Autowired
    private ComentarioRepositorio repositorioComentario;

    @Autowired
    private PublicacionRepositorio repositorioPublicacion;

    @Autowired
    private UsuarioServicio servicioUsuario;

    @Autowired

    @Override
    public List<ComentarioDTO> listarComentarios() {
        // Este método lista todos los comentarios disponibles en la base de datos.
        return repositorioComentario.findAll().stream()
                .map(comentario -> new ComentarioDTO(comentario))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("null")
    @Override
    public List<ComentarioDTO> listarComentariosPublicacion(Long id) {
        // Este método lista todos los comentarios asociados a una publicación específica.
        Publicacion publi = repositorioPublicacion.findById(id)
                .orElseThrow(() -> new NotFoundException("Publicacion no encontrada"));
        return publi.getComentarios().stream()
                .map(comentario -> new ComentarioDTO(comentario))
                .collect(Collectors.toList());
    }

    @Override
    public List<ComentarioDTO> listarMisComentarios(String email) {
        // Este método lista todos los comentarios realizados por un usuario específico.
        return servicioUsuario.encontrarPorEmail(email).getComentarios().stream()
                .map(comentario -> new ComentarioDTO(comentario))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("null")
    @Override
    public ComentarioDTO crearComentario(Comentario comentario, String emailUsuario, Long idPubli) {
        // Este método crea un nuevo comentario asociado a una publicación y un usuario.
        comentario.setUsuario(servicioUsuario.encontrarPorEmail(emailUsuario));
        comentario.setPublicacion(repositorioPublicacion.findById(idPubli)
                .orElseThrow(() -> new NotFoundException("Publicacion no encontrada")));
        return new ComentarioDTO(repositorioComentario.save(comentario));
    }

    @SuppressWarnings("null")
    @Override
    public ComentarioDTO actualizarComentario(String nuevoComentario, Long idComentario) {
        // Este método actualiza el contenido de un comentario específico.
        Comentario coment = repositorioComentario.findById(idComentario)
                .orElseThrow(() -> new NotFoundException("Comentario no encontrado"));
        coment.setComentario(nuevoComentario);
        return new ComentarioDTO(repositorioComentario.save(coment));
    }

    @SuppressWarnings("null")
    @Override
    public void borrarComentario(Long idComentario) {
        // Este método elimina un comentario específico de la base de datos.
        repositorioComentario.deleteById(idComentario);
    }

    @SuppressWarnings("null")
    @Override
    public boolean comentarioPerteneceAUsuario(Long IdComentario, String email) {
        // Este método verifica si un comentario específico pertenece a un usuario específico.
        Comentario comentario = repositorioComentario.findById(IdComentario).orElseThrow(() -> new NotFoundException("Comentario no encontrado"));
        return comentario.getUsuario().equals(servicioUsuario.encontrarPorEmail(email));
    }

}
