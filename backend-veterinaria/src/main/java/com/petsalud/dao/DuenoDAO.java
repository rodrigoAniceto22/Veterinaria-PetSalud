package com.petsalud.dao;

import com.petsalud.model.Dueno;
import java.util.List;
import java.util.Map;

/**
 * DAO Interface for Dueno entity
 * Implements DAO pattern with Stored Procedures
 */
public interface DuenoDAO {
    
    /**
     * SP1: Registrar nuevo dueño con validaciones
     */
    Dueno registrarDueno(Dueno dueno);
    
    /**
     * SP2: Buscar dueños con sus mascotas (JOIN)
     */
    Map<String, Object> buscarDuenoConMascotas(Long idDueno);
    
    /**
     * SP3: Actualizar información del dueño
     */
    Dueno actualizarDueno(Dueno dueno);
    
    /**
     * SP4: Obtener estadísticas de dueños
     */
    Map<String, Object> obtenerEstadisticasDuenos();
    
    /**
     * Listar todos los dueños
     */
    List<Dueno> listarTodos();
    
    /**
     * Buscar dueño por ID
     */
    Dueno buscarPorId(Long id);
    
    /**
     * Eliminar dueño
     */
    void eliminar(Long id);
}