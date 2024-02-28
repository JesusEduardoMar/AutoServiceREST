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
import com.itq.autoService.dto.Conductor;
import com.itq.autoService.dto.Vehiculo;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/conductores")
public class ConductorServiceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConductorServiceController.class);
    @Autowired
    private ConductorDao ConductorDao;  // Agrega esta línea para inyectar VehiculoDao
    @Autowired
    private VehiculoDao VehiculoDao;


    @PostMapping(consumes = "application/json", produces = "application/json")
    public Ack createConductor(@Valid @RequestBody Conductor conductor, BindingResult bindingResult) {
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
        	
        	if (ConductorDao.existeConductor(conductor)) {
                // Ya existe un vehículo con estos datos
                LOGGER.warn("Ya existe un conductor con estos datos en la base de datos");
                return new Ack(409, "Ya existe un conductor con estos datos en la base de datos");
            }
            // Llamamos al DAO para insertar un conductor en la base de datos
            int conductorId = ConductorDao.insertConductor(conductor);

            LOGGER.info("Conductor creado con ID: {}", conductorId);

            // Devolvemos una respuesta de éxito (con el objeto Ack)
            return new Ack(201, "Conductor creado exitosamente");
        } catch (Exception e) {
            // Devolvemos una respuesta de error
            LOGGER.error("Error al crear el conductor", e);
            return new Ack(500, "Error al procesar la solicitud");
        }
    }


    @GetMapping(produces = "application/json")
    //public List<Conductor> getConductores() {
    public Object getConductores() {
        try {
            List<Conductor> conductores = ConductorDao.getConductores();
            LOGGER.info("Conductores recuperados exitosamente");
            return conductores;
        } catch (Exception e) {
        	LOGGER.error("Error al recuperar los conductores", e);
            return new Ack(500, "Error al procesar la solicitud");
        }
    }
        
    /*@GetMapping("/conductoresPorNombre")
   // public List<Conductor> getConductoresPorNombre(@RequestParam(value = "nombre") String nombre) {
    	public Object getConductoresPorNombre(@RequestParam(value = "nombre") String nombre) {
        try {
            List<Conductor> conductores = ConductorDao.getConductoresPorNombre(nombre);
            LOGGER.info("Conductores recuperados por nombre exitosamente");
            return conductores;
        } catch (Exception e) {
            LOGGER.error("Error al recuperar conductores por nombre", e);
            return new Ack(500, "Error al procesar la solicitud");
            // Devolvemos una lista vacía 
            //return Collections.emptyList();
        }
    }*/
    
    @GetMapping("/conductoresPorNombre")
    public Ack getConductoresPorNombre(@RequestParam(value = "nombre") String nombre) {
        try {
            List<Conductor> conductores = ConductorDao.getConductoresPorNombre(nombre);

            if (conductores.isEmpty()) {
                // No se encontraron conductores con ese nombre
            	LOGGER.info("No se encontraron conductores con el nombre: {}", nombre);
                return new Ack(404, "No se encontraron conductores con el nombre: " + nombre);
            } else {
                // Se encontraron conductores
                LOGGER.info("Conductores recuperados por nombre exitosamente");
                return new Ack(200, "Conductores recuperados exitosamente", conductores);
            }
        } catch (Exception e) {
            LOGGER.error("Error al recuperar conductores por nombre", e);
            return new Ack(500, "Error al procesar la solicitud");
        }
    }
    
    
    /*  @GetMapping("/conductoresPorTipoLicencia")
     	public Object getConductoresPorTipoLicencia(@RequestParam(value = "tipoLicencia") String tipoLicencia) {
         try {
             List<Conductor> conductores = ConductorDao.getConductoresPorTipoLicencia(tipoLicencia);
             LOGGER.info("Conductores recuperados por tipo de licencia exitosamente");
             return conductores;
         } catch (Exception e) {
             LOGGER.error("Error al recuperar conductores por tipo de licencia", e);
             return new Ack(500, "Error al procesar la solicitud");
             // Devolvemos una lista vacía 
             //return Collections.emptyList();
         }
     }*/

    @GetMapping("/conductoresPorTipoLicencia")
    public Object getConductoresPorTipoLicencia(@RequestParam(value = "tipoLicencia") String tipoLicencia) {
        try {
            List<Conductor> conductores = ConductorDao.getConductoresPorTipoLicencia(tipoLicencia);

            if (!conductores.isEmpty()) {
                LOGGER.info("Conductores recuperados por tipo de licencia exitosamente");
                return new Ack(200, "Conductores recuperados exitosamente por tipo de licencia", conductores);
            } else {
                LOGGER.info("No se encontraron conductores con el tipo de licencia: {}", tipoLicencia);
                return new Ack(404, "No se encontraron conductores con el tipo de licencia especificado");
            }
        } catch (Exception e) {
            LOGGER.error("Error al recuperar conductores por tipo de licencia", e);
            return new Ack(500, "Error al procesar la solicitud");
        }
    }



    @DeleteMapping(value = "/{id}", produces = "application/json")
    public Ack deleteConductor(@PathVariable("id") int id) {
        try {
            ConductorDao.deleteConductorById(id);
            LOGGER.info("Conductor eliminado exitosamente (ID: {})", id);
            return new Ack(204, "Conductor eliminado exitosamente (ID: " + id + ")");
        } catch (Exception e) {
            LOGGER.error("Error al eliminar el conductor (ID: {})", id, e);
            return new Ack(500, "Error al procesar la solicitud");
        }
    }

 
    
   /* @PutMapping(value = "/{id_conductor}/vehiculos/{id_vehiculo}", produces = "application/json")
    public Ack asociarVehiculo(@PathVariable("id_conductor") int idConductor, @PathVariable("id_vehiculo") int idVehiculo) {
        try {
            ConductorDao.asociarVehiculo(idConductor, idVehiculo);
            LOGGER.info("Vehículo asociado al conductor exitosamente (ID Conductor: {}, ID Vehículo: {})", idConductor, idVehiculo);
            return new Ack(200, "Vehículo asociado al conductor exitosamente (ID Conductor: " + idConductor + ", ID Vehículo: " + idVehiculo + ")");
        } catch (Exception e) {
            LOGGER.error("Error al asociar el vehículo al conductor (ID Conductor: {}, ID Vehículo: {})", idConductor, idVehiculo, e);
            return new Ack(500, "Error al procesar la solicitud");
        }
    }*/
    

    @PutMapping(value = "/{id_conductor}/vehiculos/{id_vehiculo}", produces = "application/json")
    public Ack asociarVehiculo(@PathVariable("id_conductor") int idConductor, @PathVariable("id_vehiculo") int idVehiculo) {
        try {
            // Verifica que el conductor y el vehículo existan antes de asociarlos
            if (ConductorDao.conductorExiste(idConductor) && VehiculoDao.vehiculoExiste(idVehiculo)) {
                ConductorDao.asociarVehiculo(idConductor, idVehiculo);
                LOGGER.info("Vehículo asociado al conductor exitosamente (ID Conductor: {}, ID Vehículo: {})", idConductor, idVehiculo);
                return new Ack(200, "Vehículo asociado al conductor exitosamente (ID Conductor: " + idConductor + ", ID Vehículo: " + idVehiculo + ")");
            } else {
            	LOGGER.warn("Conductor o vehículo no encontrado (ID Conductor: {}, ID Vehículo: {})", idConductor, idVehiculo);

                return new Ack(404, "Conductor o vehículo no encontrado");
            }
        } catch (Exception e) {
            LOGGER.error("Error al asociar vehículo al conductor", e);
            return new Ack(500, "Error al procesar la solicitud");
        }
    }

    @DeleteMapping(value = "/{id_conductor}/vehiculos/{id_vehiculo}", produces = "application/json")
    public Ack desasociarVehiculo(@PathVariable("id_conductor") int idConductor, @PathVariable("id_vehiculo") int idVehiculo) {
        try {
            // Verifica que el conductor y el vehículo existan antes de desasociarlos
            if (ConductorDao.conductorExiste(idConductor) && VehiculoDao.vehiculoExiste(idVehiculo)) {
                ConductorDao.desasociarVehiculo(idConductor, idVehiculo);
                LOGGER.info("Vehículo desasociado del conductor exitosamente (ID Conductor: {}, ID Vehículo: {})", idConductor, idVehiculo);
                return new Ack(200, "Vehículo desasociado del conductor exitosamente (ID Conductor: " + idConductor + ", ID Vehículo: " + idVehiculo + ")");
            } else {
                LOGGER.warn("Conductor o vehículo no encontrado (ID Conductor: {}, ID Vehículo: {})", idConductor, idVehiculo);
                return new Ack(404, "Conductor o vehículo no encontrado");
            }
        } catch (Exception e) {
            LOGGER.error("Error al desasociar vehículo del conductor", e);
            return new Ack(500, "Error al procesar la solicitud");
        }
    }



    /* @DeleteMapping(value = "/{id_conductor}/vehiculos/{id_vehiculo}", produces = "application/json")
    public Ack desasociarVehiculo(@PathVariable("id_conductor") int idConductor, @PathVariable("id_vehiculo") int idVehiculo) {
        try {
            ConductorDao.desasociarVehiculo(idConductor, idVehiculo);
            LOGGER.info("Vehículo desasociado del conductor exitosamente (ID Conductor: {}, ID Vehículo: {})", idConductor, idVehiculo);
            return new Ack(200, "Vehículo desasociado del conductor exitosamente (ID Conductor: " + idConductor + ", ID Vehículo: " + idVehiculo + ")");
        } catch (Exception e) {
            LOGGER.error("Error al desasociar el vehículo del conductor (ID Conductor: {}, ID Vehículo: {})", idConductor, idVehiculo, e);
            return new Ack(500, "Error al procesar la solicitud");
        }
    }*/
    
    @GetMapping(value = "/{id_conductor}/vehiculos", produces = "application/json")
    public List<Vehiculo> getVehiculosAsociados(@PathVariable("id_conductor") int idConductor) {
        try {
            List<Vehiculo> vehiculos = ConductorDao.getVehiculosAsociados(idConductor);
            LOGGER.info("Vehículos asociados al conductor recuperados exitosamente (ID Conductor: {})", idConductor);
            return vehiculos;
        } catch (Exception e) {
            LOGGER.error("Error al recuperar los vehículos asociados al conductor (ID Conductor: {})", idConductor, e);
            // Devolver una lista vacía o manejar el error según tu necesidad
            return Collections.emptyList();
        }
    }
    

    
}
