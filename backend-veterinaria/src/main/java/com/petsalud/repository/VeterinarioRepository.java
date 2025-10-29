package com.petsalud.repository;

import com.petsalud.model.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Veterinario
 * Personal médico veterinario
 */
@Repository
public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {

    /**
     * Buscar veterinarios por nombre o apellido (case insensitive)
     */
    List<Veterinario> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(
            String nombres, String apellidos);

    /**
     * Buscar veterinarios por especialidad (case insensitive, contiene)
     */
    List<Veterinario> findByEspecialidadContainingIgnoreCase(String especialidad);

    /**
     * Buscar veterinario por colegiatura
     */
    Veterinario findByColegiatura(String colegiatura);

    /**
     * Buscar veterinario por email
     */
    Veterinario findByEmail(String email);

    /**
     * Buscar veterinarios activos
     */
    List<Veterinario> findByActivoTrue();

    /**
     * Buscar veterinarios inactivos
     */
    List<Veterinario> findByActivoFalse();

    /**
     * Contar veterinarios activos
     */
    long countByActivoTrue();

    /**
     * Verificar si existe por colegiatura
     */
    boolean existsByColegiatura(String colegiatura);

    /**
     * Buscar veterinarios por especialidad y activos
     */
    List<Veterinario> findByEspecialidadContainingIgnoreCaseAndActivoTrue(String especialidad);

    /**
     * Obtener veterinarios con más órdenes
     */
    @Query("SELECT v FROM Veterinario v LEFT JOIN v.ordenes o GROUP BY v ORDER BY COUNT(o) DESC")
    List<Veterinario> findVeterinariosMasProductivos();

    /**
     * Contar órdenes por veterinario
     */
    @Query("SELECT COUNT(o) FROM OrdenVeterinaria o WHERE o.veterinario.idVeterinario = :idVeterinario")
    long countOrdenesByVeterinario(Long idVeterinario);

    /**
     * Buscar veterinarios disponibles (activos y con menos carga)
     */
    @Query("SELECT v FROM Veterinario v WHERE v.activo = true ORDER BY (SELECT COUNT(o) FROM OrdenVeterinaria o WHERE o.veterinario = v AND o.estado IN ('PENDIENTE', 'EN_PROCESO')) ASC")
    List<Veterinario> findVeterinariosDisponibles();
}