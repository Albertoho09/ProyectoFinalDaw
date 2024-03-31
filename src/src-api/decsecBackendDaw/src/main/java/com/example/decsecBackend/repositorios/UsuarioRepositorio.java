package com.example.decsecBackend.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.decsecBackend.modelo.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    @SuppressWarnings("null")
    Optional<Usuario> findById(Long id);

    Boolean existsByEmail(String email);
}
