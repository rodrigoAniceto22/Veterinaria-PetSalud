package com.petsalud.controller;

import com.petsalud.model.Dueno;
import com.petsalud.service.DuenoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de Dueños de mascotas
 * RF-01: Registro de dueños y mascotas
 */
@RestController
@RequestMapping("/api/duenos")
@CrossOrigin(origins = "http://localhost:4200")
public class DuenoController {

    @Autowired
    private DuenoService duenoService;

    /**
     * Listar todos los dueños
     * GET /api/duenos
     */
    @GetMapping
    public ResponseEntity<List<Dueno>> listarTodos() {
        List<Dueno> duenos = duenoService.listarTodos();
        return ResponseEntity.ok(duenos);
    }

    /**
     * Obtener dueño por ID
     * GET /api/duenos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Dueno> obtenerPorId(@PathVariable Long id) {
        Dueno dueno = duenoService.obtenerPorId(id);
        if (dueno != null) {
            return ResponseEntity.ok(dueno);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Buscar dueño por DNI
     * GET /api/duenos/dni/{dni}
     */
    @GetMapping("/dni/{dni}")
    public ResponseEntity<Dueno> buscarPorDni(@PathVariable String dni) {
        Dueno dueno = duenoService.buscarPorDni(dni);
        if (dueno != null) {
            return ResponseEntity.ok(dueno);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Buscar dueños por nombre o apellido
     * GET /api/duenos/buscar?nombre={nombre}
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Dueno>> buscarPorNombre(@RequestParam String nombre) {
        List<Dueno> duenos = duenoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(duenos);
    }

    /**
     * Crear nuevo dueño
     * POST /api/duenos
     */
    @PostMapping
    public ResponseEntity<Dueno> crear(@RequestBody Dueno dueno) {
        Dueno nuevoDueno = duenoService.guardar(dueno);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDueno);
    }

    /**
     * Actualizar dueño existente
     * PUT /api/duenos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Dueno> actualizar(@PathVariable Long id, @RequestBody Dueno dueno) {
        Dueno duenoExistente = duenoService.obtenerPorId(id);
        if (duenoExistente == null) {
            return ResponseEntity.notFound().build();
        }
        dueno.setIdDueno(id);
        Dueno duenoActualizado = duenoService.guardar(dueno);
        return ResponseEntity.ok(duenoActualizado);
    }

    /**
     * Eliminar dueño
     * DELETE /api/duenos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Dueno dueno = duenoService.obtenerPorId(id);
        if (dueno == null) {
            return ResponseEntity.notFound().build();
        }
        duenoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtener todas las mascotas de un dueño
     * GET /api/duenos/{id}/mascotas
     */
    @GetMapping("/{id}/mascotas")
    public ResponseEntity<?> obtenerMascotasDeDueno(@PathVariable Long id) {
        Dueno dueno = duenoService.obtenerPorId(id);
        if (dueno == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dueno.getMascotas());
    }
}