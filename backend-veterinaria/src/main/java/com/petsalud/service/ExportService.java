package com.petsalud.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.petsalud.model.Dueno;
import com.petsalud.model.Mascota;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Servicio para exportación de datos a Excel, PDF y JSON
 */
@Service
@SuppressWarnings({"deprecation", "unchecked"})
public class ExportService {

    private final ObjectMapper objectMapper;

    public ExportService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    // =====================================================
    // EXPORTACIÓN A EXCEL
    // =====================================================

    /**
     * Exportar dueños a Excel
     */
    public byte[] exportarDuenosExcel(List<Dueno> duenos) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Dueños");

            // Estilo para encabezados
            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // Crear encabezados
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "DNI", "Nombres", "Apellidos", "Teléfono", "Email", "Dirección"};
            
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Llenar datos
            int rowNum = 1;
            for (Dueno dueno : duenos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(dueno.getIdDueno());
                row.createCell(1).setCellValue(dueno.getDni());
                row.createCell(2).setCellValue(dueno.getNombres());
                row.createCell(3).setCellValue(dueno.getApellidos());
                row.createCell(4).setCellValue(dueno.getTelefono());
                row.createCell(5).setCellValue(dueno.getEmail() != null ? dueno.getEmail() : "");
                row.createCell(6).setCellValue(dueno.getDireccion() != null ? dueno.getDireccion() : "");
            }

            // Ajustar ancho de columnas
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    /**
     * Exportar mascotas a Excel
     */
    public byte[] exportarMascotasExcel(List<Mascota> mascotas) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Mascotas");

            // Estilo para encabezados
            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // Crear encabezados
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Nombre", "Especie", "Raza", "Edad", "Sexo", "Peso (kg)", "Color", "Dueño"};
            
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            // Llenar datos
            int rowNum = 1;
            for (Mascota mascota : mascotas) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(mascota.getIdMascota());
                row.createCell(1).setCellValue(mascota.getNombre());
                row.createCell(2).setCellValue(mascota.getEspecie());
                row.createCell(3).setCellValue(mascota.getRaza() != null ? mascota.getRaza() : "");
                row.createCell(4).setCellValue(mascota.getEdad() != null ? mascota.getEdad() : 0);
                row.createCell(5).setCellValue(mascota.getSexo() != null ? mascota.getSexo() : "");
                row.createCell(6).setCellValue(mascota.getPeso() != null ? mascota.getPeso() : 0.0);
                row.createCell(7).setCellValue(mascota.getColor() != null ? mascota.getColor() : "");
                row.createCell(8).setCellValue(mascota.getDueno() != null ? 
                    mascota.getDueno().getNombres() + " " + mascota.getDueno().getApellidos() : "");
            }

            // Ajustar ancho de columnas
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }

    // =====================================================
    // EXPORTACIÓN A PDF
    // =====================================================

    /**
     * Exportar dueños a PDF
     */
    public byte[] exportarDuenosPDF(List<Dueno> duenos) throws DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Título
            com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("LISTADO DE DUEÑOS - VETERINARIA PETSALUD", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Fecha de generación
            com.itextpdf.text.Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            Paragraph date = new Paragraph("Generado: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), dateFont);
            date.setAlignment(Element.ALIGN_RIGHT);
            date.setSpacingAfter(20);
            document.add(date);

            // Tabla
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Encabezados
            com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            String[] headers = {"ID", "DNI", "Nombres", "Apellidos", "Teléfono", "Email", "Dirección"};
            
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(BaseColor.DARK_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);
            }

            // Datos
            com.itextpdf.text.Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
            for (Dueno dueno : duenos) {
                table.addCell(new Phrase(String.valueOf(dueno.getIdDueno()), dataFont));
                table.addCell(new Phrase(dueno.getDni(), dataFont));
                table.addCell(new Phrase(dueno.getNombres(), dataFont));
                table.addCell(new Phrase(dueno.getApellidos(), dataFont));
                table.addCell(new Phrase(dueno.getTelefono(), dataFont));
                table.addCell(new Phrase(dueno.getEmail() != null ? dueno.getEmail() : "", dataFont));
                table.addCell(new Phrase(dueno.getDireccion() != null ? dueno.getDireccion() : "", dataFont));
            }

            document.add(table);

            // Footer
            Paragraph footer = new Paragraph("Total de dueños: " + duenos.size(), dateFont);
            footer.setAlignment(Element.ALIGN_RIGHT);
            footer.setSpacingBefore(20);
            document.add(footer);

        } finally {
            document.close();
        }

        return out.toByteArray();
    }

    /**
     * Exportar mascotas a PDF
     */
    public byte[] exportarMascotasPDF(List<Mascota> mascotas) throws DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Título
            com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("LISTADO DE MASCOTAS - VETERINARIA PETSALUD", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Fecha de generación
            com.itextpdf.text.Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            Paragraph date = new Paragraph("Generado: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), dateFont);
            date.setAlignment(Element.ALIGN_RIGHT);
            date.setSpacingAfter(20);
            document.add(date);

            // Tabla
            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            // Encabezados
            com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.WHITE);
            String[] headers = {"ID", "Nombre", "Especie", "Raza", "Edad", "Sexo", "Peso", "Color", "Dueño"};
            
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(BaseColor.DARK_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(5);
                table.addCell(cell);
            }

            // Datos
            com.itextpdf.text.Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 8);
            for (Mascota mascota : mascotas) {
                table.addCell(new Phrase(String.valueOf(mascota.getIdMascota()), dataFont));
                table.addCell(new Phrase(mascota.getNombre(), dataFont));
                table.addCell(new Phrase(mascota.getEspecie(), dataFont));
                table.addCell(new Phrase(mascota.getRaza() != null ? mascota.getRaza() : "", dataFont));
                table.addCell(new Phrase(mascota.getEdad() != null ? String.valueOf(mascota.getEdad()) : "0", dataFont));
                table.addCell(new Phrase(mascota.getSexo() != null ? mascota.getSexo() : "", dataFont));
                table.addCell(new Phrase(mascota.getPeso() != null ? String.valueOf(mascota.getPeso()) : "0", dataFont));
                table.addCell(new Phrase(mascota.getColor() != null ? mascota.getColor() : "", dataFont));
                table.addCell(new Phrase(mascota.getDueno() != null ? 
                    mascota.getDueno().getNombres() + " " + mascota.getDueno().getApellidos() : "", dataFont));
            }

            document.add(table);

            // Footer
            Paragraph footer = new Paragraph("Total de mascotas: " + mascotas.size(), dateFont);
            footer.setAlignment(Element.ALIGN_RIGHT);
            footer.setSpacingBefore(20);
            document.add(footer);

        } finally {
            document.close();
        }

        return out.toByteArray();
    }

    // =====================================================
    // EXPORTACIÓN A JSON
    // =====================================================

    /**
     * Exportar dueños a JSON
     */
    public byte[] exportarDuenosJSON(List<Dueno> duenos) throws IOException {
        if (duenos == null || duenos.isEmpty()) {
            return objectMapper.writeValueAsBytes(List.of());
        }
        return objectMapper.writeValueAsBytes(duenos);
    }

    /**
     * Exportar mascotas a JSON
     */
    public byte[] exportarMascotasJSON(List<Mascota> mascotas) throws IOException {
        if (mascotas == null || mascotas.isEmpty()) {
            return objectMapper.writeValueAsBytes(List.of());
        }
        return objectMapper.writeValueAsBytes(mascotas);
    }
}