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

import com.example.coema.Adapter.ReceiptAdapter;
import com.example.coema.Adapter.TratamientosAdapter;
import com.example.coema.Conection.DatabaseConnection;
import com.example.coema.Conection.GlobalVariables;
import com.example.coema.Listas.Tratamientos;
import com.example.coema.Modelos.Receipt;
import com.example.coema.R;
import com.example.coema.Registro.RegistroPagoEdit;
import com.example.coema.Registro.RegistroTratamientos;
import com.example.coema.Registro.RegistroTratamientosEdit;
import com.example.coema.Registro.RegitroPago;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ListarTratamientos extends AppCompatActivity {

    private Button editHistoryButton;
    private Button addTratamientoButton;
    private RecyclerView recyclerView;
    private TratamientosAdapter adapter;
    Integer idActual= GlobalVariables.getInstance().getUserId();

    private List<Tratamientos> tratamientos = new ArrayList<>(); // Lista para almacenar los recibos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_tratamientos);
        new BuscarAsyncTask().execute();
        // Encuentra las referencias a los botones
        editHistoryButton = findViewById(R.id.editHistoryButton);
        addTratamientoButton = findViewById(R.id.addTratamientoButton);

        // Configura el RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TratamientosAdapter(tratamientos); // Pasa la lista de recibos al adaptador
        recyclerView.setAdapter(adapter);

        // Configura los listeners de los botones
        editHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén el ID del recibo seleccionado directamente desde el adaptador
                int selectedTratamientosId = adapter.getSelectedTratamientosId();

                if (selectedTratamientosId != -1) {
                    // Abre la actividad de edición y pasa los datos seleccionados
                    Intent intent = new Intent(ListarTratamientos.this, RegistroTratamientosEdit.class);
                    intent.putExtra("selectedTratamientosId", selectedTratamientosId);
                    startActivity(intent);
                } else {
                    // Mostrar un mensaje de que ningún recibo está seleccionado
                    Toast.makeText(ListarTratamientos.this, "Selecciona un tratamiento primero", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addTratamientoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agrega aquí la lógica para el botón "Agregar Pago"
                // Por ejemplo, puedes abrir la actividad RegistroPagoActivity.
                Intent intent = new Intent(ListarTratamientos.this, RegistroTratamientos.class);
                startActivity(intent);
            }
        });

        // Iniciar una tarea asincrónica para obtener datos de la base de datos
        new ObtenerDatosDeTablaAsyncTask().execute();
    }

    // Tarea asincrónica para obtener datos de la tabla
    private class ObtenerDatosDeTablaAsyncTask extends AsyncTask<Void, Void, List<Tratamientos>> {

        @Override
        protected List<Tratamientos> doInBackground(Void... voids) {
            List<Tratamientos> tratamientos = new ArrayList<>();

            try {
                // Obtener una conexión a la base de datos
                Connection conn = DatabaseConnection.getConnection();

                if (conn != null) {
                    // Ejecutar una consulta SQL para obtener datos
                    String consultaSQL = "SELECT id_tratamiento, nombre, detalle, precio FROM tratamientos";
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(consultaSQL);

                    // Procesar los resultados de la consulta y agregarlos a la lista
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id_tratamiento");
                        String name = resultSet.getString("nombre");
                        String detalle = resultSet.getString("detalle");
                        String amount = resultSet.getString("precio");

                        tratamientos.add(new Tratamientos(id, name, detalle, amount));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return tratamientos;
        }

        @Override
        protected void onPostExecute(List<Tratamientos> tratamientosList) {
            super.onPostExecute(tratamientosList);

            // Actualiza el adaptador con la lista de recibos obtenida de la base de datos
            tratamientos.addAll(tratamientosList);
            adapter.notifyDataSetChanged();
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
                addTratamientoButton.setVisibility(View.GONE);
                editHistoryButton.setVisibility(View.GONE);
            }
        }
    }
}
