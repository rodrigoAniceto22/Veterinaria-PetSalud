package com.petsalud.repository;

import com.petsalud.model.ResultadoVeterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la entidad ResultadoVeterinario
 * RF-05: Gestión de análisis
 * RF-06: Validación de resultados
 * RF-08: Entrega de resultados
 */
@Repository
public interface ResultadoRepository extends JpaRepository<ResultadoVeterinario, Long> {

    /**
     * Buscar resultados por orden
     */
    List<ResultadoVeterinario> findByOrden_IdOrden(Long idOrden);

    /**
     * Buscar resultados validados
     */
    List<ResultadoVeterinario> findByValidadoTrue();

    /**
     * Buscar resultados no validados (pendientes)
     */
    List<ResultadoVeterinario> findByValidadoFalse();

    /**
     * Buscar resultados entregados
     */
    List<ResultadoVeterinario> findByEntregadoTrue();

    /**
     * Buscar resultados no entregados
     */
    List<ResultadoVeterinario> findByEntregadoFalse();

    /**
     * Buscar resultados validados pero no entregados
     */
    List<ResultadoVeterinario> findByValidadoTrueAndEntregadoFalse();

    /**
     * Buscar resultados por rango de fechas
     */
    List<ResultadoVeterinario> findByFechaResultadoBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Contar resultados validados
     */
    long countByValidadoTrue();

    /**
     * Contar resultados pendientes
     */
    long countByValidadoFalse();

    /**
     * Contar resultados entregados
     */
    long countByEntregadoTrue();

    /**
     * Buscar resultados por fecha de validación
     */
    List<ResultadoVeterinario> findByFechaValidacionBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Buscar resultados del día
     */
    @Query("SELECT r FROM ResultadoVeterinario r WHERE DATE(r.fechaResultado) = CURRENT_DATE")
    List<ResultadoVeterinario> findResultadosDelDia();

    /**
     * Buscar resultados pendientes de validación más antiguos
     */
    @Query("SELECT r FROM ResultadoVeterinario r WHERE r.validado = false ORDER BY r.fechaResultado ASC")
    List<ResultadoVeterinario> findResultadosPendientesOrdenados();

    /**
     * Buscar resultados por veterinario (a través de la orden)
     */
    @Query("SELECT r FROM ResultadoVeterinario r WHERE r.orden.veterinario.idVeterinario = :idVeterinario")
    List<ResultadoVeterinario> findByVeterinario(@Param("idVeterinario") Long idVeterinario);

    /**
     * Buscar resultados por mascota (a través de la orden)
     */
    @Query("SELECT r FROM ResultadoVeterinario r WHERE r.orden.mascota.idMascota = :idMascota")
    List<ResultadoVeterinario> findByMascota(@Param("idMascota") Long idMascota);

    /**
     * Buscar resultados urgentes sin validar
     */
    @Query("SELECT r FROM ResultadoVeterinario r WHERE r.validado = false AND r.orden.prioridad = 'URGENTE' ORDER BY r.fechaResultado ASC")
    List<ResultadoVeterinario> findResultadosUrgentes();

    /**
     * Calcular tiempo promedio entre resultado y validación
     */
    @Query("SELECT AVG(TIMESTAMPDIFF(HOUR, r.fechaResultado, r.fechaValidacion)) FROM ResultadoVeterinario r WHERE r.validado = true AND r.fechaValidacion IS NOT NULL")
    Double calcularTiempoPromedioValidacion();

    /**
     * Buscar resultados por método de análisis
     */
    List<ResultadoVeterinario> findByMetodoAnalisisContainingIgnoreCase(String metodo);

    /**
     * Buscar resultados con conclusiones
     */
    @Query("SELECT r FROM ResultadoVeterinario r WHERE r.conclusiones IS NOT NULL AND r.conclusiones != ''")
    List<ResultadoVeterinario> findResultadosConConclusiones();

    /**
     * Contar resultados por estado de validación y entrega
     */
    @Query("SELECT r.validado, r.entregado, COUNT(r) FROM ResultadoVeterinario r GROUP BY r.validado, r.entregado")
    List<Object[]> countResultadosPorEstado();

    /**
     * Buscar últimos resultados por mascota
     */
    @Query("SELECT r FROM ResultadoVeterinario r WHERE r.orden.mascota.idMascota = :idMascota ORDER BY r.fechaResultado DESC")
    List<ResultadoVeterinario> findUltimosResultadosByMascota(@Param("idMascota") Long idMascota);
}