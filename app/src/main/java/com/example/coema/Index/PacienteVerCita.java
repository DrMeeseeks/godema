package com.example.coema.Index;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coema.Adapter.CitasAdapter;
import com.example.coema.Adapter.CitasPacAdapter;
import com.example.coema.Conection.DatabaseConnection;
import com.example.coema.Fragments.RecyclerViewItemClickListener;
import com.example.coema.Listas.CitasOd;
import com.example.coema.Listas.CitasPac;
import com.example.coema.R;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacienteVerCita extends AppCompatActivity implements RecyclerViewItemClickListener {
    private RecyclerView recyclerView;
    private CitasPacAdapter adapter;
    //OnItemClickListener listas;
    private List<CitasPac> citas = new ArrayList<CitasPac>(); // Lista para almacenar los recibos
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar_cita_paciente);
        recyclerView=findViewById(R.id.recyclerVieww);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CitasPacAdapter(citas,this); // Pasa la lista de recibos al adaptador
        recyclerView.setAdapter(adapter);
        new PacienteVerCita.ObtenerDatosDeTablaAsyncTask().execute();
    }




    @Override
    public void onItemClick(int position, int id) {
        // Aquí se muestra el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ver Documentos");
        builder.setMessage("¿Quiere visualizar las radiografías adjuntadas a esta cita?");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(PacienteVerCita.this, PacienteVerDocumentos.class);
                intent.putExtra("clave_valor", id);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Lógica para "Cancelar" aquí
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
    private class ObtenerDatosDeTablaAsyncTask extends AsyncTask<Void, Void, List<CitasPac>> {

        @Override
        protected List<CitasPac> doInBackground(Void... voids) {
            List<CitasPac> citas = new ArrayList<>();

            try {
                // Obtener una conexión a la base de datos
                Connection conn = DatabaseConnection.getConnection();

                if (conn != null) {
                    // Ejecutar una consulta SQL para obtener datos
                    String consultaSQL = "SELECT c.id_cita, d.nombres, t.nombre, c.fec_inic_cita FROM citas c, doctores d, tratamientos t where d.id_doctor=c.id_doctor and t.id_tratamiento=c.id_tratamiento";
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(consultaSQL);

                    // Procesar los resultados de la consulta y agregarlos a la lista
                    while (resultSet.next()) {
                        int id=resultSet.getInt(1);
                        String name = resultSet.getString(2);
                        String detalle = resultSet.getString(3);
                        Date amount = resultSet.getDate(4);

                        citas.add(new CitasPac(id,name, detalle, amount));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return citas;
        }

        @Override
        protected void onPostExecute(List<CitasPac> citasList) {
            super.onPostExecute(citasList);

            // Actualiza el adaptador con la lista de recibos obtenida de la base de datos
            citas.addAll(citasList);
            adapter.notifyDataSetChanged();
        }
    }
}
