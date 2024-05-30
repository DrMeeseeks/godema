package com.example.coema.Listas;

import java.io.Serializable;

public class Descanso implements Serializable {

    private String nombre, descanso, tratamiento;
    private int id;

    public Descanso() {
    }

    public Descanso(int id, String nombre, String descanso, String tratamiento) {
        this.id = id;
        this.nombre = nombre;
        this.descanso = descanso;
        this.tratamiento = tratamiento;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescanso() {
        return descanso;
    }

    public void setDescanso(String descanso) {
        this.descanso = descanso;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
