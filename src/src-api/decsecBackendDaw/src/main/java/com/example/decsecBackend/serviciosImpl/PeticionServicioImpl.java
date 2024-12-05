package com.example.decsecBackend.serviciosImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.decsecBackend.dtos.PeticionDTO;
import com.example.decsecBackend.modelo.Estado;
import com.example.decsecBackend.modelo.Peticion;
import com.example.decsecBackend.modelo.Usuario;
import com.example.decsecBackend.repositorios.PeticionRepositorio;
import com.example.decsecBackend.servicios.PeticionServicio;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PeticionServicioImpl implements PeticionServicio {

    @Autowired
    private PeticionRepositorio repositorioPeticion;
    @Autowired
    private UsuarioServicioImpl usuarioServicio;

    @Override
    public PeticionDTO obtenerPeticion(Long usuarioEmisorId, String emailReceptor){
        Usuario usu = usuarioServicio.encontrarPorEmail(emailReceptor);
        Peticion resultado = repositorioPeticion.encontrarPeticion(usuarioEmisorId, usu.getId());
        return new PeticionDTO(resultado);
    };

    @Override
    public PeticionDTO enviarPeticion(Usuario usuarioEmisor, String emailReceptor){
        Usuario usuReceptor = usuarioServicio.encontrarPorEmail(emailReceptor);
        Peticion peticionExistente = repositorioPeticion.encontrarPeticion(usuarioEmisor.getId(), usuReceptor.getId());
        if (peticionExistente != null && peticionExistente.getEstado() == Estado.DENEGADA) {
            peticionExistente.setEstado(Estado.PENDIENTE);
            return new PeticionDTO(repositorioPeticion.save(peticionExistente));
        } else {
            return null;
        }
    }

    @Override
    public PeticionDTO cambiarEstado(Usuario usuarioEmisor, String emailReceptor, String estado){
        Usuario usuReceptor = usuarioServicio.encontrarPorEmail(emailReceptor);
        Peticion peticionExistente = repositorioPeticion.encontrarPeticion(usuarioEmisor.getId(), usuReceptor.getId());
        if (peticionExistente == null) {
            throw new RuntimeException("No se encontró la petición.");
        }
            peticionExistente.setEstado(Estado.valueOf(estado));
            return new PeticionDTO(repositorioPeticion.save(peticionExistente));
    }

    @Override
    public List<PeticionDTO> misPeticiones(Long usuarioEmisorId){
        return repositorioPeticion.listarMisPeticiones(usuarioEmisorId);
    }


}
