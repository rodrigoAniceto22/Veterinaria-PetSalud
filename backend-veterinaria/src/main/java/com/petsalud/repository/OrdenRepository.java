package com.petsalud.repository;

import com.petsalud.model.OrdenVeterinaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para la entidad OrdenVeterinaria
 * RF-02: Registro de órdenes veterinarias
 */
@Repository
public interface OrdenRepository extends JpaRepository<OrdenVeterinaria, Long> {

    /**
     * Buscar órdenes por mascota
     */
    List<OrdenVeterinaria> findByMascota_IdMascota(Long idMascota);

    /**
     * Buscar órdenes por veterinario
     */
    List<OrdenVeterinaria> findByVeterinario_IdVeterinario(Long idVeterinario);

    /**
     * Buscar órdenes por fecha
     */
    List<OrdenVeterinaria> findByFechaOrden(LocalDate fecha);

    /**
     * Buscar órdenes por rango de fechas
     */
    List<OrdenVeterinaria> findByFechaOrdenBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar órdenes por tipo de examen (case insensitive, contiene)
     */
    List<OrdenVeterinaria> findByTipoExamenContainingIgnoreCase(String tipoExamen);

    /**
     * Buscar órdenes por estado (case insensitive)
     */
    List<OrdenVeterinaria> findByEstadoIgnoreCase(String estado);

    /**
     * Buscar órdenes por prioridad (case insensitive)
     */
    List<OrdenVeterinaria> findByPrioridadIgnoreCase(String prioridad);

    /**
     * Contar órdenes por estado
     */
    long countByEstadoIgnoreCase(String estado);

    /**
     * Buscar órdenes por estado y fecha
     */
    List<OrdenVeterinaria> findByEstadoIgnoreCaseAndFechaOrden(String estado, LocalDate fecha);

    /**
     * Buscar órdenes por veterinario y estado
     */
    List<OrdenVeterinaria> findByVeterinario_IdVeterinarioAndEstadoIgnoreCase(
            Long idVeterinario, String estado);

    /**
     * Buscar órdenes por mascota y rango de fechas
     */
    List<OrdenVeterinaria> findByMascota_IdMascotaAndFechaOrdenBetween(
            Long idMascota, LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Buscar órdenes urgentes pendientes
     */
    @Query("SELECT o FROM OrdenVeterinaria o WHERE o.prioridad = 'URGENTE' AND o.estado != 'COMPLETADA' ORDER BY o.fechaOrden ASC")
    List<OrdenVeterinaria> findOrdenesUrgentes();

    /**
     * Buscar órdenes del día por veterinario
     */
    @Query("SELECT o FROM OrdenVeterinaria o WHERE o.veterinario.idVeterinario = :idVeterinario AND o.fechaOrden = :fecha")
    List<OrdenVeterinaria> findOrdenesDiaByVeterinario(
            @Param("idVeterinario") Long idVeterinario, 
            @Param("fecha") LocalDate fecha);

    /**
     * Contar órdenes por mascota
     */
    @Query("SELECT COUNT(o) FROM OrdenVeterinaria o WHERE o.mascota.idMascota = :idMascota")
    long countByMascota(@Param("idMascota") Long idMascota);

    /**
     * Buscar órdenes sin toma de muestra
     */
    @Query("SELECT o FROM OrdenVeterinaria o WHERE o.tomaMuestra IS NULL AND o.estado = 'PENDIENTE'")
    List<OrdenVeterinaria> findOrdenesSinTomaMuestra();

    /**
     * Buscar órdenes completadas en un rango de fechas
     */
    @Query("SELECT o FROM OrdenVeterinaria o WHERE o.estado = 'COMPLETADA' AND o.fechaOrden BETWEEN :inicio AND :fin")
    List<OrdenVeterinaria> findOrdenesCompletadasEnRango(
            @Param("inicio") LocalDate inicio, 
            @Param("fin") LocalDate fin);

    /**
     * Estadísticas de órdenes por tipo de examen
     */
    @Query("SELECT o.tipoExamen, COUNT(o) FROM OrdenVeterinaria o GROUP BY o.tipoExamen ORDER BY COUNT(o) DESC")
    List<Object[]> countOrdenesPorTipoExamen();

    /**
     * Buscar órdenes antiguas pendientes (más de X días)
     */
    @Query("SELECT o FROM OrdenVeterinaria o WHERE o.estado = 'PENDIENTE' AND o.fechaOrden < :fecha")
    List<OrdenVeterinaria> findOrdenesPendientesAntiguas(@Param("fecha") LocalDate fecha);
}