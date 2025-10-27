package com.petsalud.controller;

import com.petsalud.model.Factura;
import com.petsalud.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para gestión de Facturas
 * Sistema de facturación y cobros
 */
@RestController
@RequestMapping("/api/facturas")
@CrossOrigin(origins = "http://localhost:4200")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    /**
     * Listar todas las facturas
     * GET /api/facturas
     */
    @GetMapping
    public ResponseEntity<List<Factura>> listarTodas() {
        List<Factura> facturas = facturaService.listarTodas();
        return ResponseEntity.ok(facturas);
    }

    /**
     * Obtener factura por ID
     * GET /api/facturas/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Factura> obtenerPorId(@PathVariable Long id) {
        Factura factura = facturaService.obtenerPorId(id);
        if (factura != null) {
            return ResponseEntity.ok(factura);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Buscar facturas por dueño
     * GET /api/facturas/dueno/{idDueno}
     */
    @GetMapping("/dueno/{idDueno}")
    public ResponseEntity<List<Factura>> buscarPorDueno(@PathVariable Long idDueno) {
        List<Factura> facturas = facturaService.buscarPorDueno(idDueno);
        return ResponseEntity.ok(facturas);
    }

    /**
     * Buscar facturas por fecha de emisión
     * GET /api/facturas/fecha?fecha=2025-01-15
     */
    @GetMapping("/fecha")
    public ResponseEntity<List<Factura>> buscarPorFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<Factura> facturas = facturaService.buscarPorFecha(fecha);
        return ResponseEntity.ok(facturas);
    }

    /**
     * Buscar facturas por rango de fechas
     * GET /api/facturas/rango?inicio=2025-01-01&fin=2025-01-31
     */
    @GetMapping("/rango")
    public ResponseEntity<List<Factura>> buscarPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        List<Factura> facturas = facturaService.buscarPorRangoFechas(inicio, fin);
        return ResponseEntity.ok(facturas);
    }

    /**
     * Buscar facturas por método de pago
     * GET /api/facturas/metodo-pago/{metodo}
     */
    @GetMapping("/metodo-pago/{metodo}")
    public ResponseEntity<List<Factura>> buscarPorMetodoPago(@PathVariable String metodo) {
        List<Factura> facturas = facturaService.buscarPorMetodoPago(metodo);
        return ResponseEntity.ok(facturas);
    }

    /**
     * Obtener facturas pendientes de pago
     * GET /api/facturas/pendientes
     */
    @GetMapping("/pendientes")
    public ResponseEntity<List<Factura>> obtenerPendientes() {
        List<Factura> facturas = facturaService.obtenerFacturasPendientes();
        return ResponseEntity.ok(facturas);
    }

    /**
     * Crear nueva factura
     * POST /api/facturas
     */
    @PostMapping
    public ResponseEntity<Factura> crear(@RequestBody Factura factura) {
        Factura nuevaFactura = facturaService.guardar(factura);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaFactura);
    }

    /**
     * Actualizar factura existente
     * PUT /api/facturas/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Factura> actualizar(@PathVariable Long id, @RequestBody Factura factura) {
        Factura facturaExistente = facturaService.obtenerPorId(id);
        if (facturaExistente == null) {
            return ResponseEntity.notFound().build();
        }
        factura.setIdFactura(id);
        Factura facturaActualizada = facturaService.guardar(factura);
        return ResponseEntity.ok(facturaActualizada);
    }

    /**
     * Eliminar factura
     * DELETE /api/facturas/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Factura factura = facturaService.obtenerPorId(id);
        if (factura == null) {
            return ResponseEntity.notFound().build();
        }
        facturaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Calcular total de ventas del día
     * GET /api/facturas/ventas-dia?fecha=2025-01-15
     */
    @GetMapping("/ventas-dia")
    public ResponseEntity<Double> calcularVentasDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        Double total = facturaService.calcularVentasDia(fecha);
        return ResponseEntity.ok(total);
    }

    /**
     * Calcular total de ventas del mes
     * GET /api/facturas/ventas-mes?mes=1&anio=2025
     */
    @GetMapping("/ventas-mes")
    public ResponseEntity<Double> calcularVentasMes(
            @RequestParam int mes,
            @RequestParam int anio) {
        Double total = facturaService.calcularVentasMes(mes, anio);
        return ResponseEntity.ok(total);
    }

    /**
     * Marcar factura como pagada
     * PATCH /api/facturas/{id}/pagar
     */
    @PatchMapping("/{id}/pagar")
    public ResponseEntity<Factura> marcarComoPagada(@PathVariable Long id) {
        Factura factura = facturaService.marcarComoPagada(id);
        if (factura != null) {
            return ResponseEntity.ok(factura);
        }
        return ResponseEntity.notFound().build();
    }
}