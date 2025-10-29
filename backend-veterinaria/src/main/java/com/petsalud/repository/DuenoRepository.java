package com.petsalud.repository;

import com.petsalud.model.Dueno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Dueno
 * RF-01: Registro de dueños y mascotas
 */
@Repository
public interface DuenoRepository extends JpaRepository<Dueno, Long> {

    /**
     * Buscar dueño por DNI
     */
    Dueno findByDni(String dni);

    /**
     * Buscar dueño por email
     */
    Dueno findByEmail(String email);

    /**
     * Buscar dueños por nombre o apellido (case insensitive)
     */
    List<Dueno> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(
            String nombres, String apellidos);

    /**
     * Buscar dueños por teléfono (contiene)
     */
    List<Dueno> findByTelefonoContaining(String telefono);

    /**
     * Verificar si existe un dueño por DNI
     */
    boolean existsByDni(String dni);

    /**
     * Verificar si existe un dueño por email
     */
    boolean existsByEmail(String email);

    /**
     * Buscar dueños con mascotas
     */
    @Query("SELECT DISTINCT d FROM Dueno d LEFT JOIN FETCH d.mascotas WHERE SIZE(d.mascotas) > 0")
    List<Dueno> findDuenosConMascotas();

    /**
     * Buscar dueños con facturas pendientes
     */
    @Query("SELECT DISTINCT d FROM Dueno d JOIN d.facturas f WHERE f.estado = 'PENDIENTE'")
    List<Dueno> findDuenosConFacturasPendientes();

    /**
     * Contar total de dueños
     */
    @Query("SELECT COUNT(d) FROM Dueno d")
    long countTotalDuenos();

    /**
     * Buscar dueños por ciudad (desde dirección)
     */
    @Query("SELECT d FROM Dueno d WHERE d.direccion LIKE %:ciudad%")
    List<Dueno> findByCiudad(@Param("ciudad") String ciudad);
}