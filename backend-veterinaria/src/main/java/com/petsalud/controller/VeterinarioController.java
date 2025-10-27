package com.petsalud.controller;

import com.petsalud.model.Veterinario;
import com.petsalud.service.VeterinarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de Veterinarios
 * Gestión del personal médico veterinario
 */
@RestController
@RequestMapping("/api/veterinarios")
@CrossOrigin(origins = "http://localhost:4200")
public class VeterinarioController {

    @Autowired
    private VeterinarioService veterinarioService;

    /**
     * Listar todos los veterinarios
     * GET /api/veterinarios
     */
    @GetMapping
    public ResponseEntity<List<Veterinario>> listarTodos() {
        List<Veterinario> veterinarios = veterinarioService.listarTodos();
        return ResponseEntity.ok(veterinarios);
    }

    /**
     * Obtener veterinario por ID
     * GET /api/veterinarios/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Veterinario> obtenerPorId(@PathVariable Long id) {
        Veterinario veterinario = veterinarioService.obtenerPorId(id);
        if (veterinario != null) {
            return ResponseEntity.ok(veterinario);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Buscar veterinarios por especialidad
     * GET /api/veterinarios/especialidad/{especialidad}
     */
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<Veterinario>> buscarPorEspecialidad(@PathVariable String especialidad) {
        List<Veterinario> veterinarios = veterinarioService.buscarPorEspecialidad(especialidad);
        return ResponseEntity.ok(veterinarios);
    }

    /**
     * Buscar veterinarios por nombre
     * GET /api/veterinarios/buscar?nombre={nombre}
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Veterinario>> buscarPorNombre(@RequestParam String nombre) {
        List<Veterinario> veterinarios = veterinarioService.buscarPorNombre(nombre);
        return ResponseEntity.ok(veterinarios);
    }

    /**
     * Crear nuevo veterinario
     * POST /api/veterinarios
     */
    @PostMapping
    public ResponseEntity<Veterinario> crear(@RequestBody Veterinario veterinario) {
        Veterinario nuevoVeterinario = veterinarioService.guardar(veterinario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoVeterinario);
    }

    /**
     * Actualizar veterinario existente
     * PUT /api/veterinarios/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Veterinario> actualizar(@PathVariable Long id, @RequestBody Veterinario veterinario) {
        Veterinario veterinarioExistente = veterinarioService.obtenerPorId(id);
        if (veterinarioExistente == null) {
            return ResponseEntity.notFound().build();
        }
        veterinario.setIdVeterinario(id);
        Veterinario veterinarioActualizado = veterinarioService.guardar(veterinario);
        return ResponseEntity.ok(veterinarioActualizado);
    }

    /**
     * Eliminar veterinario
     * DELETE /api/veterinarios/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Veterinario veterinario = veterinarioService.obtenerPorId(id);
        if (veterinario == null) {
            return ResponseEntity.notFound().build();
        }
        veterinarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtener órdenes emitidas por un veterinario
     * GET /api/veterinarios/{id}/ordenes
     */
    @GetMapping("/{id}/ordenes")
    public ResponseEntity<?> obtenerOrdenesDeVeterinario(@PathVariable Long id) {
        Veterinario veterinario = veterinarioService.obtenerPorId(id);
        if (veterinario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(veterinario.getOrdenes());
    }

    /**
     * Obtener veterinarios disponibles
     * GET /api/veterinarios/disponibles
     */
    @GetMapping("/disponibles")
    public ResponseEntity<List<Veterinario>> obtenerDisponibles() {
        List<Veterinario> veterinarios = veterinarioService.listarTodos();
        return ResponseEntity.ok(veterinarios);
    }
}