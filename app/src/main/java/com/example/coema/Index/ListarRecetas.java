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
import com.example.coema.Adapter.RecetaAdapter;
import com.example.coema.Conection.DatabaseConnection;
import com.example.coema.Listas.Descanso;
import com.example.coema.Listas.Receta;
import com.example.coema.R;
import com.example.coema.Registro.RegistroDescansoEdit;
import com.example.coema.Registro.RegistroDescansos;
import com.example.coema.Registro.RegistroRecetaEdit;
import com.example.coema.Registro.RegistroRecetas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ListarRecetas extends AppCompatActivity {

    private Button editRecetaButton;
    private Button addRecetaButton;
    private RecyclerView recyclerView;
    private RecetaAdapter adapter;
    private List<Receta> recetas = new ArrayList<>(); // Lista para almacenar los recibos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_recetas);

        // Encuentra las referencias a los botones
        editRecetaButton = findViewById(R.id.editRecetaButton);
        addRecetaButton = findViewById(R.id.addRecetaButton);

        // Configura el RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecetaAdapter(recetas); // Pasa la lista de recibos al adaptador
        recyclerView.setAdapter(adapter);

        // Configura los listeners de los botones
        editRecetaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén el ID del recibo seleccionado directamente desde el adaptador
                int selectedRecetaId = adapter.getSelectedRecetaId();

                if (selectedRecetaId != -1) {
                    // Abre la actividad de edición y pasa los datos seleccionados
                    Intent intent = new Intent(ListarRecetas.this, RegistroRecetaEdit.class);
                    intent.putExtra("selectedRecetaId", selectedRecetaId);
                    startActivity(intent);
                } else {
                    // Mostrar un mensaje de que ningún recibo está seleccionado
                    Toast.makeText(ListarRecetas.this, "Selecciona una receta primero", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addRecetaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agrega aquí la lógica para el botón "Agregar Pago"
                // Por ejemplo, puedes abrir la actividad RegistroPagoActivity.
                Intent intent = new Intent(ListarRecetas.this, RegistroRecetas.class);
                startActivity(intent);
            }
        });

        // Iniciar una tarea asincrónica para obtener datos de la base de datos
        new ObtenerDatosDeTablaAsyncTask().execute();
    }

    // Tarea asincrónica para obtener datos de la tabla
    private class ObtenerDatosDeTablaAsyncTask extends AsyncTask<Void, Void, List<Receta>> {

        @Override
        protected List<Receta> doInBackground(Void... voids) {
            List<Receta> recetas = new ArrayList<>();

            try {
                // Obtener una conexión a la base de datos
                Connection conn = DatabaseConnection.getConnection();

                if (conn != null) {
                    // Ejecutar una consulta SQL para obtener datos
                    String consultaSQL = "SELECT id, nombre, medicamento, dosis FROM receta";

                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(consultaSQL);

                    // Procesar los resultados de la consulta y agregarlos a la lista
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("nombre");
                        String descanso = resultSet.getString("medicamento");
                        String tratamiento = resultSet.getString("dosis");

                        recetas.add(new Receta(id, name, descanso, tratamiento));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return recetas;
        }

        @Override
        protected void onPostExecute(List<Receta> recetaList) {
            super.onPostExecute(recetaList);

            // Actualiza el adaptador con la lista de recibos obtenida de la base de datos
            recetas.addAll(recetaList);
            adapter.notifyDataSetChanged();
        }
    }
}
