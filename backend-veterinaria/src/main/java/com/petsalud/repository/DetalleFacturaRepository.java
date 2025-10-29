package com.petsalud.repository;

import com.petsalud.model.DetalleFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad DetalleFactura
 * Detalles de servicios y productos facturados
 */
@Repository
public interface DetalleFacturaRepository extends JpaRepository<DetalleFactura, Long> {

    /**
     * Buscar detalles por factura
     */
    List<DetalleFactura> findByFactura_IdFactura(Long idFactura);

    /**
     * Buscar detalles por tipo de servicio
     */
    List<DetalleFactura> findByTipoServicioIgnoreCase(String tipoServicio);

    /**
     * Buscar detalles por descripción (contiene, case insensitive)
     */
    List<DetalleFactura> findByDescripcionContainingIgnoreCase(String descripcion);

    /**
     * Contar detalles por tipo de servicio
     */
    long countByTipoServicioIgnoreCase(String tipoServicio);

    /**
     * Buscar servicios más facturados
     */
    @Query("SELECT d.descripcion, SUM(d.cantidad) as total FROM DetalleFactura d GROUP BY d.descripcion ORDER BY total DESC")
    List<Object[]> findServiciosMasFacturados();

    /**
     * Calcular total por tipo de servicio
     */
    @Query("SELECT d.tipoServicio, SUM(d.subtotal) FROM DetalleFactura d GROUP BY d.tipoServicio")
    List<Object[]> calcularTotalPorTipoServicio();

    /**
     * Buscar detalles por factura y tipo de servicio
     */
    @Query("SELECT d FROM DetalleFactura d WHERE d.factura.idFactura = :idFactura AND d.tipoServicio = :tipo")
    List<DetalleFactura> findByFacturaAndTipo(@Param("idFactura") Long idFactura, @Param("tipo") String tipo);

    /**
     * Obtener productos/servicios más vendidos
     */
    @Query("SELECT d.descripcion, COUNT(d) as cantidad, SUM(d.subtotal) as total FROM DetalleFactura d GROUP BY d.descripcion ORDER BY cantidad DESC")
    List<Object[]> findProductosMasVendidos();

    /**
     * Calcular precio promedio por tipo de servicio
     */
    @Query("SELECT d.tipoServicio, AVG(d.precioUnitario) FROM DetalleFactura d GROUP BY d.tipoServicio")
    List<Object[]> calcularPrecioPromedioPorTipo();

    /**
     * Buscar detalles con precio mayor a
     */
    List<DetalleFactura> findByPrecioUnitarioGreaterThan(Double precio);

    /**
     * Buscar detalles con cantidad mayor a
     */
    List<DetalleFactura> findByCantidadGreaterThan(Integer cantidad);
}