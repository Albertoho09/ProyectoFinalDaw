package com.example.decsecBackend.servicios;

import com.example.decsecBackend.dtos.PeticionDTO;

public interface PeticionServicio {

    PeticionDTO obtenerPeticion(Long usuarioEmisorId, String emailReceptor);
}
