package com.example.decsecBackend.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;
import com.example.decsecBackend.dtos.PublicacionDTO;
import com.example.decsecBackend.modelo.Publicacion;

@Repository
public interface PublicacionRepositorio extends JpaRepository<Publicacion, Long> {

    @Query("SELECT p FROM Publicacion p WHERE p.usuario.id = :usuarioId AND p.fechaPublicacion >= :fecha")
    List<PublicacionDTO> encontrarPublicacionesRecientesPorUsuario(@Param("usuarioId") Long usuarioId,
            @Param("fecha") LocalDateTime fecha);

    @Modifying
    @Query("UPDATE Publicacion p SET p.megusta = p.megusta + 1 WHERE p.id = :id")
    void meGusta(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Publicacion p SET p.megusta = p.megusta - 1 WHERE p.id = :id")
    void noMeGusta(@Param("id") Long id);

}
