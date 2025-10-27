package com.petsalud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Orden Veterinaria - Órdenes de exámenes y análisis
 * RF-02: Registro de órdenes veterinarias
 */
@Entity
@Table(name = "ordenes_veterinarias")
public class OrdenVeterinaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden")
    private Long idOrden;

    @Column(name = "fecha_orden", nullable = false)
    private LocalDate fechaOrden;

    @Column(name = "tipo_examen", nullable = false, length = 100)
    private String tipoExamen; // Hemograma, Bioquímica, Urianálisis, etc.

    @Column(name = "prioridad", length = 20)
    private String prioridad; // URGENTE, NORMAL, BAJA

    @Column(name = "estado", length = 50)
    private String estado; // PENDIENTE, EN_PROCESO, COMPLETADA, CANCELADA

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "diagnostico_presuntivo", columnDefinition = "TEXT")
    private String diagnosticoPresuntivo;

    @Column(name = "sintomas", columnDefinition = "TEXT")
    private String sintomas;

    // Relación N:1 con Mascota
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mascota", nullable = false)
    @JsonIgnoreProperties({"ordenes", "dueno"})
    private Mascota mascota;

    // Relación N:1 con Veterinario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_veterinario", nullable = false)
    @JsonIgnoreProperties({"ordenes"})
    private Veterinario veterinario;

    // Relación 1:1 con TomaMuestraVet
    @OneToOne(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"orden"})
    private TomaMuestraVet tomaMuestra;

    // Relación 1:N con ResultadoVeterinario
    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"orden"})
    private List<ResultadoVeterinario> resultados = new ArrayList<>();

    // Constructores
    public OrdenVeterinaria() {
        this.fechaOrden = LocalDate.now();
        this.estado = "PENDIENTE";
        this.prioridad = "NORMAL";
    }

    public OrdenVeterinaria(String tipoExamen, Mascota mascota, Veterinario veterinario) {
        this();
        this.tipoExamen = tipoExamen;
        this.mascota = mascota;
        this.veterinario = veterinario;
    }

    // Getters y Setters
    public Long getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(Long idOrden) {
        this.idOrden = idOrden;
    }

    public LocalDate getFechaOrden() {
        return fechaOrden;
    }

    public void setFechaOrden(LocalDate fechaOrden) {
        this.fechaOrden = fechaOrden;
    }

    public String getTipoExamen() {
        return tipoExamen;
    }

    public void setTipoExamen(String tipoExamen) {
        this.tipoExamen = tipoExamen;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getDiagnosticoPresuntivo() {
        return diagnosticoPresuntivo;
    }

    public void setDiagnosticoPresuntivo(String diagnosticoPresuntivo) {
        this.diagnosticoPresuntivo = diagnosticoPresuntivo;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public TomaMuestraVet getTomaMuestra() {
        return tomaMuestra;
    }

    public void setTomaMuestra(TomaMuestraVet tomaMuestra) {
        this.tomaMuestra = tomaMuestra;
    }

    public List<ResultadoVeterinario> getResultados() {
        return resultados;
    }

    public void setResultados(List<ResultadoVeterinario> resultados) {
        this.resultados = resultados;
    }

    // Métodos de ayuda para relaciones bidireccionales
    public void addResultado(ResultadoVeterinario resultado) {
        resultados.add(resultado);
        resultado.setOrden(this);
    }

    public void removeResultado(ResultadoVeterinario resultado) {
        resultados.remove(resultado);
        resultado.setOrden(null);
    }

    @Override
    public String toString() {
        return "OrdenVeterinaria{" +
                "idOrden=" + idOrden +
                ", fechaOrden=" + fechaOrden +
                ", tipoExamen='" + tipoExamen + '\'' +
                ", estado='" + estado + '\'' +
                ", prioridad='" + prioridad + '\'' +
                '}';
    }
}