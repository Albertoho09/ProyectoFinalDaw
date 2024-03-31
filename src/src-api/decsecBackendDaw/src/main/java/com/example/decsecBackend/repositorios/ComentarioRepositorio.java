package com.example.decsecBackend.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.decsecBackend.modelo.Comentario;

@Repository
public interface ComentarioRepositorio extends JpaRepository<Comentario, Long> {

}
