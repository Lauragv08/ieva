package com.ieva.ieva.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "egresados")
public class Egresado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_identificacion", nullable = false, length = 50)
    @NotEmpty
    private String tipoIdentificacion;

    @NotNull
    private Long identificacion;

    @NotEmpty
    @Column(name = "nombre_completo", length = 60, nullable = false)
    private String nombreCompleto;

    @Column(name = "fecha_nacimiento")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date fechaNacimiento;

    @NotEmpty
    private String ciudadResidencia;

    @NotEmpty
    private String direccion;

    @NotEmpty
    private String telefono;

    @NotEmpty
    private String celular;

    @Column(name = "correo_electronico", length = 60)
    @NotEmpty
    @Email
    private String correoElectronico;

    @NotNull
    private Integer anioGraduacion;

    @NotEmpty
    private String grupoPertenecio;

    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    private String imagen;

     @OneToMany(mappedBy = "egresado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hablanos> hablanos = new ArrayList<>(); 

    @OneToMany(mappedBy = "egresado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Documento> documentos = new ArrayList<>();

    private String grupo;

    public Egresado() {
        this.hablanos = new ArrayList<>(); // Nombre del campo debe coincidir
        this.documentos = new ArrayList<>();
    }

    public Egresado(Long id, String tipoIdentificacion, Long identificacion, String nombreCompleto,
            Date fechaNacimiento,
            String ciudadResidencia, String direccion, String telefono, String celular, String correoElectronico,
            Integer anioGraduacion, String grupoPertenecio) {
        this.id = id;
        this.tipoIdentificacion = tipoIdentificacion;
        this.identificacion = identificacion;
        this.nombreCompleto = nombreCompleto;
        this.fechaNacimiento = fechaNacimiento;
        this.ciudadResidencia = ciudadResidencia;
        this.direccion = direccion;
        this.telefono = telefono;
        this.celular = celular;
        this.correoElectronico = correoElectronico;
        this.anioGraduacion = anioGraduacion;
        this.grupoPertenecio = grupoPertenecio;
   
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public Long getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(Long identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCiudadResidencia() {
        return ciudadResidencia;
    }

    public void setCiudadResidencia(String ciudadResidencia) {
        this.ciudadResidencia = ciudadResidencia;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public Integer getAnioGraduacion() {
        return anioGraduacion;
    }

    public void setAnioGraduacion(Integer anioGraduacion) {
        this.anioGraduacion = anioGraduacion;
    }

    public String getGrupoPertenecio() {
        return grupoPertenecio;
    }

    public void setGrupoPertenecio(String grupoPertenecio) {
        this.grupoPertenecio = grupoPertenecio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public List<Hablanos> getHablanos() {
        return hablanos;
    }

    public void setHablanos(List<Hablanos> hablanos) {
        this.hablanos = hablanos;
    }

 public List<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<Documento> documentos) {
        this.documentos = documentos;
    }

    @Override
    public String toString() {
        return "{identificacion: " + identificacion +
                ", nombre completo: " + nombreCompleto +
                ", ciudad de residencia: " + ciudadResidencia +
                ", año de graduación: " + anioGraduacion + "}";
    }
}
