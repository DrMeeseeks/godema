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
    import com.example.coema.Index.ListarTratamientos;
    import com.example.coema.Index.MenuRecibosActivity;
    import com.example.coema.R;

    import java.sql.Connection;
    import java.sql.PreparedStatement;

    public class RegistroTratamientos extends AppCompatActivity {
        private EditText tratamientoEditText;
        private EditText detalleEditText;
        private EditText montoEditText;
        private Button agregarTratamientoButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_tratamiento);

            tratamientoEditText = findViewById(R.id.tratamientoEditText);
            montoEditText = findViewById(R.id.amountEditText);
            detalleEditText = findViewById(R.id.detalleEditText);
            agregarTratamientoButton = findViewById(R.id.addTratamientoButton);

            agregarTratamientoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtener los valores de los campos
                    String paciente = tratamientoEditText.getText().toString();
                    String montoStr = montoEditText.getText().toString();
                    String motivo = detalleEditText.getText().toString();

                    // Validar que todos los campos estén llenos
                    if (paciente.isEmpty() || montoStr.isEmpty() || motivo.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Realizar la inserción en la base de datos en segundo plano
                    new InsertarDatosAsyncTask().execute(paciente, motivo, montoStr);
                }
            });
        }
        private class InsertarDatosAsyncTask extends AsyncTask<String, Void, Boolean> {
            @Override
            protected Boolean doInBackground(String... params) {
                String paciente = params[0];
                String motivo = params[1];
                double monto = Double.parseDouble(params[2]);


                try {
                    Connection conn = DatabaseConnection.getConnection();

                    if (conn != null) {
                        String consultaSQL = "INSERT INTO tratamientos (nombre, detalle, precio) VALUES (?, ?, ?)";
                        PreparedStatement preparedStatement = conn.prepareStatement(consultaSQL);
                        preparedStatement.setString(1, paciente);
                        preparedStatement.setString(2, motivo);
                        preparedStatement.setDouble(3, monto);

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
                    Intent intent = new Intent(RegistroTratamientos.this, ListarTratamientos.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Error al realizar el registro.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
