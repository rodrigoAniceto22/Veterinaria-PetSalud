package com.petsalud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Entidad Cita - Citas veterinarias con sistema de alertas
 */
@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Long idCita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mascota", nullable = false)
    @JsonIgnoreProperties({"dueno", "ordenes"})
    private Mascota mascota;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_veterinario")
    @JsonIgnoreProperties({"ordenes"})
    private Veterinario veterinario;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "motivo", nullable = false, length = 200)
    private String motivo;

    @Column(name = "tipo_cita", length = 100)
    private String tipoCita; // Consulta, Vacunación, Control, Cirugía, Emergencia

    @Column(name = "estado", length = 50)
    private String estado; // PROGRAMADA, CONFIRMADA, EN_CURSO, COMPLETADA, CANCELADA

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos = 30;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "costo_consulta")
    private Double costoConsulta;

    @Column(name = "recordatorio_enviado")
    private Boolean recordatorioEnviado = false;

    // Constructores
    public Cita() {
    }

    public Cita(Mascota mascota, LocalDateTime fechaHora, String motivo, String estado) {
        this.mascota = mascota;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getIdCita() {
        return idCita;
    }

    public void setIdCita(Long idCita) {
        this.idCita = idCita;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getTipoCita() {
        return tipoCita;
    }

    public void setTipoCita(String tipoCita) {
        this.tipoCita = tipoCita;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Double getCostoConsulta() {
        return costoConsulta;
    }

    public void setCostoConsulta(Double costoConsulta) {
        this.costoConsulta = costoConsulta;
    }

    public Boolean getRecordatorioEnviado() {
        return recordatorioEnviado;
    }

    public void setRecordatorioEnviado(Boolean recordatorioEnviado) {
        this.recordatorioEnviado = recordatorioEnviado;
    }

    // Métodos de negocio - Sistema de alertas
    public long getHorasHastaCita() {
        return ChronoUnit.HOURS.between(LocalDateTime.now(), fechaHora);
    }

    public long getMinutosHastaCita() {
        return ChronoUnit.MINUTES.between(LocalDateTime.now(), fechaHora);
    }

    public String getNivelAlerta() {
        long horas = getHorasHastaCita();
        
        if (horas < 0) return "VENCIDA";
        if (horas <= 1) return "CRITICA"; // Rojo - menos de 1 hora
        if (horas <= 6) return "ALTA"; // Naranja - menos de 6 horas
        if (horas <= 24) return "MEDIA"; // Amarillo - menos de 24 horas
        return "BAJA"; // Verde - más de 24 horas
    }

    public boolean necesitaAlerta() {
        long horas = getHorasHastaCita();
        return horas >= 0 && horas <= 24 && !"CANCELADA".equalsIgnoreCase(estado);
    }

    public boolean estaProxima() {
        long horas = getHorasHastaCita();
        return horas >= 0 && horas <= 6;
    }

    public boolean estaCritica() {
        long horas = getHorasHastaCita();
        return horas >= 0 && horas <= 1;
    }

    public boolean yaOcurrio() {
        return LocalDateTime.now().isAfter(fechaHora);
    }

    @Override
    public String toString() {
        return "Cita{" +
                "idCita=" + idCita +
                ", fechaHora=" + fechaHora +
                ", motivo='" + motivo + '\'' +
                ", estado='" + estado + '\'' +
                ", nivelAlerta='" + getNivelAlerta() + '\'' +
                '}';
    }
}