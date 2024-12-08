package com.example.decsecBackend.serviciosImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.decsecBackend.dtos.PeticionDTO;
import com.example.decsecBackend.dtos.UsuarioDTO;
import com.example.decsecBackend.modelo.Estado;
import com.example.decsecBackend.modelo.Peticion;
import com.example.decsecBackend.modelo.Usuario;
import com.example.decsecBackend.repositorios.PeticionRepositorio;
import com.example.decsecBackend.servicios.PeticionServicio;
import jakarta.transaction.Transactional;

// Anotación para indicar que esta clase es un servicio de Spring
@Service
// Anotación para indicar que los métodos de esta clase son transaccionales
@Transactional
public class PeticionServicioImpl implements PeticionServicio {

    // Inyección de dependencia del repositorio de peticiones
    @Autowired
    private PeticionRepositorio repositorioPeticion;
    // Inyección de dependencia del servicio de usuarios
    @Autowired
    private UsuarioServicioImpl usuarioServicio;

    // Método para obtener una petición por el ID del usuario emisor y el email del receptor
    @Override
    public PeticionDTO obtenerPeticion(Long usuarioEmisorId, String emailReceptor){
        // Busca el usuario receptor por su email
        Usuario usu = usuarioServicio.encontrarPorEmail(emailReceptor);
        // Busca la petición correspondiente al usuario emisor y receptor
        Peticion resultado = repositorioPeticion.encontrarPeticion(usuarioEmisorId, usu.getId());
        // Si se encuentra una petición, se devuelve su DTO, de lo contrario se devuelve null
        if (resultado != null){
            return new PeticionDTO(resultado);
        }else{
            return null;
        }
    };

    // Método para enviar una petición
    @Override
    public PeticionDTO enviarPeticion(Usuario usuarioEmisor, String emailReceptor){
        // Busca el usuario receptor por su email
        Usuario usuReceptor = usuarioServicio.encontrarPorEmail(emailReceptor);
        // Busca si ya existe una petición entre el usuario emisor y receptor
        Peticion peticionExistente = repositorioPeticion.encontrarPeticion(usuarioEmisor.getId(), usuReceptor.getId());
        // Si ya existe una petición, se cambia su estado a pendiente
        if (peticionExistente != null) {
            peticionExistente.setEstado(Estado.PENDIENTE);
            // Se devuelve el DTO de la petición actualizada
            return new PeticionDTO(repositorioPeticion.save(peticionExistente));
        }else{
            // Si no existe una petición, se crea una nueva
            Peticion pe = new Peticion();
            pe.setEstado(Estado.PENDIENTE);
            pe.setUsuarioEmisor(usuarioEmisor);
            pe.setUsuarioReceptor(usuReceptor);
            // Se devuelve el DTO de la nueva petición
            return new PeticionDTO(repositorioPeticion.save(pe));
        }
    }

    // Método para cambiar el estado de una petición
    @Override
    public PeticionDTO cambiarEstado(Long id, String estado){

        // Busca la petición por su ID
        Peticion peticionExistente = repositorioPeticion.findById(id).orElse(null);
        // Si no se encuentra la petición, se lanza una excepción
        if (peticionExistente == null) {
            throw new RuntimeException("No se encontró la petición.");
        }
        // Cambia el estado de la petición
        peticionExistente.setEstado(Estado.valueOf(estado));
        // Se devuelve el DTO de la petición actualizada
        return new PeticionDTO(repositorioPeticion.save(peticionExistente));
    }

    // Método para obtener las peticiones de un usuario receptor
    @Override
    public List<PeticionDTO> misPeticiones(Long usuarioReceptorId){
        // Llamada al método del repositorio para obtener las peticiones
        return repositorioPeticion.listarMisPeticiones(usuarioReceptorId);
    }

    // Método para obtener los amigos de un usuario emisor
    @Override
    public List<PeticionDTO> misAmigos(Long usuarioEmisorId){
        // Llamada al método del repositorio para obtener los amigos
        return repositorioPeticion.listarMisAmigos(usuarioEmisorId);
    }
}
