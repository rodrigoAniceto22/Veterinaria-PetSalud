package com.petsalud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * Entidad Detalle de Factura - Líneas de detalle de servicios facturados
 * Desglose de servicios y productos en la factura
 */
@Entity
@Table(name = "detalle_facturas")
public class DetalleFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long idDetalle;

    @Column(name = "descripcion", nullable = false, length = 200)
    private String descripcion;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @Column(name = "tipo_servicio", length = 100)
    private String tipoServicio; // EXAMEN, CONSULTA, MEDICAMENTO, VACUNA, CIRUGIA, etc.

    // Relación N:1 con Factura
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_factura", nullable = false)
    @JsonIgnoreProperties({"detalles"})
    private Factura factura;

    // Constructores
    public DetalleFactura() {
    }

    public DetalleFactura(String descripcion, Integer cantidad, Double precioUnitario, String tipoServicio) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.tipoServicio = tipoServicio;
        this.subtotal = cantidad * precioUnitario;
    }

    // Getters y Setters
    public Long getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Long idDetalle) {
        this.idDetalle = idDetalle;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
        calcularSubtotal();
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    // Método para calcular subtotal automáticamente
    private void calcularSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            this.subtotal = cantidad * precioUnitario;
        }
    }

    @Override
    public String toString() {
        return "DetalleFactura{" +
                "idDetalle=" + idDetalle +
                ", descripcion='" + descripcion + '\'' +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + subtotal +
                '}';
    }
}