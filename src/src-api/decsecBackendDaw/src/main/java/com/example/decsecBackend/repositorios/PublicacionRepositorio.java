package com.example.decsecBackend.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;

import com.example.decsecBackend.dtos.PublicacionDTO;
import com.example.decsecBackend.modelo.Publicacion;

@Repository
public interface PublicacionRepositorio extends JpaRepository<Publicacion, Long> {

    @Query("SELECT p FROM Publicacion p WHERE (p.usuario.id = :usuarioId OR EXISTS (SELECT 1 FROM Peticion pet WHERE pet.usuarioEmisor.id = :usuarioId AND pet.usuarioReceptor.id = p.usuario.id AND pet.estado = 'ACEPTADO')) AND p.fechaPublicacion >= :fecha ORDER BY p.fechaPublicacion DESC")
    List<PublicacionDTO> encontrarPublicacionesRecientesPorUsuario(@Param("usuarioId") Long usuarioId,
            @Param("fecha") LocalDateTime fecha);

}
