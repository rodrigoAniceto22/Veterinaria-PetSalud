package com.petsalud.service;

import com.petsalud.model.OrdenVeterinaria;
import com.petsalud.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para gestión de Órdenes Veterinarias
 * RF-02: Registro de órdenes veterinarias
 */
@Service
@Transactional
public class OrdenService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private NotificacionService notificacionService;

    /**
     * Listar todas las órdenes
     */
    public List<OrdenVeterinaria> listarTodas() {
        return ordenRepository.findAll();
    }

    /**
     * Obtener orden por ID
     */
    public OrdenVeterinaria obtenerPorId(Long id) {
        return ordenRepository.findById(id).orElse(null);
    }

    /**
     * Buscar órdenes por mascota
     */
    public List<OrdenVeterinaria> buscarPorMascota(Long idMascota) {
        return ordenRepository.findByMascota_IdMascota(idMascota);
    }

    /**
     * Buscar órdenes por veterinario
     */
    public List<OrdenVeterinaria> buscarPorVeterinario(Long idVeterinario) {
        return ordenRepository.findByVeterinario_IdVeterinario(idVeterinario);
    }

    /**
     * Buscar órdenes por fecha
     */
    public List<OrdenVeterinaria> buscarPorFecha(LocalDate fecha) {
        return ordenRepository.findByFechaOrden(fecha);
    }

    /**
     * Buscar órdenes por tipo de examen
     */
    public List<OrdenVeterinaria> buscarPorTipoExamen(String tipoExamen) {
        return ordenRepository.findByTipoExamenContainingIgnoreCase(tipoExamen);
    }

    /**
     * Buscar órdenes por estado
     */
    public List<OrdenVeterinaria> buscarPorEstado(String estado) {
        return ordenRepository.findByEstadoIgnoreCase(estado);
    }

    /**
     * Obtener órdenes pendientes
     */
    public List<OrdenVeterinaria> obtenerOrdenesPendientes() {
        return ordenRepository.findByEstadoIgnoreCase("PENDIENTE");
    }

    /**
     * Obtener órdenes en proceso
     */
    public List<OrdenVeterinaria> obtenerOrdenesEnProceso() {
        return ordenRepository.findByEstadoIgnoreCase("EN_PROCESO");
    }

    /**
     * Obtener órdenes completadas
     */
    public List<OrdenVeterinaria> obtenerOrdenesCompletadas() {
        return ordenRepository.findByEstadoIgnoreCase("COMPLETADA");
    }

    /**
     * Guardar o actualizar orden
     */
    public OrdenVeterinaria guardar(OrdenVeterinaria orden) {
        // Validaciones básicas
        if (orden.getTipoExamen() == null || orden.getTipoExamen().trim().isEmpty()) {
            throw new RuntimeException("El tipo de examen es obligatorio");
        }
        if (orden.getMascota() == null) {
            throw new RuntimeException("La orden debe estar asociada a una mascota");
        }
        if (orden.getVeterinario() == null) {
            throw new RuntimeException("La orden debe estar asociada a un veterinario");
        }
        
        // Si es una nueva orden, establecer valores por defecto
        if (orden.getIdOrden() == null) {
            if (orden.getFechaOrden() == null) {
                orden.setFechaOrden(LocalDate.now());
            }
            if (orden.getEstado() == null) {
                orden.setEstado("PENDIENTE");
            }
            if (orden.getPrioridad() == null) {
                orden.setPrioridad("NORMAL");
            }
        }
        
        OrdenVeterinaria ordenGuardada = ordenRepository.save(orden);
        
        // Notificar creación de orden
        if (orden.getIdOrden() == null) {
            notificacionService.notificarNuevaOrden(ordenGuardada);
        }
        
        return ordenGuardada;
    }

    /**
     * Eliminar orden
     */
    public void eliminar(Long id) {
        OrdenVeterinaria orden = obtenerPorId(id);
        if (orden != null) {
            // Verificar que no tenga resultados asociados
            if (!orden.getResultados().isEmpty()) {
                throw new RuntimeException("No se puede eliminar la orden porque tiene resultados asociados");
            }
            ordenRepository.deleteById(id);
        }
    }

    /**
     * Cambiar estado de orden
     */
    public OrdenVeterinaria cambiarEstado(Long id, String nuevoEstado) {
        OrdenVeterinaria orden = obtenerPorId(id);
        if (orden != null) {
            String estadoAnterior = orden.getEstado();
            orden.setEstado(nuevoEstado.toUpperCase());
            OrdenVeterinaria ordenActualizada = ordenRepository.save(orden);
            
            // Notificar cambio de estado
            notificacionService.notificarCambioEstadoOrden(ordenActualizada, estadoAnterior);
            
            return ordenActualizada;
        }
        return null;
    }

    /**
     * Contar órdenes por estado
     */
    public long contarPorEstado(String estado) {
        return ordenRepository.countByEstadoIgnoreCase(estado);
    }

    /**
     * Obtener órdenes por rango de fechas
     */
    public List<OrdenVeterinaria> buscarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return ordenRepository.findByFechaOrdenBetween(fechaInicio, fechaFin);
    }

    /**
     * Obtener órdenes urgentes
     */
    public List<OrdenVeterinaria> obtenerOrdenesUrgentes() {
        return ordenRepository.findByPrioridadIgnoreCase("URGENTE");
    }

    /**
     * Contar total de órdenes
     */
    public long contarTotal() {
        return ordenRepository.count();
    }

    /**
     * Obtener órdenes del día
     */
    public List<OrdenVeterinaria> obtenerOrdenesDelDia() {
        return ordenRepository.findByFechaOrden(LocalDate.now());
    }
}