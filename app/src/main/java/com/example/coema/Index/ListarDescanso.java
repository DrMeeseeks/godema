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

import com.example.coema.Adapter.DescansoAdapter;
import com.example.coema.Adapter.TratamientosAdapter;
import com.example.coema.Conection.DatabaseConnection;
import com.example.coema.Listas.Descanso;
import com.example.coema.Listas.Tratamientos;
import com.example.coema.R;
import com.example.coema.Registro.RegistroDescansoEdit;
import com.example.coema.Registro.RegistroDescansos;
import com.example.coema.Registro.RegistroTratamientos;
import com.example.coema.Registro.RegistroTratamientosEdit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ListarDescanso extends AppCompatActivity {

    private Button editDescansoButton;
    private Button addDescansoButton;
    private RecyclerView recyclerView;
    private DescansoAdapter adapter;
    private List<Descanso> descansos = new ArrayList<>(); // Lista para almacenar los recibos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_descansos);

        // Encuentra las referencias a los botones
        editDescansoButton = findViewById(R.id.editDescansoButton);
        addDescansoButton = findViewById(R.id.addDescansoButton);

        // Configura el RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DescansoAdapter(descansos); // Pasa la lista de recibos al adaptador
        recyclerView.setAdapter(adapter);

        // Configura los listeners de los botones
        editDescansoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén el ID del recibo seleccionado directamente desde el adaptador
                int selectedDescansoId = adapter.getSelectedDescansoId();

                if (selectedDescansoId != -1) {
                    // Abre la actividad de edición y pasa los datos seleccionados
                    Intent intent = new Intent(ListarDescanso.this, RegistroDescansoEdit.class);
                    intent.putExtra("selectedDescansoId", selectedDescansoId);
                    startActivity(intent);
                } else {
                    // Mostrar un mensaje de que ningún recibo está seleccionado
                    Toast.makeText(ListarDescanso.this, "Selecciona un descanso primero", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addDescansoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agrega aquí la lógica para el botón "Agregar Pago"
                // Por ejemplo, puedes abrir la actividad RegistroPagoActivity.
                Intent intent = new Intent(ListarDescanso.this, RegistroDescansos.class);
                startActivity(intent);
            }
        });

        // Iniciar una tarea asincrónica para obtener datos de la base de datos
        new ObtenerDatosDeTablaAsyncTask().execute();
    }

    // Tarea asincrónica para obtener datos de la tabla
    private class ObtenerDatosDeTablaAsyncTask extends AsyncTask<Void, Void, List<Descanso>> {

        @Override
        protected List<Descanso> doInBackground(Void... voids) {
            List<Descanso> descansos = new ArrayList<>();

            try {
                // Obtener una conexión a la base de datos
                Connection conn = DatabaseConnection.getConnection();

                if (conn != null) {
                    // Ejecutar una consulta SQL para obtener datos
                    String consultaSQL = "SELECT id, nombre, descanso, tratamiento FROM descanso";

                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(consultaSQL);

                    // Procesar los resultados de la consulta y agregarlos a la lista
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("nombre");
                        String descanso = resultSet.getString("descanso");
                        String tratamiento = resultSet.getString("tratamiento");

                        descansos.add(new Descanso(id, name, descanso, tratamiento));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return descansos;
        }

        @Override
        protected void onPostExecute(List<Descanso> descansoList) {
            super.onPostExecute(descansoList);

            // Actualiza el adaptador con la lista de recibos obtenida de la base de datos
            descansos.addAll(descansoList);
            adapter.notifyDataSetChanged();
        }
    }
}
