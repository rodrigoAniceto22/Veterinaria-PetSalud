package com.petsalud.controller;

import com.petsalud.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

/**
 * Controlador REST para generación de Reportes
 * RF-10: Reportes de laboratorio veterinario
 * KPIs del sistema
 */
@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "http://localhost:4200")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    /**
     * Obtener KPIs generales del sistema
     * GET /api/reportes/kpis
     */
    @GetMapping("/kpis")
    public ResponseEntity<Map<String, Object>> obtenerKPIs() {
        Map<String, Object> kpis = reporteService.obtenerKPIsGenerales();
        return ResponseEntity.ok(kpis);
    }

    /**
     * Reporte de tiempo promedio entre toma de muestra y entrega de resultados
     * GET /api/reportes/tiempo-promedio
     * KPI: Tiempo promedio entre toma de muestra y entrega de resultados
     */
    @GetMapping("/tiempo-promedio")
    public ResponseEntity<Map<String, Object>> reporteTiempoPromedio(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        Map<String, Object> reporte = reporteService.calcularTiempoPromedio(inicio, fin);
        return ResponseEntity.ok(reporte);
    }

    /**
     * Reporte de análisis repetidos por error
     * GET /api/reportes/analisis-repetidos
     * KPI: Porcentaje de análisis repetidos por error de registro o manipulación
     */
    @GetMapping("/analisis-repetidos")
    public ResponseEntity<Map<String, Object>> reporteAnalisisRepetidos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        Map<String, Object> reporte = reporteService.calcularAnalisisRepetidos(inicio, fin);
        return ResponseEntity.ok(reporte);
    }

    /**
     * Reporte de satisfacción del cliente
     * GET /api/reportes/satisfaccion-cliente
     * KPI: Nivel de satisfacción del cliente (dueño)
     */
    @GetMapping("/satisfaccion-cliente")
    public ResponseEntity<Map<String, Object>> reporteSatisfaccionCliente() {
        Map<String, Object> reporte = reporteService.obtenerSatisfaccionCliente();
        return ResponseEntity.ok(reporte);
    }

    /**
     * Reporte de tratamientos iniciados el mismo día
     * GET /api/reportes/tratamientos-mismo-dia
     * KPI: Número de tratamientos iniciados el mismo día de la consulta
     */
    @GetMapping("/tratamientos-mismo-dia")
    public ResponseEntity<Map<String, Object>> reporteTratamientosMismoDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        Map<String, Object> reporte = reporteService.calcularTratamientosMismoDia(fecha);
        return ResponseEntity.ok(reporte);
    }

    /**
     * Reporte de análisis realizados por tipo
     * GET /api/reportes/analisis-por-tipo
     */
    @GetMapping("/analisis-por-tipo")
    public ResponseEntity<Map<String, Object>> reporteAnalisisPorTipo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        Map<String, Object> reporte = reporteService.obtenerAnalisisPorTipo(inicio, fin);
        return ResponseEntity.ok(reporte);
    }

    /**
     * Reporte de productividad de veterinarios
     * GET /api/reportes/productividad-veterinarios
     */
    @GetMapping("/productividad-veterinarios")
    public ResponseEntity<Map<String, Object>> reporteProductividadVeterinarios(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        Map<String, Object> reporte = reporteService.obtenerProductividadVeterinarios(inicio, fin);
        return ResponseEntity.ok(reporte);
    }

    /**
     * Reporte de productividad de técnicos
     * GET /api/reportes/productividad-tecnicos
     */
    @GetMapping("/productividad-tecnicos")
    public ResponseEntity<Map<String, Object>> reporteProductividadTecnicos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        Map<String, Object> reporte = reporteService.obtenerProductividadTecnicos(inicio, fin);
        return ResponseEntity.ok(reporte);
    }

    /**
     * Reporte de ingresos por período
     * GET /api/reportes/ingresos
     */
    @GetMapping("/ingresos")
    public ResponseEntity<Map<String, Object>> reporteIngresos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        Map<String, Object> reporte = reporteService.calcularIngresos(inicio, fin);
        return ResponseEntity.ok(reporte);
    }

    /**
     * Reporte de especies más atendidas
     * GET /api/reportes/especies-atendidas
     */
    @GetMapping("/especies-atendidas")
    public ResponseEntity<Map<String, Object>> reporteEspeciesAtendidas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        Map<String, Object> reporte = reporteService.obtenerEspeciesAtendidas(inicio, fin);
        return ResponseEntity.ok(reporte);
    }

    /**
     * Reporte de órdenes por estado
     * GET /api/reportes/ordenes-estado
     */
    @GetMapping("/ordenes-estado")
    public ResponseEntity<Map<String, Object>> reporteOrdenesPorEstado() {
        Map<String, Object> reporte = reporteService.obtenerOrdenesPorEstado();
        return ResponseEntity.ok(reporte);
    }

    /**
     * Dashboard completo con métricas principales
     * GET /api/reportes/dashboard
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> obtenerDashboard() {
        Map<String, Object> dashboard = reporteService.obtenerDashboardCompleto();
        return ResponseEntity.ok(dashboard);
    }
}