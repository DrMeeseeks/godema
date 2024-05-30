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
import com.example.coema.Index.ListarDescanso;
import com.example.coema.Index.ListarTratamientos;
import com.example.coema.Listas.Descanso;
import com.example.coema.Listas.Tratamientos;
import com.example.coema.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistroDescansoEdit extends AppCompatActivity {

    private EditText pacienteEditText;
    private EditText tratamientoEditText;
    private EditText descansoEditText;
    private Button updateDescansoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_descanso_edit);

        // Enlazar vistas
        tratamientoEditText = findViewById(R.id.tratamientoEditText);
        pacienteEditText = findViewById(R.id.pacienteEditText);
        descansoEditText = findViewById(R.id.descansoEditText);
        updateDescansoButton = findViewById(R.id.addDescansoButton);

        // Obtener los datos ingresados por el usuario desde la actividad anterior
        Intent intent = getIntent();
        int selectedDescansoId = intent.getIntExtra("selectedDescansoId", -1);

        // Verificar si se proporcionó un ID de recibo válido
        if (selectedDescansoId != -1) {
            // Iniciar una tarea asincrónica para obtener los detalles del recibo de la base de datos
            new ObtenerDetallesDescansoAsyncTask( selectedDescansoId).execute();
        } else {
            Toast.makeText(this, "ID de descanso no válido" + selectedDescansoId, Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad si no se proporcionó un ID de recibo válido
        }

        // Configurar el botón para actualizar el pago
        updateDescansoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los nuevos datos ingresados por el usuario
                String newTratamiento = tratamientoEditText.getText().toString();
                String newPaciente = pacienteEditText.getText().toString();
                String newDescanso= descansoEditText.getText().toString();

                // Iniciar una tarea asincrónica para actualizar el recibo en la base de datos
                new ActualizarDescansoAsyncTask(selectedDescansoId, newPaciente, newTratamiento, newDescanso).execute();
            }
        });
    }

    // Tarea asincrónica para obtener los detalles del recibo desde la base de datos
    private class ObtenerDetallesDescansoAsyncTask extends AsyncTask<Void, Void, Descanso> {
        private int selectedDescansoId;

        public ObtenerDetallesDescansoAsyncTask(int selectedDescansoId) {
            this.selectedDescansoId = selectedDescansoId;
        }

        @Override
        protected Descanso doInBackground(Void... voids) {
            Connection conn = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            try {
                conn = DatabaseConnection.getConnection();

                if (conn != null) {
                    // Query para obtener los detalles del recibo por ID (sin incluir motivocobro)
                    String query = "SELECT nombre, tratamiento, descanso FROM descanso WHERE id = ?";
                    statement = conn.prepareStatement(query);
                    statement.setInt(1, selectedDescansoId);
                    resultSet = statement.executeQuery();

                    // Verificar si se encontró el recibo
                    if (resultSet.next()) {
                        String name = resultSet.getString("nombre");
                        String date = resultSet.getString("detalle");
                        String amount = resultSet.getString("precio");

                        return new Descanso(selectedDescansoId, name, date, amount);
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
        protected void onPostExecute(Descanso descansos) {
            super.onPostExecute(descansos);

            if (descansos != null) {
                // Llenar los campos de texto con los detalles del recibo
                tratamientoEditText.setText(descansos.getTratamiento());
                pacienteEditText.setText(descansos.getNombre());
                descansoEditText.setText(String.valueOf(descansos.getDescanso()));

            } else {
                Toast.makeText(RegistroDescansoEdit.this, "Descanso no encontrado", Toast.LENGTH_SHORT).show();
                finish(); // Cierra la actividad si el recibo no se encontró
            }
        }
    }

    // Tarea asincrónica para actualizar el recibo en la base de datos
    private class ActualizarDescansoAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private int selectedDescansoId;
        private String newPaciente;

        private String newDescanso;
        private String newTratamiento;

        public ActualizarDescansoAsyncTask(int selectedDescansoId, String newPaciente, String newTratamiento, String newDescanso) {
            this.selectedDescansoId = selectedDescansoId;
            this.newPaciente = newPaciente;
            this.newTratamiento = newTratamiento;
            this.newDescanso = newDescanso;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Connection conn = null;
            PreparedStatement statement = null;

            try {
                conn = DatabaseConnection.getConnection();

                if (conn != null) {
                    // Query para actualizar el recibo (sin incluir motivocobro)
                    String query = "UPDATE descanso SET nombre = ?, tratamiento = ?, descanso = ? WHERE id = ?";
                    statement = conn.prepareStatement(query);
                    statement.setString(1, newPaciente);
                    statement.setString(2, newTratamiento);
                    statement.setString(3, newDescanso);
                    statement.setInt(4, selectedDescansoId);

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
                Toast.makeText(RegistroDescansoEdit.this, "Actualización realizada", Toast.LENGTH_LONG).show();

                // Redirigir de vuelta a MenuRecibosActivity
                Intent intent = new Intent(RegistroDescansoEdit.this, ListarDescanso.class);
                startActivity(intent);
                finish(); // Cierra la actividad actual para evitar que el usuario vuelva atrás
            } else {
                // Mostrar un mensaje de error si la actualización falla
                Toast.makeText(RegistroDescansoEdit.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
