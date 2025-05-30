
package com.ieva.ieva.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ieva.ieva.model.entity.Hablanos;

public interface HablanosDAOIface extends JpaRepository<Hablanos, Long> {
    List<Hablanos> findByEgresadoId(Long egresadoId);
}
