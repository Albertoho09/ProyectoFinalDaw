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
        if (resultado != null){
            return new PeticionDTO(resultado);
        }else{
            return null;
        }
    };

    @Override
    public PeticionDTO enviarPeticion(Usuario usuarioEmisor, String emailReceptor){
        Usuario usuReceptor = usuarioServicio.encontrarPorEmail(emailReceptor);
        Peticion peticionExistente = repositorioPeticion.encontrarPeticion(usuarioEmisor.getId(), usuReceptor.getId());
        if (peticionExistente != null) {
            peticionExistente.setEstado(Estado.PENDIENTE);
            return new PeticionDTO(repositorioPeticion.save(peticionExistente));
        }else{
            Peticion pe = new Peticion();
            pe.setEstado(Estado.PENDIENTE);
            pe.setUsuarioEmisor(usuarioEmisor);
            pe.setUsuarioReceptor(usuReceptor);
            return new PeticionDTO(repositorioPeticion.save(pe));
        }
    }

    @Override
    public PeticionDTO cambiarEstado(Long id, String estado){

        Peticion peticionExistente = repositorioPeticion.findById(id).orElse(null);
        if (peticionExistente == null) {
            throw new RuntimeException("No se encontró la petición.");
        }
            peticionExistente.setEstado(Estado.valueOf(estado));
            return new PeticionDTO(repositorioPeticion.save(peticionExistente));
    }

    @Override
    public List<PeticionDTO> misPeticiones(Long usuarioReceptorId){
        return repositorioPeticion.listarMisPeticiones(usuarioReceptorId);
    }

    @Override
    public List<PeticionDTO> misAmigos(Long usuarioEmisorId){
        return repositorioPeticion.listarMisAmigos(usuarioEmisorId);
    }
}
