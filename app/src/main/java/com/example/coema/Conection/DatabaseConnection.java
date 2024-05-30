package com.example.coema.Conection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.coema.Registro.RegistroPacientes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

public class DatabaseConnection {

    private Context context;

    public DatabaseConnection(Context context) {
        this.context = context;
    }

    private static Connection conn = null;

    public static Connection getConnection() throws SQLException {
        if (conn == null) {
            try {
                Class.forName("org.postgresql.Driver");
                String url = "jdbc:postgresql://dpg-corcqb7sc6pc73dmn500-a.oregon-postgres.render.com:5432/coema";
                String user = "coema_deidad";
                String password = "yR7nvHnyZMfw2VXiEOM0fdNPQoFLGyfQ";

                Properties properties = new Properties();
                properties.setProperty("user", user);
                properties.setProperty("password", password);
                properties.setProperty("ssl", "true");
                properties.setProperty("sslmode", "require");

                conn = DriverManager.getConnection(url, properties);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
