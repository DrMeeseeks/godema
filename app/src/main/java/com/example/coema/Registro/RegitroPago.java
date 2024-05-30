    package com.example.coema.Registro;

    import android.app.DatePickerDialog;
    import android.content.Intent;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.DatePicker;
    import android.widget.EditText;
    import android.widget.Toast;
    import androidx.appcompat.app.AppCompatActivity;
    import com.example.coema.Conection.DatabaseConnection;
    import com.example.coema.Index.MenuRecibosActivity;
    import com.example.coema.R;
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.SQLException;

    public class RegitroPago extends AppCompatActivity {
        private EditText pacienteEditText;
        private EditText fechaEditText;
        private EditText montoEditText;
        private EditText motivoEditText;
        private Button agregarPagoButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.add_receipt);

            pacienteEditText = findViewById(R.id.patientEditText);
            fechaEditText = findViewById(R.id.dateEditText);
            montoEditText = findViewById(R.id.amountEditText);
            motivoEditText = findViewById(R.id.reasonEditText);
            agregarPagoButton = findViewById(R.id.addPaymentButton);

            agregarPagoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtener los valores de los campos
                    String paciente = pacienteEditText.getText().toString();
                    String fecha = fechaEditText.getText().toString();
                    String montoStr = montoEditText.getText().toString();
                    String motivo = motivoEditText.getText().toString();

                    // Validar que todos los campos estén llenos
                    if (paciente.isEmpty() || fecha.isEmpty() || montoStr.isEmpty() || motivo.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Realizar la inserción en la base de datos en segundo plano
                    new InsertarDatosAsyncTask().execute(paciente, fecha, montoStr);
                }
            });
        }
        public void showDatePickerDialog(View v) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    // Cuando se seleccione una fecha, actualiza el campo de texto de fecha
                    String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth; // Formatea la fecha como desees
                    fechaEditText.setText(selectedDate);
                }
            }, 2023, 0, 1);
            datePickerDialog.show();
        }
        private class InsertarDatosAsyncTask extends AsyncTask<String, Void, Boolean> {
            @Override
            protected Boolean doInBackground(String... params) {
                String paciente = params[0];
                String fecha = params[1];
                double monto = Double.parseDouble(params[2]);


                try {
                    Connection conn = DatabaseConnection.getConnection();

                    if (conn != null) {
                        String consultaSQL = "INSERT INTO receipt (name, date, amount) VALUES (?, ?, ?)";
                        PreparedStatement preparedStatement = conn.prepareStatement(consultaSQL);
                        preparedStatement.setString(1, paciente);
                        preparedStatement.setString(2, fecha);
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
                    Intent intent = new Intent(RegitroPago.this, MenuRecibosActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Error al realizar el registro.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
