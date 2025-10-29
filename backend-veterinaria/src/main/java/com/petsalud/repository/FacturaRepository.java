package com.petsalud.repository;

import com.petsalud.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para la entidad Factura
 * Sistema de facturación y cobros
 */
@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    /**
     * Buscar factura por número
     */
    Factura findByNumeroFactura(String numeroFactura);

    /**
     * Buscar facturas por dueño
     */
    List<Factura> findByDueno_IdDueno(Long idDueno);

    /**
     * Buscar facturas por fecha de emisión
     */
    List<Factura> findByFechaEmision(LocalDate fecha);

    /**
     * Buscar facturas por rango de fechas
     */
    List<Factura> findByFechaEmisionBetween(LocalDate inicio, LocalDate fin);

    /**
     * Buscar facturas por método de pago (case insensitive)
     */
    List<Factura> findByMetodoPagoIgnoreCase(String metodoPago);

    /**
     * Buscar facturas por estado (case insensitive)
     */
    List<Factura> findByEstadoIgnoreCase(String estado);

    /**
     * Buscar facturas por fecha de emisión y estado
     */
    List<Factura> findByFechaEmisionAndEstadoIgnoreCase(LocalDate fecha, String estado);

    /**
     * Buscar facturas por rango de fechas y estado
     */
    List<Factura> findByFechaEmisionBetweenAndEstadoIgnoreCase(
            LocalDate inicio, LocalDate fin, String estado);

    /**
     * Contar facturas por estado
     */
    long countByEstadoIgnoreCase(String estado);

    /**
     * Verificar si existe número de factura
     */
    boolean existsByNumeroFactura(String numeroFactura);

    /**
     * Buscar facturas pendientes de pago
     */
    @Query("SELECT f FROM Factura f WHERE f.estado = 'PENDIENTE' ORDER BY f.fechaEmision ASC")
    List<Factura> findFacturasPendientes();

    /**
     * Buscar facturas vencidas
     */
    @Query("SELECT f FROM Factura f WHERE f.estado = 'PENDIENTE' AND f.fechaVencimiento < :fechaActual")
    List<Factura> findFacturasVencidas(@Param("fechaActual") LocalDate fechaActual);

    /**
     * Calcular total facturado en un período
     */
    @Query("SELECT SUM(f.total) FROM Factura f WHERE f.fechaEmision BETWEEN :inicio AND :fin")
    Double calcularTotalFacturado(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    /**
     * Calcular total cobrado (facturas pagadas) en un período
     */
    @Query("SELECT SUM(f.total) FROM Factura f WHERE f.estado = 'PAGADA' AND f.fechaPago BETWEEN :inicio AND :fin")
    Double calcularTotalCobrado(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    /**
     * Buscar facturas por dueño y estado
     */
    @Query("SELECT f FROM Factura f WHERE f.dueno.idDueno = :idDueno AND f.estado = :estado")
    List<Factura> findByDuenoAndEstado(@Param("idDueno") Long idDueno, @Param("estado") String estado);

    /**
     * Buscar facturas del mes actual
     */
    @Query("SELECT f FROM Factura f WHERE YEAR(f.fechaEmision) = :anio AND MONTH(f.fechaEmision) = :mes")
    List<Factura> findFacturasDelMes(@Param("anio") int anio, @Param("mes") int mes);

    /**
     * Obtener top clientes por monto facturado
     */
    @Query("SELECT f.dueno, SUM(f.total) as total FROM Factura f WHERE f.estado = 'PAGADA' GROUP BY f.dueno ORDER BY total DESC")
    List<Object[]> findTopClientes();

    /**
     * Estadísticas de facturación por método de pago
     */
    @Query("SELECT f.metodoPago, COUNT(f), SUM(f.total) FROM Factura f WHERE f.estado = 'PAGADA' GROUP BY f.metodoPago")
    List<Object[]> getEstadisticasPorMetodoPago();

    /**
     * Buscar últimas facturas
     */
    @Query("SELECT f FROM Factura f ORDER BY f.fechaEmision DESC")
    List<Factura> findUltimasFacturas();

    /**
     * Calcular total pendiente por dueño
     */
    @Query("SELECT SUM(f.total) FROM Factura f WHERE f.dueno.idDueno = :idDueno AND f.estado = 'PENDIENTE'")
    Double calcularTotalPendienteByDueno(@Param("idDueno") Long idDueno);
}