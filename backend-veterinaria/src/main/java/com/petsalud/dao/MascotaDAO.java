package com.petsalud.dao;

import com.petsalud.model.Mascota;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * DAO Interface for Mascota entity
 * Implements DAO pattern with Stored Procedures
 */
public interface MascotaDAO {
    
    /**
     * SP5: Registrar nueva mascota con cálculo automático de edad
     */
    Mascota registrarMascota(Mascota mascota, LocalDate fechaNacimiento);
    
    /**
     * SP6: Buscar mascotas por dueño con detalles
     */
    List<Map<String, Object>> buscarMascotasPorDueno(Long idDueno);
    
    /**
     * SP7: Actualizar mascota con cálculo de edad
     */
    Mascota actualizarMascota(Mascota mascota, LocalDate fechaNacimiento);
    
    /**
     * SP8: Obtener estadísticas de mascotas
     */
    Map<String, Object> obtenerEstadisticasMascotas();
    
    /**
     * Listar todas las mascotas
     */
    List<Mascota> listarTodas();
    
    /**
     * Buscar mascota por ID
     */
    Mascota buscarPorId(Long id);
    
    /**
     * Eliminar mascota
     */
    void eliminar(Long id);
}