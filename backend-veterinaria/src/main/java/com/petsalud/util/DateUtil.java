package com.petsalud.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Utilidades para manejo de fechas y tiempos
 * Sistema de Gestión Veterinaria PetSalud
 */
public class DateUtil {

    // Formateadores comunes
    public static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter FORMATTER_DATETIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    public static final DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter FORMATTER_ISO_DATE = DateTimeFormatter.ISO_LOCAL_DATE;
    public static final DateTimeFormatter FORMATTER_ISO_DATETIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Convertir LocalDate a String con formato dd/MM/yyyy
     */
    public static String formatearFecha(LocalDate fecha) {
        return fecha != null ? fecha.format(FORMATTER_DATE) : "";
    }

    /**
     * Convertir LocalDateTime a String con formato dd/MM/yyyy HH:mm:ss
     */
    public static String formatearFechaHora(LocalDateTime fechaHora) {
        return fechaHora != null ? fechaHora.format(FORMATTER_DATETIME) : "";
    }

    /**
     * Convertir String a LocalDate
     */
    public static LocalDate parsearFecha(String fecha) {
        try {
            return LocalDate.parse(fecha, FORMATTER_DATE);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Convertir String a LocalDateTime
     */
    public static LocalDateTime parsearFechaHora(String fechaHora) {
        try {
            return LocalDateTime.parse(fechaHora, FORMATTER_DATETIME);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Obtener fecha actual
     */
    public static LocalDate obtenerFechaActual() {
        return LocalDate.now();
    }

    /**
     * Obtener fecha y hora actual
     */
    public static LocalDateTime obtenerFechaHoraActual() {
        return LocalDateTime.now();
    }

    /**
     * Calcular diferencia en días entre dos fechas
     */
    public static long calcularDiferenciaDias(LocalDate fechaInicio, LocalDate fechaFin) {
        return ChronoUnit.DAYS.between(fechaInicio, fechaFin);
    }

    /**
     * Calcular diferencia en horas entre dos fechas/horas
     */
    public static long calcularDiferenciaHoras(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ChronoUnit.HOURS.between(fechaInicio, fechaFin);
    }

    /**
     * Calcular diferencia en minutos entre dos fechas/horas
     */
    public static long calcularDiferenciaMinutos(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ChronoUnit.MINUTES.between(fechaInicio, fechaFin);
    }

    /**
     * Verificar si una fecha es hoy
     */
    public static boolean esFechaHoy(LocalDate fecha) {
        return fecha != null && fecha.equals(LocalDate.now());
    }

    /**
     * Verificar si una fecha es pasada
     */
    public static boolean esFechaPasada(LocalDate fecha) {
        return fecha != null && fecha.isBefore(LocalDate.now());
    }

    /**
     * Verificar si una fecha es futura
     */
    public static boolean esFechaFutura(LocalDate fecha) {
        return fecha != null && fecha.isAfter(LocalDate.now());
    }

    /**
     * Obtener el primer día del mes actual
     */
    public static LocalDate obtenerPrimerDiaMes() {
        return LocalDate.now().withDayOfMonth(1);
    }

    /**
     * Obtener el último día del mes actual
     */
    public static LocalDate obtenerUltimoDiaMes() {
        LocalDate hoy = LocalDate.now();
        return hoy.withDayOfMonth(hoy.lengthOfMonth());
    }

    /**
     * Obtener el primer día del año actual
     */
    public static LocalDate obtenerPrimerDiaAnio() {
        return LocalDate.now().withDayOfYear(1);
    }

    /**
     * Obtener el último día del año actual
     */
    public static LocalDate obtenerUltimoDiaAnio() {
        return LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear());
    }

    /**
     * Agregar días a una fecha
     */
    public static LocalDate agregarDias(LocalDate fecha, int dias) {
        return fecha != null ? fecha.plusDays(dias) : null;
    }

    /**
     * Agregar horas a una fecha/hora
     */
    public static LocalDateTime agregarHoras(LocalDateTime fechaHora, int horas) {
        return fechaHora != null ? fechaHora.plusHours(horas) : null;
    }

    /**
     * Restar días a una fecha
     */
    public static LocalDate restarDias(LocalDate fecha, int dias) {
        return fecha != null ? fecha.minusDays(dias) : null;
    }

    /**
     * Convertir LocalDate a Date (para compatibilidad)
     */
    public static Date convertirADate(LocalDate fecha) {
        return fecha != null ? Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
    }

    /**
     * Convertir Date a LocalDate
     */
    public static LocalDate convertirALocalDate(Date fecha) {
        return fecha != null ? fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }

    /**
     * Obtener nombre del día de la semana
     */
    public static String obtenerNombreDiaSemana(LocalDate fecha) {
        if (fecha == null) return "";
        switch (fecha.getDayOfWeek()) {
            case MONDAY: return "Lunes";
            case TUESDAY: return "Martes";
            case WEDNESDAY: return "Miércoles";
            case THURSDAY: return "Jueves";
            case FRIDAY: return "Viernes";
            case SATURDAY: return "Sábado";
            case SUNDAY: return "Domingo";
            default: return "";
        }
    }

    /**
     * Obtener nombre del mes
     */
    public static String obtenerNombreMes(LocalDate fecha) {
        if (fecha == null) return "";
        switch (fecha.getMonth()) {
            case JANUARY: return "Enero";
            case FEBRUARY: return "Febrero";
            case MARCH: return "Marzo";
            case APRIL: return "Abril";
            case MAY: return "Mayo";
            case JUNE: return "Junio";
            case JULY: return "Julio";
            case AUGUST: return "Agosto";
            case SEPTEMBER: return "Septiembre";
            case OCTOBER: return "Octubre";
            case NOVEMBER: return "Noviembre";
            case DECEMBER: return "Diciembre";
            default: return "";
        }
    }

    /**
     * Verificar si una fecha está entre dos fechas
     */
    public static boolean estaEnRango(LocalDate fecha, LocalDate inicio, LocalDate fin) {
        return fecha != null && inicio != null && fin != null &&
               (fecha.isEqual(inicio) || fecha.isAfter(inicio)) &&
               (fecha.isEqual(fin) || fecha.isBefore(fin));
    }

    /**
     * Calcular edad en años desde una fecha de nacimiento
     */
    public static int calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    /**
     * Formatear duración en texto legible
     */
    public static String formatearDuracion(long horas) {
        if (horas < 24) {
            return horas + " hora(s)";
        } else {
            long dias = horas / 24;
            long horasRestantes = horas % 24;
            return dias + " día(s) " + horasRestantes + " hora(s)";
        }
    }

    /**
     * Verificar si es fin de semana
     */
    public static boolean esFinDeSemana(LocalDate fecha) {
        if (fecha == null) return false;
        DayOfWeek dia = fecha.getDayOfWeek();
        return dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY;
    }

    /**
     * Obtener timestamp actual en milisegundos
     */
    public static long obtenerTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * Formatear fecha para SQL
     */
    public static String formatearParaSQL(LocalDate fecha) {
        return fecha != null ? fecha.format(FORMATTER_ISO_DATE) : null;
    }

    /**
     * Formatear fecha/hora para SQL
     */
    public static String formatearParaSQL(LocalDateTime fechaHora) {
        return fechaHora != null ? fechaHora.format(FORMATTER_ISO_DATETIME) : null;
    }
}