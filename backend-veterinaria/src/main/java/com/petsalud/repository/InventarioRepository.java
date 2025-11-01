package com.petsalud.repository;

import com.petsalud.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para Inventario
 * Control de stock y precios
 */
@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    /**
     * Buscar por código
     */
    Inventario findByCodigo(String codigo);

    /**
     * Buscar por nombre (contiene)
     */
    List<Inventario> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Buscar por categoría
     */
    List<Inventario> findByCategoriaIgnoreCase(String categoria);

    /**
     * Buscar productos activos
     */
    List<Inventario> findByActivoTrue();

    /**
     * Buscar productos con stock bajo (menor o igual al mínimo)
     */
    @Query("SELECT i FROM Inventario i WHERE i.stockActual <= i.stockMinimo AND i.activo = true")
    List<Inventario> findProductosConStockBajo();

    /**
     * Buscar productos vencidos
     */
    @Query("SELECT i FROM Inventario i WHERE i.fechaVencimiento < :fecha AND i.activo = true")
    List<Inventario> findProductosVencidos(@Param("fecha") LocalDate fecha);

    /**
     * Buscar productos próximos a vencer
     */
    @Query("SELECT i FROM Inventario i WHERE i.fechaVencimiento BETWEEN :fechaInicio AND :fechaFin AND i.activo = true")
    List<Inventario> findProductosProximosAVencer(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);

    /**
     * Buscar por proveedor
     */
    List<Inventario> findByProveedorContainingIgnoreCase(String proveedor);

    /**
     * Contar productos activos
     */
    long countByActivoTrue();

    /**
     * Verificar si existe código
     */
    boolean existsByCodigo(String codigo);

    /**
     * Calcular valor total del inventario
     */
    @Query("SELECT SUM(i.precioCompra * i.stockActual) FROM Inventario i WHERE i.activo = true")
    Double calcularValorTotalInventario();
}