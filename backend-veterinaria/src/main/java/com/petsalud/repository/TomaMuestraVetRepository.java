package com.petsalud.repository;

import com.petsalud.model.TomaMuestraVet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la entidad TomaMuestraVet
 * RF-03: Programación de toma de muestra
 * RF-04: Registro de toma de muestra
 */
@Repository
public interface TomaMuestraVetRepository extends JpaRepository<TomaMuestraVet, Long> {

    /**
     * Buscar toma de muestra por orden
     */
    TomaMuestraVet findByOrden_IdOrden(Long idOrden);

    /**
     * Buscar tomas de muestra por técnico
     */
    List<TomaMuestraVet> findByTecnico_IdTecnico(Long idTecnico);

    /**
     * Buscar tomas de muestra por rango de fechas
     */
    List<TomaMuestraVet> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Buscar tomas de muestra por tipo de muestra (case insensitive, contiene)
     */
    List<TomaMuestraVet> findByTipoMuestraContainingIgnoreCase(String tipoMuestra);

    /**
     * Buscar toma de muestra por código
     */
    TomaMuestraVet findByCodigoMuestra(String codigoMuestra);

    /**
     * Buscar tomas de muestra por estado (case insensitive)
     */
    List<TomaMuestraVet> findByEstadoIgnoreCase(String estado);

    /**
     * Contar tomas de muestra por estado
     */
    long countByEstadoIgnoreCase(String estado);

    /**
     * Buscar tomas de muestra por técnico y estado
     */
    List<TomaMuestraVet> findByTecnico_IdTecnicoAndEstadoIgnoreCase(Long idTecnico, String estado);

    /**
     * Buscar tomas programadas para hoy
     */
    @Query("SELECT tm FROM TomaMuestraVet tm WHERE DATE(tm.fechaHora) = CURRENT_DATE AND tm.estado = 'PROGRAMADA'")
    List<TomaMuestraVet> findTomasProgramadasHoy();

    /**
     * Buscar tomas por técnico en una fecha específica
     */
    @Query("SELECT tm FROM TomaMuestraVet tm WHERE tm.tecnico.idTecnico = :idTecnico AND DATE(tm.fechaHora) = :fecha")
    List<TomaMuestraVet> findTomasByTecnicoAndFecha(
            @Param("idTecnico") Long idTecnico, 
            @Param("fecha") java.time.LocalDate fecha);

    /**
     * Buscar tomas pendientes (programadas pero no realizadas)
     */
    @Query("SELECT tm FROM TomaMuestraVet tm WHERE tm.estado IN ('PROGRAMADA') ORDER BY tm.fechaHora ASC")
    List<TomaMuestraVet> findTomasPendientes();

    /**
     * Buscar tomas atrasadas (programadas para fechas pasadas y no realizadas)
     */
    @Query("SELECT tm FROM TomaMuestraVet tm WHERE tm.estado = 'PROGRAMADA' AND tm.fechaHora < :fechaActual")
    List<TomaMuestraVet> findTomasAtrasadas(@Param("fechaActual") LocalDateTime fechaActual);

    /**
     * Contar tomas por tipo de muestra
     */
    @Query("SELECT tm.tipoMuestra, COUNT(tm) FROM TomaMuestraVet tm GROUP BY tm.tipoMuestra")
    List<Object[]> countTomasPorTipo();

    /**
     * Buscar tomas realizadas en un rango de fechas
     */
    @Query("SELECT tm FROM TomaMuestraVet tm WHERE tm.estado IN ('REALIZADA', 'COMPLETADA') AND tm.fechaHora BETWEEN :inicio AND :fin")
    List<TomaMuestraVet> findTomasRealizadasEnRango(
            @Param("inicio") LocalDateTime inicio, 
            @Param("fin") LocalDateTime fin);

    /**
     * Verificar si existe código de muestra
     */
    boolean existsByCodigoMuestra(String codigoMuestra);

    /**
     * Buscar tomas por método de obtención
     */
    List<TomaMuestraVet> findByMetodoObtencionContainingIgnoreCase(String metodo);

    /**
     * Buscar próximas tomas programadas (futuras)
     */
    @Query("SELECT tm FROM TomaMuestraVet tm WHERE tm.estado = 'PROGRAMADA' AND tm.fechaHora > :fechaActual ORDER BY tm.fechaHora ASC")
    List<TomaMuestraVet> findProximasTomas(@Param("fechaActual") LocalDateTime fechaActual);
}