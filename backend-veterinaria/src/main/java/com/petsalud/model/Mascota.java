package com.petsalud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Mascota - Animales de compañía
 * RF-01: Registro de dueños y mascotas
 */
@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mascota")
    private Long idMascota;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "especie", nullable = false, length = 50)
    private String especie; // Perro, Gato, Ave, etc.

    @Column(name = "raza", length = 100)
    private String raza;

    @Column(name = "edad")
    private Integer edad; // En años

    @Column(name = "sexo", length = 10)
    private String sexo; // Macho, Hembra

    @Column(name = "peso")
    private Double peso; // En kilogramos

    @Column(name = "color", length = 50)
    private String color;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    // Relación N:1 con Dueno
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dueno", nullable = false)
    @JsonIgnoreProperties({"mascotas", "facturas"})
    private Dueno dueno;

    // Relación 1:N con OrdenVeterinaria
    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"mascota"})
    private List<OrdenVeterinaria> ordenes = new ArrayList<>();

    // Constructores
    public Mascota() {
    }

    public Mascota(String nombre, String especie, String raza, Integer edad, String sexo) {
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.edad = edad;
        this.sexo = sexo;
    }

    // Getters y Setters
    public Long getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(Long idMascota) {
        this.idMascota = idMascota;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Dueno getDueno() {
        return dueno;
    }

    public void setDueno(Dueno dueno) {
        this.dueno = dueno;
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
        orden.setMascota(this);
    }

    public void removeOrden(OrdenVeterinaria orden) {
        ordenes.remove(orden);
        orden.setMascota(null);
    }

    @Override
    public String toString() {
        return "Mascota{" +
                "idMascota=" + idMascota +
                ", nombre='" + nombre + '\'' +
                ", especie='" + especie + '\'' +
                ", raza='" + raza + '\'' +
                ", edad=" + edad +
                ", sexo='" + sexo + '\'' +
                '}';
    }
}