package com.petsalud.controller;

import com.petsalud.model.Cita;
import com.petsalud.service.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de Citas
 * Sistema de citas con alertas
 */
@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "http://localhost:4200")
public class CitaController {

    @Autowired
    private CitaService citaService;

    /**
     * Listar todas las citas
     * GET /api/citas
     */
    @GetMapping
    public ResponseEntity<List<Cita>> listarTodas() {
        return ResponseEntity.ok(citaService.listarTodas());
    }

    /**
     * Obtener cita por ID
     * GET /api/citas/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cita> obtenerPorId(@PathVariable Long id) {
        Cita cita = citaService.obtenerPorId(id);
        if (cita != null) {
            return ResponseEntity.ok(cita);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Buscar citas por mascota
     * GET /api/citas/mascota/{idMascota}
     */
    @GetMapping("/mascota/{idMascota}")
    public ResponseEntity<List<Cita>> buscarPorMascota(@PathVariable Long idMascota) {
        return ResponseEntity.ok(citaService.buscarPorMascota(idMascota));
    }

    /**
     * Buscar citas por veterinario
     * GET /api/citas/veterinario/{idVeterinario}
     */
    @GetMapping("/veterinario/{idVeterinario}")
    public ResponseEntity<List<Cita>> buscarPorVeterinario(@PathVariable Long idVeterinario) {
        return ResponseEntity.ok(citaService.buscarPorVeterinario(idVeterinario));
    }

    /**
     * Buscar por estado
     * GET /api/citas/estado/{estado}
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Cita>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(citaService.buscarPorEstado(estado));
    }

    /**
     * Obtener citas del día
     * GET /api/citas/hoy
     */
    @GetMapping("/hoy")
    public ResponseEntity<List<Cita>> obtenerCitasDelDia() {
        return ResponseEntity.ok(citaService.obtenerCitasDelDia());
    }

    /**
     * Obtener citas próximas (próximas 24 horas)
     * GET /api/citas/proximas
     */
    @GetMapping("/proximas")
    public ResponseEntity<List<Cita>> obtenerCitasProximas() {
        return ResponseEntity.ok(citaService.obtenerCitasProximas());
    }

    /**
     * Obtener citas críticas (próxima hora)
     * GET /api/citas/criticas
     */
    @GetMapping("/criticas")
    public ResponseEntity<List<Cita>> obtenerCitasCriticas() {
        return ResponseEntity.ok(citaService.obtenerCitasCriticas());
    }

    /**
     * Obtener citas con alertas
     * GET /api/citas/con-alertas
     */
    @GetMapping("/con-alertas")
    public ResponseEntity<List<Cita>> obtenerCitasConAlertas() {
        return ResponseEntity.ok(citaService.obtenerCitasConAlertas());
    }

    /**
     * Obtener dashboard de alertas
     * GET /api/citas/dashboard-alertas
     */
    @GetMapping("/dashboard-alertas")
    public ResponseEntity<CitaService.CitaAlertas> obtenerDashboardAlertas() {
        return ResponseEntity.ok(citaService.obtenerDashboardAlertas());
    }

    /**
     * Crear nueva cita
     * POST /api/citas
     */
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Cita cita) {
        try {
            Cita nueva = citaService.guardar(cita);
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Actualizar cita
     * PUT /api/citas/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Cita cita) {
        try {
            Cita existente = citaService.obtenerPorId(id);
            if (existente == null) {
                return ResponseEntity.notFound().build();
            }
            cita.setIdCita(id);
            Cita actualizada = citaService.guardar(cita);
            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Confirmar cita
     * PATCH /api/citas/{id}/confirmar
     */
    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmar(@PathVariable Long id) {
        try {
            Cita cita = citaService.confirmarCita(id);
            return ResponseEntity.ok(cita);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Cancelar cita
     * PATCH /api/citas/{id}/cancelar
     */
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id, @RequestParam(required = false) String motivo) {
        try {
            Cita cita = citaService.cancelarCita(id, motivo);
            return ResponseEntity.ok(cita);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Completar cita
     * PATCH /api/citas/{id}/completar
     */
    @PatchMapping("/{id}/completar")
    public ResponseEntity<?> completar(@PathVariable Long id) {
        try {
            Cita cita = citaService.completarCita(id);
            return ResponseEntity.ok(cita);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Marcar recordatorio como enviado
     * PATCH /api/citas/{id}/recordatorio-enviado
     */
    @PatchMapping("/{id}/recordatorio-enviado")
    public ResponseEntity<?> marcarRecordatorioEnviado(@PathVariable Long id) {
        Cita cita = citaService.marcarRecordatorioEnviado(id);
        if (cita != null) {
            return ResponseEntity.ok(cita);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Eliminar cita
     * DELETE /api/citas/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Cita cita = citaService.obtenerPorId(id);
        if (cita == null) {
            return ResponseEntity.notFound().build();
        }
        citaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}