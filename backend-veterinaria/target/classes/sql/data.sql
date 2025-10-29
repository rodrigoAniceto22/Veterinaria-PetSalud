-- =====================================================
-- DATA SQL - Datos iniciales del sistema
-- Sistema de Gestión Veterinaria PetSalud
-- VERSIÓN CORREGIDA - Coincide con entidades Java
-- =====================================================

USE veterinaria_petsalud;

-- =====================================================
-- USUARIOS DEL SISTEMA
-- Contraseñas: todas son "password123" (encriptadas con BCrypt)
-- Hash: $2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5lW7QK3XtO4bq
-- =====================================================
INSERT INTO usuario (nombre_completo, username, contrasena, email, rol, activo, fecha_creacion) VALUES
('Administrador Sistema', 'admin', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5lW7QK3XtO4bq', 'admin@petsalud.com', 'ADMIN', TRUE, NOW()),
('Dra. María García', 'vet.maria', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5lW7QK3XtO4bq', 'maria.garcia@petsalud.com', 'VETERINARIO', TRUE, NOW()),
('Dr. Juan López', 'vet.juan', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5lW7QK3XtO4bq', 'juan.lopez@petsalud.com', 'VETERINARIO', TRUE, NOW()),
('Ana Martínez - Técnico', 'tec.ana', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5lW7QK3XtO4bq', 'ana.martinez@petsalud.com', 'TECNICO', TRUE, NOW()),
('Pedro Ramírez - Técnico', 'tec.pedro', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5lW7QK3XtO4bq', 'pedro.ramirez@petsalud.com', 'TECNICO', TRUE, NOW()),
('Lucía Fernández', 'recep.lucia', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5lW7QK3XtO4bq', 'lucia.fernandez@petsalud.com', 'RECEPCIONISTA', TRUE, NOW());

-- =====================================================
-- VETERINARIOS
-- =====================================================
INSERT INTO veterinario (nombre_completo, colegiatura, especialidad, telefono, email, activo, fecha_registro) VALUES
('Dra. María García Pérez', 'CMVP-12345', 'Medicina General', '912345678', 'maria.garcia@petsalud.com', TRUE, NOW()),
('Dr. Juan López Sánchez', 'CMVP-12346', 'Cirugía Veterinaria', '912345679', 'juan.lopez@petsalud.com', TRUE, NOW()),
('Dra. Andrea Mendoza Rivera', 'CMVP-12347', 'Dermatología Veterinaria', '912345680', 'andrea.mendoza@petsalud.com', TRUE, NOW()),
('Dr. Carlos Ruiz Vargas', 'CMVP-12348', 'Medicina Interna', '912345681', 'carlos.ruiz@petsalud.com', TRUE, NOW()),
('Dra. Sofia Paredes Luna', 'CMVP-12349', 'Laboratorio Clínico', '912345682', 'sofia.paredes@petsalud.com', TRUE, NOW());

-- =====================================================
-- TÉCNICOS VETERINARIOS
-- =====================================================
INSERT INTO tecnico_veterinario (nombre_completo, dni, telefono, email, activo, fecha_contratacion) VALUES
('Ana Martínez Torres', '12345678', '923456789', 'ana.martinez@petsalud.com', TRUE, NOW()),
('Pedro Ramírez Cruz', '23456789', '923456790', 'pedro.ramirez@petsalud.com', TRUE, NOW()),
('Laura Díaz Campos', '34567890', '923456791', 'laura.diaz@petsalud.com', TRUE, NOW()),
('Ricardo Castro Gómez', '45678901', '923456792', 'ricardo.castro@petsalud.com', TRUE, NOW());

-- =====================================================
-- DUEÑOS DE MASCOTAS
-- =====================================================
INSERT INTO dueno (nombres, apellidos, dni, telefono, email, direccion, fecha_registro) VALUES
('Roberto', 'Gutiérrez Mendoza', '78901234', '987654321', 'roberto.gutierrez@email.com', 'Av. Los Pinos 123, San Isidro, Lima', NOW()),
('Carmen', 'Silva Rojas', '89012345', '987654322', 'carmen.silva@email.com', 'Jr. Las Flores 456, Miraflores, Lima', NOW()),
('Luis', 'Vega Castro', '90123456', '987654323', 'luis.vega@email.com', 'Av. Central 789, San Borja, Lima', NOW()),
('Patricia', 'Quispe Flores', '01234567', '987654324', 'patricia.quispe@email.com', 'Calle Lima 321, Surco, Lima', NOW()),
('Jorge', 'Sánchez Paredes', '11234567', '987654325', 'jorge.sanchez@email.com', 'Av. Colonial 654, Callao', NOW());

-- =====================================================
-- MASCOTAS
-- =====================================================
INSERT INTO mascota (nombre, especie, raza, fecha_nacimiento, sexo, peso_kg, color, observaciones, id_dueno, fecha_registro) VALUES
-- Mascotas de Roberto Gutiérrez
('Max', 'PERRO', 'Labrador Retriever', '2020-01-15', 'M', 32.5, 'Dorado', 'Muy activo y juguetón', 1, NOW()),
('Luna', 'GATO', 'Siamés', '2022-03-20', 'H', 4.2, 'Crema', 'Tranquila, le gusta dormir', 1, NOW()),

-- Mascotas de Carmen Silva
('Rocky', 'PERRO', 'Bulldog Francés', '2023-05-10', 'M', 12.8, 'Atigrado', 'Problemas respiratorios leves', 2, NOW()),

-- Mascotas de Luis Vega
('Michi', 'GATO', 'Persa', '2021-07-15', 'H', 5.5, 'Blanco', 'Requiere cuidado especial del pelaje', 3, NOW()),
('Toby', 'PERRO', 'Golden Retriever', '2019-02-20', 'M', 35.0, 'Dorado', 'Muy amigable con niños', 3, NOW()),

-- Mascotas de Patricia Quispe
('Bella', 'PERRO', 'Poodle', '2022-08-05', 'H', 8.5, 'Blanco', 'Alérgica a algunos alimentos', 4, NOW()),

-- Mascotas de Jorge Sánchez
('Simba', 'GATO', 'Mestizo', '2023-01-10', 'M', 4.8, 'Naranja', 'Muy territorial', 5, NOW()),
('Bruno', 'PERRO', 'Beagle', '2020-06-15', 'M', 14.2, 'Tricolor', 'Le encanta comer', 5, NOW());

-- =====================================================
-- ÓRDENES VETERINARIAS
-- =====================================================
INSERT INTO orden_veterinaria (fecha_orden, tipo_examen, prioridad, estado, sintomas, diagnostico_presuntivo, observaciones, id_mascota, id_veterinario) VALUES
-- Órdenes completadas
('2025-01-15', 'HEMOGRAMA', 'NORMAL', 'COMPLETADA', 'Control de rutina', 'Animal sano', 'Checkup anual', 1, 1),
('2025-01-16', 'BIOQUIMICA', 'NORMAL', 'COMPLETADA', 'Poliuria, polidipsia', 'Posible insuficiencia renal', 'Evaluación función renal', 2, 2),
('2025-01-17', 'URIANALISIS', 'URGENTE', 'COMPLETADA', 'Disuria, hematuria', 'ITU', 'Sospecha de infección urinaria', 3, 1),

-- Órdenes en proceso
('2025-01-20', 'COPROPARASITOLOGICO', 'NORMAL', 'EN_PROCESO', 'Diarrea leve', 'Posible parasitosis', 'Desparasitación de rutina', 4, 3),
('2025-01-21', 'PERFIL_COMPLETO', 'ALTA', 'EN_PROCESO', 'Ninguno', 'Preparación para cirugía', 'Prequirúrgico', 5, 2),

-- Órdenes pendientes
('2025-01-25', 'PERFIL_TIROIDEO', 'NORMAL', 'PENDIENTE', 'Letargia, aumento de peso', 'Hipotiroidismo', 'Evaluación hormonal', 6, 4),
('2025-01-26', 'CULTIVO', 'ALTA', 'PENDIENTE', 'Fiebre, secreción', 'Infección bacteriana', 'Tratamiento de infección', 7, 1),
('2025-01-27', 'HEMOGRAMA', 'URGENTE', 'PENDIENTE', 'Debilidad, mucosas pálidas', 'Anemia aguda', 'Emergencia', 8, 5);

-- =====================================================
-- FACTURAS
-- =====================================================
INSERT INTO factura (numero_factura, fecha_emision, fecha_vencimiento, subtotal, igv, total, estado, metodo_pago, fecha_pago, observaciones, id_dueno) VALUES
('F2025-000001', '2025-01-16', '2025-02-16', 120.00, 21.60, 141.60, 'PAGADA', 'EFECTIVO', '2025-01-16', 'Pago completo', 1),
('F2025-000002', '2025-01-17', '2025-02-17', 180.00, 32.40, 212.40, 'PAGADA', 'TARJETA', '2025-01-17', 'Pago con tarjeta Visa', 2),
('F2025-000003', '2025-01-22', '2025-02-22', 200.00, 36.00, 236.00, 'PENDIENTE', NULL, NULL, 'Pendiente de pago', 3),
('F2025-000004', '2025-01-23', '2025-02-23', 250.00, 45.00, 295.00, 'PENDIENTE', NULL, NULL, 'Cliente solicitó plazo', 4);

-- =====================================================
-- DETALLES DE FACTURAS
-- =====================================================
INSERT INTO detalle_factura (descripcion, cantidad, precio_unitario, subtotal, tipo_servicio, id_factura) VALUES
-- Factura 1 (Roberto Gutiérrez - Max)
('Hemograma Completo', 1, 80.00, 80.00, 'EXAMEN', 1),
('Consulta Veterinaria', 1, 40.00, 40.00, 'CONSULTA', 1),

-- Factura 2 (Carmen Silva - Rocky)
('Urianálisis', 1, 70.00, 70.00, 'EXAMEN', 2),
('Consulta Veterinaria', 1, 40.00, 40.00, 'CONSULTA', 2),
('Antibiótico Enrofloxacina', 1, 70.00, 70.00, 'MEDICAMENTO', 2),

-- Factura 3 (Luis Vega - Michi)
('Perfil Bioquímico', 1, 120.00, 120.00, 'EXAMEN', 3),
('Consulta Especializada', 1, 80.00, 80.00, 'CONSULTA', 3),

-- Factura 4 (Patricia Quispe - Bella)
('Hemograma + Bioquímica', 1, 150.00, 150.00, 'EXAMEN', 4),
('Consulta Prequirúrgica', 1, 80.00, 80.00, 'CONSULTA', 4),
('Electrocardiograma', 1, 20.00, 20.00, 'EXAMEN', 4);

-- =====================================================
-- VERIFICACIÓN DE DATOS INSERTADOS
-- =====================================================
SELECT 'USUARIOS' as tabla, COUNT(*) as total FROM usuario
UNION ALL
SELECT 'VETERINARIOS', COUNT(*) FROM veterinario
UNION ALL
SELECT 'TÉCNICOS', COUNT(*) FROM tecnico_veterinario
UNION ALL
SELECT 'DUEÑOS', COUNT(*) FROM dueno
UNION ALL
SELECT 'MASCOTAS', COUNT(*) FROM mascota
UNION ALL
SELECT 'ÓRDENES', COUNT(*) FROM orden_veterinaria
UNION ALL
SELECT 'FACTURAS', COUNT(*) FROM factura
UNION ALL
SELECT 'DETALLES FACTURA', COUNT(*) FROM detalle_factura;

-- =====================================================
-- FIN DE DATOS INICIALES
-- =====================================================