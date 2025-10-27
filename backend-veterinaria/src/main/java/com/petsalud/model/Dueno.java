package com.petsalud.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Dueño - Propietarios de mascotas
 * RF-01: Registro de dueños y mascotas
 */
@Entity
@Table(name = "duenos")
public class Dueno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dueno")
    private Long idDueno;

    @Column(name = "dni", nullable = false, unique = true, length = 20)
    private String dni;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "direccion", length = 200)
    private String direccion;

    // Relación 1:N con Mascota
    @OneToMany(mappedBy = "dueno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mascota> mascotas = new ArrayList<>();

    // Relación 1:N con Factura
    @OneToMany(mappedBy = "dueno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Factura> facturas = new ArrayList<>();

    // Constructores
    public Dueno() {
    }

    public Dueno(String dni, String nombres, String apellidos, String telefono) {
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
    }

    // Getters y Setters
    public Long getIdDueno() {
        return idDueno;
    }

    public void setIdDueno(Long idDueno) {
        this.idDueno = idDueno;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Mascota> getMascotas() {
        return mascotas;
    }

    public void setMascotas(List<Mascota> mascotas) {
        this.mascotas = mascotas;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

    // Métodos de ayuda para relaciones bidireccionales
    public void addMascota(Mascota mascota) {
        mascotas.add(mascota);
        mascota.setDueno(this);
    }

    public void removeMascota(Mascota mascota) {
        mascotas.remove(mascota);
        mascota.setDueno(null);
    }

    public void addFactura(Factura factura) {
        facturas.add(factura);
        factura.setDueno(this);
    }

    public void removeFactura(Factura factura) {
        facturas.remove(factura);
        factura.setDueno(null);
    }

    @Override
    public String toString() {
        return "Dueno{" +
                "idDueno=" + idDueno +
                ", dni='" + dni + '\'' +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}