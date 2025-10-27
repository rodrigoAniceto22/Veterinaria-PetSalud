package com.petsalud.controller;

import com.petsalud.model.ResultadoVeterinario;
import com.petsalud.service.ResultadoService;
import com.petsalud.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestión de Resultados Veterinarios
 * RF-05: Gestión de análisis
 * RF-06: Validación de resultados
 * RF-07: Generación de informe veterinario
 * RF-08: Entrega de resultados al dueño
 * RF-09: Consulta de resultados en línea
 */
@RestController
@RequestMapping("/api/resultados")
@CrossOrigin(origins = "http://localhost:4200")
public class ResultadoController {

    @Autowired
    private ResultadoService resultadoService;

    @Autowired
    private PdfService pdfService;

    /**
     * Listar todos los resultados
     * GET /api/resultados
     */
    @GetMapping
    public ResponseEntity<List<ResultadoVeterinario>> listarTodos() {
        List<ResultadoVeterinario> resultados = resultadoService.listarTodos();
        return ResponseEntity.ok(resultados);
    }

    /**
     * Obtener resultado por ID
     * GET /api/resultados/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResultadoVeterinario> obtenerPorId(@PathVariable Long id) {
        ResultadoVeterinario resultado = resultadoService.obtenerPorId(id);
        if (resultado != null) {
            return ResponseEntity.ok(resultado);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Buscar resultado por orden
     * GET /api/resultados/orden/{idOrden}
     */
    @GetMapping("/orden/{idOrden}")
    public ResponseEntity<List<ResultadoVeterinario>> buscarPorOrden(@PathVariable Long idOrden) {
        List<ResultadoVeterinario> resultados = resultadoService.buscarPorOrden(idOrden);
        return ResponseEntity.ok(resultados);
    }

    /**
     * Obtener resultados validados
     * GET /api/resultados/validados
     */
    @GetMapping("/validados")
    public ResponseEntity<List<ResultadoVeterinario>> obtenerValidados() {
        List<ResultadoVeterinario> resultados = resultadoService.obtenerResultadosValidados();
        return ResponseEntity.ok(resultados);
    }

    /**
     * Obtener resultados pendientes de validación
     * GET /api/resultados/pendientes
     */
    @GetMapping("/pendientes")
    public ResponseEntity<List<ResultadoVeterinario>> obtenerPendientes() {
        List<ResultadoVeterinario> resultados = resultadoService.obtenerResultadosPendientes();
        return ResponseEntity.ok(resultados);
    }

    /**
     * Crear nuevo resultado (Técnico registra resultado preliminar)
     * POST /api/resultados
     * RF-05: Gestión de análisis
     */
    @PostMapping
    public ResponseEntity<ResultadoVeterinario> crear(@RequestBody ResultadoVeterinario resultado) {
        resultado.setValidado(false); // Inicialmente no validado
        ResultadoVeterinario nuevoResultado = resultadoService.guardar(resultado);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoResultado);
    }

    /**
     * Actualizar resultado existente
     * PUT /api/resultados/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResultadoVeterinario> actualizar(@PathVariable Long id, @RequestBody ResultadoVeterinario resultado) {
        ResultadoVeterinario resultadoExistente = resultadoService.obtenerPorId(id);
        if (resultadoExistente == null) {
            return ResponseEntity.notFound().build();
        }
        resultado.setIdResultado(id);
        ResultadoVeterinario resultadoActualizado = resultadoService.guardar(resultado);
        return ResponseEntity.ok(resultadoActualizado);
    }

    /**
     * Validar resultado (Veterinario valida)
     * PATCH /api/resultados/{id}/validar
     * RF-06: Validación de resultados
     */
    @PatchMapping("/{id}/validar")
    public ResponseEntity<ResultadoVeterinario> validarResultado(@PathVariable Long id) {
        ResultadoVeterinario resultado = resultadoService.validarResultado(id);
        if (resultado != null) {
            return ResponseEntity.ok(resultado);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Eliminar resultado
     * DELETE /api/resultados/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ResultadoVeterinario resultado = resultadoService.obtenerPorId(id);
        if (resultado == null) {
            return ResponseEntity.notFound().build();
        }
        resultadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Generar y descargar PDF del resultado
     * GET /api/resultados/{id}/pdf
     * RF-07: Generación de informe veterinario
     */
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generarPdf(@PathVariable Long id) {
        try {
            ResultadoVeterinario resultado = resultadoService.obtenerPorId(id);
            if (resultado == null) {
                return ResponseEntity.notFound().build();
            }

            byte[] pdfBytes = pdfService.generarInformeResultado(resultado);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "resultado_" + id + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Marcar resultado como entregado
     * PATCH /api/resultados/{id}/entregar
     * RF-08: Entrega de resultados al dueño
     */
    @PatchMapping("/{id}/entregar")
    public ResponseEntity<ResultadoVeterinario> marcarComoEntregado(@PathVariable Long id) {
        ResultadoVeterinario resultado = resultadoService.marcarComoEntregado(id);
        if (resultado != null) {
            return ResponseEntity.ok(resultado);
        }
        return ResponseEntity.notFound().build();
    }
}