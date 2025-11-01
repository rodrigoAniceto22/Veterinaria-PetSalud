package com.petsalud.repository;

import com.petsalud.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para Pagos
 * Sistema de pagos y cobros
 */
@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    /**
     * Buscar por número de pago
     */
    Pago findByNumeroPago(String numeroPago);

    /**
     * Buscar pagos por dueño
     */
    List<Pago> findByDueno_IdDueno(Long idDueno);

    /**
     * Buscar pagos por mascota
     */
    List<Pago> findByMascota_IdMascota(Long idMascota);

    /**
     * Buscar por estado
     */
    List<Pago> findByEstadoIgnoreCase(String estado);

    /**
     * Buscar pagos pendientes
     */
    @Query("SELECT p FROM Pago p WHERE p.estado = 'PENDIENTE' ORDER BY p.fechaVencimiento ASC")
    List<Pago> findPagosPendientes();

    /**
     * Buscar pagos vencidos
     */
    @Query("SELECT p FROM Pago p WHERE p.estado = 'PENDIENTE' AND p.fechaVencimiento < :fecha")
    List<Pago> findPagosVencidos(@Param("fecha") LocalDate fecha);

    /**
     * Buscar por rango de fechas
     */
    List<Pago> findByFechaEmisionBetween(LocalDate inicio, LocalDate fin);

    /**
     * Buscar internamientos activos
     */
    @Query("SELECT p FROM Pago p WHERE p.esInternamiento = true AND p.fechaFinInternamiento IS NULL")
    List<Pago> findInternamientosActivos();

    /**
     * Buscar por tipo de pago
     */
    List<Pago> findByTipoPagoIgnoreCase(String tipoPago);

    /**
     * Calcular total pendiente por dueño
     */
    @Query("SELECT SUM(p.monto - p.montoPagado) FROM Pago p WHERE p.dueno.idDueno = :idDueno AND p.estado = 'PENDIENTE'")
    Double calcularTotalPendientePorDueno(@Param("idDueno") Long idDueno);

    /**
     * Calcular total cobrado en un período
     */
    @Query("SELECT SUM(p.montoPagado) FROM Pago p WHERE p.fechaPago BETWEEN :inicio AND :fin")
    Double calcularTotalCobrado(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    /**
     * Contar pagos por estado
     */
    long countByEstadoIgnoreCase(String estado);
}