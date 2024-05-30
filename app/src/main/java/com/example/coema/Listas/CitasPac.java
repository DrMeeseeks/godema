package com.example.coema.Listas;

import java.io.Serializable;
import java.util.Date;

public class CitasPac implements Serializable {

    private int id;
    private String nom;
    private String trat;
    private Date fec;

    public CitasPac(int id, String nom, String trat, Date fec) {
        this.id=id;
        this.nom = nom;
        this.trat = trat;
        this.fec = fec;
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

    public String getTrat() {
        return trat;
    }

    public void setTrat(String trat) {
        this.trat = trat;
    }

    public Date getFec() {
        return fec;
    }

    public void setFec(Date fec) {
        this.fec = fec;
    }

}
