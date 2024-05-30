package com.example.coema.Index;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coema.Adapter.CitasAdapter;
import com.example.coema.Conection.DatabaseConnection;
import com.example.coema.Listas.CitasOd;
import com.example.coema.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListarCitas extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CitasAdapter adapter;
    //OnItemClickListener listas;
    private List<CitasOd> citas = new ArrayList<>(); // Lista para almacenar los recibos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_citas_odontologo);


        // Configura el RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CitasAdapter(citas, null); // Pasa la lista de recibos al adaptador
        recyclerView.setAdapter(adapter);
        new ObtenerDatosDeTablaAsyncTask().execute();



        // Configura los listeners de los botones
        /*editHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén el ID del recibo seleccionado directamente desde el adaptador
                int selectedTratamientosId = adapter.getSelectedTratamientosId();

                if (selectedTratamientosId != -1) {
                    // Abre la actividad de edición y pasa los datos seleccionados
                    Intent intent = new Intent(ListarCitas.this, RegistroTratamientosEdit.class);
                    intent.putExtra("selectedCitasId", selectedTratamientosId);
                    startActivity(intent);
                } else {
                    // Mostrar un mensaje de que ningún recibo está seleccionado
                    Toast.makeText(ListarCitas.this, "Selecciona una cita primero", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addTratamientoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agrega aquí la lógica para el botón "Agregar Pago"
                // Por ejemplo, puedes abrir la actividad RegistroPagoActivity.
                Intent intent = new Intent(ListarCitas.this, RegistroTratamientos.class);
                startActivity(intent);
            }
        });*/

        // Iniciar una tarea asincrónica para obtener datos de la base de datos

    }

    /*private void clickenListView() {
        CitasAdapter adaptador = new CitasAdapter(citas, new OnItemClickListener() {
                @Override
                public void onItemClick(String elemento) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OnItemClickListener.this);
                    builder.setTitle("Elemento seleccionado");
                    builder.setMessage("Has seleccionado: " + elemento);

                    // Configura botones y acciones según tus necesidades.
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        recyclerView.setAdapter(adaptador);
    }*/
    // Tarea asincrónica para obtener datos de la tabla
    private class ObtenerDatosDeTablaAsyncTask extends AsyncTask<Void, Void, List<CitasOd>> {

        @Override
        protected List<CitasOd> doInBackground(Void... voids) {
            List<CitasOd> citas = new ArrayList<>();

            try {
                // Obtener una conexión a la base de datos
                Connection conn = DatabaseConnection.getConnection();

                if (conn != null) {
                    // Ejecutar una consulta SQL para obtener datos
                    String consultaSQL = "SELECT p.nombres, t.nombre, c.fec_inic_cita FROM citas c, pacientes p, tratamientos t where p.id_paciente=c.id_paciente and t.id_tratamiento=c.id_tratamiento";
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(consultaSQL);

                    // Procesar los resultados de la consulta y agregarlos a la lista
                    while (resultSet.next()) {
                        String name = resultSet.getString(1);
                        String detalle = resultSet.getString(2);
                        Date amount = resultSet.getDate(3);

                        citas.add(new CitasOd(0,name, detalle, amount));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return citas;
        }

        @Override
        protected void onPostExecute(List<CitasOd> citasList) {
            super.onPostExecute(citasList);

            // Actualiza el adaptador con la lista de recibos obtenida de la base de datos
            citas.addAll(citasList);
            adapter.notifyDataSetChanged();
        }
    }
}
