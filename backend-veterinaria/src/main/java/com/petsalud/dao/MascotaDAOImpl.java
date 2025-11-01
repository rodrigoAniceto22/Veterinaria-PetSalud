package com.petsalud.dao;

import com.petsalud.model.Mascota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.*;

/**
 * DAO Implementation for Mascota entity using Stored Procedures
 * Patrón DAO explícito
 */
@Repository
@SuppressWarnings({"unchecked", "rawtypes"})
public class MascotaDAOImpl implements MascotaDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall registrarMascotaCall;
    private SimpleJdbcCall buscarMascotasPorDuenoCall;
    private SimpleJdbcCall actualizarMascotaCall;
    private SimpleJdbcCall estadisticasMascotasCall;

    @PostConstruct
    public void init() {
        // Configurar llamadas a stored procedures
        registrarMascotaCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_registrar_mascota")
                .declareParameters(
                        new SqlParameter("p_nombre", Types.VARCHAR),
                        new SqlParameter("p_especie", Types.VARCHAR),
                        new SqlParameter("p_raza", Types.VARCHAR),
                        new SqlParameter("p_fecha_nacimiento", Types.DATE),
                        new SqlParameter("p_sexo", Types.VARCHAR),
                        new SqlParameter("p_peso", Types.DOUBLE),
                        new SqlParameter("p_color", Types.VARCHAR),
                        new SqlParameter("p_observaciones", Types.VARCHAR),
                        new SqlParameter("p_id_dueno", Types.BIGINT),
                        new SqlOutParameter("p_id_mascota", Types.BIGINT),
                        new SqlOutParameter("p_edad", Types.INTEGER),
                        new SqlOutParameter("p_mensaje", Types.VARCHAR)
                );

        buscarMascotasPorDuenoCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_buscar_mascotas_por_dueno")
                .returningResultSet("mascotas", new MascotaDetalleRowMapper());

        actualizarMascotaCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_actualizar_mascota")
                .declareParameters(
                        new SqlParameter("p_id_mascota", Types.BIGINT),
                        new SqlParameter("p_nombre", Types.VARCHAR),
                        new SqlParameter("p_especie", Types.VARCHAR),
                        new SqlParameter("p_raza", Types.VARCHAR),
                        new SqlParameter("p_fecha_nacimiento", Types.DATE),
                        new SqlParameter("p_sexo", Types.VARCHAR),
                        new SqlParameter("p_peso", Types.DOUBLE),
                        new SqlParameter("p_color", Types.VARCHAR),
                        new SqlParameter("p_observaciones", Types.VARCHAR),
                        new SqlOutParameter("p_edad", Types.INTEGER),
                        new SqlOutParameter("p_mensaje", Types.VARCHAR)
                );

        estadisticasMascotasCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_estadisticas_mascotas")
                .returningResultSet("estadisticas_generales", new EstadisticasGeneralesRowMapper())
                .returningResultSet("estadisticas_por_especie", new EstadisticasPorEspecieRowMapper());
    }

    @Override
    public Mascota registrarMascota(Mascota mascota, LocalDate fechaNacimiento) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_nombre", mascota.getNombre())
                .addValue("p_especie", mascota.getEspecie())
                .addValue("p_raza", mascota.getRaza())
                .addValue("p_fecha_nacimiento", fechaNacimiento)
                .addValue("p_sexo", mascota.getSexo())
                .addValue("p_peso", mascota.getPeso())
                .addValue("p_color", mascota.getColor())
                .addValue("p_observaciones", mascota.getObservaciones())
                .addValue("p_id_dueno", mascota.getDueno() != null ? mascota.getDueno().getIdDueno() : null);

        Map<String, Object> result = registrarMascotaCall.execute(params);
        
        Long idMascota = ((Number) result.get("p_id_mascota")).longValue();
        Integer edad = result.get("p_edad") != null ? ((Number) result.get("p_edad")).intValue() : null;
        String mensaje = (String) result.get("p_mensaje");

        if (idMascota > 0) {
            mascota.setIdMascota(idMascota);
            mascota.setEdad(edad);
            return mascota;
        } else {
            throw new RuntimeException(mensaje);
        }
    }

    @Override
    public List<Map<String, Object>> buscarMascotasPorDueno(Long idDueno) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_id_dueno", idDueno);

        Map<String, Object> result = buscarMascotasPorDuenoCall.execute(params);
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> mascotas = (List<Map<String, Object>>) result.get("mascotas");
        
        return mascotas != null ? mascotas : new ArrayList<>();
    }

    @Override
    public Mascota actualizarMascota(Mascota mascota, LocalDate fechaNacimiento) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_id_mascota", mascota.getIdMascota())
                .addValue("p_nombre", mascota.getNombre())
                .addValue("p_especie", mascota.getEspecie())
                .addValue("p_raza", mascota.getRaza())
                .addValue("p_fecha_nacimiento", fechaNacimiento)
                .addValue("p_sexo", mascota.getSexo())
                .addValue("p_peso", mascota.getPeso())
                .addValue("p_color", mascota.getColor())
                .addValue("p_observaciones", mascota.getObservaciones());

        Map<String, Object> result = actualizarMascotaCall.execute(params);
        
        Integer edad = result.get("p_edad") != null ? ((Number) result.get("p_edad")).intValue() : null;
        String mensaje = (String) result.get("p_mensaje");

        if (mensaje.contains("exitosamente")) {
            mascota.setEdad(edad);
            return mascota;
        } else {
            throw new RuntimeException(mensaje);
        }
    }

    @Override
    public Map<String, Object> obtenerEstadisticasMascotas() {
        Map<String, Object> result = estadisticasMascotasCall.execute();
        
        Map<String, Object> response = new HashMap<>();
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> estadisticasGenerales = (List<Map<String, Object>>) result.get("estadisticas_generales");
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> estadisticasPorEspecie = (List<Map<String, Object>>) result.get("estadisticas_por_especie");
        
        if (!estadisticasGenerales.isEmpty()) {
            response.put("estadisticas_generales", estadisticasGenerales.get(0));
        }
        response.put("estadisticas_por_especie", estadisticasPorEspecie);
        
        return response;
    }

    @Override
    public List<Mascota> listarTodas() {
        String sql = "SELECT * FROM mascotas ORDER BY nombre";
        return jdbcTemplate.query(sql, new MascotaRowMapper());
    }

    @Override
    public Mascota buscarPorId(Long id) {
        String sql = "SELECT * FROM mascotas WHERE id_mascota = ?";
        List<Mascota> mascotas = jdbcTemplate.query(sql, new MascotaRowMapper(), id);
        return mascotas.isEmpty() ? null : mascotas.get(0);
    }

    @Override
    public void eliminar(Long id) {
        String sql = "DELETE FROM mascotas WHERE id_mascota = ?";
        jdbcTemplate.update(sql, id);
    }

    // RowMappers
    private static class MascotaRowMapper implements RowMapper<Mascota> {
        @Override
        public Mascota mapRow(ResultSet rs, int rowNum) throws SQLException {
            Mascota mascota = new Mascota();
            mascota.setIdMascota(rs.getLong("id_mascota"));
            mascota.setNombre(rs.getString("nombre"));
            mascota.setEspecie(rs.getString("especie"));
            mascota.setRaza(rs.getString("raza"));
            mascota.setEdad(rs.getInt("edad"));
            mascota.setSexo(rs.getString("sexo"));
            mascota.setPeso(rs.getDouble("peso"));
            mascota.setColor(rs.getString("color"));
            mascota.setObservaciones(rs.getString("observaciones"));
            return mascota;
        }
    }

    private static class MascotaDetalleRowMapper implements RowMapper<Map<String, Object>> {
        @Override
        public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
            Map<String, Object> mascota = new HashMap<>();
            mascota.put("id_mascota", rs.getLong("id_mascota"));
            mascota.put("nombre", rs.getString("nombre"));
            mascota.put("especie", rs.getString("especie"));
            mascota.put("raza", rs.getString("raza"));
            mascota.put("edad", rs.getInt("edad"));
            mascota.put("sexo", rs.getString("sexo"));
            mascota.put("peso", rs.getDouble("peso"));
            mascota.put("color", rs.getString("color"));
            mascota.put("observaciones", rs.getString("observaciones"));
            mascota.put("dueno_nombres", rs.getString("dueno_nombres"));
            mascota.put("dueno_apellidos", rs.getString("dueno_apellidos"));
            mascota.put("dueno_telefono", rs.getString("dueno_telefono"));
            mascota.put("total_ordenes", rs.getLong("total_ordenes"));
            return mascota;
        }
    }

    private static class EstadisticasGeneralesRowMapper implements RowMapper<Map<String, Object>> {
        @Override
        public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
            Map<String, Object> stats = new HashMap<>();
            stats.put("total_mascotas", rs.getLong("total_mascotas"));
            stats.put("total_especies", rs.getLong("total_especies"));
            stats.put("edad_promedio", rs.getDouble("edad_promedio"));
            stats.put("peso_promedio", rs.getDouble("peso_promedio"));
            stats.put("total_machos", rs.getLong("total_machos"));
            stats.put("total_hembras", rs.getLong("total_hembras"));
            return stats;
        }
    }

    private static class EstadisticasPorEspecieRowMapper implements RowMapper<Map<String, Object>> {
        @Override
        public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
            Map<String, Object> stats = new HashMap<>();
            stats.put("especie", rs.getString("especie"));
            stats.put("cantidad", rs.getLong("cantidad"));
            stats.put("edad_promedio", rs.getDouble("edad_promedio"));
            stats.put("peso_promedio", rs.getDouble("peso_promedio"));
            return stats;
        }
    }
}