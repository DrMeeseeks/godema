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
import com.example.coema.Index.MenuRecibosActivity;
import com.example.coema.Modelos.Receipt;
import com.example.coema.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistroPagoEdit extends AppCompatActivity {

    private EditText patientEditText;
    private EditText dateEditText;
    private EditText amountEditText;
    private EditText reasonEditText;
    private Button updatePaymentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_pago_edit);

        // Enlazar vistas
        patientEditText = findViewById(R.id.patientEditText);
        dateEditText = findViewById(R.id.dateEditText);
        amountEditText = findViewById(R.id.amountEditText);
        reasonEditText = findViewById(R.id.reasonEditText);
        updatePaymentButton = findViewById(R.id.addPaymentButton);

        // Obtener los datos ingresados por el usuario desde la actividad anterior
        Intent intent = getIntent();
        long selectedReceiptId = intent.getLongExtra("selectedReceiptId", -1);

        // Verificar si se proporcionó un ID de recibo válido
        if (selectedReceiptId != -1) {
            // Iniciar una tarea asincrónica para obtener los detalles del recibo de la base de datos
            new ObtenerDetallesReciboAsyncTask(selectedReceiptId).execute();
        } else {
            Toast.makeText(this, "ID de recibo no válido", Toast.LENGTH_SHORT).show();
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
                new ActualizarReciboAsyncTask(selectedReceiptId, newPaciente, newFecha, newMonto, newMotivoCobro).execute();
            }
        });
    }

    // Tarea asincrónica para obtener los detalles del recibo desde la base de datos
    private class ObtenerDetallesReciboAsyncTask extends AsyncTask<Void, Void, Receipt> {
        private long selectedReceiptId;

        public ObtenerDetallesReciboAsyncTask(long selectedReceiptId) {
            this.selectedReceiptId = selectedReceiptId;
        }

        @Override
        protected Receipt doInBackground(Void... voids) {
            Connection conn = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            try {
                conn = DatabaseConnection.getConnection();

                if (conn != null) {
                    // Query para obtener los detalles del recibo por ID (sin incluir motivocobro)
                    String query = "SELECT name, date, amount FROM receipt WHERE id = ?";
                    statement = conn.prepareStatement(query);
                    statement.setLong(1, selectedReceiptId);
                    resultSet = statement.executeQuery();

                    // Verificar si se encontró el recibo
                    if (resultSet.next()) {
                        String name = resultSet.getString("name");
                        String date = resultSet.getString("date");
                        double amount = resultSet.getDouble("amount");

                        return new Receipt(selectedReceiptId, name, date, amount);
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
        protected void onPostExecute(Receipt receipt) {
            super.onPostExecute(receipt);

            if (receipt != null) {
                // Llenar los campos de texto con los detalles del recibo
                patientEditText.setText(receipt.getName());
                dateEditText.setText(receipt.getDate());
                amountEditText.setText(String.valueOf(receipt.getAmount()));

            } else {
                Toast.makeText(RegistroPagoEdit.this, "Recibo no encontrado", Toast.LENGTH_SHORT).show();
                finish(); // Cierra la actividad si el recibo no se encontró
            }
        }
    }

    // Tarea asincrónica para actualizar el recibo en la base de datos
    private class ActualizarReciboAsyncTask extends AsyncTask<Void, Void, Boolean> {
        private long selectedReceiptId;
        private String newPaciente;
        private String newFecha;
        private String newMonto;
        private String newMotivoCobro;

        public ActualizarReciboAsyncTask(long selectedReceiptId, String newPaciente, String newFecha, String newMonto, String newMotivoCobro) {
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
                    String query = "UPDATE receipt SET name = ?, date = ?, amount = ? WHERE id = ?";
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
                Toast.makeText(RegistroPagoEdit.this, "Actualización realizada", Toast.LENGTH_LONG).show();

                // Redirigir de vuelta a MenuRecibosActivity
                Intent intent = new Intent(RegistroPagoEdit.this, MenuRecibosActivity.class);
                startActivity(intent);
                finish(); // Cierra la actividad actual para evitar que el usuario vuelva atrás
            } else {
                // Mostrar un mensaje de error si la actualización falla
                Toast.makeText(RegistroPagoEdit.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
