package com.itq.autoService.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class Vehiculo {
	
	private int idVehiculo;
	@Pattern(regexp = "[A-Z]{3}-\\d{3}$") // para patrones o restricciones en el contrato
    private String placa;
	@Pattern(regexp="Chevrolet|Hyundai|Toyota|Nissan|Kia")//para patrones
    private String marca;
	@Min(1995)// Valores minimos y maximos en el contrato
	@Max(2026)
	@NotNull 
    private int modelo;
    private String color;

    public int getIdVehiculo() {
        return idVehiculo;
    }
    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getModelo() {
        return modelo;
    }

    public void setModelo(int modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
