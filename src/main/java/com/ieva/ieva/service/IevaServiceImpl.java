package com.ieva.ieva.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ieva.ieva.model.dao.DocumentoDAOIface;
import com.ieva.ieva.model.dao.EgresadoDAOIface;
import com.ieva.ieva.model.dao.HablanosDAOIface;
import com.ieva.ieva.model.entity.Documento;
import com.ieva.ieva.model.entity.Egresado;
import com.ieva.ieva.model.entity.Hablanos;
import java.util.Optional;

@Service
public class IevaServiceImpl implements IevaServiceIface {

    private final EgresadoDAOIface egresadoDAO;
    private final DocumentoDAOIface documentoDAO;
    private final HablanosDAOIface hablanosDAO;

    public IevaServiceImpl(EgresadoDAOIface egresadoDAO, 
                         DocumentoDAOIface documentoDAO,
                         HablanosDAOIface hablanosDAO) {
        this.egresadoDAO = egresadoDAO;
        this.documentoDAO = documentoDAO;
        this.hablanosDAO = hablanosDAO;
    }

    // Implementación de métodos para Egresado
    @Override
    @Transactional(readOnly = true)
    public List<Egresado> buscarEgresadosTodos() {
        return egresadoDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Egresado> buscarEgresadosTodos(Pageable pageable) {
        return egresadoDAO.findAll(pageable);
    }

    @Override
    @Transactional
    public void guardarEgresado(Egresado egresado) {
        egresadoDAO.save(egresado);
    }

    @Override
@Transactional(readOnly = true)
public Optional<Egresado> buscarEgresadoPorId(Long id) {
    return egresadoDAO.findById(id);
}

    @Override
    @Transactional
    public void eliminarEgresadoPorId(Long id) {
        egresadoDAO.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Egresado> buscarEgresadosConFiltro(String filtro, Pageable pageable) {
        return egresadoDAO.filtrarEgresados(filtro, pageable);
    }

       @Override
    @Transactional(readOnly = true)
    public Page<Egresado> filtrarPorAnioYGrupo(Integer anioGraduacion, String grupoPertenecio, Pageable pageable) {
        return egresadoDAO.filtrarPorAnioYGrupo(anioGraduacion, grupoPertenecio, pageable);
    }

    // Implementación de métodos para Documento
    @Override
    @Transactional(readOnly = true)
    public List<Documento> buscarDocumentosPorEgresadoId(Long egresadoId) {
        return documentoDAO.findByEgresadoId(egresadoId);
    }

    @Override
    @Transactional(readOnly = true)
    public Documento buscarDocumentoPorId(Long id) {
        return documentoDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void guardarDocumento(Documento documento) {
        documentoDAO.save(documento);
    }

    @Override
    @Transactional
    public void eliminarDocumentoPorId(Long id) {
        documentoDAO.deleteById(id);
    }

    // Implementación de métodos para HáblanosDeTi
    @Override
    @Transactional
    public void guardarHablanos(Hablanos hablanos) {
        hablanosDAO.save(hablanos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hablanos> buscarHablanosPorEgresadoId(Long egresadoId) {
        return hablanosDAO.findByEgresadoId(egresadoId);
    }

    @Override
    @Transactional
    public void eliminarHablanos(Long id) {
        hablanosDAO.deleteById(id);
    }
}