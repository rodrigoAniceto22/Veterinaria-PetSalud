package com.petsalud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad Usuario - Usuarios del sistema
 * RF-09: Consulta de resultados en línea (autenticación)
 * Gestión de autenticación y autorización
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre_usuario", nullable = false, unique = true, length = 50)
    private String nombreUsuario;

    @Column(name = "contrasena", nullable = false, length = 255)
    @JsonIgnore // No exponer la contraseña en respuestas JSON
    private String contrasena;

    @Column(name = "rol", nullable = false, length = 50)
    private String rol; // ADMIN, VETERINARIO, TECNICO, RECEPCIONISTA

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "nombres", length = 100)
    private String nombres;

    @Column(name = "apellidos", length = 100)
    private String apellidos;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;

    @Column(name = "intentos_fallidos")
    private Integer intentosFallidos = 0;

    @Column(name = "bloqueado")
    private Boolean bloqueado = false;

    // Constructores
    public Usuario() {
        this.fechaCreacion = LocalDateTime.now();
        this.activo = true;
        this.intentosFallidos = 0;
        this.bloqueado = false;
    }

    public Usuario(String nombreUsuario, String contrasena, String rol) {
        this();
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    // Getters y Setters
    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(LocalDateTime ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public Integer getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(Integer intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public Boolean getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(Boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    // Métodos auxiliares
    public String getNombreCompleto() {
        if (nombres != null && apellidos != null) {
            return nombres + " " + apellidos;
        }
        return nombreUsuario;
    }

    public void registrarAcceso() {
        this.ultimoAcceso = LocalDateTime.now();
        this.intentosFallidos = 0;
    }

    public void registrarIntentoFallido() {
        this.intentosFallidos++;
        if (this.intentosFallidos >= 3) {
            this.bloqueado = true;
        }
    }

    public void desbloquear() {
        this.bloqueado = false;
        this.intentosFallidos = 0;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", rol='" + rol + '\'' +
                ", email='" + email + '\'' +
                ", activo=" + activo +
                '}';
    }
}