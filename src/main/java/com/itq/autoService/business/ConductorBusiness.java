package com.itq.autoService.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.itq.autoService.dao.ConductorDao;
import com.itq.autoService.dto.Conductor;

@Service
public class ConductorBusiness {

    @Autowired
    private ConductorDao conductorDao;

    public int crearConductor(Conductor conductor) {
        // Lógica de negocio, si es necesario

        // Llamada al método del DAO para insertar el conductor en la base de datos
        return conductorDao.insertConductor(conductor);
    }
}
