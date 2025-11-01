package com.petsalud.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Utilidad para validaciones avanzadas
 * Validaciones de fechas y datos
 */
public class ValidationUtil {

    /**
     * Validar que una fecha no sea futura
     */
    public static boolean esFechaFutura(LocalDate fecha) {
        if (fecha == null) {
            return false;
        }
        return fecha.isAfter(LocalDate.now());
    }

    /**
     * Validar que una fecha/hora no sea futura
     */
    public static boolean esFechaHoraFutura(LocalDateTime fechaHora) {
        if (fechaHora == null) {
            return false;
        }
        return fechaHora.isAfter(LocalDateTime.now());
    }

    /**
     * Validar que una fecha sea hoy
     */
    public static boolean esFechaHoy(LocalDate fecha) {
        if (fecha == null) {
            return false;
        }
        return fecha.isEqual(LocalDate.now());
    }

    /**
     * Validar que una fecha sea pasada
     */
    public static boolean esFechaPasada(LocalDate fecha) {
        if (fecha == null) {
            return false;
        }
        return fecha.isBefore(LocalDate.now());
    }

    /**
     * Validar fecha de nacimiento (no puede ser futura ni muy antigua)
     */
    public static void validarFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria");
        }
        if (esFechaFutura(fechaNacimiento)) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura");
        }
        // Validar que no sea más de 50 años atrás (para mascotas)
        LocalDate hace50Anos = LocalDate.now().minusYears(50);
        if (fechaNacimiento.isBefore(hace50Anos)) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser mayor a 50 años");
        }
    }

    /**
     * Validar fecha de cita (no puede ser pasada)
     */
    public static void validarFechaCita(LocalDateTime fechaHora) {
        if (fechaHora == null) {
            throw new IllegalArgumentException("La fecha y hora de la cita son obligatorias");
        }
        if (fechaHora.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de la cita no puede ser pasada");
        }
    }

    /**
     * Validar fecha de vencimiento (debe ser futura)
     */
    public static void validarFechaVencimiento(LocalDate fechaVencimiento) {
        if (fechaVencimiento != null && esFechaPasada(fechaVencimiento)) {
            throw new IllegalArgumentException("La fecha de vencimiento debe ser futura");
        }
    }

    /**
     * Validar rango de fechas
     */
    public static void validarRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
    }

    /**
     * Validar que un string no esté vacío
     */
    public static void validarNoVacio(String valor, String nombreCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(nombreCampo + " es obligatorio");
        }
    }

    /**
     * Validar que un número sea positivo
     */
    public static void validarPositivo(Number valor, String nombreCampo) {
        if (valor == null || valor.doubleValue() <= 0) {
            throw new IllegalArgumentException(nombreCampo + " debe ser mayor a 0");
        }
    }

    /**
     * Validar que un número no sea negativo
     */
    public static void validarNoNegativo(Number valor, String nombreCampo) {
        if (valor == null || valor.doubleValue() < 0) {
            throw new IllegalArgumentException(nombreCampo + " no puede ser negativo");
        }
    }

    /**
     * Validar email
     */
    public static boolean esEmailValido(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Validar DNI (8 dígitos)
     */
    public static boolean esDNIValido(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            return false;
        }
        return dni.matches("^\\d{8}$");
    }

    /**
     * Validar teléfono (9 dígitos)
     */
    public static boolean esTelefonoValido(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return false;
        }
        return telefono.matches("^\\d{9}$");
    }
}