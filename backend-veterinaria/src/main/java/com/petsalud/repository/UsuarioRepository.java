package com.petsalud.repository;

import com.petsalud.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la entidad Usuario
 * RF-09: Consulta de resultados en línea (autenticación)
 * Gestión de usuarios y autenticación
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Buscar usuario por nombre de usuario
     */
    Usuario findByNombreUsuario(String nombreUsuario);

    /**
     * Buscar usuario por email
     */
    Usuario findByEmail(String email);

    /**
     * Buscar usuarios por rol (case insensitive)
     */
    List<Usuario> findByRolIgnoreCase(String rol);

    /**
     * Buscar usuarios activos
     */
    List<Usuario> findByActivoTrue();

    /**
     * Buscar usuarios inactivos
     */
    List<Usuario> findByActivoFalse();

    /**
     * Buscar usuarios bloqueados
     */
    List<Usuario> findByBloqueadoTrue();

    /**
     * Buscar usuarios no bloqueados
     */
    List<Usuario> findByBloqueadoFalse();

    /**
     * Verificar si existe usuario por nombre
     */
    boolean existsByNombreUsuario(String nombreUsuario);

    /**
     * Verificar si existe usuario por email
     */
    boolean existsByEmail(String email);

    /**
     * Contar usuarios por rol
     */
    long countByRolIgnoreCase(String rol);

    /**
     * Contar usuarios activos
     */
    long countByActivoTrue();

    /**
     * Contar usuarios bloqueados
     */
    long countByBloqueadoTrue();

    /**
     * Buscar usuarios por rol y estado activo
     */
    List<Usuario> findByRolIgnoreCaseAndActivoTrue(String rol);

    /**
     * Buscar usuarios con intentos fallidos mayor a X
     */
    List<Usuario> findByIntentosFallidosGreaterThanEqual(Integer intentos);

    /**
     * Buscar usuarios por rango de fecha de creación
     */
    List<Usuario> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Buscar usuarios que no han accedido recientemente
     */
    @Query("SELECT u FROM Usuario u WHERE u.ultimoAcceso < :fecha OR u.ultimoAcceso IS NULL")
    List<Usuario> findUsuariosInactivos(@Param("fecha") LocalDateTime fecha);

    /**
     * Buscar usuarios activos por rol
     */
    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol AND u.activo = true AND u.bloqueado = false")
    List<Usuario> findUsuariosActivosPorRol(@Param("rol") String rol);

    /**
     * Contar usuarios por estado
     */
    @Query("SELECT u.activo, u.bloqueado, COUNT(u) FROM Usuario u GROUP BY u.activo, u.bloqueado")
    List<Object[]> countUsuariosPorEstado();

    /**
     * Buscar usuarios creados recientemente
     */
    @Query("SELECT u FROM Usuario u WHERE u.fechaCreacion >= :desde ORDER BY u.fechaCreacion DESC")
    List<Usuario> findUsuariosRecientes(@Param("desde") LocalDateTime desde);

    /**
     * Buscar usuarios por nombre o apellido
     */
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombres) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Usuario> findByNombresOrApellidos(@Param("texto") String texto);

    /**
     * Obtener estadísticas de usuarios por rol
     */
    @Query("SELECT u.rol, COUNT(u) FROM Usuario u GROUP BY u.rol")
    List<Object[]> getEstadisticasPorRol();
}