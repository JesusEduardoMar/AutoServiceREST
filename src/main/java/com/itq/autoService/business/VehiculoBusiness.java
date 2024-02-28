package com.itq.autoService.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itq.autoService.dao.VehiculoDao;
import com.itq.autoService.dto.Vehiculo;

@Service
public class VehiculoBusiness {

    @Autowired
    private VehiculoDao vehiculoDao;

    public int crearVehiculo(Vehiculo vehiculo) {
        // Lógica de negocio, si es necesario

        // Llamada al método del DAO para insertar el vehículo en la base de datos
        return vehiculoDao.insertVehiculo(vehiculo);
    }
}
