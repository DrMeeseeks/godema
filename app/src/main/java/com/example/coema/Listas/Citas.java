package com.example.coema.Listas;

import java.io.Serializable;

public class Citas implements Serializable {


    private int id_foto, id;
    private String nom, odo, trat, hor, fecCit, apePat, apeMat, sexo, correo;

    public Citas(int id_foto, String nom, String odo, String trat, String hor, String fecCit, String apePat, String apeMat, String sexo, String correo) {
        this.id_foto = id_foto;
        this.nom = nom;
        this.odo = odo;
        this.trat = trat;
        this.hor = hor;
        this.fecCit = fecCit;
        this.apePat = apePat;
        this.apeMat = apeMat;
        this.sexo = sexo;
        this.correo = correo;

    }

    public Citas(int id, int id_foto, String nom, String odo, String trat, String hor, String fecCit, String apePat, String apeMat, String sexo, String correo) {
        this.id = id;
        this.id_foto = id_foto;
        this.nom = nom;
        this.odo = odo;
        this.trat = trat;
        this.hor = hor;
        this.fecCit = fecCit;
        this.apePat = apePat;
        this.apeMat = apeMat;
        this.sexo = sexo;
        this.correo = correo;

    }

    public Citas() {

    }

    public int getIdFoto() {
        return id_foto;
    }

    public void setIdFoto(int id_foto) {
        this.id_foto = id_foto;
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

    public String getOdo() {
        return odo;
    }

    public void setOdo(String odo) {
        this.odo = odo;
    }

    public String getTrat() {
        return trat;
    }

    public void setTrat(String trat) {
        this.trat = trat;
    }

    public String getHor() {
        return hor;
    }

    public void setHor(String hor) {
        this.hor = hor;
    }

    public String getFecCit() {
        return fecCit;
    }

    public void setFecCit(String fecCit) {
        this.fecCit = fecCit;
    }

    public String getApePat() {
        return apePat;
    }

    public void setApePat(String apePat) {
        this.apePat = apePat;
    }

    public String getApeMat() {
        return apeMat;
    }

    public void setApeMat(String apeMat) {
        this.apeMat = apeMat;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}