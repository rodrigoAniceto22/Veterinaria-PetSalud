package com.petsalud.controller;

import com.petsalud.model.Mascota;
import com.petsalud.service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de Mascotas
 * RF-01: Registro de dueños y mascotas
 */
@RestController
@RequestMapping("/api/mascotas")
@CrossOrigin(origins = "http://localhost:4200")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    /**
     * Listar todas las mascotas
     * GET /api/mascotas
     */
    @GetMapping
    public ResponseEntity<List<Mascota>> listarTodas() {
        List<Mascota> mascotas = mascotaService.listarTodas();
        return ResponseEntity.ok(mascotas);
    }

    /**
     * Obtener mascota por ID
     * GET /api/mascotas/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Mascota> obtenerPorId(@PathVariable Long id) {
        Mascota mascota = mascotaService.obtenerPorId(id);
        if (mascota != null) {
            return ResponseEntity.ok(mascota);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Buscar mascotas por nombre
     * GET /api/mascotas/buscar?nombre={nombre}
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Mascota>> buscarPorNombre(@RequestParam String nombre) {
        List<Mascota> mascotas = mascotaService.buscarPorNombre(nombre);
        return ResponseEntity.ok(mascotas);
    }

    /**
     * Buscar mascotas por especie
     * GET /api/mascotas/especie/{especie}
     */
    @GetMapping("/especie/{especie}")
    public ResponseEntity<List<Mascota>> buscarPorEspecie(@PathVariable String especie) {
        List<Mascota> mascotas = mascotaService.buscarPorEspecie(especie);
        return ResponseEntity.ok(mascotas);
    }

    /**
     * Buscar mascotas por dueño
     * GET /api/mascotas/dueno/{idDueno}
     */
    @GetMapping("/dueno/{idDueno}")
    public ResponseEntity<List<Mascota>> buscarPorDueno(@PathVariable Long idDueno) {
        List<Mascota> mascotas = mascotaService.buscarPorDueno(idDueno);
        return ResponseEntity.ok(mascotas);
    }

    /**
     * Crear nueva mascota
     * POST /api/mascotas
     */
    @PostMapping
    public ResponseEntity<Mascota> crear(@RequestBody Mascota mascota) {
        Mascota nuevaMascota = mascotaService.guardar(mascota);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMascota);
    }

    /**
     * Actualizar mascota existente
     * PUT /api/mascotas/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Mascota> actualizar(@PathVariable Long id, @RequestBody Mascota mascota) {
        Mascota mascotaExistente = mascotaService.obtenerPorId(id);
        if (mascotaExistente == null) {
            return ResponseEntity.notFound().build();
        }
        mascota.setIdMascota(id);
        Mascota mascotaActualizada = mascotaService.guardar(mascota);
        return ResponseEntity.ok(mascotaActualizada);
    }

    /**
     * Eliminar mascota
     * DELETE /api/mascotas/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Mascota mascota = mascotaService.obtenerPorId(id);
        if (mascota == null) {
            return ResponseEntity.notFound().build();
        }
        mascotaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtener historial de órdenes de una mascota
     * GET /api/mascotas/{id}/ordenes
     */
    @GetMapping("/{id}/ordenes")
    public ResponseEntity<?> obtenerOrdenesDeMascota(@PathVariable Long id) {
        Mascota mascota = mascotaService.obtenerPorId(id);
        if (mascota == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mascota.getOrdenes());
    }
}