package com.ieva.ieva.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ieva.ieva.model.entity.Documento;
import com.ieva.ieva.model.entity.Egresado;
import com.ieva.ieva.model.entity.Hablanos;
import java.util.Optional;

public interface IevaServiceIface {
    // Métodos para Egresado
    List<Egresado> buscarEgresadosTodos();
    Page<Egresado> buscarEgresadosTodos(Pageable pageable);
    void guardarEgresado(Egresado egresado);
    Optional<Egresado> buscarEgresadoPorId(Long id);
    void eliminarEgresadoPorId(Long id);
    Page<Egresado> buscarEgresadosConFiltro(String filtro, Pageable pageable);
     Page<Egresado> filtrarPorAnioYGrupo(Integer anioGraduacion, String grupoPertenecio, Pageable pageable);
    
    // Métodos para Documento
    List<Documento> buscarDocumentosPorEgresadoId(Long egresadoId);
    Documento buscarDocumentoPorId(Long id);
    void guardarDocumento(Documento documento);
    void eliminarDocumentoPorId(Long id);
    
    
    void guardarHablanos(Hablanos hablanos);
    List<Hablanos> buscarHablanosPorEgresadoId(Long egresadoId);
    void eliminarHablanos(Long id);
}