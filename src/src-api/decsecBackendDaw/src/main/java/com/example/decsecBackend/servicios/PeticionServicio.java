package com.example.decsecBackend.servicios;
import com.example.decsecBackend.dtos.PeticionDTO;
import com.example.decsecBackend.modelo.Usuario;
import java.util.List;

public interface PeticionServicio {

    PeticionDTO obtenerPeticion(Long usuarioEmisorId, String emailReceptor);

    PeticionDTO enviarPeticion(Usuario usuarioEmisor, String emailReceptor);

    PeticionDTO cambiarEstado(Usuario usuarioEmisor, String emailReceptor, String estado);

    List<PeticionDTO> misPeticiones(Long usuarioEmisorId);

}
