package com.petsalud.service;

import com.petsalud.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

/**
 * Servicio para gestión de Notificaciones
 * Sistema de notificaciones por email/SMS (simulado)
 */
@Service
public class NotificacionService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Notificar nueva orden creada
     */
    public void notificarNuevaOrden(OrdenVeterinaria orden) {
        String mensaje = String.format(
            "Nueva orden veterinaria #%d creada para %s (%s). Tipo: %s. Prioridad: %s",
            orden.getIdOrden(),
            orden.getMascota().getNombre(),
            orden.getMascota().getEspecie(),
            orden.getTipoExamen(),
            orden.getPrioridad()
        );
        
        logger.info("NOTIFICACIÓN - {}", mensaje);
        
        // Notificar al dueño
        enviarNotificacionDueno(orden.getMascota().getDueno(), 
            "Nueva Orden Veterinaria", mensaje);
    }

    /**
     * Notificar cambio de estado de orden
     */
    public void notificarCambioEstadoOrden(OrdenVeterinaria orden, String estadoAnterior) {
        String mensaje = String.format(
            "La orden #%d de %s cambió de estado: %s → %s",
            orden.getIdOrden(),
            orden.getMascota().getNombre(),
            estadoAnterior,
            orden.getEstado()
        );
        
        logger.info("NOTIFICACIÓN - {}", mensaje);
        
        // Notificar al dueño
        enviarNotificacionDueno(orden.getMascota().getDueno(), 
            "Actualización de Orden", mensaje);
    }

    /**
     * Notificar toma de muestra programada
     */
    public void notificarTomaMuestraProgramada(TomaMuestraVet tomaMuestra) {
        String mensaje = String.format(
            "Toma de muestra programada para %s. Fecha: %s. Tipo: %s. Código: %s",
            tomaMuestra.getOrden().getMascota().getNombre(),
            tomaMuestra.getFechaHora().format(DATE_FORMATTER),
            tomaMuestra.getTipoMuestra(),
            tomaMuestra.getCodigoMuestra()
        );
        
        logger.info("NOTIFICACIÓN - {}", mensaje);
        
        // Notificar al dueño
        enviarNotificacionDueno(tomaMuestra.getOrden().getMascota().getDueno(), 
            "Toma de Muestra Programada", mensaje);
        
        // Notificar al técnico
        enviarNotificacionTecnico(tomaMuestra.getTecnico(), 
            "Nueva Toma de Muestra Asignada", mensaje);
    }

    /**
     * Notificar toma de muestra realizada
     */
    public void notificarTomaMuestraRealizada(TomaMuestraVet tomaMuestra) {
        String mensaje = String.format(
            "La toma de muestra de %s fue realizada exitosamente. Código: %s",
            tomaMuestra.getOrden().getMascota().getNombre(),
            tomaMuestra.getCodigoMuestra()
        );
        
        logger.info("NOTIFICACIÓN - {}", mensaje);
        
        // Notificar al veterinario
        enviarNotificacionVeterinario(tomaMuestra.getOrden().getVeterinario(), 
            "Toma de Muestra Completada", mensaje);
    }

    /**
     * Notificar nuevo resultado registrado
     */
    public void notificarNuevoResultado(ResultadoVeterinario resultado) {
        String mensaje = String.format(
            "Nuevo resultado registrado para orden #%d de %s. Pendiente de validación.",
            resultado.getOrden().getIdOrden(),
            resultado.getOrden().getMascota().getNombre()
        );
        
        logger.info("NOTIFICACIÓN - {}", mensaje);
        
        // Notificar al veterinario
        enviarNotificacionVeterinario(resultado.getOrden().getVeterinario(), 
            "Nuevo Resultado Pendiente", mensaje);
    }

    /**
     * Notificar resultado validado
     */
    public void notificarResultadoValidado(ResultadoVeterinario resultado) {
        String mensaje = String.format(
            "El resultado de la orden #%d de %s ha sido validado y está listo para entrega.",
            resultado.getOrden().getIdOrden(),
            resultado.getOrden().getMascota().getNombre()
        );
        
        logger.info("NOTIFICACIÓN - {}", mensaje);
        
        // Notificar al dueño
        enviarNotificacionDueno(resultado.getOrden().getMascota().getDueno(), 
            "Resultados Disponibles", mensaje);
    }

    /**
     * Notificar resultado entregado
     */
    public void notificarResultadoEntregado(ResultadoVeterinario resultado) {
        String mensaje = String.format(
            "Los resultados de %s han sido entregados al propietario.",
            resultado.getOrden().getMascota().getNombre()
        );
        
        logger.info("NOTIFICACIÓN - {}", mensaje);
    }

    /**
     * Enviar notificación al dueño (simulado)
     */
    private void enviarNotificacionDueno(Dueno dueno, String asunto, String mensaje) {
        if (dueno.getEmail() != null && !dueno.getEmail().isEmpty()) {
            logger.info("EMAIL a {}: {} - {}", dueno.getEmail(), asunto, mensaje);
        }
        
        if (dueno.getTelefono() != null && !dueno.getTelefono().isEmpty()) {
            logger.info("SMS a {}: {}", dueno.getTelefono(), mensaje);
        }
    }

    /**
     * Enviar notificación al veterinario (simulado)
     */
    private void enviarNotificacionVeterinario(Veterinario veterinario, String asunto, String mensaje) {
        if (veterinario.getEmail() != null && !veterinario.getEmail().isEmpty()) {
            logger.info("EMAIL a {} (Veterinario): {} - {}", 
                veterinario.getEmail(), asunto, mensaje);
        }
    }

    /**
     * Enviar notificación al técnico (simulado)
     */
    private void enviarNotificacionTecnico(TecnicoVeterinario tecnico, String asunto, String mensaje) {
        if (tecnico.getEmail() != null && !tecnico.getEmail().isEmpty()) {
            logger.info("EMAIL a {} (Técnico): {} - {}", 
                tecnico.getEmail(), asunto, mensaje);
        }
    }

    /**
     * Notificar resultado con urgencia
     */
    public void notificarResultadoUrgente(ResultadoVeterinario resultado) {
        String mensaje = String.format(
            "⚠️ URGENTE: Resultado crítico detectado en orden #%d de %s. Revisar inmediatamente.",
            resultado.getOrden().getIdOrden(),
            resultado.getOrden().getMascota().getNombre()
        );
        
        logger.warn("NOTIFICACIÓN URGENTE - {}", mensaje);
        
        // Notificar al veterinario
        enviarNotificacionVeterinario(resultado.getOrden().getVeterinario(), 
            "⚠️ RESULTADO URGENTE", mensaje);
        
        // Notificar al dueño
        enviarNotificacionDueno(resultado.getOrden().getMascota().getDueno(), 
            "⚠️ Resultado Importante", 
            "Se ha detectado un resultado importante en los análisis de " + 
            resultado.getOrden().getMascota().getNombre() + 
            ". Por favor contacte a su veterinario.");
    }

    /**
     * Recordatorio de orden pendiente
     */
    public void enviarRecordatorioOrdenPendiente(OrdenVeterinaria orden) {
        String mensaje = String.format(
            "Recordatorio: La orden #%d de %s está pendiente desde %s",
            orden.getIdOrden(),
            orden.getMascota().getNombre(),
            orden.getFechaOrden().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );
        
        logger.info("RECORDATORIO - {}", mensaje);
        
        enviarNotificacionVeterinario(orden.getVeterinario(), 
            "Recordatorio de Orden Pendiente", mensaje);
    }

    /**
     * Notificación de factura generada
     */
    public void notificarFacturaGenerada(Factura factura) {
        String mensaje = String.format(
            "Nueva factura #%s generada. Total: S/. %.2f. Estado: %s",
            factura.getNumeroFactura(),
            factura.getTotal(),
            factura.getEstado()
        );
        
        logger.info("NOTIFICACIÓN - {}", mensaje);
        
        enviarNotificacionDueno(factura.getDueno(), 
            "Nueva Factura Generada", mensaje);
    }

    /**
     * Notificación de pago recibido
     */
    public void notificarPagoRecibido(Factura factura) {
        String mensaje = String.format(
            "Pago recibido para factura #%s. Monto: S/. %.2f. Método: %s",
            factura.getNumeroFactura(),
            factura.getTotal(),
            factura.getMetodoPago()
        );
        
        logger.info("NOTIFICACIÓN - {}", mensaje);
        
        enviarNotificacionDueno(factura.getDueno(), 
            "Pago Recibido", mensaje);
    }
}