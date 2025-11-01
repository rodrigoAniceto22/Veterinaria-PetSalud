package com.petsalud.dao;

import com.petsalud.model.Dueno;
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
import java.util.*;

/**
 * DAO Implementation for Dueno entity using Stored Procedures
 * Patrón DAO explícito
 */
@Repository
@SuppressWarnings({"unchecked", "rawtypes"})
public class DuenoDAOImpl implements DuenoDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall registrarDuenoCall;
    private SimpleJdbcCall buscarDuenoConMascotasCall;
    private SimpleJdbcCall actualizarDuenoCall;
    private SimpleJdbcCall estadisticasDuenosCall;

    @PostConstruct
    public void init() {
        // Configurar llamadas a stored procedures
        registrarDuenoCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_registrar_dueno")
                .declareParameters(
                        new SqlParameter("p_dni", Types.VARCHAR),
                        new SqlParameter("p_nombres", Types.VARCHAR),
                        new SqlParameter("p_apellidos", Types.VARCHAR),
                        new SqlParameter("p_telefono", Types.VARCHAR),
                        new SqlParameter("p_email", Types.VARCHAR),
                        new SqlParameter("p_direccion", Types.VARCHAR),
                        new SqlOutParameter("p_id_dueno", Types.BIGINT),
                        new SqlOutParameter("p_mensaje", Types.VARCHAR)
                );

        buscarDuenoConMascotasCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_buscar_dueno_con_mascotas")
                .returningResultSet("dueno", new DuenoRowMapper())
                .returningResultSet("mascotas", new MascotaRowMapper());

        actualizarDuenoCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_actualizar_dueno")
                .declareParameters(
                        new SqlParameter("p_id_dueno", Types.BIGINT),
                        new SqlParameter("p_dni", Types.VARCHAR),
                        new SqlParameter("p_nombres", Types.VARCHAR),
                        new SqlParameter("p_apellidos", Types.VARCHAR),
                        new SqlParameter("p_telefono", Types.VARCHAR),
                        new SqlParameter("p_email", Types.VARCHAR),
                        new SqlParameter("p_direccion", Types.VARCHAR),
                        new SqlOutParameter("p_mensaje", Types.VARCHAR)
                );

        estadisticasDuenosCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("sp_estadisticas_duenos")
                .returningResultSet("estadisticas", new EstadisticasRowMapper())
                .returningResultSet("top_duenos", new TopDuenosRowMapper());
    }

    @Override
    public Dueno registrarDueno(Dueno dueno) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_dni", dueno.getDni())
                .addValue("p_nombres", dueno.getNombres())
                .addValue("p_apellidos", dueno.getApellidos())
                .addValue("p_telefono", dueno.getTelefono())
                .addValue("p_email", dueno.getEmail())
                .addValue("p_direccion", dueno.getDireccion());

        Map<String, Object> result = registrarDuenoCall.execute(params);
        
        Long idDueno = ((Number) result.get("p_id_dueno")).longValue();
        String mensaje = (String) result.get("p_mensaje");

        if (idDueno > 0) {
            dueno.setIdDueno(idDueno);
            return dueno;
        } else {
            throw new RuntimeException(mensaje);
        }
    }

    @Override
    public Map<String, Object> buscarDuenoConMascotas(Long idDueno) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_id_dueno", idDueno);

        Map<String, Object> result = buscarDuenoConMascotasCall.execute(params);
        
        Map<String, Object> response = new HashMap<>();
        
        @SuppressWarnings("unchecked")
        List<Dueno> duenos = (List<Dueno>) result.get("dueno");
        
        @SuppressWarnings("unchecked")
        List<Mascota> mascotas = (List<Mascota>) result.get("mascotas");
        
        if (!duenos.isEmpty()) {
            response.put("dueno", duenos.get(0));
            response.put("mascotas", mascotas);
        }
        
        return response;
    }

    @Override
    public Dueno actualizarDueno(Dueno dueno) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("p_id_dueno", dueno.getIdDueno())
                .addValue("p_dni", dueno.getDni())
                .addValue("p_nombres", dueno.getNombres())
                .addValue("p_apellidos", dueno.getApellidos())
                .addValue("p_telefono", dueno.getTelefono())
                .addValue("p_email", dueno.getEmail())
                .addValue("p_direccion", dueno.getDireccion());

        Map<String, Object> result = actualizarDuenoCall.execute(params);
        String mensaje = (String) result.get("p_mensaje");

        if (mensaje.contains("exitosamente")) {
            return dueno;
        } else {
            throw new RuntimeException(mensaje);
        }
    }

    @Override
    public Map<String, Object> obtenerEstadisticasDuenos() {
        Map<String, Object> result = estadisticasDuenosCall.execute();
        
        Map<String, Object> response = new HashMap<>();
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> estadisticas = (List<Map<String, Object>>) result.get("estadisticas");
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> topDuenos = (List<Map<String, Object>>) result.get("top_duenos");
        
        if (!estadisticas.isEmpty()) {
            response.put("estadisticas", estadisticas.get(0));
        }
        response.put("top_duenos", topDuenos);
        
        return response;
    }

    @Override
    public List<Dueno> listarTodos() {
        String sql = "SELECT * FROM duenos ORDER BY nombres, apellidos";
        return jdbcTemplate.query(sql, new DuenoRowMapper());
    }

    @Override
    public Dueno buscarPorId(Long id) {
        String sql = "SELECT * FROM duenos WHERE id_dueno = ?";
        List<Dueno> duenos = jdbcTemplate.query(sql, new DuenoRowMapper(), id);
        return duenos.isEmpty() ? null : duenos.get(0);
    }

    @Override
    public void eliminar(Long id) {
        String sql = "DELETE FROM duenos WHERE id_dueno = ?";
        jdbcTemplate.update(sql, id);
    }

    // RowMappers
    private static class DuenoRowMapper implements RowMapper<Dueno> {
        @Override
        public Dueno mapRow(ResultSet rs, int rowNum) throws SQLException {
            Dueno dueno = new Dueno();
            dueno.setIdDueno(rs.getLong("id_dueno"));
            dueno.setDni(rs.getString("dni"));
            dueno.setNombres(rs.getString("nombres"));
            dueno.setApellidos(rs.getString("apellidos"));
            dueno.setTelefono(rs.getString("telefono"));
            dueno.setEmail(rs.getString("email"));
            dueno.setDireccion(rs.getString("direccion"));
            return dueno;
        }
    }

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

    private static class EstadisticasRowMapper implements RowMapper<Map<String, Object>> {
        @Override
        public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
            Map<String, Object> stats = new HashMap<>();
            stats.put("total_duenos", rs.getLong("total_duenos"));
            stats.put("duenos_con_mascotas", rs.getLong("duenos_con_mascotas"));
            stats.put("duenos_sin_mascotas", rs.getLong("duenos_sin_mascotas"));
            stats.put("total_mascotas", rs.getLong("total_mascotas"));
            stats.put("promedio_mascotas_por_dueno", rs.getDouble("promedio_mascotas_por_dueno"));
            return stats;
        }
    }

    private static class TopDuenosRowMapper implements RowMapper<Map<String, Object>> {
        @Override
        public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
            Map<String, Object> dueno = new HashMap<>();
            dueno.put("id_dueno", rs.getLong("id_dueno"));
            dueno.put("nombres", rs.getString("nombres"));
            dueno.put("apellidos", rs.getString("apellidos"));
            dueno.put("dni", rs.getString("dni"));
            dueno.put("cantidad_mascotas", rs.getLong("cantidad_mascotas"));
            return dueno;
        }
    }
}