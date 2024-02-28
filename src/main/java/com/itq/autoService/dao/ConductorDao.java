package com.itq.autoService.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.itq.autoService.dto.Conductor;
import com.itq.autoService.dto.Vehiculo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Repository
public class ConductorDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConductorDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insertConductor(final Conductor conductorDto) {
        LOGGER.info("Insertando conductor en la base de datos: {}", conductorDto);
        StringBuffer sql = new StringBuffer("");
       /*sql.append("INSERT INTO conductores (ID_CONDUCTOR, NOMBRE, TIPO_LICENCIA) ");
        sql.append("VALUES (?, ?, ?)");*/
        sql.append("INSERT INTO conductores (NOMBRE, TIPO_LICENCIA, DIRECCION, TELEFONO, FECHA_NACIMIENTO, EMAIL) ");
        sql.append("VALUES (?, ?, ?, ?, ?, ?)");

        final String query = sql.toString();

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        try {
        	
        	if (existeConductor(conductorDto)) {
                LOGGER.warn("Ya existe un vehículo con estos datos en la base de datos");
                return -1; // Retorna un valor negativo para indicar que ya existe
            }
        	
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

                    //ps.setInt(1, conductorDto.getIdConductor());
                    ps.setString(1, conductorDto.getNombre());
                    ps.setString(2, conductorDto.getTipoLicencia());
                    ps.setString(3, conductorDto.getDireccion());
                    ps.setString(4, conductorDto.getTelefono());
                    ps.setString(5, conductorDto.getFechaNacimiento());
                    ps.setString(6, conductorDto.getEmail());

                    return ps;
                }
            }, keyHolder);
        } catch (Exception e) {
            LOGGER.error("Error al insertar conductor en la base de datos: {}", e.getMessage());
            e.printStackTrace();
        }

        int conductorId = keyHolder.getKey().intValue();
        LOGGER.info("Conductor insertado con ID: {}", conductorId);

        return conductorId;
    }
    
    
    public boolean existeConductor(Conductor conductor) {
        String query = "SELECT COUNT(*) FROM conductores WHERE NOMBRE = ? AND TIPO_LICENCIA = ?";
        try {
            int count = jdbcTemplate.queryForObject(query, Integer.class, conductor.getNombre(), conductor.getTipoLicencia());
            return count > 0;
        } catch (EmptyResultDataAccessException e) {
            return false; // No se encontró ningún conductor con esos datos
        }
    }
    
    
    public List<Conductor> getConductores() {
        try {
            String sql = "SELECT * FROM conductores";
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Conductor.class));
        } catch (Exception e) {
            LOGGER.error("Error al recuperar conductores de la base de datos: {}", e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    public List<Conductor> getConductoresPorNombre(String nombre) {
        try {
            String sql = "SELECT * FROM conductores WHERE NOMBRE LIKE ?";
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Conductor.class), "%" + nombre + "%");
        } catch (Exception e) {
            LOGGER.error("Error al recuperar conductores por nombre de la base de datos: {}", e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Conductor> getConductoresPorTipoLicencia(String tipoLicencia) {
        try {
            String sql = "SELECT * FROM conductores WHERE TIPO_LICENCIA = ?";
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Conductor.class), tipoLicencia);
        } catch (Exception e) {
            LOGGER.error("Error al recuperar conductores por tipo de licencia de la base de datos: {}", e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    public void deleteConductorById(int id) {
        String query = "DELETE FROM conductores WHERE ID_CONDUCTOR = ?";
        try {
            jdbcTemplate.update(query, id);
        } catch (Exception e) {
            LOGGER.error("Error al eliminar el conductor por ID", e);
        }
    }
    
    
    public void asociarVehiculo(int idConductor, int idVehiculo) {
        String query = "INSERT INTO conductor_vehiculo (ID_CONDUCTOR, ID_VEHICULO) VALUES (?, ?)";
        try {
            LOGGER.info("Asociando vehículo al conductor. ID Conductor: {}, ID Vehículo: {}", idConductor, idVehiculo);
            jdbcTemplate.update(query, idConductor, idVehiculo);
            LOGGER.info("Asociación exitosa.");
        } catch (Exception e) {
            LOGGER.error("Error al asociar el vehículo al conductor", e);
        }
    }


    
    public void desasociarVehiculo(int idConductor, int idVehiculo) {
        String query = "DELETE FROM conductor_vehiculo WHERE ID_CONDUCTOR = ? AND ID_VEHICULO = ?";
        try {
            jdbcTemplate.update(query, idConductor, idVehiculo);
        } catch (Exception e) {
            LOGGER.error("Error al desasociar el vehículo del conductor", e);
        }
    }
    
    public List<Vehiculo> getVehiculosAsociados(int idConductor) {
        String query = "SELECT v.* FROM m_vehiculo v " +
                       "JOIN conductor_vehiculo cv ON v.ID_VEHICULO = cv.ID_VEHICULO " +
                       "WHERE cv.ID_CONDUCTOR = ?";
        try {
            return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Vehiculo.class), idConductor);
        } catch (Exception e) {
            LOGGER.error("Error al recuperar los vehículos asociados al conductor", e);
            return Collections.emptyList();
        }
    }
    
    public boolean conductorExiste(int idConductor) {
        String query = "SELECT COUNT(*) FROM conductores WHERE ID_CONDUCTOR = ?";
        try {
            int count = jdbcTemplate.queryForObject(query, Integer.class, idConductor);
            return count > 0;
        } catch (EmptyResultDataAccessException e) {
            return false; // No se encontró ningún conductor con ese ID
        }
    }
    
  
}
