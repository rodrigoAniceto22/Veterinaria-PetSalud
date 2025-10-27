package com.petsalud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad Resultado Veterinario - Resultados de análisis
 * RF-05: Gestión de análisis
 * RF-06: Validación de resultados
 * RF-07: Generación de informe veterinario
 */
@Entity
@Table(name = "resultados_veterinarios")
public class ResultadoVeterinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resultado")
    private Long idResultado;

    @Column(name = "fecha_resultado", nullable = false)
    private LocalDateTime fechaResultado;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "valores", columnDefinition = "TEXT")
    private String valores; // Valores numéricos o descriptivos del análisis

    @Column(name = "valores_referencia", columnDefinition = "TEXT")
    private String valoresReferencia; // Rangos normales de referencia

    @Column(name = "conclusiones", columnDefinition = "TEXT")
    private String conclusiones;

    @Column(name = "recomendaciones", columnDefinition = "TEXT")
    private String recomendaciones;

    @Column(name = "validado", nullable = false)
    private Boolean validado = false;

    @Column(name = "fecha_validacion")
    private LocalDateTime fechaValidacion;

    @Column(name = "entregado")
    private Boolean entregado = false;

    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

    @Column(name = "metodo_analisis", length = 200)
    private String metodoAnalisis;

    @Column(name = "observaciones_tecnicas", columnDefinition = "TEXT")
    private String observacionesTecnicas;

    // Relación N:1 con OrdenVeterinaria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden", nullable = false)
    @JsonIgnoreProperties({"resultados", "tomaMuestra"})
    private OrdenVeterinaria orden;

    // Constructores
    public ResultadoVeterinario() {
        this.fechaResultado = LocalDateTime.now();
        this.validado = false;
        this.entregado = false;
    }

    public ResultadoVeterinario(String descripcion, String valores, OrdenVeterinaria orden) {
        this();
        this.descripcion = descripcion;
        this.valores = valores;
        this.orden = orden;
    }

    // Getters y Setters
    public Long getIdResultado() {
        return idResultado;
    }

    public void setIdResultado(Long idResultado) {
        this.idResultado = idResultado;
    }

    public LocalDateTime getFechaResultado() {
        return fechaResultado;
    }

    public void setFechaResultado(LocalDateTime fechaResultado) {
        this.fechaResultado = fechaResultado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getValores() {
        return valores;
    }

    public void setValores(String valores) {
        this.valores = valores;
    }

    public String getValoresReferencia() {
        return valoresReferencia;
    }

    public void setValoresReferencia(String valoresReferencia) {
        this.valoresReferencia = valoresReferencia;
    }

    public String getConclusiones() {
        return conclusiones;
    }

    public void setConclusiones(String conclusiones) {
        this.conclusiones = conclusiones;
    }

    public String getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(String recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    public Boolean getValidado() {
        return validado;
    }

    public void setValidado(Boolean validado) {
        this.validado = validado;
    }

    public LocalDateTime getFechaValidacion() {
        return fechaValidacion;
    }

    public void setFechaValidacion(LocalDateTime fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }

    public Boolean getEntregado() {
        return entregado;
    }

    public void setEntregado(Boolean entregado) {
        this.entregado = entregado;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getMetodoAnalisis() {
        return metodoAnalisis;
    }

    public void setMetodoAnalisis(String metodoAnalisis) {
        this.metodoAnalisis = metodoAnalisis;
    }

    public String getObservacionesTecnicas() {
        return observacionesTecnicas;
    }

    public void setObservacionesTecnicas(String observacionesTecnicas) {
        this.observacionesTecnicas = observacionesTecnicas;
    }

    public OrdenVeterinaria getOrden() {
        return orden;
    }

    public void setOrden(OrdenVeterinaria orden) {
        this.orden = orden;
    }

    @Override
    public String toString() {
        return "ResultadoVeterinario{" +
                "idResultado=" + idResultado +
                ", fechaResultado=" + fechaResultado +
                ", validado=" + validado +
                ", entregado=" + entregado +
                '}';
    }
}