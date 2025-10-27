package com.petsalud.controller;

import com.petsalud.model.TecnicoVeterinario;
import com.petsalud.service.TecnicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de Técnicos Veterinarios
 * Personal de laboratorio y toma de muestras
 */
@RestController
@RequestMapping("/api/tecnicos")
@CrossOrigin(origins = "http://localhost:4200")
public class TecnicoController {

    @Autowired
    private TecnicoService tecnicoService;

    /**
     * Listar todos los técnicos
     * GET /api/tecnicos
     */
    @GetMapping
    public ResponseEntity<List<TecnicoVeterinario>> listarTodos() {
        List<TecnicoVeterinario> tecnicos = tecnicoService.listarTodos();
        return ResponseEntity.ok(tecnicos);
    }

    /**
     * Obtener técnico por ID
     * GET /api/tecnicos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TecnicoVeterinario> obtenerPorId(@PathVariable Long id) {
        TecnicoVeterinario tecnico = tecnicoService.obtenerPorId(id);
        if (tecnico != null) {
            return ResponseEntity.ok(tecnico);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Buscar técnicos por especialidad
     * GET /api/tecnicos/especialidad/{especialidad}
     */
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<List<TecnicoVeterinario>> buscarPorEspecialidad(@PathVariable String especialidad) {
        List<TecnicoVeterinario> tecnicos = tecnicoService.buscarPorEspecialidad(especialidad);
        return ResponseEntity.ok(tecnicos);
    }

    /**
     * Buscar técnicos por nombre
     * GET /api/tecnicos/buscar?nombre={nombre}
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<TecnicoVeterinario>> buscarPorNombre(@RequestParam String nombre) {
        List<TecnicoVeterinario> tecnicos = tecnicoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(tecnicos);
    }

    /**
     * Crear nuevo técnico
     * POST /api/tecnicos
     */
    @PostMapping
    public ResponseEntity<TecnicoVeterinario> crear(@RequestBody TecnicoVeterinario tecnico) {
        TecnicoVeterinario nuevoTecnico = tecnicoService.guardar(tecnico);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTecnico);
    }

    /**
     * Actualizar técnico existente
     * PUT /api/tecnicos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TecnicoVeterinario> actualizar(@PathVariable Long id, @RequestBody TecnicoVeterinario tecnico) {
        TecnicoVeterinario tecnicoExistente = tecnicoService.obtenerPorId(id);
        if (tecnicoExistente == null) {
            return ResponseEntity.notFound().build();
        }
        tecnico.setIdTecnico(id);
        TecnicoVeterinario tecnicoActualizado = tecnicoService.guardar(tecnico);
        return ResponseEntity.ok(tecnicoActualizado);
    }

    /**
     * Eliminar técnico
     * DELETE /api/tecnicos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        TecnicoVeterinario tecnico = tecnicoService.obtenerPorId(id);
        if (tecnico == null) {
            return ResponseEntity.notFound().build();
        }
        tecnicoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtener tomas de muestra realizadas por un técnico
     * GET /api/tecnicos/{id}/tomas-muestra
     */
    @GetMapping("/{id}/tomas-muestra")
    public ResponseEntity<?> obtenerTomasDeTecnico(@PathVariable Long id) {
        TecnicoVeterinario tecnico = tecnicoService.obtenerPorId(id);
        if (tecnico == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tecnico.getTomasMuestra());
    }

    /**
     * Obtener técnicos disponibles para toma de muestra
     * GET /api/tecnicos/disponibles
     */
    @GetMapping("/disponibles")
    public ResponseEntity<List<TecnicoVeterinario>> obtenerDisponibles() {
        List<TecnicoVeterinario> tecnicos = tecnicoService.listarTodos();
        return ResponseEntity.ok(tecnicos);
    }
}