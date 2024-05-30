package com.example.coema.Listas;

import java.io.Serializable;

public class Tratamientos implements Serializable {


    private int id;
    private String nom, det, prec;

    public Tratamientos( String nom, String det, String prec) {
        this.nom = nom;
        this.det = det;
        this.prec = prec;
    }
    public Tratamientos(int id, String nom, String det, String prec) {
        this.id = id;
        this.nom = nom;
        this.det = det;
        this.prec = prec;
    }

    public Tratamientos() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDet() {
        return det;
    }

    public void setDet(String det) {
        this.det = det;
    }

    public String getPrec() {
        return prec;
    }

    public void setPrec(String prec) {
        this.prec = prec;
    }
}
