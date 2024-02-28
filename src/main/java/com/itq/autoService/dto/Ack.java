package com.itq.autoService.dto;

import java.util.List;
import java.util.Map;

public class Ack {
    private int code;
    private String description;
    private Vehiculo vehiculo;
    private List<Conductor> conductores;
    private Map<String, String> errors;

    // Constructor solo con código y descripción (para casos generales)
    public Ack(int code, String description) {
        this.code = code;
        this.description = description;
    }

    // Constructor para casos con lista de conductores
    public Ack(int code, String description, List<Conductor> conductores) {
        this(code, description);  // Llama al constructor general para establecer código y descripción
        this.conductores = conductores;
    }

    // Constructor para casos con vehículo
    public Ack(int code, String description, Vehiculo vehiculo) {
        this(code, description);  // Llama al constructor general para establecer código y descripción
        this.vehiculo = vehiculo;
        this.conductores = null;  // Establece conductores como null para evitar su inclusión
    }
    
    //Para manejo de errorres
    public Ack(int code, String description, Map<String, String> errors) {
        this.code = code;
        this.description = description;
        this.errors = errors;
    }

    // Getters y setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getters y setters para la lista de conductores
    public List<Conductor> getConductores() {
        return conductores;
    }

    public void setConductores(List<Conductor> conductores) {
        this.conductores = conductores;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }
}
