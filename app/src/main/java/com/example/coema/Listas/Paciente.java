package com.example.coema.Listas;

import com.example.coema.Modelos.Receipt;

import java.io.Serializable;

public class Paciente implements Serializable {
    private String nombre, apellido, correo, contra, telefono, fecNac, sexo;
    private int id_foto, id;

    public Paciente(int id,  String nombre, String apellido, String correo) {

        this.id = id;
        this.nombre = nombre;
            this.apellido = apellido;
            this.correo = correo;

    }

    public Paciente(String nombre, String apellido, String correo, String contra, String telefono, String fecNac, String sexo, int id_foto, int id) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contra = contra;
        this.telefono = telefono;
        this.fecNac = fecNac;
        this.sexo = sexo;
        this.id_foto = id_foto;
        this.id = id;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFecNac() {
        return fecNac;
    }

    public void setFecNac(String fecNac) {
        this.fecNac = fecNac;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getId_foto() {
        return id_foto;
    }

    public void setId_foto(int id_foto) {
        this.id_foto = id_foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

