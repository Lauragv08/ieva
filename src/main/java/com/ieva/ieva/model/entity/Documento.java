package com.ieva.ieva.model.entity;


import java.util.Date;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "documentos")
public class Documento {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name = "nombre_documento")
    private String nombreDocumento;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "tipo_archivo")
    private String tipoArchivo;

    @Column(name = "fecha_carga")
    private LocalDateTime fechaCarga;

    @Column(name = "tamanio")
    private long tamanio;

    @Column(name = "estado")
    private String estado;

    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @Column(name = "fecha_modificacion")
    private Date fechaModificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "egresado_id", nullable = false)
    private Egresado egresado;

    public Documento() {
    }

    public Documento(Long id, String nombreDocumento, String descripcion, String tipoArchivo,
                     LocalDateTime fechaCarga, long tamanio, String estado,
                     Date fechaCreacion, Date fechaModificacion, Egresado egresado) {
        this.id = id;
        this.nombreDocumento = nombreDocumento;
        this.descripcion = descripcion;
        this.tipoArchivo = tipoArchivo;
        this.fechaCarga = fechaCarga;
        this.tamanio = tamanio;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.egresado = egresado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public LocalDateTime getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(LocalDateTime fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public long getTamanio() {
        return tamanio;
    }

    public void setTamanio(long tamanio) {
        this.tamanio = tamanio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Egresado getEgresado() {
        return egresado;
    }

    public void setEgresado(Egresado egresado) {
        this.egresado = egresado;
    }

    @Override
    public String toString() {
        return "Documento [id=" + id + ", nombreDocumento=" + nombreDocumento + ", descripcion=" + descripcion
                + ", tipoArchivo=" + tipoArchivo + ", fechaCarga=" + fechaCarga + ", tamanio=" + tamanio + ", estado="
                + estado + ", fechaCreacion=" + fechaCreacion + ", fechaModificacion=" + fechaModificacion + "]";
    }
}
