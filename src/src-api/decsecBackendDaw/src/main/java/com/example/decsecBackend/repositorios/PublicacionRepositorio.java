package com.example.decsecBackend.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.decsecBackend.modelo.Publicacion;

@Repository
public interface PublicacionRepositorio extends JpaRepository<Publicacion, Long> {

    @Modifying
    @Query("UPDATE Publicacion p SET p.megusta = p.megusta + 1 WHERE p.id = :id")
    void meGusta(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Publicacion p SET p.megusta = p.megusta - 1 WHERE p.id = :id")
    void noMeGusta(@Param("id") Long id);

}
