package com.petsalud.service;

import com.petsalud.model.TomaMuestraVet;
import com.petsalud.repository.TomaMuestraVetRepository;
import com.petsalud.util.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Servicio para gestión de Toma de Muestras
 * RF-03: Programación de toma de muestra
 * RF-04: Registro de toma de muestra
 */
@Service
@Transactional
public class TomaMuestraService {

    @Autowired
    private TomaMuestraVetRepository tomaMuestraRepository;

    @Autowired
    private NotificacionService notificacionService;

    /**
     * Listar todas las tomas de muestra
     */
    public List<TomaMuestraVet> listarTodas() {
        return tomaMuestraRepository.findAll();
    }

    /**
     * Obtener toma de muestra por ID
     */
    public TomaMuestraVet obtenerPorId(Long id) {
        return tomaMuestraRepository.findById(id).orElse(null);
    }

    /**
     * Buscar toma de muestra por orden
     */
    public TomaMuestraVet buscarPorOrden(Long idOrden) {
        return tomaMuestraRepository.findByOrden_IdOrden(idOrden);
    }

    /**
     * Buscar tomas de muestra por técnico
     */
    public List<TomaMuestraVet> buscarPorTecnico(Long idTecnico) {
        return tomaMuestraRepository.findByTecnico_IdTecnico(idTecnico);
    }

    /**
     * Buscar tomas de muestra por fecha
     */
    public List<TomaMuestraVet> buscarPorFecha(LocalDateTime fecha) {
        LocalDateTime inicioDia = fecha.toLocalDate().atStartOfDay();
        LocalDateTime finDia = fecha.toLocalDate().atTime(23, 59, 59);
        return tomaMuestraRepository.findByFechaHoraBetween(inicioDia, finDia);
    }

    /**
     * Buscar tomas de muestra por tipo
     */
    public List<TomaMuestraVet> buscarPorTipoMuestra(String tipoMuestra) {
        return tomaMuestraRepository.findByTipoMuestraContainingIgnoreCase(tipoMuestra);
    }

    /**
     * Buscar por código de muestra
     */
    public TomaMuestraVet buscarPorCodigoMuestra(String codigoMuestra) {
        return tomaMuestraRepository.findByCodigoMuestra(codigoMuestra);
    }

    /**
     * Obtener tomas de muestra por estado
     */
    public List<TomaMuestraVet> buscarPorEstado(String estado) {
        return tomaMuestraRepository.findByEstadoIgnoreCase(estado);
    }

    /**
     * Obtener tomas pendientes
     */
    public List<TomaMuestraVet> obtenerTomasPendientes() {
        return tomaMuestraRepository.findByEstadoIgnoreCase("PROGRAMADA");
    }

    /**
     * Guardar o actualizar toma de muestra
     */
    public TomaMuestraVet guardar(TomaMuestraVet tomaMuestra) {
        // Validaciones básicas
        if (tomaMuestra.getTipoMuestra() == null || tomaMuestra.getTipoMuestra().trim().isEmpty()) {
            throw new RuntimeException("El tipo de muestra es obligatorio");
        }
        if (tomaMuestra.getOrden() == null) {
            throw new RuntimeException("La toma de muestra debe estar asociada a una orden");
        }
        if (tomaMuestra.getTecnico() == null) {
            throw new RuntimeException("La toma de muestra debe tener un técnico asignado");
        }
        
        // Si es una nueva toma, generar código único
        if (tomaMuestra.getIdToma() == null) {
            if (tomaMuestra.getCodigoMuestra() == null) {
                tomaMuestra.setCodigoMuestra(generarCodigoMuestra());
            }
            if (tomaMuestra.getEstado() == null) {
                tomaMuestra.setEstado("PROGRAMADA");
            }
        }
        
        TomaMuestraVet tomaGuardada = tomaMuestraRepository.save(tomaMuestra);
        
        // Actualizar estado de la orden a EN_PROCESO
        if (tomaMuestra.getOrden() != null) {
            tomaMuestra.getOrden().setEstado("EN_PROCESO");
        }
        
        // Notificar programación de toma
        notificacionService.notificarTomaMuestraProgramada(tomaGuardada);
        
        return tomaGuardada;
    }

    /**
     * Eliminar toma de muestra
     */
    public void eliminar(Long id) {
        tomaMuestraRepository.deleteById(id);
    }

    /**
     * Cambiar estado de toma de muestra
     */
    public TomaMuestraVet cambiarEstado(Long id, String nuevoEstado) {
        TomaMuestraVet toma = obtenerPorId(id);
        if (toma != null) {
            toma.setEstado(nuevoEstado.toUpperCase());
            return tomaMuestraRepository.save(toma);
        }
        return null;
    }

    /**
     * Registrar toma realizada
     */
    public TomaMuestraVet registrarTomaRealizada(Long id) {
        TomaMuestraVet toma = obtenerPorId(id);
        if (toma != null) {
            toma.setEstado("REALIZADA");
            toma.setFechaHora(LocalDateTime.now());
            TomaMuestraVet tomaActualizada = tomaMuestraRepository.save(toma);
            
            // Notificar que la toma fue realizada
            notificacionService.notificarTomaMuestraRealizada(tomaActualizada);
            
            return tomaActualizada;
        }
        return null;
    }

    /**
     * Generar código único de muestra
     */
    private String generarCodigoMuestra() {
        String prefijo = "TM";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return prefijo + "-" + timestamp.substring(timestamp.length() - 6) + "-" + uuid;
    }

    /**
     * Obtener tomas de muestra del día
     */
    public List<TomaMuestraVet> obtenerTomasDelDia() {
        LocalDateTime inicioDia = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime finDia = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);
        return tomaMuestraRepository.findByFechaHoraBetween(inicioDia, finDia);
    }

    /**
     * Contar tomas por estado
     */
    public long contarPorEstado(String estado) {
        return tomaMuestraRepository.countByEstadoIgnoreCase(estado);
    }

    /**
     * Obtener tomas de muestra por rango de fechas
     */
    public List<TomaMuestraVet> buscarPorRangoFechas(LocalDateTime inicio, LocalDateTime fin) {
        return tomaMuestraRepository.findByFechaHoraBetween(inicio, fin);
    }
}