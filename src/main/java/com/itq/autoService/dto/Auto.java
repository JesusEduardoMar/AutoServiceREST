package com.itq.autoService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class Auto {
	
	@JsonProperty("id") // hace referencia a que asi voy a ser la correspondencia (elemento id) que tengo en mi contrato
	@Pattern(regexp="[A-Z](2)[0-9](2)")// para patrones o restricciones en el contrato
	private String idAuto;
	@Min(1995)// Valores minimos y maximos en el contrato
	@Max(2026)
	@NotNull 
    private int modelo;
	@Pattern(regexp="[Chevrolet, Mercedes]")//para patrones
    private String marca;
	@NotNull  //
	@NotEmpty // no debe de estar vacío
    private String color;
    
    
    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

	public void setMarca(String marca) {
		this.marca = marca;
	}
	
	public String getMarca() {
        return marca;
    }
	
	public void setModelo(int modelo) {
		this.modelo = modelo;
	}
	
	public int getModelo() {
        return modelo;
    }

	public String getIdAuto() {
		return idAuto;
	}

	public void setIdAuto(String idAuto) {
		this.idAuto = idAuto;
	}


}
