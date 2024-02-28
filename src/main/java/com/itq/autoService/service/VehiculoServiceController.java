package com.itq.autoService.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import com.itq.autoService.dao.ConductorDao;
import com.itq.autoService.dao.VehiculoDao;
import com.itq.autoService.dto.Ack;
import com.itq.autoService.dto.Vehiculo;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/vehiculo")
public class VehiculoServiceController {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(VehiculoServiceController.class);
    @Autowired
    private VehiculoDao VehiculoDao;  //inyectar VehiculoDao

    
   /* @PostMapping(consumes = "application/json", produces = "application/json")
    public Ack createVehiculo(@Valid @RequestBody Vehiculo vehiculo) {
    	// lógica

        Ack ack = new Ack(201, "Vehículo creado exitosamente");
        LOGGER.info("Vehículo creado: {}", vehiculo);
        return ack;
    }*/
    
    @PostMapping(consumes = "application/json", produces = "application/json")
    public Ack createVehiculo(@Valid @RequestBody Vehiculo vehiculo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Si hay errores de validación construimos una respuesta personalizada
            Map<String, String> errors = new HashMap<>();
            
         // Agregamos el primer error al logger
            FieldError firstError = bindingResult.getFieldErrors().get(0);
            LOGGER.error("Error de validación en el campo '{}': {}", firstError.getField(), firstError.getDefaultMessage());

            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new Ack(400, "Error de validación", errors);
        }

        try {
        	
        	if (VehiculoDao.existeVehiculo(vehiculo)) {
                // Ya existe un vehículo con estos datos
                LOGGER.warn("Ya existe un vehículo con estos datos en la base de datos");
                return new Ack(409, "Ya existe un vehículo con estos datos en la base de datos");
            }
        	
            // Llamamos al DAO para insertar vehículos en la base de datos
            int vehiculoId = VehiculoDao.insertVehiculo(vehiculo);

            LOGGER.info("Vehículo creado con ID: {}", vehiculoId);

            // Devolvemos una respuesta de éxito (con el objeto Ack)
            return new Ack(201, "Vehículo creado exitosamente");
        } catch (Exception e) {
            // Devolvemos una respuesta de error
            LOGGER.error("Error al crear el vehículo", e);
            return new Ack(500, "Error al procesar la solicitud");
        }
    }


 /*   @GetMapping(produces = "application/json")
    public Ack getVehiculos() {
        try {
            VehiculoDao.getVehiculos();
            LOGGER.info("Vehículos recuperados exitosamente");
            // Puedes procesar la lista de vehículos aquí si es necesario
            return new Ack(200, "Vehículos recuperados exitosamente");
        } catch (Exception e) {
            LOGGER.error("Error al recuperar los vehículos", e);
            return new Ack(500, "Error al procesar la solicitud");
        }
    }*/
    @GetMapping(produces = "application/json")
    public List<Vehiculo> getVehiculos() {
        try {
            List<Vehiculo> vehiculos = VehiculoDao.getVehiculos();
            LOGGER.info("Vehículos recuperados exitosamente");
            return vehiculos;
        } catch (Exception e) {
            LOGGER.error("Error al recuperar los vehículos", e);
            // Devolvemos una lista vacía 
            return Collections.emptyList();
        }
    }
    
    @GetMapping(value = "/{id}", produces = "application/json")
    public Object getVehiculoById(@PathVariable("id") int id) {
        try {
            Vehiculo vehiculo = VehiculoDao.getVehiculoById(id);

            if (vehiculo != null) {
                LOGGER.info("Vehículo recuperado exitosamente (ID: {})", id);
                return new Ack(200, "Vehículo recuperado exitosamente", vehiculo);
            } else {
                LOGGER.warn("No se encontró un vehículo con ID: {}", id);
                return new Ack(404, "No se encontró un vehículo con ID: " + id);
            }
        } catch (Exception e) {
            LOGGER.error("Error al recuperar el vehículo con ID: {}", id, e);
            return new Ack(500, "Error al procesar la solicitud");
        }
    }





    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public Ack updateVehiculo(@PathVariable("id") int id, @Valid @RequestBody Vehiculo vehiculo) {
       

        return new Ack(200, "Vehículo actualizado exitosamente (ID: " + id + ")");
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public Ack deleteVehiculo(@PathVariable("id") int id) {
        try {
            // Llamamos al DAO para eliminar un vehículo por ID
            boolean eliminado = VehiculoDao.deleteVehiculoById(id);

            if (eliminado) {
                LOGGER.info("Vehículo eliminado exitosamente (ID: {})", id);
                return new Ack(204, "Vehículo eliminado exitosamente (ID: " + id + ")");
            } else {
                LOGGER.warn("No se encontró un vehículo con ID: {} para eliminar", id);
                          return new Ack(404, "No se encontró un vehículo con ID: " + id);
            }
        } catch (Exception e) {
            LOGGER.error("Error al eliminar el vehículo con ID: {}", id, e);
            return new Ack(500, "Error al procesar la solicitud");
        }
    }
    

}
