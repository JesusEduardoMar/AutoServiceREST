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

import com.itq.autoService.dto.Vehiculo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class VehiculoDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(VehiculoDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public int insertVehiculo(final Vehiculo vehiculoDto) {
        LOGGER.info("Insertando vehículo en la base de datos: {}", vehiculoDto);
        StringBuffer sql = new StringBuffer("");
        /*sql.append("INSERT INTO m_vehiculo (ID_VEHICULO, PLACA, MARCA, MODELO, COLOR) ");
        sql.append("VALUES (?, ?, ?, ?, ?)");*/
        sql.append("INSERT INTO m_vehiculo (PLACA, MARCA, MODELO, COLOR) ");
        sql.append("VALUES (?, ?, ?, ?)");
        

        final String query = sql.toString();

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            // Verificamos si ya existe un vehículo con los mismos datos antes de intentar insertarlo
            if (existeVehiculo(vehiculoDto)) {
                LOGGER.warn("Ya existe un vehículo con estos datos en la base de datos");
                return -1; // Retorna un valor negativo para indicar que ya existe
            }

        //try {
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

                   //ps.setInt(1, vehiculoDto.getIdVehiculo());
                    ps.setString(1, vehiculoDto.getPlaca());
                    ps.setString(2, vehiculoDto.getMarca());
                    ps.setInt(3, vehiculoDto.getModelo());
                    ps.setString(4, vehiculoDto.getColor());
          
                    return ps;
                }
            }, keyHolder);
        } catch (Exception e) {
            LOGGER.error("Error al insertar vehículo en la base de datos: {}", e.getMessage());
            e.printStackTrace();
        }

        int vehiculoId = keyHolder.getKey().intValue();
        LOGGER.info("Vehículo insertado con ID: {}", vehiculoId);

        return vehiculoId;
    }
    
    
    public boolean existeVehiculo(Vehiculo vehiculo) {
        String query = "SELECT COUNT(*) FROM m_vehiculo WHERE PLACA = ? AND MARCA = ? AND MODELO = ? AND COLOR = ?";
        try {
            int count = jdbcTemplate.queryForObject(query, Integer.class, vehiculo.getPlaca(), vehiculo.getMarca(),
                    vehiculo.getModelo(), vehiculo.getColor());
            return count > 0;
        } catch (EmptyResultDataAccessException e) {
            return false; // No se encontró ningún vehículo con esos datos
        }
    }
    
    
    public List<Vehiculo> getVehiculos() {
        String sql = "SELECT * FROM m_vehiculo";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Vehiculo.class));//Mapea las filas de la base de datos a objetos de tipo Vehiculo
    }
    
    @SuppressWarnings("deprecation")
	public Vehiculo getVehiculoById(int id) {
        try {
            String sql = "SELECT * FROM m_vehiculo WHERE ID_VEHICULO = ?";
            return jdbcTemplate.queryForObject(sql, new Object[] { id }, BeanPropertyRowMapper.newInstance(Vehiculo.class));
        } catch (EmptyResultDataAccessException e) {
            // Si no se encuentra ningún vehículo con ese ID
            return null;
        } catch (Exception e) {
            LOGGER.error("Error al recuperar el vehículo con ID: {}", id, e);
            throw new RuntimeException("Error al recuperar el vehículo con ID: " + id, e);
        }
    }
    
    public boolean deleteVehiculoById(int id) {
        LOGGER.info("Eliminando vehículo de la base de datos con ID: {}", id);

        try {
            // Consulta SQL para eliminar el vehículo por ID
            String sql = "DELETE FROM m_vehiculo WHERE ID_VEHICULO = ?";

            // Ejecutamos la consulta 
            int rowsAffected = jdbcTemplate.update(sql, id);

            // Devolvemos true si se eliminó al menos una fila, indicando éxito
            return rowsAffected > 0;
        } catch (Exception e) {
            LOGGER.error("Error al eliminar el vehículo con ID: {}", id, e);
            return false;
        }
    }
    
    public boolean vehiculoExiste(int idVehiculo) {
        String query = "SELECT COUNT(*) FROM m_vehiculo WHERE ID_VEHICULO = ?";
        try {
            int count = jdbcTemplate.queryForObject(query, Integer.class, idVehiculo);
            return count > 0;
        } catch (EmptyResultDataAccessException e) {
            return false; // No se encontró ningún vehículo con ese ID
        }
    }
  
}
