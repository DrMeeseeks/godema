package com.example.coema.Registro;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coema.Conection.DatabaseConnection;
import com.example.coema.Index.ActMenuAdmin;
import com.example.coema.Index.MenuRecibosActivity;
import com.example.coema.Listas.Paciente;
import com.example.coema.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistroPacientesEdit extends AppCompatActivity {

    private EditText patientEditText;
    private EditText dateEditText;
    private EditText amountEditText;
    private EditText reasonEditText;
    private Button updatePaymentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_paciente_edit);

        // Enlazar vistas
        patientEditText = findViewById(R.id.nombreEditText);
        amountEditText = findViewById(R.id.apellidoEditText);
        reasonEditText = findViewById(R.id.correoEditText);
        updatePaymentButton = findViewById(R.id.addPacienteButton);

        // Obtener los datos ingresados por el usuario desde la actividad anterior
        Intent intent = getIntent();
        long selectedPacienteId = intent.getLongExtra("selectedPacienteId", -1);

        // Verificar si se proporcionó un ID de recibo válido
        if (selectedPacienteId != -1) {
            // Iniciar una tarea asincrónica para obtener los detalles del recibo de la base de datos
            new ObtenerDetallesPacienteAsyncTask(selectedPacienteId).execute();
        } else {
            Toast.makeText(this, "ID de paciente no válido", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad si no se proporcionó un ID de recibo válido
        }

        // Configurar el botón para actualizar el pago
        updatePaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los nuevos datos ingresados por el usuario
                String newPaciente = patientEditText.getText().toString();
                String newFecha = dateEditText.getText().toString();
                String newMonto = amountEditText.getText().toString();
                String newMotivoCobro = reasonEditText.getText().toString();

                // Iniciar una tarea asincrónica para actualizar el recibo en la base de datos
                new ActualizarPacienteAsyncTask(selectedPacienteId, newPaciente, newFecha, newMonto, newMotivoCobro).execute();
            }
        });
    }

    // Tarea asincrónica para obtener los detalles del recibo desde la base de datos
    private class ObtenerDetallesPacienteAsyncTask extends AsyncTask<Void, Void, Paciente> {
        private long selectedPacienteId;

        public ObtenerDetallesPacienteAsyncTask(long selectedPacienteId) {
            this.selectedPacienteId = selectedPacienteId;
        }

        @Override
        protected Paciente doInBackground(Void... voids) {
            Connection conn = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            try {
                conn = DatabaseConnection.getConnection();

                if (conn != null) {
                    // Query para obtener los detalles del recibo por ID (sin incluir motivocobro)
                    String query = "SELECT nombres, apellidos, correo FROM pacientes WHERE id = ?";
                    statement = conn.prepareStatement(query);
                    statement.setLong(1, selectedPacienteId);
                    resultSet = statement.executeQuery();

                    // Verificar si se encontró el recibo
                    if (resultSet.next()) {
                        String name = resultSet.getString("nombres");
                        String date = resultSet.getString("apellidos");
                        double amount = resultSet.getDouble("correo");
                        int a= (int)selectedPacienteId;
                        return new Paciente(a, name, date, amount+"");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Cerrar la conexión, el statement y el resultSet
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                // No cerrar la conexión aquí para que no se cierre accidentalmente
            }

            return null; // Devolver null si no se encontró el recibo
        }

        @Override
        protected void onPostExecute(Paciente paciente) {
            super.onPostExecute(paciente);

            if (paciente != null) {
                // Llenar los campos de texto con los detalles del recibo
                patientEditText.setText(paciente.getNombre());
                dateEditText.setText(paciente.getApellido());
                amountEditText.setText(String.valueOf(paciente.getCorreo()));

            } else {
                Toast.makeText(RegistroPacientesEdit.this, "Paciente no encontrado", Toast.LENGTH_SHORT).show();
                finish(); // Cierra la actividad si el recibo no se encontró
            }
        }
    }

    // Tarea asincrónica para actualizar el recibo en la base de datos
    private class ActualizarPacienteAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private long selectedReceiptId;
        private String newPaciente;
        private String newFecha;
        private String newMonto;
        private String newMotivoCobro;

        public ActualizarPacienteAsyncTask(long selectedReceiptId, String newPaciente, String newFecha, String newMonto, String newMotivoCobro) {
            this.selectedReceiptId = selectedReceiptId;
            this.newPaciente = newPaciente;
            this.newFecha = newFecha;
            this.newMonto = newMonto;
            this.newMotivoCobro = newMotivoCobro;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection conn = null;
            PreparedStatement statement = null;

            try {
                conn = DatabaseConnection.getConnection();

                if (conn != null) {
                    // Query para actualizar el recibo (sin incluir motivocobro)
                    String query = "UPDATE pacientes SET name = ?, date = ?, amount = ? WHERE id = ?";
                    statement = conn.prepareStatement(query);
                    statement.setString(1, newPaciente);
                    statement.setString(2, newFecha);
                    statement.setDouble(3, Double.parseDouble(newMonto));
                    statement.setLong(4, selectedReceiptId);

                    // Ejecutar la actualización
                    int rowsAffected = statement.executeUpdate();

                    // Verificar si se realizó la actualización correctamente
                    return rowsAffected > 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Cerrar la conexión y el statement
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                // No cerrar la conexión aquí para que no se cierre accidentalmente
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {
                // Actualización exitosa
                Toast.makeText(RegistroPacientesEdit.this, "Actualización realizada", Toast.LENGTH_LONG).show();

                // Redirigir de vuelta a MenuRecibosActivity
                Intent intent = new Intent(RegistroPacientesEdit.this, ActMenuAdmin.class);
                startActivity(intent);
                finish(); // Cierra la actividad actual para evitar que el usuario vuelva atrás
            } else {
                // Mostrar un mensaje de error si la actualización falla
                Toast.makeText(RegistroPacientesEdit.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
