package com.petsalud.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Veterinario - Médicos veterinarios
 * Personal médico que emite órdenes y valida resultados
 */
@Entity
@Table(name = "veterinarios")
public class Veterinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_veterinario")
    private Long idVeterinario;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "especialidad", length = 100)
    private String especialidad; // Cirugía, Dermatología, Laboratorio, etc.

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "colegiatura", unique = true, length = 50)
    private String colegiatura; // Número de colegiatura profesional

    @Column(name = "activo")
    private Boolean activo = true;

    // Relación 1:N con OrdenVeterinaria
    @OneToMany(mappedBy = "veterinario", cascade = CascadeType.ALL)
    private List<OrdenVeterinaria> ordenes = new ArrayList<>();

    // Constructores
    public Veterinario() {
    }

    public Veterinario(String nombres, String apellidos, String especialidad, String colegiatura) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.especialidad = especialidad;
        this.colegiatura = colegiatura;
    }

    // Getters y Setters
    public Long getIdVeterinario() {
        return idVeterinario;
    }

    public void setIdVeterinario(Long idVeterinario) {
        this.idVeterinario = idVeterinario;
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

    public String getColegiatura() {
        return colegiatura;
    }

    public void setColegiatura(String colegiatura) {
        this.colegiatura = colegiatura;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public List<OrdenVeterinaria> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(List<OrdenVeterinaria> ordenes) {
        this.ordenes = ordenes;
    }

    // Métodos de ayuda para relaciones bidireccionales
    public void addOrden(OrdenVeterinaria orden) {
        ordenes.add(orden);
        orden.setVeterinario(this);
    }

    public void removeOrden(OrdenVeterinaria orden) {
        ordenes.remove(orden);
        orden.setVeterinario(null);
    }

    // Método auxiliar para obtener nombre completo
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    @Override
    public String toString() {
        return "Veterinario{" +
                "idVeterinario=" + idVeterinario +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", colegiatura='" + colegiatura + '\'' +
                '}';
    }
}