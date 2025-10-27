package com.petsalud.controller;

import com.petsalud.model.TomaMuestraVet;
import com.petsalud.service.TomaMuestraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para gestión de Toma de Muestras
 * RF-03: Programación de toma de muestra
 * RF-04: Registro de toma de muestra
 */
@RestController
@RequestMapping("/api/toma-muestras")
@CrossOrigin(origins = "http://localhost:4200")
public class TomaMuestraController {

    @Autowired
    private TomaMuestraService tomaMuestraService;

    /**
     * Listar todas las tomas de muestra
     * GET /api/toma-muestras
     */
    @GetMapping
    public ResponseEntity<List<TomaMuestraVet>> listarTodas() {
        List<TomaMuestraVet> tomas = tomaMuestraService.listarTodas();
        return ResponseEntity.ok(tomas);
    }

    /**
     * Obtener toma de muestra por ID
     * GET /api/toma-muestras/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TomaMuestraVet> obtenerPorId(@PathVariable Long id) {
        TomaMuestraVet toma = tomaMuestraService.obtenerPorId(id);
        if (toma != null) {
            return ResponseEntity.ok(toma);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Buscar tomas de muestra por orden
     * GET /api/toma-muestras/orden/{idOrden}
     */
    @GetMapping("/orden/{idOrden}")
    public ResponseEntity<TomaMuestraVet> buscarPorOrden(@PathVariable Long idOrden) {
        TomaMuestraVet toma = tomaMuestraService.buscarPorOrden(idOrden);
        if (toma != null) {
            return ResponseEntity.ok(toma);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Buscar tomas por técnico
     * GET /api/toma-muestras/tecnico/{idTecnico}
     */
    @GetMapping("/tecnico/{idTecnico}")
    public ResponseEntity<List<TomaMuestraVet>> buscarPorTecnico(@PathVariable Long idTecnico) {
        List<TomaMuestraVet> tomas = tomaMuestraService.buscarPorTecnico(idTecnico);
        return ResponseEntity.ok(tomas);
    }

    /**
     * Buscar tomas por fecha
     * GET /api/toma-muestras/fecha?fecha=2025-01-15T10:30:00
     */
    @GetMapping("/fecha")
    public ResponseEntity<List<TomaMuestraVet>> buscarPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha) {
        List<TomaMuestraVet> tomas = tomaMuestraService.buscarPorFecha(fecha);
        return ResponseEntity.ok(tomas);
    }

    /**
     * Buscar tomas por tipo de muestra
     * GET /api/toma-muestras/tipo/{tipo}
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<TomaMuestraVet>> buscarPorTipoMuestra(@PathVariable String tipo) {
        List<TomaMuestraVet> tomas = tomaMuestraService.buscarPorTipoMuestra(tipo);
        return ResponseEntity.ok(tomas);
    }

    /**
     * Crear nueva toma de muestra (Programar o Registrar)
     * POST /api/toma-muestras
     */
    @PostMapping
    public ResponseEntity<TomaMuestraVet> crear(@RequestBody TomaMuestraVet tomaMuestra) {
        TomaMuestraVet nuevaToma = tomaMuestraService.guardar(tomaMuestra);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaToma);
    }

    /**
     * Actualizar toma de muestra
     * PUT /api/toma-muestras/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TomaMuestraVet> actualizar(@PathVariable Long id, @RequestBody TomaMuestraVet tomaMuestra) {
        TomaMuestraVet tomaExistente = tomaMuestraService.obtenerPorId(id);
        if (tomaExistente == null) {
            return ResponseEntity.notFound().build();
        }
        tomaMuestra.setIdToma(id);
        TomaMuestraVet tomaActualizada = tomaMuestraService.guardar(tomaMuestra);
        return ResponseEntity.ok(tomaActualizada);
    }

    /**
     * Eliminar toma de muestra
     * DELETE /api/toma-muestras/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        TomaMuestraVet toma = tomaMuestraService.obtenerPorId(id);
        if (toma == null) {
            return ResponseEntity.notFound().build();
        }
        tomaMuestraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtener tomas pendientes de procesamiento
     * GET /api/toma-muestras/pendientes
     */
    @GetMapping("/pendientes")
    public ResponseEntity<List<TomaMuestraVet>> obtenerPendientes() {
        List<TomaMuestraVet> tomas = tomaMuestraService.obtenerTomasPendientes();
        return ResponseEntity.ok(tomas);
    }
}