package com.example.coema.Index;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coema.Adapter.PacienteAdapter;
import com.example.coema.Adapter.TratamientosAdapter;
import com.example.coema.Conection.DatabaseConnection;
import com.example.coema.Conection.GlobalVariables;
import com.example.coema.Listas.Paciente;
import com.example.coema.Listas.Tratamientos;
import com.example.coema.R;
import com.example.coema.Registro.RegistroPacientes;
import com.example.coema.Registro.RegistroPacientesEdit;
import com.example.coema.Registro.RegistroTratamientos;
import com.example.coema.Registro.RegistroTratamientosEdit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ListarPaciente extends AppCompatActivity {

    private Button editHistoryButton;
    private Button addPacienteButton;
    private RecyclerView recyclerView;
    private PacienteAdapter adapter;
    Integer idActual= GlobalVariables.getInstance().getUserId();

    private List<Paciente> pacientes = new ArrayList<>(); // Lista para almacenar los recibos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_pacientes);
        new BuscarAsyncTask().execute();
        // Encuentra las referencias a los botones
        editHistoryButton = findViewById(R.id.editHistoryButton);
        addPacienteButton = findViewById(R.id.addPacienteButton);

        // Configura el RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PacienteAdapter(pacientes); // Pasa la lista de recibos al adaptador
        recyclerView.setAdapter(adapter);

        // Configura los listeners de los botones
        editHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén el ID del recibo seleccionado directamente desde el adaptador
                int selectedPacientesId = adapter.getSelectedPacienteId();

                if (selectedPacientesId != -1) {
                    // Abre la actividad de edición y pasa los datos seleccionados
                    Intent intent = new Intent(ListarPaciente.this, RegistroPacientesEdit.class);
                    intent.putExtra("selectedPacientesId", selectedPacientesId);
                    startActivity(intent);
                } else {
                    // Mostrar un mensaje de que ningún recibo está seleccionado
                    Toast.makeText(ListarPaciente.this, "Selecciona un Paciente primero", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addPacienteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agrega aquí la lógica para el botón "Agregar Pago"
                // Por ejemplo, puedes abrir la actividad RegistroPagoActivity.
                Intent intent = new Intent(ListarPaciente.this, RegistroPacientes.class);
                startActivity(intent);
            }
        });

        // Iniciar una tarea asincrónica para obtener datos de la base de datos
        new ObtenerDatosDeTablaAsyncTask().execute();
    }

    // Tarea asincrónica para obtener datos de la tabla
    private class ObtenerDatosDeTablaAsyncTask extends AsyncTask<Void, Void, List<Paciente>> {

        @Override
        protected List<Paciente> doInBackground(Void... voids) {
            List<Paciente> paciente = new ArrayList<>();

            try {
                // Obtener una conexión a la base de datos
                Connection conn = DatabaseConnection.getConnection();

                if (conn != null) {
                    // Ejecutar una consulta SQL para obtener datos
                    String consultaSQL = "SELECT id_paciente, nombres, apellidos, correo FROM pacientes";
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(consultaSQL);

                    // Procesar los resultados de la consulta y agregarlos a la lista
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id_paciente");
                        String name = resultSet.getString("nombres");
                        String detalle = resultSet.getString("apellidos");
                        String amount = resultSet.getString("correo");

                        paciente.add(new Paciente(id, name, detalle, amount));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return paciente;
        }

        @Override
        protected void onPostExecute(List<Paciente> pacienteList) {
            super.onPostExecute(pacienteList);

            // Actualiza el adaptador con la lista de recibos obtenida de la base de datos
            pacientes.addAll(pacienteList);
        }
    }

    private class BuscarAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                // Obtener una conexión a la base de datos
                Connection conn = DatabaseConnection.getConnection();

                if (conn != null) {
                    // Ejecutar una consulta SQL para obtener datos
                    String consultaSQL = "SELECT id_rol FROM usuarios where id_usuario="+idActual+" and id_rol=3";
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(consultaSQL);

                    // Procesar los resultados de la consulta y agregarlos a la lista
                    if (resultSet.next()) {
                        return true;
                    }else {
                        return false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean usuario) {
            if(usuario){
                addPacienteButton.setVisibility(View.GONE);
                editHistoryButton.setVisibility(View.GONE);
            }
        }
    }
}
