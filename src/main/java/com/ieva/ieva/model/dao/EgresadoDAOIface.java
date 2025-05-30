package com.ieva.ieva.model.dao;

import com.ieva.ieva.model.entity.Egresado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface EgresadoDAOIface extends JpaRepository<Egresado, Long> {
    
    @Query("SELECT e FROM Egresado e " +
    "WHERE LOWER(e.nombreCompleto) LIKE LOWER(CONCAT('%', :filtro, '%')) " +
    "   OR CAST(e.identificacion AS string) LIKE :filtro " +
    "   OR CAST(e.anioGraduacion AS string) LIKE :filtro " +
    "   OR LOWER(e.grupoPertenecio) LIKE LOWER(CONCAT('%', :filtro, '%'))")
Page<Egresado> filtrarEgresados(@Param("filtro") String filtro, Pageable pageable);

 @Query("SELECT e FROM Egresado e WHERE " +
           "(:anioGraduacion IS NULL OR e.anioGraduacion = :anioGraduacion) " +
           "AND (:grupoPertenecio IS NULL OR e.grupoPertenecio = :grupoPertenecio)")
    Page<Egresado> filtrarPorAnioYGrupo(
        @Param("anioGraduacion") Integer anioGraduacion,
        @Param("grupoPertenecio") String grupoPertenecio,
        Pageable pageable);

}

