package com.petsalud.repository;

import com.petsalud.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para Citas
 * Sistema de citas con alertas
 */
@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {

    /**
     * Buscar citas por mascota
     */
    List<Cita> findByMascota_IdMascota(Long idMascota);

    /**
     * Buscar citas por veterinario
     */
    List<Cita> findByVeterinario_IdVeterinario(Long idVeterinario);

    /**
     * Buscar por estado
     */
    List<Cita> findByEstadoIgnoreCase(String estado);

    /**
     * Buscar citas por rango de fechas
     */
    List<Cita> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Buscar citas del día
     */
    @Query("SELECT c FROM Cita c WHERE DATE(c.fechaHora) = DATE(:fecha) ORDER BY c.fechaHora ASC")
    List<Cita> findCitasDelDia(@Param("fecha") LocalDateTime fecha);

    /**
     * Buscar citas próximas (próximas 24 horas)
     */
    @Query("SELECT c FROM Cita c WHERE c.fechaHora BETWEEN :ahora AND :limite AND c.estado != 'CANCELADA' ORDER BY c.fechaHora ASC")
    List<Cita> findCitasProximas(@Param("ahora") LocalDateTime ahora, @Param("limite") LocalDateTime limite);

    /**
     * Buscar citas críticas (próxima hora)
     */
    @Query("SELECT c FROM Cita c WHERE c.fechaHora BETWEEN :ahora AND :limite AND c.estado != 'CANCELADA' ORDER BY c.fechaHora ASC")
    List<Cita> findCitasCriticas(@Param("ahora") LocalDateTime ahora, @Param("limite") LocalDateTime limite);

    /**
     * Buscar citas pendientes de confirmación
     */
    @Query("SELECT c FROM Cita c WHERE c.estado = 'PROGRAMADA' AND c.fechaHora > :ahora ORDER BY c.fechaHora ASC")
    List<Cita> findCitasPendientesConfirmacion(@Param("ahora") LocalDateTime ahora);

    /**
     * Buscar citas sin recordatorio enviado
     */
    @Query("SELECT c FROM Cita c WHERE c.recordatorioEnviado = false AND c.fechaHora > :ahora AND c.estado != 'CANCELADA'")
    List<Cita> findCitasSinRecordatorio(@Param("ahora") LocalDateTime ahora);

    /**
     * Contar citas por estado
     */
    long countByEstadoIgnoreCase(String estado);

    /**
     * Buscar citas por tipo
     */
    List<Cita> findByTipoCitaIgnoreCase(String tipoCita);
}