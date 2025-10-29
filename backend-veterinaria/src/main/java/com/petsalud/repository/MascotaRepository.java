package com.petsalud.repository;

import com.petsalud.model.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Mascota
 * RF-01: Registro de dueños y mascotas
 */
@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    /**
     * Buscar mascotas por nombre (case insensitive, contiene)
     */
    List<Mascota> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Buscar mascotas por especie (case insensitive)
     */
    List<Mascota> findByEspecieIgnoreCase(String especie);

    /**
     * Buscar mascotas por raza (case insensitive, contiene)
     */
    List<Mascota> findByRazaContainingIgnoreCase(String raza);

    /**
     * Buscar mascotas por sexo
     */
    List<Mascota> findBySexoIgnoreCase(String sexo);

    /**
     * Buscar mascotas por dueño
     */
    List<Mascota> findByDueno_IdDueno(Long idDueno);

    /**
     * Buscar mascotas por edad mayor o igual
     */
    List<Mascota> findByEdadGreaterThanEqual(Integer edad);

    /**
     * Buscar mascotas por edad menor o igual
     */
    List<Mascota> findByEdadLessThanEqual(Integer edad);

    /**
     * Buscar mascotas por rango de edad
     */
    List<Mascota> findByEdadBetween(Integer edadMin, Integer edadMax);

    /**
     * Contar mascotas por especie
     */
    long countByEspecieIgnoreCase(String especie);

    /**
     * Contar mascotas por dueño
     */
    long countByDueno_IdDueno(Long idDueno);

    /**
     * Buscar mascotas con órdenes veterinarias
     */
    @Query("SELECT DISTINCT m FROM Mascota m LEFT JOIN FETCH m.ordenes WHERE SIZE(m.ordenes) > 0")
    List<Mascota> findMascotasConOrdenes();

    /**
     * Buscar mascotas por especie y raza
     */
    @Query("SELECT m FROM Mascota m WHERE LOWER(m.especie) = LOWER(:especie) AND LOWER(m.raza) LIKE LOWER(CONCAT('%', :raza, '%'))")
    List<Mascota> findByEspecieAndRaza(@Param("especie") String especie, @Param("raza") String raza);

    /**
     * Obtener mascotas más atendidas (con más órdenes)
     */
    @Query("SELECT m FROM Mascota m LEFT JOIN m.ordenes o GROUP BY m ORDER BY COUNT(o) DESC")
    List<Mascota> findMascotasMasAtendidas();

    /**
     * Buscar mascotas por peso mayor a
     */
    List<Mascota> findByPesoGreaterThan(Double peso);

    /**
     * Buscar mascotas por peso menor a
     */
    List<Mascota> findByPesoLessThan(Double peso);
}