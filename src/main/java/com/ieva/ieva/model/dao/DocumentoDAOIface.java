package com.ieva.ieva.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ieva.ieva.model.entity.Documento;


public interface DocumentoDAOIface extends JpaRepository<Documento, Long>  {
    List<Documento> findByEgresadoId(Long egresadoId);
    

  

}
