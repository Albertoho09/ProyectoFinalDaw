package com.example.decsecBackend.serviciosImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.decsecBackend.dtos.PeticionDTO;
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
        PeticionDTO resultado = repositorioPeticion.encontrarPeticion(usuarioEmisorId, usu.getId());
        return resultado;
    };

}
