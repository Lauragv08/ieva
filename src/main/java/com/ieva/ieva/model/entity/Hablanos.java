package com.ieva.ieva.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "hablanos") // También podrías usar "hablanosDeTi"
public class Hablanos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDate fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "egresado_id", nullable = false)
    private Egresado egresado;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDate.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public Hablanos() {
    }

    public Hablanos(String descripcion, Egresado egresado) {
        this.descripcion = descripcion;
        this.egresado = egresado;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public Egresado getEgresado() {
        return egresado;
    }

    public void setEgresado(Egresado egresado) {
        this.egresado = egresado;
    }

    @Override
    public String toString() {
        return "Hablanos [id=" + id + ", descripcion=" + descripcion + 
               ", fechaCreacion=" + fechaCreacion + ", fechaActualizacion=" + 
               fechaActualizacion + "]";
    }
}
