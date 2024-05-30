package com.example.coema.Registro;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coema.Conection.DatabaseConnection;
import com.example.coema.Index.Principal;
import com.example.coema.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegistroPacientes extends AppCompatActivity {

    EditText edtNom, edtApe, edtCor, edtCon, edtDni, edtTel, edtFec;
    TextView txtYaTienesCuenta;
    Spinner spnSexR;
    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar_paciente);

        asignarReferencias();
    }

    private void asignarReferencias() {
        edtCor = findViewById(R.id.txtEmailR);
        edtApe = findViewById(R.id.txtApeR);
        edtNom = findViewById(R.id.txtNomR);
        edtCon = findViewById(R.id.txtContraR);
        edtDni = findViewById(R.id.txtDniR);
        edtTel = findViewById(R.id.txtTelR);
        edtFec = findViewById(R.id.txtFechaR);

        txtYaTienesCuenta = findViewById(R.id.txtYaTienesCuenta);

        spnSexR = findViewById(R.id.spnSexR);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                new String[]{"Hombre", "Mujer"});
        spnSexR.setAdapter(adapter);

        txtYaTienesCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iLog = new Intent(RegistroPacientes.this, Principal.class);
                startActivity(iLog);
            }
        });

        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreUsuario = edtCor.getText().toString();
                String contrasena = edtCon.getText().toString();
                new DatabaseConnectionTask().execute(nombreUsuario, contrasena);
            }
        });

        edtFec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarSelector();
            }
        });
    }

    private void mostrarSelector() {
        final Calendar calendario = Calendar.getInstance();
        int aActual = calendario.get(Calendar.YEAR);
        int mActual = calendario.get(Calendar.MONTH);
        int dActual = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int a, int m, int d) {
                        final String fecha = d + "/" + (m + 1) + "/" + a;
                        edtFec.setText(fecha);
                    }
                }, aActual, mActual, dActual);
        datePickerDialog.show();
    }

    private class DatabaseConnectionTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            String nombreUsuario = params[0];
            String contrasena = params[1];

            int resultado = -1;

            try {
                Connection connection = DatabaseConnection.getConnection();
                if (connection != null) {
                    String selectedGender = spnSexR.getSelectedItem().toString();
                    String sqlUsuario = "INSERT INTO usuarios (nombre_usuario, contrasena, id_rol) VALUES (?, ?, ?)";
                    PreparedStatement pstUsuario = connection.prepareStatement(sqlUsuario, PreparedStatement.RETURN_GENERATED_KEYS);
                    pstUsuario.setString(1, nombreUsuario);
                    pstUsuario.setString(2, contrasena);
                    pstUsuario.setInt(3, 3);

                    int rowsAffectedUsuario = pstUsuario.executeUpdate();

                    if (rowsAffectedUsuario > 0) {
                        ResultSet generatedKeys = pstUsuario.getGeneratedKeys();
                        int idUsuario = -1;
                        if (generatedKeys.next()) {
                            idUsuario = generatedKeys.getInt(1);
                        }

                        // Convertir la cadena de fecha en un objeto java.sql.Date


                        String sqlPaciente = "INSERT INTO pacientes (id_paciente, nombres, apellidos, correo, contrasena, telefono, fecha_nacimiento, sexo, id_usuario) VALUES (?, ?, ?, ?, ?, ?,  to_date(?,'dd-MM-yyyy'), ?, ?)";
                        PreparedStatement pstPaciente = connection.prepareStatement(sqlPaciente);
                        pstPaciente.setString(1, edtDni.getText().toString());
                        pstPaciente.setString(2, edtNom.getText().toString());
                        pstPaciente.setString(3, edtApe.getText().toString());
                        pstPaciente.setString(4, edtCor.getText().toString());
                        pstPaciente.setString(5, edtCon.getText().toString());
                        pstPaciente.setString(6, edtTel.getText().toString());
                        pstPaciente.setString(7, edtFec.getText().toString());
                        pstPaciente.setString(8, selectedGender);
                        pstPaciente.setInt(9, idUsuario);

                        int rowsAffectedPaciente = pstPaciente.executeUpdate();

                        pstPaciente.close();

                        if (rowsAffectedPaciente > 0) {
                            resultado = idUsuario;
                        }
                    }

                    pstUsuario.close();

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return resultado;
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
            Toast.makeText(RegistroPacientes.this, mensaje, Toast.LENGTH_SHORT).show();
        }
    }
}
