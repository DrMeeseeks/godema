package com.example.coema.Registro;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coema.Conection.DatabaseConnection;
import com.example.coema.R;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistroOdontologo extends AppCompatActivity {

    TextInputEditText edtCod,edtApellidos, edtNombres, edtCorreo, edtDate,edtContra;
    Button btnAgregar, btnSiguiente;

    Spinner spnEsp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_odontologo);
        edtNombres= findViewById(R.id.edtNombres);
        edtApellidos= findViewById(R.id.edtApellidos);
        edtCorreo= findViewById(R.id.edtCorreo);
        edtCod= findViewById(R.id.edtCod);
        edtContra= findViewById(R.id.edtContra);
        btnAgregar=(Button) findViewById(R.id.btnSesionDes);
        btnSiguiente=(Button) findViewById(R.id.btnSiguiente);
        spnEsp = findViewById(R.id.spnEsp);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                new String[]{"Ortodoncia", "Implantología", "Endodoncia", "Periodoncia", "Rehabilitación oral", "Cirugía BMF","Odontopediatria" });
        spnEsp.setAdapter(adapter);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistroOdontologo.InsertarOdoTask task = new RegistroOdontologo.InsertarOdoTask();
                task.execute();}

        });
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iCrud = new Intent(RegistroOdontologo.this, CrudOdontologo.class);
                startActivity(iCrud);}

        });


    }


    private class InsertarOdoTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {

            try {
                Connection connection = DatabaseConnection.getConnection();
                if (connection != null) {
                    try {
                        String selectedEsp = spnEsp.getSelectedItem().toString();
                        // Inicia la transacción
                        connection.setAutoCommit(false);

                        // Primera sentencia SQL
                        String sqlUsu = "INSERT INTO usuarios (nombre_usuario, contrasena, id_rol) VALUES (?, ?, 2)";
                        PreparedStatement pstUsu = connection.prepareStatement(sqlUsu);
                        pstUsu.setString(1, edtCorreo.getText().toString());
                        pstUsu.setString(2, edtContra.getText().toString());
                        pstUsu.executeUpdate();
                        pstUsu.close();

                        // Segunda sentencia SQL
                        String sqlDoctor = "INSERT INTO doctores (id_doctor, nombres, apellidos, especialidad, id_usuario) VALUES (?, ?, ?, (select especialidadid from especialidadesodontologicas where nombreespecialidad= ?), (select id_usuario from usuarios where nombre_usuario= ? and contrasena= ?))";
                        PreparedStatement pstDoctor = connection.prepareStatement(sqlDoctor);
                        pstDoctor.setString(1, edtCod.getText().toString());
                        pstDoctor.setString(2, edtNombres.getText().toString());
                        pstDoctor.setString(3, edtApellidos.getText().toString());
                        pstDoctor.setString(4, selectedEsp);
                        pstDoctor.setString(5, edtCorreo.getText().toString());
                        pstDoctor.setString(6, edtContra.getText().toString());
                        pstDoctor.executeUpdate();
                        pstDoctor.close();

                        // Confirma la transacción
                        connection.commit();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        try {
                            // En caso de error, realiza un rollback
                            connection.rollback();
                        } catch (SQLException rollbackException) {
                            rollbackException.printStackTrace();
                        }
                    } finally {
                        try {
                            // Asegúrate de volver al modo de autocommit
                            connection.setAutoCommit(true);
                        } catch (SQLException autoCommitException) {
                            autoCommitException.printStackTrace();
                        }
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer idUsuario) {
            if (idUsuario != -1) {
                mostrarToast("Usuario registrado exitosamente");
            } else {
                mostrarToast("No se pudo registrar el usuario");
            }
        }

        private void mostrarToast(String mensaje) {
            Toast.makeText(RegistroOdontologo.this, mensaje, Toast.LENGTH_SHORT).show();
        }
    }


}