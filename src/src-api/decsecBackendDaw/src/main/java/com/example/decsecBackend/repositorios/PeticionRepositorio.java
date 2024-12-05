package com.example.decsecBackend.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.decsecBackend.dtos.PeticionDTO;
import com.example.decsecBackend.modelo.Peticion;
import com.example.decsecBackend.modelo.Usuario;
import java.util.List;

@Repository
public interface PeticionRepositorio extends JpaRepository<Peticion, Long> {

    @Query("SELECT p FROM Peticion p WHERE p.usuarioEmisor = :usuarioEmisorId AND p.usuarioReceptor = :usuarioReceptorId")
    Peticion encontrarPeticion(@Param("usuarioEmisorId") Long usuarioEmisorId,
            @Param("usuarioReceptorId") Long usuarioReceptorId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Peticion p WHERE p.usuarioEmisor.id = :usuarioEmisorId AND p.usuarioReceptor.id = :usuarioReceptorId")
    boolean existsByUsuarioEmisorIdAndUsuarioReceptorId(@Param("usuarioEmisorId") Long usuarioEmisorId,
            @Param("usuarioReceptorId") Long usuarioReceptorId);

    @Query("SELECT p FROM Peticion p WHERE p.usuarioReceptor = :usuarioReceptorId")
    List<PeticionDTO> listarMisPeticiones(@Param("usuarioReceptorId") Long usuarioReceptorId);
}
