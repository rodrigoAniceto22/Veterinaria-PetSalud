package com.petsalud.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Técnico Veterinario - Personal de laboratorio
 * RF-04: Registro de toma de muestra
 * RF-05: Gestión de análisis
 */
@Entity
@Table(name = "tecnicos_veterinarios")
public class TecnicoVeterinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tecnico")
    private Long idTecnico;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "especialidad", length = 100)
    private String especialidad; // Hematología, Microbiología, Química Clínica, etc.

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "certificacion", length = 100)
    private String certificacion; // Certificaciones o títulos técnicos

    @Column(name = "activo")
    private Boolean activo = true;

    // Relación 1:N con TomaMuestraVet
    @OneToMany(mappedBy = "tecnico", cascade = CascadeType.ALL)
    private List<TomaMuestraVet> tomasMuestra = new ArrayList<>();

    // Constructores
    public TecnicoVeterinario() {
    }

    public TecnicoVeterinario(String nombres, String apellidos, String especialidad) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.especialidad = especialidad;
    }

    // Getters y Setters
    public Long getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(Long idTecnico) {
        this.idTecnico = idTecnico;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCertificacion() {
        return certificacion;
    }

    public void setCertificacion(String certificacion) {
        this.certificacion = certificacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public List<TomaMuestraVet> getTomasMuestra() {
        return tomasMuestra;
    }

    public void setTomasMuestra(List<TomaMuestraVet> tomasMuestra) {
        this.tomasMuestra = tomasMuestra;
    }

    // Métodos de ayuda para relaciones bidireccionales
    public void addTomaMuestra(TomaMuestraVet tomaMuestra) {
        tomasMuestra.add(tomaMuestra);
        tomaMuestra.setTecnico(this);
    }

    public void removeTomaMuestra(TomaMuestraVet tomaMuestra) {
        tomasMuestra.remove(tomaMuestra);
        tomaMuestra.setTecnico(null);
    }

    // Método auxiliar para obtener nombre completo
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    @Override
    public String toString() {
        return "TecnicoVeterinario{" +
                "idTecnico=" + idTecnico +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", certificacion='" + certificacion + '\'' +
                '}';
    }
}