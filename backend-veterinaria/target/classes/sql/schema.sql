-- =====================================================
-- SCHEMA SQL - Sistema de Gestión Veterinaria PetSalud
-- Base de datos: MySQL 8.0+
-- =====================================================

-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS veterinaria_petsalud
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE veterinaria_petsalud;

-- =====================================================
-- TABLA: usuarios
-- =====================================================
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    nombres VARCHAR(100),
    apellidos VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    ultimo_acceso DATETIME,
    intentos_fallidos INT DEFAULT 0,
    bloqueado BOOLEAN DEFAULT FALSE,
    INDEX idx_usuario_rol (rol),
    INDEX idx_usuario_email (email),
    INDEX idx_usuario_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: duenos
-- =====================================================
CREATE TABLE IF NOT EXISTS duenos (
    id_dueno BIGINT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(20) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    direccion VARCHAR(200),
    INDEX idx_dueno_dni (dni),
    INDEX idx_dueno_nombres (nombres, apellidos),
    INDEX idx_dueno_telefono (telefono)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: mascotas
-- =====================================================
CREATE TABLE IF NOT EXISTS mascotas (
    id_mascota BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    especie VARCHAR(50) NOT NULL,
    raza VARCHAR(100),
    edad INT,
    sexo VARCHAR(10),
    peso DOUBLE,
    color VARCHAR(50),
    observaciones TEXT,
    id_dueno BIGINT NOT NULL,
    FOREIGN KEY (id_dueno) REFERENCES duenos(id_dueno) ON DELETE RESTRICT,
    INDEX idx_mascota_nombre (nombre),
    INDEX idx_mascota_especie (especie),
    INDEX idx_mascota_dueno (id_dueno)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: veterinarios
-- =====================================================
CREATE TABLE IF NOT EXISTS veterinarios (
    id_veterinario BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100),
    telefono VARCHAR(20),
    email VARCHAR(100),
    colegiatura VARCHAR(50) UNIQUE,
    activo BOOLEAN DEFAULT TRUE,
    INDEX idx_veterinario_nombres (nombres, apellidos),
    INDEX idx_veterinario_especialidad (especialidad),
    INDEX idx_veterinario_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: tecnicos_veterinarios
-- =====================================================
CREATE TABLE IF NOT EXISTS tecnicos_veterinarios (
    id_tecnico BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100),
    telefono VARCHAR(20),
    email VARCHAR(100),
    certificacion VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE,
    INDEX idx_tecnico_nombres (nombres, apellidos),
    INDEX idx_tecnico_especialidad (especialidad),
    INDEX idx_tecnico_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: ordenes_veterinarias
-- =====================================================
CREATE TABLE IF NOT EXISTS ordenes_veterinarias (
    id_orden BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_orden DATE NOT NULL,
    tipo_examen VARCHAR(100) NOT NULL,
    prioridad VARCHAR(20) DEFAULT 'NORMAL',
    estado VARCHAR(50) DEFAULT 'PENDIENTE',
    observaciones TEXT,
    diagnostico_presuntivo TEXT,
    sintomas TEXT,
    id_mascota BIGINT NOT NULL,
    id_veterinario BIGINT NOT NULL,
    FOREIGN KEY (id_mascota) REFERENCES mascotas(id_mascota) ON DELETE RESTRICT,
    FOREIGN KEY (id_veterinario) REFERENCES veterinarios(id_veterinario) ON DELETE RESTRICT,
    INDEX idx_orden_fecha (fecha_orden),
    INDEX idx_orden_estado (estado),
    INDEX idx_orden_prioridad (prioridad),
    INDEX idx_orden_mascota (id_mascota),
    INDEX idx_orden_veterinario (id_veterinario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: toma_muestras_vet
-- =====================================================
CREATE TABLE IF NOT EXISTS toma_muestras_vet (
    id_toma BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_hora DATETIME NOT NULL,
    tipo_muestra VARCHAR(100) NOT NULL,
    metodo_obtencion VARCHAR(100),
    volumen_muestra VARCHAR(50),
    condiciones_muestra VARCHAR(200),
    observaciones TEXT,
    estado VARCHAR(50) DEFAULT 'PROGRAMADA',
    codigo_muestra VARCHAR(50) UNIQUE,
    id_orden BIGINT NOT NULL UNIQUE,
    id_tecnico BIGINT NOT NULL,
    FOREIGN KEY (id_orden) REFERENCES ordenes_veterinarias(id_orden) ON DELETE RESTRICT,
    FOREIGN KEY (id_tecnico) REFERENCES tecnicos_veterinarios(id_tecnico) ON DELETE RESTRICT,
    INDEX idx_toma_fecha (fecha_hora),
    INDEX idx_toma_estado (estado),
    INDEX idx_toma_codigo (codigo_muestra),
    INDEX idx_toma_tecnico (id_tecnico)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: resultados_veterinarios
-- =====================================================
CREATE TABLE IF NOT EXISTS resultados_veterinarios (
    id_resultado BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_resultado DATETIME NOT NULL,
    descripcion TEXT,
    valores TEXT,
    valores_referencia TEXT,
    conclusiones TEXT,
    recomendaciones TEXT,
    validado BOOLEAN DEFAULT FALSE,
    fecha_validacion DATETIME,
    entregado BOOLEAN DEFAULT FALSE,
    fecha_entrega DATETIME,
    metodo_analisis VARCHAR(200),
    observaciones_tecnicas TEXT,
    id_orden BIGINT NOT NULL,
    FOREIGN KEY (id_orden) REFERENCES ordenes_veterinarias(id_orden) ON DELETE RESTRICT,
    INDEX idx_resultado_fecha (fecha_resultado),
    INDEX idx_resultado_validado (validado),
    INDEX idx_resultado_entregado (entregado),
    INDEX idx_resultado_orden (id_orden)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: facturas
-- =====================================================
CREATE TABLE IF NOT EXISTS facturas (
    id_factura BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_factura VARCHAR(50) NOT NULL UNIQUE,
    fecha_emision DATE NOT NULL,
    fecha_vencimiento DATE,
    subtotal DOUBLE NOT NULL,
    igv DOUBLE DEFAULT 0,
    total DOUBLE NOT NULL,
    estado VARCHAR(50) DEFAULT 'PENDIENTE',
    metodo_pago VARCHAR(50),
    fecha_pago DATE,
    observaciones TEXT,
    id_dueno BIGINT NOT NULL,
    FOREIGN KEY (id_dueno) REFERENCES duenos(id_dueno) ON DELETE RESTRICT,
    INDEX idx_factura_numero (numero_factura),
    INDEX idx_factura_fecha (fecha_emision),
    INDEX idx_factura_estado (estado),
    INDEX idx_factura_dueno (id_dueno)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABLA: detalle_facturas
-- =====================================================
CREATE TABLE IF NOT EXISTS detalle_facturas (
    id_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(200) NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DOUBLE NOT NULL,
    subtotal DOUBLE NOT NULL,
    tipo_servicio VARCHAR(100),
    id_factura BIGINT NOT NULL,
    FOREIGN KEY (id_factura) REFERENCES facturas(id_factura) ON DELETE CASCADE,
    INDEX idx_detalle_factura (id_factura)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- VISTAS ÚTILES
-- =====================================================

-- Vista: Órdenes con información completa
CREATE OR REPLACE VIEW v_ordenes_completas AS
SELECT 
    o.id_orden,
    o.fecha_orden,
    o.tipo_examen,
    o.estado,
    o.prioridad,
    m.nombre AS mascota_nombre,
    m.especie AS mascota_especie,
    CONCAT(d.nombres, ' ', d.apellidos) AS dueno_nombre,
    d.telefono AS dueno_telefono,
    CONCAT(v.nombres, ' ', v.apellidos) AS veterinario_nombre,
    v.especialidad AS veterinario_especialidad
FROM ordenes_veterinarias o
INNER JOIN mascotas m ON o.id_mascota = m.id_mascota
INNER JOIN duenos d ON m.id_dueno = d.id_dueno
INNER JOIN veterinarios v ON o.id_veterinario = v.id_veterinario;

-- Vista: Resultados pendientes de validación
CREATE OR REPLACE VIEW v_resultados_pendientes AS
SELECT 
    r.id_resultado,
    r.fecha_resultado,
    o.id_orden,
    o.tipo_examen,
    m.nombre AS mascota_nombre,
    CONCAT(d.nombres, ' ', d.apellidos) AS dueno_nombre,
    CONCAT(v.nombres, ' ', v.apellidos) AS veterinario_nombre
FROM resultados_veterinarios r
INNER JOIN ordenes_veterinarias o ON r.id_orden = o.id_orden
INNER JOIN mascotas m ON o.id_mascota = m.id_mascota
INNER JOIN duenos d ON m.id_dueno = d.id_dueno
INNER JOIN veterinarios v ON o.id_veterinario = v.id_veterinario
WHERE r.validado = FALSE;

-- Vista: Facturación del mes
CREATE OR REPLACE VIEW v_facturacion_mes AS
SELECT 
    YEAR(fecha_emision) AS anio,
    MONTH(fecha_emision) AS mes,
    COUNT(*) AS total_facturas,
    SUM(total) AS total_facturado,
    SUM(CASE WHEN estado = 'PAGADA' THEN total ELSE 0 END) AS total_pagado,
    SUM(CASE WHEN estado = 'PENDIENTE' THEN total ELSE 0 END) AS total_pendiente
FROM facturas
GROUP BY YEAR(fecha_emision), MONTH(fecha_emision);

-- =====================================================
-- FIN DEL SCHEMA
-- =====================================================