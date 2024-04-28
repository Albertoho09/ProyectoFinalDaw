package com.example.decsecBackend.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.decsecBackend.modelo.Imagen;

@Repository
public interface ImagenRepositorio extends JpaRepository<Imagen, Long>{

}
