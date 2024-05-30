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
    import com.example.coema.R;

    import java.sql.Connection;
    import java.sql.PreparedStatement;

    public class RegistroDescansos extends AppCompatActivity {
        private EditText pacienteEditText;
        private EditText tratamientoEditText;
        private Button agregarDescansoButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_descanso);


            pacienteEditText = findViewById(R.id.pacienteEditText);
            tratamientoEditText = findViewById(R.id.tratCitEditText);
            agregarDescansoButton = findViewById(R.id.addDescansoButton);

            agregarDescansoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtener los valores de los campos
                    String paciente = pacienteEditText.getText().toString();
                    String tratamiento = tratamientoEditText.getText().toString();

                    // Validar que todos los campos estén llenos
                    if (paciente.isEmpty() || tratamiento.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Realizar la inserción en la base de datos en segundo plano
                    new InsertarDatosAsyncTask().execute(paciente, tratamiento);
                }
            });
        }
        private class InsertarDatosAsyncTask extends AsyncTask<String, Void, Boolean> {
            @Override
            protected Boolean doInBackground(String... params) {
                String paciente = params[0];
                String tratamiento = params[1];


                try {
                    Connection conn = DatabaseConnection.getConnection();

                    if (conn != null) {
                        String consultaSQL = "INSERT INTO descanso (nombre, tratamiento) VALUES (?, ?)";
                        PreparedStatement preparedStatement = conn.prepareStatement(consultaSQL);
                        preparedStatement.setString(1, paciente);
                        preparedStatement.setString(2, tratamiento);

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
                    Intent intent = new Intent(RegistroDescansos.this, ListarDescanso.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Error al realizar el registro.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
