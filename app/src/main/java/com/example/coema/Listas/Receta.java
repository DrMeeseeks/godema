package com.example.coema.Listas;

import java.io.Serializable;

public class Receta implements Serializable {

    private String nombre, tratamiento, medicamento, dosis;
    private int id;

    public Receta() {
    }

    public Receta(int id, String nombre, String medicamento, String dosis) {

        this.id = id;
        this.nombre = nombre;
        this.medicamento = medicamento;
        this.dosis = dosis;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
