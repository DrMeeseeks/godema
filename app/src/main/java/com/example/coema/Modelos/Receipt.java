package com.example.coema.Modelos;

public class Receipt {
    private long id; //id de la tabla
    private String name; // nombre del doctor
    private String date; // fecha de recibo
    private double amount; // monto

    public Receipt(long id, String name, String date, double amount) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }
}