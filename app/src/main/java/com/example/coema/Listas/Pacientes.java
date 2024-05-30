package com.example.coema.Listas;

import java.util.Date;

public class Pacientes {
    private int idCita;
    private int idTratamiento;
    private Date fechaCita;
    private String estadoCita; // Agregar este campo para el estado de la cita
    private String nombreTratamiento; // Agregar este campo para el nombre del tratamiento

    public Pacientes(int idCita, Date fechaCita, String estadoCita, String nombreTratamiento) {
        this.idCita = idCita;
        this.idTratamiento = idTratamiento;
        this.fechaCita = fechaCita;
        this.estadoCita = estadoCita;
        this.nombreTratamiento = nombreTratamiento;
    }

    public int getIdCita() {
        return idCita;
    }

    public int getIdTratamiento() {
        return idTratamiento;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public String getEstadoCita() {
        return estadoCita;
    }

    public String getNombreTratamiento() {
        return nombreTratamiento;
    }
}


