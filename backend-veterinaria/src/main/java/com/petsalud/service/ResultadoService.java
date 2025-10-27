package com.petsalud.service;

import com.petsalud.model.ResultadoVeterinario;
import com.petsalud.repository.ResultadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para gestión de Resultados Veterinarios
 * RF-05: Gestión de análisis
 * RF-06: Validación de resultados
 * RF-08: Entrega de resultados
 */
@Service
@Transactional
public class ResultadoService {

    @Autowired
    private ResultadoRepository resultadoRepository;

    @Autowired
    private NotificacionService notificacionService;

    /**
     * Listar todos los resultados
     */
    public List<ResultadoVeterinario> listarTodos() {
        return resultadoRepository.findAll();
    }

    /**
     * Obtener resultado por ID
     */
    public ResultadoVeterinario obtenerPorId(Long id) {
        return resultadoRepository.findById(id).orElse(null);
    }

    /**
     * Buscar resultados por orden
     */
    public List<ResultadoVeterinario> buscarPorOrden(Long idOrden) {
        return resultadoRepository.findByOrden_IdOrden(idOrden);
    }

    /**
     * Obtener resultados validados
     */
    public List<ResultadoVeterinario> obtenerResultadosValidados() {
        return resultadoRepository.findByValidadoTrue();
    }

    /**
     * Obtener resultados pendientes de validación
     */
    public List<ResultadoVeterinario> obtenerResultadosPendientes() {
        return resultadoRepository.findByValidadoFalse();
    }

    /**
     * Obtener resultados entregados
     */
    public List<ResultadoVeterinario> obtenerResultadosEntregados() {
        return resultadoRepository.findByEntregadoTrue();
    }

    /**
     * Obtener resultados pendientes de entrega
     */
    public List<ResultadoVeterinario> obtenerResultadosPendientesEntrega() {
        return resultadoRepository.findByValidadoTrueAndEntregadoFalse();
    }

    /**
     * Guardar o actualizar resultado
     */
    public ResultadoVeterinario guardar(ResultadoVeterinario resultado) {
        // Validaciones básicas
        if (resultado.getOrden() == null) {
            throw new RuntimeException("El resultado debe estar asociado a una orden");
        }
        
        // Si es un nuevo resultado
        if (resultado.getIdResultado() == null) {
            if (resultado.getFechaResultado() == null) {
                resultado.setFechaResultado(LocalDateTime.now());
            }
            resultado.setValidado(false);
            resultado.setEntregado(false);
        }
        
        ResultadoVeterinario resultadoGuardado = resultadoRepository.save(resultado);
        
        // Notificar nuevo resultado registrado
        if (resultado.getIdResultado() == null) {
            notificacionService.notificarNuevoResultado(resultadoGuardado);
        }
        
        return resultadoGuardado;
    }

    /**
     * Eliminar resultado
     */
    public void eliminar(Long id) {
        ResultadoVeterinario resultado = obtenerPorId(id);
        if (resultado != null) {
            if (resultado.getValidado()) {
                throw new RuntimeException("No se puede eliminar un resultado ya validado");
            }
            resultadoRepository.deleteById(id);
        }
    }

    /**
     * Validar resultado (Veterinario aprueba)
     * RF-06: Validación de resultados
     */
    public ResultadoVeterinario validarResultado(Long id) {
        ResultadoVeterinario resultado = obtenerPorId(id);
        if (resultado != null) {
            if (resultado.getValidado()) {
                throw new RuntimeException("El resultado ya ha sido validado");
            }
            
            resultado.setValidado(true);
            resultado.setFechaValidacion(LocalDateTime.now());
            
            // Actualizar estado de la orden a COMPLETADA
            if (resultado.getOrden() != null) {
                resultado.getOrden().setEstado("COMPLETADA");
            }
            
            ResultadoVeterinario resultadoValidado = resultadoRepository.save(resultado);
            
            // Notificar validación de resultado
            notificacionService.notificarResultadoValidado(resultadoValidado);
            
            return resultadoValidado;
        }
        return null;
    }

    /**
     * Marcar resultado como entregado
     * RF-08: Entrega de resultados
     */
    public ResultadoVeterinario marcarComoEntregado(Long id) {
        ResultadoVeterinario resultado = obtenerPorId(id);
        if (resultado != null) {
            if (!resultado.getValidado()) {
                throw new RuntimeException("No se puede entregar un resultado sin validar");
            }
            if (resultado.getEntregado()) {
                throw new RuntimeException("El resultado ya ha sido entregado");
            }
            
            resultado.setEntregado(true);
            resultado.setFechaEntrega(LocalDateTime.now());
            
            ResultadoVeterinario resultadoEntregado = resultadoRepository.save(resultado);
            
            // Notificar entrega de resultado
            notificacionService.notificarResultadoEntregado(resultadoEntregado);
            
            return resultadoEntregado;
        }
        return null;
    }

    /**
     * Buscar resultados por rango de fechas
     */
    public List<ResultadoVeterinario> buscarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return resultadoRepository.findByFechaResultadoBetween(inicio, fin);
    }

    /**
     * Contar resultados validados
     */
    public long contarValidados() {
        return resultadoRepository.countByValidadoTrue();
    }

    /**
     * Contar resultados pendientes
     */
    public long contarPendientes() {
        return resultadoRepository.countByValidadoFalse();
    }

    /**
     * Contar resultados entregados
     */
    public long contarEntregados() {
        return resultadoRepository.countByEntregadoTrue();
    }

    /**
     * Obtener resultados del día
     */
    public List<ResultadoVeterinario> obtenerResultadosDelDia() {
        LocalDateTime inicioDia = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime finDia = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);
        return resultadoRepository.findByFechaResultadoBetween(inicioDia, finDia);
    }

    /**
     * Verificar si una orden tiene resultados
     */
    public boolean ordenTieneResultados(Long idOrden) {
        return !resultadoRepository.findByOrden_IdOrden(idOrden).isEmpty();
    }

    /**
     * Obtener último resultado de una orden
     */
    public ResultadoVeterinario obtenerUltimoResultadoDeOrden(Long idOrden) {
        List<ResultadoVeterinario> resultados = resultadoRepository.findByOrden_IdOrden(idOrden);
        if (!resultados.isEmpty()) {
            return resultados.stream()
                    .max((r1, r2) -> r1.getFechaResultado().compareTo(r2.getFechaResultado()))
                    .orElse(null);
        }
        return null;
    }
}