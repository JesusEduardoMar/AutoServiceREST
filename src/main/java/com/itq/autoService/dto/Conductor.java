package com.itq.autoService.dto;

import jakarta.validation.constraints.Pattern;

public class Conductor {
	
	private int idConductor;
	@Pattern(regexp = "[A-Za-z ]+$") // para patrones o restricciones en el contrato
    private String nombre;
	@Pattern(regexp = "[A-E]$")
    private String tipoLicencia;
	@Pattern(regexp = "^[A-Za-z0-9\\s\\-\\.,]+$")
    private String direccion;
	@Pattern(regexp = "^\\+?\\d+$")
    private String telefono;
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")//formato yyyy-MM-dd
    private String fechaNacimiento;
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$")
    private String email;

    public int getIdConductor() {
        return idConductor;
    }

    public void setIdConductor(int idConductor) {
        this.idConductor = idConductor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoLicencia() {
        return tipoLicencia;
    }

    public void setTipoLicencia(String tipoLicencia) {
        this.tipoLicencia = tipoLicencia;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
