package com.petsalud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Factura - Facturas de servicios veterinarios
 * Sistema de facturación y cobros
 */
@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_factura")
    private Long idFactura;

    @Column(name = "numero_factura", nullable = false, unique = true, length = 50)
    private String numeroFactura;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @Column(name = "igv")
    private Double igv; // Impuesto General a las Ventas (18% en Perú)

    @Column(name = "total", nullable = false)
    private Double total;

    @Column(name = "estado", length = 50)
    private String estado; // PENDIENTE, PAGADA, ANULADA

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago; // EFECTIVO, TARJETA, TRANSFERENCIA, YAPE, PLIN

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    // Relación N:1 con Dueno
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dueno", nullable = false)
    @JsonIgnoreProperties({"facturas", "mascotas"})
    private Dueno dueno;

    // Relación 1:N con DetalleFactura
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"factura"})
    private List<DetalleFactura> detalles = new ArrayList<>();

    // Constructores
    public Factura() {
        this.fechaEmision = LocalDate.now();
        this.estado = "PENDIENTE";
        this.igv = 0.0;
        this.subtotal = 0.0;
        this.total = 0.0;
    }

    public Factura(String numeroFactura, Dueno dueno) {
        this();
        this.numeroFactura = numeroFactura;
        this.dueno = dueno;
    }

    // Getters y Setters
    public Long getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Long idFactura) {
        this.idFactura = idFactura;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
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

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getIgv() {
        return igv;
    }

    public void setIgv(Double igv) {
        this.igv = igv;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Dueno getDueno() {
        return dueno;
    }

    public void setDueno(Dueno dueno) {
        this.dueno = dueno;
    }

    public List<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFactura> detalles) {
        this.detalles = detalles;
    }

    // Métodos de ayuda para relaciones bidireccionales
    public void addDetalle(DetalleFactura detalle) {
        detalles.add(detalle);
        detalle.setFactura(this);
        calcularTotales();
    }

    public void removeDetalle(DetalleFactura detalle) {
        detalles.remove(detalle);
        detalle.setFactura(null);
        calcularTotales();
    }

    // Método para calcular totales automáticamente
    public void calcularTotales() {
        this.subtotal = detalles.stream()
                .mapToDouble(DetalleFactura::getSubtotal)
                .sum();
        this.igv = this.subtotal * 0.18; // 18% de IGV
        this.total = this.subtotal + this.igv;
    }

    @Override
    public String toString() {
        return "Factura{" +
                "idFactura=" + idFactura +
                ", numeroFactura='" + numeroFactura + '\'' +
                ", fechaEmision=" + fechaEmision +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                '}';
    }
}