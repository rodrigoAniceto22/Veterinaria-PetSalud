package com.petsalud.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
// AGREGADO: Import específico para LineSeparator
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.petsalud.model.ResultadoVeterinario;
import com.petsalud.model.OrdenVeterinaria;
import com.petsalud.model.Mascota;
import com.petsalud.model.Dueno;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

/**
 * Servicio para generación de documentos PDF
 * RF-07: Generación de informe veterinario
 */
@Service
public class PdfService {

    private static final Font FONT_TITLE = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font FONT_SUBTITLE = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.BLUE);
    private static final Font FONT_NORMAL = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font FONT_BOLD = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    private static final Font FONT_SMALL = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.GRAY);

    /**
     * Generar informe de resultado veterinario en PDF
     * RF-07: Generación de informe veterinario
     */
    public byte[] generarInformeResultado(ResultadoVeterinario resultado) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // Encabezado
            agregarEncabezado(document);
            document.add(Chunk.NEWLINE);

            // Título del documento
            Paragraph titulo = new Paragraph("INFORME DE RESULTADOS VETERINARIOS", FONT_TITLE);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(Chunk.NEWLINE);

            // Información de la orden
            agregarInformacionOrden(document, resultado.getOrden());
            document.add(Chunk.NEWLINE);

            // Información del paciente (mascota)
            agregarInformacionPaciente(document, resultado.getOrden().getMascota());
            document.add(Chunk.NEWLINE);

            // Resultados del análisis
            agregarResultadosAnalisis(document, resultado);
            document.add(Chunk.NEWLINE);

            // Pie de página
            agregarPieDePagina(document, resultado);

        } finally {
            document.close();
        }

        return outputStream.toByteArray();
    }

    /**
     * Agregar encabezado del documento
     */
    private void agregarEncabezado(Document document) throws DocumentException {
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setSpacingAfter(10f);

        // Logo y nombre de la veterinaria
        PdfPCell logoCell = new PdfPCell();
        logoCell.setBorder(Rectangle.NO_BORDER);
        Paragraph veterinaria = new Paragraph("VETERINARIA PETSALUD", FONT_SUBTITLE);
        veterinaria.setAlignment(Element.ALIGN_LEFT);
        logoCell.addElement(veterinaria);
        
        Paragraph direccion = new Paragraph("Av. Principal 123, Lima, Perú", FONT_SMALL);
        direccion.setAlignment(Element.ALIGN_LEFT);
        logoCell.addElement(direccion);
        
        Paragraph contacto = new Paragraph("Tel: (01) 234-5678 | Email: info@petsalud.com", FONT_SMALL);
        contacto.setAlignment(Element.ALIGN_LEFT);
        logoCell.addElement(contacto);

        // Fecha de emisión
        PdfPCell fechaCell = new PdfPCell();
        fechaCell.setBorder(Rectangle.NO_BORDER);
        fechaCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        Paragraph fecha = new Paragraph("Fecha de emisión:\n" + 
            java.time.LocalDateTime.now().format(formatter), FONT_SMALL);
        fecha.setAlignment(Element.ALIGN_RIGHT);
        fechaCell.addElement(fecha);

        headerTable.addCell(logoCell);
        headerTable.addCell(fechaCell);
        document.add(headerTable);

        // Línea separadora - CORREGIDO
        LineSeparator linea = new LineSeparator();
        linea.setLineColor(BaseColor.BLUE);
        document.add(new Chunk(linea));
    }

    /**
     * Agregar información de la orden
     */
    private void agregarInformacionOrden(Document document, OrdenVeterinaria orden) throws DocumentException {
        Paragraph subtitulo = new Paragraph("INFORMACIÓN DE LA ORDEN", FONT_SUBTITLE);
        document.add(subtitulo);
        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 2});

        agregarCelda(table, "N° de Orden:", true);
        agregarCelda(table, String.valueOf(orden.getIdOrden()), false);

        agregarCelda(table, "Fecha de Orden:", true);
        agregarCelda(table, orden.getFechaOrden().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), false);

        agregarCelda(table, "Tipo de Examen:", true);
        agregarCelda(table, orden.getTipoExamen(), false);

        agregarCelda(table, "Veterinario:", true);
        agregarCelda(table, orden.getVeterinario().getNombreCompleto(), false);

        agregarCelda(table, "Estado:", true);
        agregarCelda(table, orden.getEstado(), false);

        document.add(table);
    }

    /**
     * Agregar información del paciente (mascota)
     */
    private void agregarInformacionPaciente(Document document, Mascota mascota) throws DocumentException {
        Paragraph subtitulo = new Paragraph("INFORMACIÓN DEL PACIENTE", FONT_SUBTITLE);
        document.add(subtitulo);
        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 2});

        agregarCelda(table, "Nombre:", true);
        agregarCelda(table, mascota.getNombre(), false);

        agregarCelda(table, "Especie:", true);
        agregarCelda(table, mascota.getEspecie(), false);

        agregarCelda(table, "Raza:", true);
        agregarCelda(table, mascota.getRaza() != null ? mascota.getRaza() : "No especificada", false);

        agregarCelda(table, "Edad:", true);
        agregarCelda(table, mascota.getEdad() + " años", false);

        agregarCelda(table, "Sexo:", true);
        agregarCelda(table, mascota.getSexo(), false);

        // Información del dueño
        Dueno dueno = mascota.getDueno();
        agregarCelda(table, "Propietario:", true);
        agregarCelda(table, dueno.getNombres() + " " + dueno.getApellidos(), false);

        agregarCelda(table, "DNI:", true);
        agregarCelda(table, dueno.getDni(), false);

        agregarCelda(table, "Teléfono:", true);
        agregarCelda(table, dueno.getTelefono(), false);

        document.add(table);
    }

    /**
     * Agregar resultados del análisis
     */
    private void agregarResultadosAnalisis(Document document, ResultadoVeterinario resultado) throws DocumentException {
        Paragraph subtitulo = new Paragraph("RESULTADOS DEL ANÁLISIS", FONT_SUBTITLE);
        document.add(subtitulo);
        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        // Descripción
        PdfPCell cellDescripcion = new PdfPCell();
        cellDescripcion.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellDescripcion.addElement(new Paragraph("DESCRIPCIÓN:", FONT_BOLD));
        table.addCell(cellDescripcion);
        
        PdfPCell cellDescripcionContent = new PdfPCell();
        cellDescripcionContent.addElement(new Paragraph(resultado.getDescripcion() != null ? 
            resultado.getDescripcion() : "Sin descripción", FONT_NORMAL));
        table.addCell(cellDescripcionContent);

        // Valores
        PdfPCell cellValores = new PdfPCell();
        cellValores.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellValores.addElement(new Paragraph("VALORES OBTENIDOS:", FONT_BOLD));
        table.addCell(cellValores);
        
        PdfPCell cellValoresContent = new PdfPCell();
        cellValoresContent.addElement(new Paragraph(resultado.getValores() != null ? 
            resultado.getValores() : "Sin valores registrados", FONT_NORMAL));
        table.addCell(cellValoresContent);

        // Valores de referencia
        if (resultado.getValoresReferencia() != null) {
            PdfPCell cellReferencia = new PdfPCell();
            cellReferencia.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cellReferencia.addElement(new Paragraph("VALORES DE REFERENCIA:", FONT_BOLD));
            table.addCell(cellReferencia);
            
            PdfPCell cellReferenciaContent = new PdfPCell();
            cellReferenciaContent.addElement(new Paragraph(resultado.getValoresReferencia(), FONT_NORMAL));
            table.addCell(cellReferenciaContent);
        }

        // Conclusiones
        PdfPCell cellConclusiones = new PdfPCell();
        cellConclusiones.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cellConclusiones.addElement(new Paragraph("CONCLUSIONES:", FONT_BOLD));
        table.addCell(cellConclusiones);
        
        PdfPCell cellConclusionesContent = new PdfPCell();
        cellConclusionesContent.addElement(new Paragraph(resultado.getConclusiones() != null ? 
            resultado.getConclusiones() : "Sin conclusiones", FONT_NORMAL));
        table.addCell(cellConclusionesContent);

        // Recomendaciones
        if (resultado.getRecomendaciones() != null) {
            PdfPCell cellRecomendaciones = new PdfPCell();
            cellRecomendaciones.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cellRecomendaciones.addElement(new Paragraph("RECOMENDACIONES:", FONT_BOLD));
            table.addCell(cellRecomendaciones);
            
            PdfPCell cellRecomendacionesContent = new PdfPCell();
            cellRecomendacionesContent.addElement(new Paragraph(resultado.getRecomendaciones(), FONT_NORMAL));
            table.addCell(cellRecomendacionesContent);
        }

        document.add(table);
    }

    /**
     * Agregar pie de página
     */
    private void agregarPieDePagina(Document document, ResultadoVeterinario resultado) throws DocumentException {
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        PdfPTable footerTable = new PdfPTable(2);
        footerTable.setWidthPercentage(100);

        PdfPCell leftCell = new PdfPCell();
        leftCell.setBorder(Rectangle.NO_BORDER);
        Paragraph validacion = new Paragraph(
            "Estado: " + (resultado.getValidado() ? "VALIDADO" : "PENDIENTE DE VALIDACIÓN"), 
            FONT_SMALL
        );
        leftCell.addElement(validacion);
        
        if (resultado.getFechaValidacion() != null) {
            Paragraph fechaValidacion = new Paragraph(
                "Fecha de validación: " + resultado.getFechaValidacion().format(formatter), 
                FONT_SMALL
            );
            leftCell.addElement(fechaValidacion);
        }

        PdfPCell rightCell = new PdfPCell();
        rightCell.setBorder(Rectangle.NO_BORDER);
        rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Paragraph firma = new Paragraph("_____________________\nFirma del Veterinario", FONT_SMALL);
        firma.setAlignment(Element.ALIGN_RIGHT);
        rightCell.addElement(firma);

        footerTable.addCell(leftCell);
        footerTable.addCell(rightCell);

        document.add(footerTable);

        // Nota final
        document.add(Chunk.NEWLINE);
        Paragraph nota = new Paragraph(
            "Este documento es un informe médico veterinario. " +
            "Los resultados deben ser interpretados por un profesional veterinario.", 
            FONT_SMALL
        );
        nota.setAlignment(Element.ALIGN_CENTER);
        document.add(nota);
    }

    /**
     * Método auxiliar para agregar celdas a la tabla
     */
    private void agregarCelda(PdfPTable table, String texto, boolean esBold) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.BOX);
        cell.setPadding(5);
        
        if (esBold) {
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.addElement(new Paragraph(texto, FONT_BOLD));
        } else {
            cell.addElement(new Paragraph(texto, FONT_NORMAL));
        }
        
        table.addCell(cell);
    }
}