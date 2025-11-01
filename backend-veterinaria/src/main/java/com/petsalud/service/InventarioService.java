package com.petsalud.service;

import com.petsalud.model.Inventario;
import com.petsalud.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para gestión de Inventario
 * Control de stock y precios
 */
@Service
@Transactional
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    /**
     * Listar todos los productos
     */
    public List<Inventario> listarTodos() {
        return inventarioRepository.findAll();
    }

    /**
     * Listar productos activos
     */
    public List<Inventario> listarActivos() {
        return inventarioRepository.findByActivoTrue();
    }

    /**
     * Obtener por ID
     */
    public Inventario obtenerPorId(Long id) {
        return inventarioRepository.findById(id).orElse(null);
    }

    /**
     * Buscar por código
     */
    public Inventario buscarPorCodigo(String codigo) {
        return inventarioRepository.findByCodigo(codigo);
    }

    /**
     * Buscar por nombre
     */
    public List<Inventario> buscarPorNombre(String nombre) {
        return inventarioRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Buscar por categoría
     */
    public List<Inventario> buscarPorCategoria(String categoria) {
        return inventarioRepository.findByCategoriaIgnoreCase(categoria);
    }

    /**
     * Obtener productos con stock bajo
     */
    public List<Inventario> obtenerProductosConStockBajo() {
        return inventarioRepository.findProductosConStockBajo();
    }

    /**
     * Obtener productos vencidos
     */
    public List<Inventario> obtenerProductosVencidos() {
        return inventarioRepository.findProductosVencidos(LocalDate.now());
    }

    /**
     * Obtener productos próximos a vencer (30 días)
     */
    public List<Inventario> obtenerProductosProximosAVencer(int dias) {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(dias);
        return inventarioRepository.findProductosProximosAVencer(hoy, limite);
    }

    /**
     * Guardar o actualizar producto
     */
    public Inventario guardar(Inventario inventario) {
        // Validaciones
        if (inventario.getCodigo() == null || inventario.getCodigo().trim().isEmpty()) {
            throw new RuntimeException("El código del producto es obligatorio");
        }
        if (inventario.getNombre() == null || inventario.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del producto es obligatorio");
        }
        if (inventario.getPrecioCompra() == null || inventario.getPrecioCompra() < 0) {
            throw new RuntimeException("El precio de compra debe ser mayor o igual a 0");
        }
        if (inventario.getPrecioVenta() == null || inventario.getPrecioVenta() < 0) {
            throw new RuntimeException("El precio de venta debe ser mayor o igual a 0");
        }
        if (inventario.getStockActual() == null || inventario.getStockActual() < 0) {
            throw new RuntimeException("El stock actual debe ser mayor o igual a 0");
        }

        // Verificar código duplicado
        if (inventario.getIdInventario() == null) {
            if (inventarioRepository.existsByCodigo(inventario.getCodigo())) {
                throw new RuntimeException("Ya existe un producto con el código: " + inventario.getCodigo());
            }
        }

        return inventarioRepository.save(inventario);
    }

    /**
     * Actualizar stock
     */
    public Inventario actualizarStock(Long id, Integer cantidad, String operacion) {
        Inventario producto = obtenerPorId(id);
        if (producto == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        if ("AGREGAR".equalsIgnoreCase(operacion)) {
            producto.setStockActual(producto.getStockActual() + cantidad);
        } else if ("RESTAR".equalsIgnoreCase(operacion)) {
            if (producto.getStockActual() < cantidad) {
                throw new RuntimeException("Stock insuficiente. Stock actual: " + producto.getStockActual());
            }
            producto.setStockActual(producto.getStockActual() - cantidad);
        } else {
            throw new RuntimeException("Operación no válida. Use AGREGAR o RESTAR");
        }

        return inventarioRepository.save(producto);
    }

    /**
     * Eliminar producto (desactivar)
     */
    public void eliminar(Long id) {
        Inventario producto = obtenerPorId(id);
        if (producto != null) {
            producto.setActivo(false);
            inventarioRepository.save(producto);
        }
    }

    /**
     * Calcular valor total del inventario
     */
    public Double calcularValorTotal() {
        Double valor = inventarioRepository.calcularValorTotalInventario();
        return valor != null ? valor : 0.0;
    }

    /**
     * Contar productos activos
     */
    public long contarProductosActivos() {
        return inventarioRepository.countByActivoTrue();
    }

    /**
     * Obtener alertas de inventario
     */
    public InventarioAlertas obtenerAlertas() {
        InventarioAlertas alertas = new InventarioAlertas();
        alertas.setProductosStockBajo(obtenerProductosConStockBajo());
        alertas.setProductosVencidos(obtenerProductosVencidos());
        alertas.setProductosProximosAVencer(obtenerProductosProximosAVencer(30));
        return alertas;
    }

    // Clase interna para alertas
    public static class InventarioAlertas {
        private List<Inventario> productosStockBajo;
        private List<Inventario> productosVencidos;
        private List<Inventario> productosProximosAVencer;

        public List<Inventario> getProductosStockBajo() {
            return productosStockBajo;
        }

        public void setProductosStockBajo(List<Inventario> productosStockBajo) {
            this.productosStockBajo = productosStockBajo;
        }

        public List<Inventario> getProductosVencidos() {
            return productosVencidos;
        }

        public void setProductosVencidos(List<Inventario> productosVencidos) {
            this.productosVencidos = productosVencidos;
        }

        public List<Inventario> getProductosProximosAVencer() {
            return productosProximosAVencer;
        }

        public void setProductosProximosAVencer(List<Inventario> productosProximosAVencer) {
            this.productosProximosAVencer = productosProximosAVencer;
        }
    }
}