package com.petsalud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad Pago - Sistema de pagos y cobros
 * Incluye costos de internamiento
 */
@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;

    @Column(name = "numero_pago", unique = true, length = 50)
    private String numeroPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dueno", nullable = false)
    @JsonIgnoreProperties({"mascotas", "facturas", "pagos"})
    private Dueno dueno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mascota")
    @JsonIgnoreProperties({"dueno", "ordenes"})
    private Mascota mascota;

    @Column(name = "concepto", nullable = false, length = 200)
    private String concepto;

    @Column(name = "tipo_pago", length = 50)
    private String tipoPago; // Consulta, Internamiento, Cirugía, Medicamento, etc.

    @Column(name = "monto", nullable = false)
    private Double monto;

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago; // Efectivo, Tarjeta, Transferencia, Yape, Plin

    @Column(name = "estado", length = 50)
    private String estado; // PENDIENTE, PAGADO, PARCIAL, CANCELADO

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @Column(name = "monto_pagado")
    private Double montoPagado = 0.0;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    // Campos específicos para internamiento
    @Column(name = "es_internamiento")
    private Boolean esInternamiento = false;

    @Column(name = "fecha_inicio_internamiento")
    private LocalDateTime fechaInicioInternamiento;

    @Column(name = "fecha_fin_internamiento")
    private LocalDateTime fechaFinInternamiento;

    @Column(name = "dias_internamiento")
    private Integer diasInternamiento;

    @Column(name = "costo_dia_internamiento")
    private Double costoDiaInternamiento;

    // Constructores
    public Pago() {
    }

    public Pago(Dueno dueno, String concepto, Double monto, String estado) {
        this.dueno = dueno;
        this.concepto = concepto;
        this.monto = monto;
        this.estado = estado;
        this.fechaEmision = LocalDate.now();
    }

    // Getters y Setters
    public Long getIdPago() {
        return idPago;
    }

    public void setIdPago(Long idPago) {
        this.idPago = idPago;
    }

    public String getNumeroPago() {
        return numeroPago;
    }

    public void setNumeroPago(String numeroPago) {
        this.numeroPago = numeroPago;
    }

    public Dueno getDueno() {
        return dueno;
    }

    public void setDueno(Dueno dueno) {
        this.dueno = dueno;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Double getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(Double montoPagado) {
        this.montoPagado = montoPagado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getEsInternamiento() {
        return esInternamiento;
    }

    public void setEsInternamiento(Boolean esInternamiento) {
        this.esInternamiento = esInternamiento;
    }

    public LocalDateTime getFechaInicioInternamiento() {
        return fechaInicioInternamiento;
    }

    public void setFechaInicioInternamiento(LocalDateTime fechaInicioInternamiento) {
        this.fechaInicioInternamiento = fechaInicioInternamiento;
    }

    public LocalDateTime getFechaFinInternamiento() {
        return fechaFinInternamiento;
    }

    public void setFechaFinInternamiento(LocalDateTime fechaFinInternamiento) {
        this.fechaFinInternamiento = fechaFinInternamiento;
    }

    public Integer getDiasInternamiento() {
        return diasInternamiento;
    }

    public void setDiasInternamiento(Integer diasInternamiento) {
        this.diasInternamiento = diasInternamiento;
    }

    public Double getCostoDiaInternamiento() {
        return costoDiaInternamiento;
    }

    public void setCostoDiaInternamiento(Double costoDiaInternamiento) {
        this.costoDiaInternamiento = costoDiaInternamiento;
    }

    // Métodos de negocio
    public Double calcularSaldo() {
        return monto - (montoPagado != null ? montoPagado : 0.0);
    }

    public boolean estaPagado() {
        return "PAGADO".equalsIgnoreCase(estado);
    }

    public boolean estaVencido() {
        return fechaVencimiento != null && 
               LocalDate.now().isAfter(fechaVencimiento) && 
               !"PAGADO".equalsIgnoreCase(estado);
    }

    public void calcularCostoInternamiento() {
        if (esInternamiento != null && esInternamiento && 
            diasInternamiento != null && costoDiaInternamiento != null) {
            this.monto = diasInternamiento * costoDiaInternamiento;
        }
    }

    @Override
    public String toString() {
        return "Pago{" +
                "idPago=" + idPago +
                ", numeroPago='" + numeroPago + '\'' +
                ", concepto='" + concepto + '\'' +
                ", monto=" + monto +
                ", estado='" + estado + '\'' +
                '}';
    }
}