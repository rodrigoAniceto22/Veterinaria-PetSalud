package com.petsalud.controller;

import com.petsalud.model.Inventario;
import com.petsalud.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de Inventario
 * Control de stock y precios
 */
@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "http://localhost:4200")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    /**
     * Listar todos los productos
     * GET /api/inventario
     */
    @GetMapping
    public ResponseEntity<List<Inventario>> listarTodos() {
        return ResponseEntity.ok(inventarioService.listarTodos());
    }

    /**
     * Listar productos activos
     * GET /api/inventario/activos
     */
    @GetMapping("/activos")
    public ResponseEntity<List<Inventario>> listarActivos() {
        return ResponseEntity.ok(inventarioService.listarActivos());
    }

    /**
     * Obtener producto por ID
     * GET /api/inventario/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Inventario> obtenerPorId(@PathVariable Long id) {
        Inventario producto = inventarioService.obtenerPorId(id);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Buscar por código
     * GET /api/inventario/codigo/{codigo}
     */
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Inventario> buscarPorCodigo(@PathVariable String codigo) {
        Inventario producto = inventarioService.buscarPorCodigo(codigo);
        if (producto != null) {
            return ResponseEntity.ok(producto);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Buscar por nombre
     * GET /api/inventario/buscar?nombre={nombre}
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Inventario>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(inventarioService.buscarPorNombre(nombre));
    }

    /**
     * Buscar por categoría
     * GET /api/inventario/categoria/{categoria}
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Inventario>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(inventarioService.buscarPorCategoria(categoria));
    }

    /**
     * Obtener productos con stock bajo
     * GET /api/inventario/alertas/stock-bajo
     */
    @GetMapping("/alertas/stock-bajo")
    public ResponseEntity<List<Inventario>> obtenerStockBajo() {
        return ResponseEntity.ok(inventarioService.obtenerProductosConStockBajo());
    }

    /**
     * Obtener productos vencidos
     * GET /api/inventario/alertas/vencidos
     */
    @GetMapping("/alertas/vencidos")
    public ResponseEntity<List<Inventario>> obtenerVencidos() {
        return ResponseEntity.ok(inventarioService.obtenerProductosVencidos());
    }

    /**
     * Obtener productos próximos a vencer
     * GET /api/inventario/alertas/proximos-vencer?dias=30
     */
    @GetMapping("/alertas/proximos-vencer")
    public ResponseEntity<List<Inventario>> obtenerProximosAVencer(@RequestParam(defaultValue = "30") int dias) {
        return ResponseEntity.ok(inventarioService.obtenerProductosProximosAVencer(dias));
    }

    /**
     * Obtener todas las alertas de inventario
     * GET /api/inventario/alertas
     */
    @GetMapping("/alertas")
    public ResponseEntity<InventarioService.InventarioAlertas> obtenerAlertas() {
        return ResponseEntity.ok(inventarioService.obtenerAlertas());
    }

    /**
     * Crear nuevo producto
     * POST /api/inventario
     */
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Inventario inventario) {
        try {
            Inventario nuevo = inventarioService.guardar(inventario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Actualizar producto
     * PUT /api/inventario/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Inventario inventario) {
        try {
            Inventario existente = inventarioService.obtenerPorId(id);
            if (existente == null) {
                return ResponseEntity.notFound().build();
            }
            inventario.setIdInventario(id);
            Inventario actualizado = inventarioService.guardar(inventario);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Actualizar stock
     * PATCH /api/inventario/{id}/stock?cantidad=10&operacion=AGREGAR
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> actualizarStock(
            @PathVariable Long id,
            @RequestParam Integer cantidad,
            @RequestParam String operacion) {
        try {
            Inventario actualizado = inventarioService.actualizarStock(id, cantidad, operacion);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Eliminar producto (desactivar)
     * DELETE /api/inventario/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Inventario producto = inventarioService.obtenerPorId(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Calcular valor total del inventario
     * GET /api/inventario/estadisticas/valor-total
     */
    @GetMapping("/estadisticas/valor-total")
    public ResponseEntity<Double> calcularValorTotal() {
        return ResponseEntity.ok(inventarioService.calcularValorTotal());
    }

    /**
     * Contar productos activos
     * GET /api/inventario/estadisticas/total-activos
     */
    @GetMapping("/estadisticas/total-activos")
    public ResponseEntity<Long> contarActivos() {
        return ResponseEntity.ok(inventarioService.contarProductosActivos());
    }
}