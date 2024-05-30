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
import com.example.coema.Index.ListarTratamientos;
import com.example.coema.Index.MenuRecibosActivity;
import com.example.coema.Listas.Tratamientos;
import com.example.coema.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistroTratamientosEdit extends AppCompatActivity {

    private EditText tratamientoEditText;
    private EditText amountEditText;
    private EditText detalleEditText;
    private Button updateTratamientoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_tratamientos_edit);

        // Enlazar vistas
        tratamientoEditText = findViewById(R.id.tratamientoEditText);
        amountEditText = findViewById(R.id.amountEditText);
        detalleEditText = findViewById(R.id.detalleEditText);
        updateTratamientoButton = findViewById(R.id.addTratamientoButton);

        // Obtener los datos ingresados por el usuario desde la actividad anterior
        Intent intent = getIntent();
        int selectedTratamientoId = intent.getIntExtra("selectedTratamientosId", -1);

        // Verificar si se proporcionó un ID de recibo válido
        if (selectedTratamientoId != -1) {
            // Iniciar una tarea asincrónica para obtener los detalles del recibo de la base de datos
            new ObtenerDetallesTratamientoAsyncTask( selectedTratamientoId).execute();
        } else {
            Toast.makeText(this, "ID de tratamiento no válido" + selectedTratamientoId, Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad si no se proporcionó un ID de recibo válido
        }

        // Configurar el botón para actualizar el pago
        updateTratamientoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los nuevos datos ingresados por el usuario
                String newTratamiento = tratamientoEditText.getText().toString();
                String newMonto = amountEditText.getText().toString();
                String newDetalle = detalleEditText.getText().toString();

                // Iniciar una tarea asincrónica para actualizar el recibo en la base de datos
                new ActualizarTratamientoAsyncTask(selectedTratamientoId, newTratamiento, newMonto, newDetalle).execute();
            }
        });
    }

    // Tarea asincrónica para obtener los detalles del recibo desde la base de datos
    private class ObtenerDetallesTratamientoAsyncTask extends AsyncTask<Void, Void, Tratamientos> {
        private int selectedTratamientoId;

        public ObtenerDetallesTratamientoAsyncTask(int selectedTratamientoId) {
            this.selectedTratamientoId = selectedTratamientoId;
        }

        @Override
        protected Tratamientos doInBackground(Void... voids) {
            Connection conn = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            try {
                conn = DatabaseConnection.getConnection();

                if (conn != null) {
                    // Query para obtener los detalles del recibo por ID (sin incluir motivocobro)
                    String query = "SELECT nombre, detalle, precio FROM tratamientos WHERE id_tratamiento = ?";
                    statement = conn.prepareStatement(query);
                    statement.setInt(1, selectedTratamientoId);
                    resultSet = statement.executeQuery();

                    // Verificar si se encontró el recibo
                    if (resultSet.next()) {
                        String name = resultSet.getString("nombre");
                        String date = resultSet.getString("detalle");
                        String amount = resultSet.getString("precio");

                        return new Tratamientos(selectedTratamientoId, name, date, amount);
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
        protected void onPostExecute(Tratamientos tratamientos) {
            super.onPostExecute(tratamientos);

            if (tratamientos != null) {
                // Llenar los campos de texto con los detalles del recibo
                tratamientoEditText.setText(tratamientos.getNom());
                detalleEditText.setText(tratamientos.getDet());
                amountEditText.setText(String.valueOf(tratamientos.getPrec()));

            } else {
                Toast.makeText(RegistroTratamientosEdit.this, "Tratamiento no encontrado", Toast.LENGTH_SHORT).show();
                finish(); // Cierra la actividad si el recibo no se encontró
            }
        }
    }

    // Tarea asincrónica para actualizar el recibo en la base de datos
    private class ActualizarTratamientoAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private int selectedTratamientoId;
        private String newPaciente;

        private String newDetalle;
        private String newMotivoCobro;

        public ActualizarTratamientoAsyncTask(int selectedTratamientoId, String newPaciente, String newDetalle, String newMotivoCobro) {
            this.selectedTratamientoId = selectedTratamientoId;
            this.newPaciente = newPaciente;
            this.newDetalle = newDetalle;
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
                    String query = "UPDATE tratamientos SET nombre = ?, detalle = ?, precio = CAST(? AS numeric) WHERE id_tratamiento = ?";
                    statement = conn.prepareStatement(query);
                    statement.setString(1, newPaciente);
                    statement.setString(2, newMotivoCobro);
                    statement.setString(3, newDetalle);
                    statement.setInt(4, selectedTratamientoId);

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
                Toast.makeText(RegistroTratamientosEdit.this, "Actualización realizada", Toast.LENGTH_LONG).show();

                // Redirigir de vuelta a MenuRecibosActivity
                Intent intent = new Intent(RegistroTratamientosEdit.this, ListarTratamientos.class);
                startActivity(intent);
                finish(); // Cierra la actividad actual para evitar que el usuario vuelva atrás
            } else {
                // Mostrar un mensaje de error si la actualización falla
                Toast.makeText(RegistroTratamientosEdit.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
