package com.petsalud.repository;

import com.petsalud.model.TecnicoVeterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad TecnicoVeterinario
 * RF-04: Registro de toma de muestra
 * RF-05: Gestión de análisis
 */
@Repository
public interface TecnicoVeterinarioRepository extends JpaRepository<TecnicoVeterinario, Long> {

    /**
     * Buscar técnicos por nombre o apellido (case insensitive)
     */
    List<TecnicoVeterinario> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(
            String nombres, String apellidos);

    /**
     * Buscar técnicos por especialidad (case insensitive, contiene)
     */
    List<TecnicoVeterinario> findByEspecialidadContainingIgnoreCase(String especialidad);

    /**
     * Buscar técnico por email
     */
    TecnicoVeterinario findByEmail(String email);

    /**
     * Buscar técnicos por certificación (case insensitive, contiene)
     */
    List<TecnicoVeterinario> findByCertificacionContainingIgnoreCase(String certificacion);

    /**
     * Buscar técnicos activos
     */
    List<TecnicoVeterinario> findByActivoTrue();

    /**
     * Buscar técnicos inactivos
     */
    List<TecnicoVeterinario> findByActivoFalse();

    /**
     * Contar técnicos activos
     */
    long countByActivoTrue();

    /**
     * Buscar técnicos por especialidad y activos
     */
    List<TecnicoVeterinario> findByEspecialidadContainingIgnoreCaseAndActivoTrue(String especialidad);

    /**
     * Obtener técnicos con más tomas de muestra
     */
    @Query("SELECT t FROM TecnicoVeterinario t LEFT JOIN t.tomasMuestra tm GROUP BY t ORDER BY COUNT(tm) DESC")
    List<TecnicoVeterinario> findTecnicosMasProductivos();

    /**
     * Contar tomas de muestra por técnico
     */
    @Query("SELECT COUNT(tm) FROM TomaMuestraVet tm WHERE tm.tecnico.idTecnico = :idTecnico")
    long countTomasByTecnico(Long idTecnico);

    /**
     * Buscar técnicos disponibles (activos y con menos carga)
     */
    @Query("SELECT t FROM TecnicoVeterinario t WHERE t.activo = true ORDER BY (SELECT COUNT(tm) FROM TomaMuestraVet tm WHERE tm.tecnico = t AND tm.estado IN ('PROGRAMADA', 'REALIZADA')) ASC")
    List<TecnicoVeterinario> findTecnicosDisponibles();

    /**
     * Buscar técnicos por número de tomas pendientes
     */
    @Query("SELECT t FROM TecnicoVeterinario t WHERE t.activo = true AND (SELECT COUNT(tm) FROM TomaMuestraVet tm WHERE tm.tecnico = t AND tm.estado = 'PROGRAMADA') < :maxTomas")
    List<TecnicoVeterinario> findTecnicosConCapacidad(int maxTomas);
}