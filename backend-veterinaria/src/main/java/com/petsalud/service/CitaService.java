package com.petsalud.service;

import com.petsalud.model.Cita;
import com.petsalud.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de Citas
 * Sistema de citas con alertas
 */
@Service
@Transactional
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    /**
     * Listar todas las citas
     */
    public List<Cita> listarTodas() {
        return citaRepository.findAll();
    }

    /**
     * Obtener por ID
     */
    public Cita obtenerPorId(Long id) {
        return citaRepository.findById(id).orElse(null);
    }

    /**
     * Buscar citas por mascota
     */
    public List<Cita> buscarPorMascota(Long idMascota) {
        return citaRepository.findByMascota_IdMascota(idMascota);
    }

    /**
     * Buscar citas por veterinario
     */
    public List<Cita> buscarPorVeterinario(Long idVeterinario) {
        return citaRepository.findByVeterinario_IdVeterinario(idVeterinario);
    }

    /**
     * Buscar por estado
     */
    public List<Cita> buscarPorEstado(String estado) {
        return citaRepository.findByEstadoIgnoreCase(estado);
    }

    /**
     * Obtener citas del día
     */
    public List<Cita> obtenerCitasDelDia() {
        return citaRepository.findCitasDelDia(LocalDateTime.now());
    }

    /**
     * Obtener citas próximas (próximas 24 horas)
     */
    public List<Cita> obtenerCitasProximas() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limite = ahora.plusHours(24);
        return citaRepository.findCitasProximas(ahora, limite);
    }

    /**
     * Obtener citas críticas (próxima hora)
     */
    public List<Cita> obtenerCitasCriticas() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limite = ahora.plusHours(1);
        return citaRepository.findCitasCriticas(ahora, limite);
    }

    /**
     * Obtener citas con alertas
     */
    public List<Cita> obtenerCitasConAlertas() {
        List<Cita> todasCitas = citaRepository.findAll();
        return todasCitas.stream()
                .filter(Cita::necesitaAlerta)
                .collect(Collectors.toList());
    }

    /**
     * Obtener dashboard de alertas
     */
    public CitaAlertas obtenerDashboardAlertas() {
        CitaAlertas alertas = new CitaAlertas();
        alertas.setCitasCriticas(obtenerCitasCriticas());
        alertas.setCitasProximas(obtenerCitasProximas());
        alertas.setCitasDelDia(obtenerCitasDelDia());
        alertas.setCitasPendientesConfirmacion(citaRepository.findCitasPendientesConfirmacion(LocalDateTime.now()));
        return alertas;
    }

    /**
     * Guardar o actualizar cita
     */
    public Cita guardar(Cita cita) {
        // Validaciones
        if (cita.getMascota() == null) {
            throw new RuntimeException("La cita debe estar asociada a una mascota");
        }
        if (cita.getFechaHora() == null) {
            throw new RuntimeException("La fecha y hora de la cita son obligatorias");
        }
        if (cita.getMotivo() == null || cita.getMotivo().trim().isEmpty()) {
            throw new RuntimeException("El motivo de la cita es obligatorio");
        }

        // Validar que la fecha no sea pasada
        if (cita.getFechaHora().isBefore(LocalDateTime.now()) && cita.getIdCita() == null) {
            throw new RuntimeException("No se puede crear una cita con fecha pasada");
        }

        // Si es nueva cita
        if (cita.getIdCita() == null) {
            if (cita.getEstado() == null) {
                cita.setEstado("PROGRAMADA");
            }
            if (cita.getDuracionMinutos() == null) {
                cita.setDuracionMinutos(30);
            }
            if (cita.getRecordatorioEnviado() == null) {
                cita.setRecordatorioEnviado(false);
            }
        }

        return citaRepository.save(cita);
    }

    /**
     * Confirmar cita
     */
    public Cita confirmarCita(Long idCita) {
        Cita cita = obtenerPorId(idCita);
        if (cita == null) {
            throw new RuntimeException("Cita no encontrada");
        }
        cita.setEstado("CONFIRMADA");
        return citaRepository.save(cita);
    }

    /**
     * Cancelar cita
     */
    public Cita cancelarCita(Long idCita, String motivo) {
        Cita cita = obtenerPorId(idCita);
        if (cita == null) {
            throw new RuntimeException("Cita no encontrada");
        }
        cita.setEstado("CANCELADA");
        if (motivo != null && !motivo.trim().isEmpty()) {
            String obs = cita.getObservaciones() != null ? cita.getObservaciones() + "\n" : "";
            cita.setObservaciones(obs + "Cancelada: " + motivo);
        }
        return citaRepository.save(cita);
    }

    /**
     * Completar cita
     */
    public Cita completarCita(Long idCita) {
        Cita cita = obtenerPorId(idCita);
        if (cita == null) {
            throw new RuntimeException("Cita no encontrada");
        }
        cita.setEstado("COMPLETADA");
        return citaRepository.save(cita);
    }

    /**
     * Eliminar cita
     */
    public void eliminar(Long id) {
        citaRepository.deleteById(id);
    }

    /**
     * Marcar recordatorio como enviado
     */
    public Cita marcarRecordatorioEnviado(Long idCita) {
        Cita cita = obtenerPorId(idCita);
        if (cita != null) {
            cita.setRecordatorioEnviado(true);
            return citaRepository.save(cita);
        }
        return null;
    }

    // Clase interna para dashboard de alertas
    public static class CitaAlertas {
        private List<Cita> citasCriticas;
        private List<Cita> citasProximas;
        private List<Cita> citasDelDia;
        private List<Cita> citasPendientesConfirmacion;

        public List<Cita> getCitasCriticas() {
            return citasCriticas;
        }

        public void setCitasCriticas(List<Cita> citasCriticas) {
            this.citasCriticas = citasCriticas;
        }

        public List<Cita> getCitasProximas() {
            return citasProximas;
        }

        public void setCitasProximas(List<Cita> citasProximas) {
            this.citasProximas = citasProximas;
        }

        public List<Cita> getCitasDelDia() {
            return citasDelDia;
        }

        public void setCitasDelDia(List<Cita> citasDelDia) {
            this.citasDelDia = citasDelDia;
        }

        public List<Cita> getCitasPendientesConfirmacion() {
            return citasPendientesConfirmacion;
        }

        public void setCitasPendientesConfirmacion(List<Cita> citasPendientesConfirmacion) {
            this.citasPendientesConfirmacion = citasPendientesConfirmacion;
        }
    }
}