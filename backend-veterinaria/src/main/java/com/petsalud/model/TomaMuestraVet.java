package com.petsalud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad Toma de Muestra Veterinaria
 * RF-03: Programación de toma de muestra
 * RF-04: Registro de toma de muestra
 */
@Entity
@Table(name = "toma_muestras_vet")
public class TomaMuestraVet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_toma")
    private Long idToma;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "tipo_muestra", nullable = false, length = 100)
    private String tipoMuestra; // Sangre, Orina, Heces, Piel, etc.

    @Column(name = "metodo_obtencion", length = 100)
    private String metodoObtencion; // Venopunción, cistocentesis, etc.

    @Column(name = "volumen_muestra", length = 50)
    private String volumenMuestra; // 5ml, 10ml, etc.

    @Column(name = "condiciones_muestra", length = 200)
    private String condicionesMuestra; // Temperatura, conservación, etc.

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "estado", length = 50)
    private String estado; // PROGRAMADA, REALIZADA, PROCESANDO, COMPLETADA

    @Column(name = "codigo_muestra", unique = true, length = 50)
    private String codigoMuestra; // Código único de identificación de la muestra

    // Relación 1:1 con OrdenVeterinaria
    @OneToOne
    @JoinColumn(name = "id_orden", nullable = false, unique = true)
    @JsonIgnoreProperties({"tomaMuestra", "resultados"})
    private OrdenVeterinaria orden;

    // Relación N:1 con TecnicoVeterinario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tecnico", nullable = false)
    @JsonIgnoreProperties({"tomasMuestra"})
    private TecnicoVeterinario tecnico;

    // Constructores
    public TomaMuestraVet() {
        this.estado = "PROGRAMADA";
    }

    public TomaMuestraVet(LocalDateTime fechaHora, String tipoMuestra, OrdenVeterinaria orden, TecnicoVeterinario tecnico) {
        this();
        this.fechaHora = fechaHora;
        this.tipoMuestra = tipoMuestra;
        this.orden = orden;
        this.tecnico = tecnico;
    }

    // Getters y Setters
    public Long getIdToma() {
        return idToma;
    }

    public void setIdToma(Long idToma) {
        this.idToma = idToma;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getTipoMuestra() {
        return tipoMuestra;
    }

    public void setTipoMuestra(String tipoMuestra) {
        this.tipoMuestra = tipoMuestra;
    }

    public String getMetodoObtencion() {
        return metodoObtencion;
    }

    public void setMetodoObtencion(String metodoObtencion) {
        this.metodoObtencion = metodoObtencion;
    }

    public String getVolumenMuestra() {
        return volumenMuestra;
    }

    public void setVolumenMuestra(String volumenMuestra) {
        this.volumenMuestra = volumenMuestra;
    }

    public String getCondicionesMuestra() {
        return condicionesMuestra;
    }

    public void setCondicionesMuestra(String condicionesMuestra) {
        this.condicionesMuestra = condicionesMuestra;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCodigoMuestra() {
        return codigoMuestra;
    }

    public void setCodigoMuestra(String codigoMuestra) {
        this.codigoMuestra = codigoMuestra;
    }

    public OrdenVeterinaria getOrden() {
        return orden;
    }

    public void setOrden(OrdenVeterinaria orden) {
        this.orden = orden;
    }

    public TecnicoVeterinario getTecnico() {
        return tecnico;
    }

    public void setTecnico(TecnicoVeterinario tecnico) {
        this.tecnico = tecnico;
    }

    @Override
    public String toString() {
        return "TomaMuestraVet{" +
                "idToma=" + idToma +
                ", fechaHora=" + fechaHora +
                ", tipoMuestra='" + tipoMuestra + '\'' +
                ", codigoMuestra='" + codigoMuestra + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}