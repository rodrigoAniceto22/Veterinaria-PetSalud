package com.petsalud.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entidad Inventario - Productos y medicamentos
 * Control de stock y precios
 */
@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Long idInventario;

    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "categoria", length = 100)
    private String categoria; // Medicamento, Alimento, Accesorio, etc.

    @Column(name = "precio_compra", nullable = false)
    private Double precioCompra;

    @Column(name = "precio_venta", nullable = false)
    private Double precioVenta;

    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual;

    @Column(name = "stock_minimo")
    private Integer stockMinimo;

    @Column(name = "stock_maximo")
    private Integer stockMaximo;

    @Column(name = "unidad_medida", length = 50)
    private String unidadMedida; // Unidad, Caja, Frasco, ml, gr, etc.

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "proveedor", length = 200)
    private String proveedor;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    // Constructores
    public Inventario() {
    }

    public Inventario(String codigo, String nombre, Double precioCompra, Double precioVenta, Integer stockActual) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.stockActual = stockActual;
    }

    // Getters y Setters
    public Long getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(Long idInventario) {
        this.idInventario = idInventario;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Integer getStockMaximo() {
        return stockMaximo;
    }

    public void setStockMaximo(Integer stockMaximo) {
        this.stockMaximo = stockMaximo;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    // Métodos de negocio
    public boolean necesitaReposicion() {
        return stockMinimo != null && stockActual <= stockMinimo;
    }

    public boolean estaVencido() {
        return fechaVencimiento != null && fechaVencimiento.isBefore(LocalDate.now());
    }

    public boolean proximoAVencer(int diasAntes) {
        if (fechaVencimiento == null) return false;
        LocalDate fechaLimite = LocalDate.now().plusDays(diasAntes);
        return fechaVencimiento.isBefore(fechaLimite) || fechaVencimiento.isEqual(fechaLimite);
    }

    public Double calcularMargenGanancia() {
        if (precioCompra == null || precioVenta == null || precioCompra == 0) return 0.0;
        return ((precioVenta - precioCompra) / precioCompra) * 100;
    }

    @Override
    public String toString() {
        return "Inventario{" +
                "idInventario=" + idInventario +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", stockActual=" + stockActual +
                ", precioVenta=" + precioVenta +
                '}';
    }
}