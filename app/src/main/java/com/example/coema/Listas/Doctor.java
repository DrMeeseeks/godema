package com.example.coema.Listas;

import java.io.Serializable;

public class Doctor implements Serializable {


    private int id_doctores;
    private String nom, ape, espe,correo,contrasena;

    public Doctor(int id_doctores, String nom, String ape, String espe, String correo, String contrasena) {
        this.id_doctores = id_doctores;
        this.nom = nom;
        this.ape = ape;
        this.espe = espe;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public Doctor(String nom, String ape, String espe, String correo, String contrasena) {
        this.nom = nom;
        this.ape = ape;
        this.espe = espe;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public Doctor() {

    }

    public int getId_doctores() {
        return id_doctores;
    }

    public void setId_doctores(int id_doctores) {
        this.id_doctores = id_doctores;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getApe() {
        return ape;
    }

    public void setApe(String ape) {
        this.ape = ape;
    }

    public String getEspe() {
        return espe;
    }

    public void setEspe(String espe) {
        this.espe = espe;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
