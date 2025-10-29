package com.petsalud.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilidades para generación de códigos QR
 * Útil para trazabilidad de muestras y resultados
 * Sistema de Gestión Veterinaria PetSalud
 */
public class QRCodeUtil {

    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;
    private static final String DEFAULT_FORMAT = "PNG";

    /**
     * Generar código QR como imagen BufferedImage
     */
    public static BufferedImage generarQRCode(String texto, int ancho, int alto) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);
        
        BitMatrix bitMatrix = qrCodeWriter.encode(texto, BarcodeFormat.QR_CODE, ancho, alto, hints);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    /**
     * Generar código QR con dimensiones por defecto
     */
    public static BufferedImage generarQRCode(String texto) throws WriterException {
        return generarQRCode(texto, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Generar código QR y guardarlo en archivo
     */
    public static void generarQRCodeArchivo(String texto, String rutaArchivo, int ancho, int alto) 
            throws WriterException, IOException {
        BufferedImage qrImage = generarQRCode(texto, ancho, alto);
        File outputFile = new File(rutaArchivo);
        ImageIO.write(qrImage, DEFAULT_FORMAT, outputFile);
    }

    /**
     * Generar código QR y guardarlo en archivo con dimensiones por defecto
     */
    public static void generarQRCodeArchivo(String texto, String rutaArchivo) 
            throws WriterException, IOException {
        generarQRCodeArchivo(texto, rutaArchivo, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Generar código QR como array de bytes
     */
    public static byte[] generarQRCodeBytes(String texto, int ancho, int alto) 
            throws WriterException, IOException {
        BufferedImage qrImage = generarQRCode(texto, ancho, alto);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrImage, DEFAULT_FORMAT, baos);
        return baos.toByteArray();
    }

    /**
     * Generar código QR como array de bytes con dimensiones por defecto
     */
    public static byte[] generarQRCodeBytes(String texto) throws WriterException, IOException {
        return generarQRCodeBytes(texto, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Generar código QR para muestra veterinaria
     */
    public static BufferedImage generarQRParaMuestra(String codigoMuestra, Long idOrden, String tipoMuestra) 
            throws WriterException {
        String contenido = String.format(
            "PETSALUD-MUESTRA\nCódigo: %s\nOrden: %d\nTipo: %s\nFecha: %s",
            codigoMuestra,
            idOrden,
            tipoMuestra,
            DateUtil.formatearFechaHora(DateUtil.obtenerFechaHoraActual())
        );
        return generarQRCode(contenido);
    }

    /**
     * Generar código QR para resultado veterinario
     */
    public static BufferedImage generarQRParaResultado(Long idResultado, Long idOrden, String mascota) 
            throws WriterException {
        String contenido = String.format(
            "PETSALUD-RESULTADO\nID: %d\nOrden: %d\nMascota: %s\nFecha: %s\nURL: https://petsalud.com/resultados/%d",
            idResultado,
            idOrden,
            mascota,
            DateUtil.formatearFecha(DateUtil.obtenerFechaActual()),
            idResultado
        );
        return generarQRCode(contenido);
    }

    /**
     * Generar código QR para factura
     */
    public static BufferedImage generarQRParaFactura(String numeroFactura, Double total, String dueno) 
            throws WriterException {
        String contenido = String.format(
            "PETSALUD-FACTURA\nNúmero: %s\nTotal: S/. %.2f\nCliente: %s\nFecha: %s",
            numeroFactura,
            total,
            dueno,
            DateUtil.formatearFecha(DateUtil.obtenerFechaActual())
        );
        return generarQRCode(contenido);
    }

    /**
     * Generar URL para consulta de resultados
     */
    public static String generarURLResultado(Long idResultado) {
        return String.format("https://petsalud.com/resultados/%d", idResultado);
    }

    /**
     * Generar código QR con URL de consulta
     */
    public static BufferedImage generarQRConURL(String url) throws WriterException {
        return generarQRCode(url);
    }

    /**
     * Validar que el texto no exceda el tamaño máximo para QR
     */
    public static boolean validarTamanoTexto(String texto) {
        // QR Code puede almacenar hasta 4,296 caracteres alfanuméricos
        return texto != null && texto.length() <= 4296;
    }

    /**
     * Generar código QR para identificación de mascota
     */
    public static BufferedImage generarQRParaMascota(Long idMascota, String nombre, String especie, String dueno) 
            throws WriterException {
        String contenido = String.format(
            "PETSALUD-MASCOTA\nID: %d\nNombre: %s\nEspecie: %s\nPropietario: %s\nURL: https://petsalud.com/mascotas/%d",
            idMascota,
            nombre,
            especie,
            dueno,
            idMascota
        );
        return generarQRCode(contenido);
    }

    /**
     * Generar código QR con información de contacto veterinaria
     */
    public static BufferedImage generarQRContacto() throws WriterException {
        String contenido = "BEGIN:VCARD\n" +
                "VERSION:3.0\n" +
                "FN:Veterinaria PetSalud\n" +
                "ORG:PetSalud S.A.C.\n" +
                "TEL:(01) 234-5678\n" +
                "EMAIL:info@petsalud.com\n" +
                "URL:https://petsalud.com\n" +
                "ADR:;;Av. Principal 123;Lima;Lima;15001;Perú\n" +
                "END:VCARD";
        return generarQRCode(contenido);
    }

    /**
     * Generar código QR compacto (para etiquetas pequeñas)
     */
    public static BufferedImage generarQRCompacto(String texto) throws WriterException {
        return generarQRCode(texto, 150, 150);
    }

    /**
     * Generar código QR grande (para carteles)
     */
    public static BufferedImage generarQRGrande(String texto) throws WriterException {
        return generarQRCode(texto, 600, 600);
    }
}