package com.example.coema.Registro;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coema.Conection.DatabaseConnection;
import com.example.coema.Conection.GlobalVariables;
import com.example.coema.Perfil.PacientePrincipal;
import com.example.coema.R;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

public class RegistroCitas extends AppCompatActivity {

    EditText edtFecCita;
    Spinner sprTratCita, sprHorCita, sprDocCita;
    Button btnRegistrarCita;
    Button btnInicioCita;
    ArrayList<String> listaTratamientos = new ArrayList<>();
    ArrayList<String> listaHorarios = new ArrayList<>();
    private ArrayList<Integer> listaIdTratamientos = new ArrayList<>();
    String pacAct;
    int idActual = GlobalVariables.getInstance().getUserId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar_cita);
        asignarReferencias();
        new LoadEspecialidadesTask().execute();
    }

    private void asignarReferencias() {
        edtFecCita = findViewById(R.id.edtFecCita);
        sprTratCita = findViewById(R.id.sprTratCita);
        sprHorCita = findViewById(R.id.sprHorCita);
        btnRegistrarCita = findViewById(R.id.btnRegistrar);
        btnInicioCita = findViewById(R.id.btnInicioCita);

        btnRegistrarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarCita();
            }
        });

        edtFecCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarSelectorFecha();
            }
        });

        btnInicioCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistroCitas.this, PacientePrincipal.class);
                startActivity(intent);
            }
        });
    }

    private void cargarListaHorarios(String fechaSeleccionada) {
        listaHorarios.clear();

        Calendar calendar = Calendar.getInstance();
        String[] diasSemana = {"", "Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);

        if (diaSemana == Calendar.SATURDAY) {
            for (int hora = 9; hora <= 14; hora++) {
                String horario = String.format("%02d:00", hora);
                listaHorarios.add(horario);
            }
        } else {
            for (int hora = 9; hora <= 16; hora += 2) {
                String horarioInicio = String.format("%02d:00", hora);
                String horarioFin = String.format("%02d:00", hora + 2);
                String horario = horarioInicio + " - " + horarioFin;
                listaHorarios.add(horario);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaHorarios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprHorCita.setAdapter(adapter);
    }

    private void mostrarSelectorFecha() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
                edtFecCita.setText(fechaSeleccionada);
                cargarListaHorarios(fechaSeleccionada);
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        final Calendar minDate = Calendar.getInstance();
        minDate.add(Calendar.DAY_OF_MONTH, 1);
        final Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.DAY_OF_MONTH, 365);

        datePickerDialog.getDatePicker().init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, monthOfYear, dayOfMonth);
                if (selectedDate.before(minDate) || selectedDate.after(maxDate)) {
                    view.updateDate(minDate.get(Calendar.YEAR), minDate.get(Calendar.MONTH), minDate.get(Calendar.DAY_OF_MONTH));
                } else if (selectedDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    selectedDate.add(Calendar.DAY_OF_MONTH, 1);
                    view.updateDate(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
                }
            }
        });

        datePickerDialog.show();
    }

    private void registrarCita() {
        int especialidadID = listaIdTratamientos.get(sprTratCita.getSelectedItemPosition());
        String fechaCita = edtFecCita.getText().toString();
        String horaCita = sprHorCita.getSelectedItem().toString();
        new InsertarPacienteActualTask().execute();
        new InsertarCitaTask().execute(Integer.toString(especialidadID), fechaCita, horaCita);
        Intent intent = new Intent(RegistroCitas.this, RegitrarPagoPaciente.class);
        intent.putExtra("especialidadID", especialidadID);
        intent.putExtra("id_paciente", GlobalVariables.getInstance().getUserId());
        intent.putExtra("fechaCita", fechaCita);
        intent.putExtra("horaCita", horaCita);
        startActivity(intent);
    }

    private class InsertarPacienteActualTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            Connection connection = null;
            String idPaciente = "";

            try {
                connection = DatabaseConnection.getConnection();
                if (connection != null) {
                    String query = "SELECT t1.id_paciente " +
                            "FROM pacientes AS t1 " +
                            "JOIN usuarios AS t2 " +
                            "ON t1.correo=t2.nombre_usuario " +
                            "WHERE t2.id_usuario = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, idActual);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        idPaciente = resultSet.getString("id_paciente");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return idPaciente;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                pacAct = result;
            }
        }
    }

    private class InsertarCitaTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String especialidad = params[0];
            int doctor = 0;
            String fecha = params[1];
            String hora = params[2];

            String[] parts = fecha.split("/");
            if (parts.length == 3) {
                String nuevaFecha = parts[2] + "-" + parts[1] + "-" + parts[0] + " 00:00:00";

                Connection connection = null;
                PreparedStatement preparedStatement = null;

                try {
                    connection = DatabaseConnection.getConnection();
                    if (connection != null) {
                        int idPaciente = obtenerIdPaciente();

                        if (idPaciente != -1) {
                            String insertQuery = "INSERT INTO citas (id_paciente, id_doctor, id_tratamiento, fec_inic_cita, fec_fin_cita) " +
                                    "VALUES (?, ?, ?, ?, ?)";
                            preparedStatement = connection.prepareStatement(insertQuery);
                            preparedStatement.setInt(1, idPaciente);
                            preparedStatement.setInt(2, doctor);
                            preparedStatement.setLong(3, Long.parseLong(especialidad));
                            preparedStatement.setTimestamp(4, Timestamp.valueOf(nuevaFecha));
                            preparedStatement.setTimestamp(5, Timestamp.valueOf(nuevaFecha));

                            preparedStatement.executeUpdate();
                            preparedStatement.close();

                            return true;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (preparedStatement != null) {
                            preparedStatement.close();
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return false;
        }

        private int obtenerIdPaciente() {
            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                connection = DatabaseConnection.getConnection();

                if (connection != null) {
                    String selectQuery = "SELECT id_paciente FROM pacientes WHERE id_usuario = ?";
                    preparedStatement = connection.prepareStatement(selectQuery);
                    preparedStatement.setInt(1, GlobalVariables.getInstance().getUserId());

                    ResultSet rs = preparedStatement.executeQuery();

                    if (rs.next()) {
                        return rs.getInt("id_paciente");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return -1;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(RegistroCitas.this, "Inserción exitosa", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegistroCitas.this, "No se pudo realizar la inserción", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class LoadEspecialidadesTask extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            Connection connection = null;
            ArrayList<String> especialidadesOdontologicas = new ArrayList<>();

            try {
                connection = DatabaseConnection.getConnection();
                if (connection != null) {
                    String query = "SELECT id_tratamiento, nombre FROM tratamientos";
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);

                    while (resultSet.next()) {
                        String especialidad = resultSet.getString("nombre");
                        int idTratamiento = resultSet.getInt("id_tratamiento");
                        especialidadesOdontologicas.add(especialidad);
                        listaIdTratamientos.add(idTratamiento);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return especialidadesOdontologicas;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            if (result != null) {
                cargarListaEspecialidades(result);
            }
        }
    }

    private void cargarListaEspecialidades(ArrayList<String> especialidades) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, especialidades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprTratCita.setAdapter(adapter);
    }

    private void cargarListaDoctores(ArrayList<String> doctores) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, doctores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sprDocCita.setAdapter(adapter);
    }
}
