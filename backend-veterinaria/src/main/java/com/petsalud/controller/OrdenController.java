package com.petsalud.controller;

import com.petsalud.model.OrdenVeterinaria;
import com.petsalud.service.OrdenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para gestión de Órdenes Veterinarias
 * RF-02: Registro de órdenes veterinarias
 */
@RestController
@RequestMapping("/api/ordenes")
@CrossOrigin(origins = "http://localhost:4200")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    /**
     * Listar todas las órdenes
     * GET /api/ordenes
     */
    @GetMapping
    public ResponseEntity<List<OrdenVeterinaria>> listarTodas() {
        List<OrdenVeterinaria> ordenes = ordenService.listarTodas();
        return ResponseEntity.ok(ordenes);
    }

    /**
     * Obtener orden por ID
     * GET /api/ordenes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrdenVeterinaria> obtenerPorId(@PathVariable Long id) {
        OrdenVeterinaria orden = ordenService.obtenerPorId(id);
        if (orden != null) {
            return ResponseEntity.ok(orden);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Buscar órdenes por mascota
     * GET /api/ordenes/mascota/{idMascota}
     */
    @GetMapping("/mascota/{idMascota}")
    public ResponseEntity<List<OrdenVeterinaria>> buscarPorMascota(@PathVariable Long idMascota) {
        List<OrdenVeterinaria> ordenes = ordenService.buscarPorMascota(idMascota);
        return ResponseEntity.ok(ordenes);
    }

    /**
     * Buscar órdenes por veterinario
     * GET /api/ordenes/veterinario/{idVeterinario}
     */
    @GetMapping("/veterinario/{idVeterinario}")
    public ResponseEntity<List<OrdenVeterinaria>> buscarPorVeterinario(@PathVariable Long idVeterinario) {
        List<OrdenVeterinaria> ordenes = ordenService.buscarPorVeterinario(idVeterinario);
        return ResponseEntity.ok(ordenes);
    }

    /**
     * Buscar órdenes por fecha
     * GET /api/ordenes/fecha?fecha=2025-01-15
     */
    @GetMapping("/fecha")
    public ResponseEntity<List<OrdenVeterinaria>> buscarPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<OrdenVeterinaria> ordenes = ordenService.buscarPorFecha(fecha);
        return ResponseEntity.ok(ordenes);
    }

    /**
     * Buscar órdenes por tipo de examen
     * GET /api/ordenes/tipo-examen/{tipo}
     */
    @GetMapping("/tipo-examen/{tipo}")
    public ResponseEntity<List<OrdenVeterinaria>> buscarPorTipoExamen(@PathVariable String tipo) {
        List<OrdenVeterinaria> ordenes = ordenService.buscarPorTipoExamen(tipo);
        return ResponseEntity.ok(ordenes);
    }

    /**
     * Obtener órdenes pendientes
     * GET /api/ordenes/pendientes
     */
    @GetMapping("/pendientes")
    public ResponseEntity<List<OrdenVeterinaria>> obtenerPendientes() {
        List<OrdenVeterinaria> ordenes = ordenService.obtenerOrdenesPendientes();
        return ResponseEntity.ok(ordenes);
    }

    /**
     * Crear nueva orden
     * POST /api/ordenes
     */
    @PostMapping
    public ResponseEntity<OrdenVeterinaria> crear(@RequestBody OrdenVeterinaria orden) {
        OrdenVeterinaria nuevaOrden = ordenService.guardar(orden);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaOrden);
    }

    /**
     * Actualizar orden existente
     * PUT /api/ordenes/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrdenVeterinaria> actualizar(@PathVariable Long id, @RequestBody OrdenVeterinaria orden) {
        OrdenVeterinaria ordenExistente = ordenService.obtenerPorId(id);
        if (ordenExistente == null) {
            return ResponseEntity.notFound().build();
        }
        orden.setIdOrden(id);
        OrdenVeterinaria ordenActualizada = ordenService.guardar(orden);
        return ResponseEntity.ok(ordenActualizada);
    }

    /**
     * Eliminar orden
     * DELETE /api/ordenes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        OrdenVeterinaria orden = ordenService.obtenerPorId(id);
        if (orden == null) {
            return ResponseEntity.notFound().build();
        }
        ordenService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Cambiar estado de orden
     * PATCH /api/ordenes/{id}/estado
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<OrdenVeterinaria> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        OrdenVeterinaria orden = ordenService.cambiarEstado(id, estado);
        if (orden != null) {
            return ResponseEntity.ok(orden);
        }
        return ResponseEntity.notFound().build();
    }
}