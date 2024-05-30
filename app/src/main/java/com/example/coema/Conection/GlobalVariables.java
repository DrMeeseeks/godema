package com.example.coema.Conection;

public class GlobalVariables {
    private static GlobalVariables instance;
    private int userId;

    private GlobalVariables() {
        // Constructor privado para prevenir la creación de múltiples instancias
    }

    public static GlobalVariables getInstance() {
        if (instance == null) {
            instance = new GlobalVariables();
        }
        return instance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

