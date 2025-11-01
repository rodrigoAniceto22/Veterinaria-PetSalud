package com.petsalud.controller;

import com.petsalud.model.Pago;
import com.petsalud.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para gestión de Pagos
 * Sistema de pagos y cobros con internamientos
 */
@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "http://localhost:4200")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    /**
     * Listar todos los pagos
     * GET /api/pagos
     */
    @GetMapping
    public ResponseEntity<List<Pago>> listarTodos() {
        return ResponseEntity.ok(pagoService.listarTodos());
    }

    /**
     * Obtener pago por ID
     * GET /api/pagos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPorId(@PathVariable Long id) {
        Pago pago = pagoService.obtenerPorId(id);
        if (pago != null) {
            return ResponseEntity.ok(pago);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Buscar por número de pago
     * GET /api/pagos/numero/{numeroPago}
     */
    @GetMapping("/numero/{numeroPago}")
    public ResponseEntity<Pago> buscarPorNumero(@PathVariable String numeroPago) {
        Pago pago = pagoService.buscarPorNumeroPago(numeroPago);
        if (pago != null) {
            return ResponseEntity.ok(pago);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Buscar pagos por dueño
     * GET /api/pagos/dueno/{idDueno}
     */
    @GetMapping("/dueno/{idDueno}")
    public ResponseEntity<List<Pago>> buscarPorDueno(@PathVariable Long idDueno) {
        return ResponseEntity.ok(pagoService.buscarPorDueno(idDueno));
    }

    /**
     * Buscar pagos por mascota
     * GET /api/pagos/mascota/{idMascota}
     */
    @GetMapping("/mascota/{idMascota}")
    public ResponseEntity<List<Pago>> buscarPorMascota(@PathVariable Long idMascota) {
        return ResponseEntity.ok(pagoService.buscarPorMascota(idMascota));
    }

    /**
     * Obtener pagos pendientes
     * GET /api/pagos/pendientes
     */
    @GetMapping("/pendientes")
    public ResponseEntity<List<Pago>> obtenerPendientes() {
        return ResponseEntity.ok(pagoService.obtenerPagosPendientes());
    }

    /**
     * Obtener pagos vencidos
     * GET /api/pagos/vencidos
     */
    @GetMapping("/vencidos")
    public ResponseEntity<List<Pago>> obtenerVencidos() {
        return ResponseEntity.ok(pagoService.obtenerPagosVencidos());
    }

    /**
     * Obtener internamientos activos
     * GET /api/pagos/internamientos/activos
     */
    @GetMapping("/internamientos/activos")
    public ResponseEntity<List<Pago>> obtenerInternamientosActivos() {
        return ResponseEntity.ok(pagoService.obtenerInternamientosActivos());
    }

    /**
     * Crear nuevo pago
     * POST /api/pagos
     */
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Pago pago) {
        try {
            Pago nuevo = pagoService.guardar(pago);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Actualizar pago
     * PUT /api/pagos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Pago pago) {
        try {
            Pago existente = pagoService.obtenerPorId(id);
            if (existente == null) {
                return ResponseEntity.notFound().build();
            }
            pago.setIdPago(id);
            Pago actualizado = pagoService.guardar(pago);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Registrar pago
     * POST /api/pagos/{id}/registrar-pago
     */
    @PostMapping("/{id}/registrar-pago")
    public ResponseEntity<?> registrarPago(
            @PathVariable Long id,
            @RequestParam Double montoPagado,
            @RequestParam String metodoPago) {
        try {
            Pago pago = pagoService.registrarPago(id, montoPagado, metodoPago);
            return ResponseEntity.ok(pago);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Finalizar internamiento
     * POST /api/pagos/{id}/finalizar-internamiento
     */
    @PostMapping("/{id}/finalizar-internamiento")
    public ResponseEntity<?> finalizarInternamiento(@PathVariable Long id) {
        try {
            Pago pago = pagoService.finalizarInternamiento(id);
            return ResponseEntity.ok(pago);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Eliminar pago
     * DELETE /api/pagos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Pago pago = pagoService.obtenerPorId(id);
        if (pago == null) {
            return ResponseEntity.notFound().build();
        }
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Calcular total pendiente por dueño
     * GET /api/pagos/dueno/{idDueno}/total-pendiente
     */
    @GetMapping("/dueno/{idDueno}/total-pendiente")
    public ResponseEntity<Double> calcularTotalPendiente(@PathVariable Long idDueno) {
        return ResponseEntity.ok(pagoService.calcularTotalPendientePorDueno(idDueno));
    }

    /**
     * Calcular total cobrado en período
     * GET /api/pagos/estadisticas/total-cobrado?inicio=2025-01-01&fin=2025-01-31
     */
    @GetMapping("/estadisticas/total-cobrado")
    public ResponseEntity<Double> calcularTotalCobrado(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(pagoService.calcularTotalCobrado(inicio, fin));
    }
}