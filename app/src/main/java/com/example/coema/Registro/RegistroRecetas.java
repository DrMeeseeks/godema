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
    import com.example.coema.R;

    import java.sql.Connection;
    import java.sql.PreparedStatement;

    public class RegistroRecetas  extends AppCompatActivity {
        private EditText pacienteEditText;
        private EditText medicamentoEditText;
        private EditText dosisEditText;
        private Button agregarRecetaButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_receta);


            pacienteEditText = findViewById(R.id.pacienteEditText);
            medicamentoEditText = findViewById(R.id.medicamentoEditText);
            dosisEditText = findViewById(R.id.dosisEditText);
            agregarRecetaButton = findViewById(R.id.addRecetaButton);

            agregarRecetaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtener los valores de los campos
                    String paciente = pacienteEditText.getText().toString();
                    String medicamento = medicamentoEditText.getText().toString();
                    String dosis = dosisEditText.getText().toString();

                    // Validar que todos los campos estén llenos
                    if (paciente.isEmpty() || medicamento.isEmpty() || dosis.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Realizar la inserción en la base de datos en segundo plano
                    new RegistroRecetas.InsertarDatosAsyncTask().execute(paciente, medicamento, dosis);
                }
            });
        }
        private class InsertarDatosAsyncTask extends AsyncTask<String, Void, Boolean> {
            @Override
            protected Boolean doInBackground(String... params) {
                String paciente = params[0];
                String medicamento = params[1];
                String dosis = params[2];


                try {
                    Connection conn = DatabaseConnection.getConnection();

                    if (conn != null) {
                        String consultaSQL = "INSERT INTO receta (nombre, medicamento, dosis) VALUES (?, ?, ?)";
                        PreparedStatement preparedStatement = conn.prepareStatement(consultaSQL);
                        preparedStatement.setString(1, paciente);
                        preparedStatement.setString(2, medicamento);
                        preparedStatement.setString(3, dosis);

                        int filasAfectadas = preparedStatement.executeUpdate();



                        return filasAfectadas > 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean exito) {
                super.onPostExecute(exito);

                if (exito) {
                    Toast.makeText(getApplicationContext(), "Registro realizado con éxito.", Toast.LENGTH_SHORT).show();
                    // Redirigir a la vista MenuRecibosActivity
                    Intent intent = new Intent(RegistroRecetas.this, ListarRecetas.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Error al realizar el registro.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
