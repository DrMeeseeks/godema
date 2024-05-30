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
    import com.example.coema.Index.ListarRecetas;
    import com.example.coema.Listas.Descanso;
    import com.example.coema.Listas.Receta;
    import com.example.coema.R;

    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;

    public class RegistroRecetaEdit extends AppCompatActivity {
        private EditText pacienteEditText;
        private EditText medicamentoEditText;
        private EditText dosisEditText;
        private Button updateRecetaButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.registro_receta_edit);

            // Enlazar vistas
            medicamentoEditText = findViewById(R.id.medicamentoEditText);
            pacienteEditText = findViewById(R.id.pacienteEditText);
            dosisEditText = findViewById(R.id.dosisEditText);
            updateRecetaButton = findViewById(R.id.addRecetaButton);

            // Obtener los datos ingresados por el usuario desde la actividad anterior
            Intent intent = getIntent();
            int selectedRecetaId = intent.getIntExtra("selectedRecetaId", -1);

            // Verificar si se proporcionó un ID de recibo válido
            if (selectedRecetaId != -1) {
                // Iniciar una tarea asincrónica para obtener los detalles del recibo de la base de datos
                new RegistroRecetaEdit.ObtenerDetallesRecetaAsyncTask( selectedRecetaId).execute();
            } else {
                Toast.makeText(this, "ID de receta no válido" + selectedRecetaId, Toast.LENGTH_SHORT).show();
                finish(); // Cierra la actividad si no se proporcionó un ID de recibo válido
            }

            // Configurar el botón para actualizar el pago
            updateRecetaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtener los nuevos datos ingresados por el usuario
                    String newMedicamento = medicamentoEditText.getText().toString();
                    String newPaciente = pacienteEditText.getText().toString();
                    String newDosis= dosisEditText.getText().toString();

                    // Iniciar una tarea asincrónica para actualizar el recibo en la base de datos
                    new RegistroRecetaEdit.ActualizarRecetaAsyncTask(selectedRecetaId, newPaciente, newMedicamento, newDosis).execute();
                }
            });
        }

        // Tarea asincrónica para obtener los detalles del recibo desde la base de datos
        private class ObtenerDetallesRecetaAsyncTask extends AsyncTask<Void, Void, Receta> {
            private int selectedRecetaId;

            public ObtenerDetallesRecetaAsyncTask(int selectedRecetaId) {
                this.selectedRecetaId = selectedRecetaId;
            }

            @Override
            protected Receta doInBackground(Void... voids) {
                Connection conn = null;
                PreparedStatement statement = null;
                ResultSet resultSet = null;

                try {
                    conn = DatabaseConnection.getConnection();

                    if (conn != null) {
                        // Query para obtener los detalles del recibo por ID (sin incluir motivocobro)
                        String query = "SELECT nombre, tratamiento, descanso FROM descanso WHERE id = ?";
                        statement = conn.prepareStatement(query);
                        statement.setInt(1, selectedRecetaId);
                        resultSet = statement.executeQuery();

                        // Verificar si se encontró el recibo
                        if (resultSet.next()) {
                            String name = resultSet.getString("nombre");
                            String date = resultSet.getString("detalle");
                            String amount = resultSet.getString("precio");

                            return new Receta(selectedRecetaId, name, date, amount);
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
            protected void onPostExecute(Receta recetas) {
                super.onPostExecute(recetas);

                if (recetas != null) {
                    // Llenar los campos de texto con los detalles del recibo
                    medicamentoEditText.setText(recetas.getMedicamento());
                    pacienteEditText.setText(recetas.getNombre());
                    dosisEditText.setText(String.valueOf(recetas.getDosis()));

                } else {
                    Toast.makeText(RegistroRecetaEdit.this, "Receta no encontrado", Toast.LENGTH_SHORT).show();
                    finish(); // Cierra la actividad si el recibo no se encontró
                }
            }
        }

        // Tarea asincrónica para actualizar el recibo en la base de datos
        private class ActualizarRecetaAsyncTask extends AsyncTask<Void, Void, Boolean> {
            private int selectedRecetaId;
            private String newPaciente;

            private String newMedicamento;
            private String newDosis;

            public ActualizarRecetaAsyncTask(int selectedRecetaId, String newPaciente, String newMedicamento, String newDosis) {
                this.selectedRecetaId = selectedRecetaId;
                this.newPaciente = newPaciente;
                this.newDosis = newDosis;
                this.newMedicamento = newMedicamento;
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                Connection conn = null;
                PreparedStatement statement = null;

                try {
                    conn = DatabaseConnection.getConnection();

                    if (conn != null) {
                        // Query para actualizar el recibo (sin incluir motivocobro)
                        String query = "UPDATE receta SET nombre = ?, medicamento = ?, receta = ? WHERE id = ?";
                        statement = conn.prepareStatement(query);
                        statement.setString(1, newPaciente);
                        statement.setString(2, newMedicamento);
                        statement.setString(3, newDosis);
                        statement.setInt(4, selectedRecetaId);

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
                    Toast.makeText(RegistroRecetaEdit.this, "Actualización realizada", Toast.LENGTH_LONG).show();

                    // Redirigir de vuelta a MenuRecibosActivity
                    Intent intent = new Intent(RegistroRecetaEdit.this, ListarDescanso.class);
                    startActivity(intent);
                    finish(); // Cierra la actividad actual para evitar que el usuario vuelva atrás
                } else {
                    // Mostrar un mensaje de error si la actualización falla
                    Toast.makeText(RegistroRecetaEdit.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
