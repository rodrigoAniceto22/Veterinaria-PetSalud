package com.petsalud.controller;

import com.petsalud.model.Dueno;
import com.petsalud.service.DuenoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

import com.petsalud.dao.DuenoDAO;
import com.petsalud.service.ExportService;

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

    @Autowired
    private DuenoDAO duenoDAO;

    @Autowired
    private ExportService exportService;

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
    // Registrar dueño con SP
@PostMapping("/sp")
public ResponseEntity<?> crearConSP(@RequestBody Dueno dueno) {
    try {
        Dueno creado = duenoDAO.registrarDueno(dueno);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

// Actualizar dueño con SP
@PutMapping("/{id}/sp")
public ResponseEntity<?> actualizarConSP(@PathVariable Long id, @RequestBody Dueno dueno) {
    try {
        Dueno existente = duenoService.obtenerPorId(id);
        if (existente == null) return ResponseEntity.notFound().build();
        dueno.setIdDueno(id);
        return ResponseEntity.ok(duenoDAO.actualizarDueno(dueno));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

// Obtener dueño con sus mascotas (SP, devuelve 2 resultsets consolidados)
@GetMapping("/{id}/sp")
public ResponseEntity<?> obtenerDuenoConMascotasSP(@PathVariable Long id) {
    var data = duenoDAO.buscarDuenoConMascotas(id);
    if (data == null || data.isEmpty()) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(data);
}

// Estadísticas de dueños (SP)
@GetMapping("/estadisticas/sp")
public ResponseEntity<?> estadisticasDuenosSP() {
    return ResponseEntity.ok(duenoDAO.obtenerEstadisticasDuenos());
}

// Export: Excel
@GetMapping(value = "/export/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
public ResponseEntity<byte[]> exportarExcel() {
    try {
        var duenos = duenoService.listarTodos();
        byte[] bytes = exportService.exportarDuenosExcel(duenos);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=duenos.xlsx");
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

// Export: PDF
@GetMapping(value = "/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
public ResponseEntity<byte[]> exportarPDF() {
    try {
        var duenos = duenoService.listarTodos();
        byte[] bytes = exportService.exportarDuenosPDF(duenos);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=duenos.pdf");
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

// Export: JSON
@GetMapping(value = "/export/json", produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<byte[]> exportarJSON() {
    try {
        var duenos = duenoService.listarTodos();
        byte[] bytes = exportService.exportarDuenosJSON(duenos);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=duenos.json");
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
}