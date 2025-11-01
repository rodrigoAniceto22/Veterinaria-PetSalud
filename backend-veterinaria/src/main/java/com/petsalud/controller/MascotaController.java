package com.petsalud.controller;

import com.petsalud.model.Mascota;
import com.petsalud.service.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import com.petsalud.dao.MascotaDAO;
import com.petsalud.service.ExportService;
import java.time.LocalDate;

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

    @Autowired
    private MascotaDAO mascotaDAO;

    @Autowired
    private ExportService exportService;

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

    /**
     * Registrar mascota con SP (fechaNacimiento como query param yyyy-MM-dd)
     * POST /api/mascotas/sp?fechaNacimiento=2020-05-10
     */
    @PostMapping("/sp")
    public ResponseEntity<?> crearConSP(@RequestBody Mascota mascota, @RequestParam(required = false) String fechaNacimiento) {
        try {
            LocalDate fn = (fechaNacimiento != null && !fechaNacimiento.isBlank()) ? LocalDate.parse(fechaNacimiento) : null;
            Mascota creada = mascotaDAO.registrarMascota(mascota, fn);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Actualizar mascota con SP
     * PUT /api/mascotas/{id}/sp?fechaNacimiento=2018-01-01
     */
    @PutMapping("/{id}/sp")
    public ResponseEntity<?> actualizarConSP(@PathVariable Long id, @RequestBody Mascota mascota, @RequestParam(required = false) String fechaNacimiento) {
        try {
            Mascota existente = mascotaService.obtenerPorId(id);
            if (existente == null) return ResponseEntity.notFound().build();
            mascota.setIdMascota(id);
            LocalDate fn = (fechaNacimiento != null && !fechaNacimiento.isBlank()) ? LocalDate.parse(fechaNacimiento) : null;
            Mascota actualizada = mascotaDAO.actualizarMascota(mascota, fn);
            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Buscar mascotas por dueño usando SP
     * GET /api/mascotas/dueno/{idDueno}/sp
     */
    @GetMapping("/dueno/{idDueno}/sp")
    public ResponseEntity<?> buscarPorDuenoSP(@PathVariable Long idDueno) {
        return ResponseEntity.ok(mascotaDAO.buscarMascotasPorDueno(idDueno));
    }

    /**
     * Estadísticas de mascotas usando SP
     * GET /api/mascotas/estadisticas/sp
     */
    @GetMapping("/estadisticas/sp")
    public ResponseEntity<?> estadisticasMascotasSP() {
        return ResponseEntity.ok(mascotaDAO.obtenerEstadisticasMascotas());
    }

    /**
     * Exportar mascotas a Excel
     * GET /api/mascotas/export/excel
     */
    @GetMapping(value = "/export/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> exportarExcel() {
        try {
            var mascotas = mascotaService.listarTodas();
            byte[] bytes = exportService.exportarMascotasExcel(mascotas);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=mascotas.xlsx");
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exportar mascotas a PDF
     * GET /api/mascotas/export/pdf
     */
    @GetMapping(value = "/export/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportarPDF() {
        try {
            var mascotas = mascotaService.listarTodas();
            byte[] bytes = exportService.exportarMascotasPDF(mascotas);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=mascotas.pdf");
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Exportar mascotas a JSON
     * GET /api/mascotas/export/json
     */
    @GetMapping(value = "/export/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> exportarJSON() {
        try {
            var mascotas = mascotaService.listarTodas();
            byte[] bytes = exportService.exportarMascotasJSON(mascotas);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=mascotas.json");
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}