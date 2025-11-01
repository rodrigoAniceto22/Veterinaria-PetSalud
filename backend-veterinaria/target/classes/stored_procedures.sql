-- =====================================================
-- STORED PROCEDURES FOR VETERINARIA PETSALUD
-- Implementación del Patrón DAO
-- =====================================================

USE veterinaria_petsalud;

DELIMITER $$

-- =====================================================
-- SP1: REGISTRAR NUEVO DUEÑO CON VALIDACIONES
-- =====================================================
DROP PROCEDURE IF EXISTS sp_registrar_dueno$$
CREATE PROCEDURE sp_registrar_dueno(
    IN p_dni VARCHAR(20),
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_email VARCHAR(100),
    IN p_direccion VARCHAR(200),
    OUT p_id_dueno BIGINT,
    OUT p_mensaje VARCHAR(255)
)
BEGIN
    DECLARE v_existe INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_mensaje = 'Error al registrar el dueño';
        SET p_id_dueno = -1;
    END;
    
    START TRANSACTION;
    
    -- Validar que el DNI no esté vacío
    IF p_dni IS NULL OR TRIM(p_dni) = '' THEN
        SET p_mensaje = 'El DNI es obligatorio';
        SET p_id_dueno = -1;
        ROLLBACK;
    ELSE
        -- Verificar si ya existe un dueño con ese DNI
        SELECT COUNT(*) INTO v_existe 
        FROM duenos 
        WHERE dni = p_dni;
        
        IF v_existe > 0 THEN
            SET p_mensaje = 'Ya existe un dueño registrado con ese DNI';
            SET p_id_dueno = -1;
            ROLLBACK;
        ELSE
            -- Insertar el nuevo dueño
            INSERT INTO duenos (dni, nombres, apellidos, telefono, email, direccion)
            VALUES (p_dni, p_nombres, p_apellidos, p_telefono, p_email, p_direccion);
            
            SET p_id_dueno = LAST_INSERT_ID();
            SET p_mensaje = 'Dueño registrado exitosamente';
            COMMIT;
        END IF;
    END IF;
END$$

-- =====================================================
-- SP2: BUSCAR DUEÑO CON SUS MASCOTAS (JOIN)
-- =====================================================
DROP PROCEDURE IF EXISTS sp_buscar_dueno_con_mascotas$$
CREATE PROCEDURE sp_buscar_dueno_con_mascotas(
    IN p_id_dueno BIGINT
)
BEGIN
    -- Información del dueño
    SELECT 
        d.id_dueno,
        d.dni,
        d.nombres,
        d.apellidos,
        d.telefono,
        d.email,
        d.direccion,
        COUNT(m.id_mascota) as total_mascotas
    FROM duenos d
    LEFT JOIN mascotas m ON d.id_dueno = m.id_dueno
    WHERE d.id_dueno = p_id_dueno
    GROUP BY d.id_dueno, d.dni, d.nombres, d.apellidos, d.telefono, d.email, d.direccion;
    
    -- Lista de mascotas del dueño
    SELECT 
        m.id_mascota,
        m.nombre,
        m.especie,
        m.raza,
        m.edad,
        m.sexo,
        m.peso,
        m.color,
        m.observaciones
    FROM mascotas m
    WHERE m.id_dueno = p_id_dueno
    ORDER BY m.nombre;
END$$

-- =====================================================
-- SP3: ACTUALIZAR INFORMACIÓN DEL DUEÑO
-- =====================================================
DROP PROCEDURE IF EXISTS sp_actualizar_dueno$$
CREATE PROCEDURE sp_actualizar_dueno(
    IN p_id_dueno BIGINT,
    IN p_dni VARCHAR(20),
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_email VARCHAR(100),
    IN p_direccion VARCHAR(200),
    OUT p_mensaje VARCHAR(255)
)
BEGIN
    DECLARE v_existe INT DEFAULT 0;
    DECLARE v_dni_duplicado INT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_mensaje = 'Error al actualizar el dueño';
    END;
    
    START TRANSACTION;
    
    -- Verificar si el dueño existe
    SELECT COUNT(*) INTO v_existe 
    FROM duenos 
    WHERE id_dueno = p_id_dueno;
    
    IF v_existe = 0 THEN
        SET p_mensaje = 'El dueño no existe';
        ROLLBACK;
    ELSE
        -- Verificar si el DNI ya está en uso por otro dueño
        SELECT COUNT(*) INTO v_dni_duplicado
        FROM duenos
        WHERE dni = p_dni AND id_dueno != p_id_dueno;
        
        IF v_dni_duplicado > 0 THEN
            SET p_mensaje = 'El DNI ya está registrado para otro dueño';
            ROLLBACK;
        ELSE
            -- Actualizar el dueño
            UPDATE duenos
            SET 
                dni = p_dni,
                nombres = p_nombres,
                apellidos = p_apellidos,
                telefono = p_telefono,
                email = p_email,
                direccion = p_direccion
            WHERE id_dueno = p_id_dueno;
            
            SET p_mensaje = 'Dueño actualizado exitosamente';
            COMMIT;
        END IF;
    END IF;
END$$

-- =====================================================
-- SP4: OBTENER ESTADÍSTICAS DE DUEÑOS
-- =====================================================
DROP PROCEDURE IF EXISTS sp_estadisticas_duenos$$
CREATE PROCEDURE sp_estadisticas_duenos()
BEGIN
    -- Estadísticas generales
    SELECT 
        COUNT(DISTINCT d.id_dueno) as total_duenos,
        COUNT(DISTINCT CASE WHEN m.id_mascota IS NOT NULL THEN d.id_dueno END) as duenos_con_mascotas,
        COUNT(DISTINCT CASE WHEN m.id_mascota IS NULL THEN d.id_dueno END) as duenos_sin_mascotas,
        COUNT(m.id_mascota) as total_mascotas,
        COALESCE(ROUND(AVG(mascotas_por_dueno.cantidad), 2), 0) as promedio_mascotas_por_dueno
    FROM duenos d
    LEFT JOIN mascotas m ON d.id_dueno = m.id_dueno
    LEFT JOIN (
        SELECT id_dueno, COUNT(*) as cantidad
        FROM mascotas
        GROUP BY id_dueno
    ) mascotas_por_dueno ON d.id_dueno = mascotas_por_dueno.id_dueno;
    
    -- Top 5 dueños con más mascotas
    SELECT 
        d.id_dueno,
        d.nombres,
        d.apellidos,
        d.dni,
        COUNT(m.id_mascota) as cantidad_mascotas
    FROM duenos d
    LEFT JOIN mascotas m ON d.id_dueno = m.id_dueno
    GROUP BY d.id_dueno, d.nombres, d.apellidos, d.dni
    HAVING cantidad_mascotas > 0
    ORDER BY cantidad_mascotas DESC
    LIMIT 5;
END$$

-- =====================================================
-- SP5: REGISTRAR NUEVA MASCOTA CON CÁLCULO DE EDAD
-- =====================================================
DROP PROCEDURE IF EXISTS sp_registrar_mascota$$
CREATE PROCEDURE sp_registrar_mascota(
    IN p_nombre VARCHAR(100),
    IN p_especie VARCHAR(50),
    IN p_raza VARCHAR(100),
    IN p_fecha_nacimiento DATE,
    IN p_sexo VARCHAR(10),
    IN p_peso DOUBLE,
    IN p_color VARCHAR(50),
    IN p_observaciones TEXT,
    IN p_id_dueno BIGINT,
    OUT p_id_mascota BIGINT,
    OUT p_edad INT,
    OUT p_mensaje VARCHAR(255)
)
BEGIN
    DECLARE v_dueno_existe INT DEFAULT 0;
    DECLARE v_edad_calculada INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_mensaje = 'Error al registrar la mascota';
        SET p_id_mascota = -1;
    END;
    
    START TRANSACTION;
    
    -- Validar que el nombre no esté vacío
    IF p_nombre IS NULL OR TRIM(p_nombre) = '' THEN
        SET p_mensaje = 'El nombre de la mascota es obligatorio';
        SET p_id_mascota = -1;
        ROLLBACK;
    ELSE
        -- Verificar si el dueño existe
        SELECT COUNT(*) INTO v_dueno_existe 
        FROM duenos 
        WHERE id_dueno = p_id_dueno;
        
        IF v_dueno_existe = 0 THEN
            SET p_mensaje = 'El dueño especificado no existe';
            SET p_id_mascota = -1;
            ROLLBACK;
        ELSE
            -- Calcular edad automáticamente
            IF p_fecha_nacimiento IS NOT NULL THEN
                SET v_edad_calculada = TIMESTAMPDIFF(YEAR, p_fecha_nacimiento, CURDATE());
            ELSE
                SET v_edad_calculada = NULL;
            END IF;
            
            -- Insertar la nueva mascota
            INSERT INTO mascotas (nombre, especie, raza, edad, sexo, peso, color, observaciones, id_dueno)
            VALUES (p_nombre, p_especie, p_raza, v_edad_calculada, p_sexo, p_peso, p_color, p_observaciones, p_id_dueno);
            
            SET p_id_mascota = LAST_INSERT_ID();
            SET p_edad = v_edad_calculada;
            SET p_mensaje = 'Mascota registrada exitosamente';
            COMMIT;
        END IF;
    END IF;
END$$

-- =====================================================
-- SP6: BUSCAR MASCOTAS POR DUEÑO CON DETALLES
-- =====================================================
DROP PROCEDURE IF EXISTS sp_buscar_mascotas_por_dueno$$
CREATE PROCEDURE sp_buscar_mascotas_por_dueno(
    IN p_id_dueno BIGINT
)
BEGIN
    SELECT 
        m.id_mascota,
        m.nombre,
        m.especie,
        m.raza,
        m.edad,
        m.sexo,
        m.peso,
        m.color,
        m.observaciones,
        d.nombres as dueno_nombres,
        d.apellidos as dueno_apellidos,
        d.telefono as dueno_telefono,
        COUNT(o.id_orden) as total_ordenes
    FROM mascotas m
    INNER JOIN duenos d ON m.id_dueno = d.id_dueno
    LEFT JOIN ordenes_veterinarias o ON m.id_mascota = o.id_mascota
    WHERE m.id_dueno = p_id_dueno
    GROUP BY m.id_mascota, m.nombre, m.especie, m.raza, m.edad, m.sexo, m.peso, m.color, m.observaciones,
             d.nombres, d.apellidos, d.telefono
    ORDER BY m.nombre;
END$$

-- =====================================================
-- SP7: ACTUALIZAR MASCOTA CON CÁLCULO DE EDAD
-- =====================================================
DROP PROCEDURE IF EXISTS sp_actualizar_mascota$$
CREATE PROCEDURE sp_actualizar_mascota(
    IN p_id_mascota BIGINT,
    IN p_nombre VARCHAR(100),
    IN p_especie VARCHAR(50),
    IN p_raza VARCHAR(100),
    IN p_fecha_nacimiento DATE,
    IN p_sexo VARCHAR(10),
    IN p_peso DOUBLE,
    IN p_color VARCHAR(50),
    IN p_observaciones TEXT,
    OUT p_edad INT,
    OUT p_mensaje VARCHAR(255)
)
BEGIN
    DECLARE v_existe INT DEFAULT 0;
    DECLARE v_edad_calculada INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_mensaje = 'Error al actualizar la mascota';
    END;
    
    START TRANSACTION;
    
    -- Verificar si la mascota existe
    SELECT COUNT(*) INTO v_existe 
    FROM mascotas 
    WHERE id_mascota = p_id_mascota;
    
    IF v_existe = 0 THEN
        SET p_mensaje = 'La mascota no existe';
        ROLLBACK;
    ELSE
        -- Calcular edad automáticamente
        IF p_fecha_nacimiento IS NOT NULL THEN
            SET v_edad_calculada = TIMESTAMPDIFF(YEAR, p_fecha_nacimiento, CURDATE());
        ELSE
            SET v_edad_calculada = NULL;
        END IF;
        
        -- Actualizar la mascota
        UPDATE mascotas
        SET 
            nombre = p_nombre,
            especie = p_especie,
            raza = p_raza,
            edad = v_edad_calculada,
            sexo = p_sexo,
            peso = p_peso,
            color = p_color,
            observaciones = p_observaciones
        WHERE id_mascota = p_id_mascota;
        
        SET p_edad = v_edad_calculada;
        SET p_mensaje = 'Mascota actualizada exitosamente';
        COMMIT;
    END IF;
END$$

-- =====================================================
-- SP8: OBTENER ESTADÍSTICAS DE MASCOTAS
-- =====================================================
DROP PROCEDURE IF EXISTS sp_estadisticas_mascotas$$
CREATE PROCEDURE sp_estadisticas_mascotas()
BEGIN
    -- Estadísticas generales
    SELECT 
        COUNT(*) as total_mascotas,
        COUNT(DISTINCT especie) as total_especies,
        ROUND(AVG(edad), 2) as edad_promedio,
        ROUND(AVG(peso), 2) as peso_promedio,
        COUNT(CASE WHEN sexo = 'Macho' THEN 1 END) as total_machos,
        COUNT(CASE WHEN sexo = 'Hembra' THEN 1 END) as total_hembras
    FROM mascotas;
    
    -- Mascotas por especie
    SELECT 
        especie,
        COUNT(*) as cantidad,
        ROUND(AVG(edad), 2) as edad_promedio,
        ROUND(AVG(peso), 2) as peso_promedio
    FROM mascotas
    GROUP BY especie
    ORDER BY cantidad DESC;
END$$

DELIMITER ;

-- Verificar creación
SHOW PROCEDURE STATUS WHERE Db = 'veterinaria_petsalud';