package com.example.coema.Index;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coema.Adapter.NotifyAdapter;
import com.example.coema.Conection.DatabaseConnection;
import com.example.coema.Conection.GlobalVariables;
import com.example.coema.Listas.Notify;
import com.example.coema.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotifyMod extends AppCompatActivity {

    private List<Notify> notifyList;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_layout);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userId = GlobalVariables.getInstance().getUserId();

        Button btnConfirmarCita = findViewById(R.id.btnConfirmarCita);
        Button btnCancelarCita = findViewById(R.id.btnCancelarCita);

        btnConfirmarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (citaSeleccionadaId != -1) {
                    // Inicia el AsyncTask para realizar la actualización en la base de datos
                    new ActualizarCitaSeleccionadaAsync().execute(citaSeleccionadaId);
                }  else {
                // No se ha seleccionado una cita para confirmar
                    Toast.makeText(NotifyMod.this, "(\"Por favor, selecciona una cita para confirmar.\")", Toast.LENGTH_SHORT).show();
                 }
            }
        });


        btnCancelarCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (citaSeleccionadaId != -1) {
                    // Inicia el AsyncTask para realizar la actualización en la base de datos
                    new CancelarCitaSeleccionadaAsync().execute(citaSeleccionadaId);
                }  else {
                    // No se ha seleccionado una cita para confirmar
                    Toast.makeText(NotifyMod.this, "(\"Por favor, selecciona una cita para cancelar.\")", Toast.LENGTH_SHORT).show();
                }
            }
        });

        new ObtenerCitasPendientesAsync().execute(userId);
    }

    private int citaSeleccionadaId = -1; // Valor predeterminado para indicar que no hay cita seleccionada

    public void setCitaSeleccionadaId(int id) {
        citaSeleccionadaId = id;
    }


    private class ObtenerCitasPendientesAsync extends AsyncTask<Integer, Void, List<Notify>> {

        @Override
        protected List<Notify> doInBackground(Integer... params) {
            int userId = params[0];
            return obtenerCitasPendientes(userId);
        }

        @Override
        protected void onPostExecute(List<Notify> result) {
            super.onPostExecute(result);
            notifyList = result;

            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            NotifyAdapter notifyAdapter = new NotifyAdapter(NotifyMod.this, notifyList);
            recyclerView.setAdapter(notifyAdapter);
        }
    }

    private boolean actualizarCitaSeleccionada(int citaId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();

            // Consulta SQL para actualizar la columna not_p en la base de datos
            String updateSql = "UPDATE citas SET not_p = 1 WHERE id_cita = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setInt(1, citaId);

            int rowsUpdated = updateStmt.executeUpdate();

            return rowsUpdated > 0; // Devuelve true si se actualizó al menos una fila, de lo contrario, devuelve false

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Devuelve false en caso de error
        }
    }

    private class ActualizarCitaSeleccionadaAsync extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... params) {
            int citaId = params[0];
            return actualizarCitaSeleccionada(citaId);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // La actualización se realizó correctamente
                // Actualiza la vista llamando a obtenerCitasPendientesAsync nuevamente
                new ObtenerCitasPendientesAsync().execute(userId);
            } else {
                // Hubo un error en la actualización
                // Puedes mostrar un mensaje de error o manejar la situación según sea necesario
            }
        }
    }

    private boolean cancelarCitaSeleccionada(int citaId) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();

            // Consulta SQL para actualizar la columna not_p en la base de datos
            String updateSql = "UPDATE citas SET not_p = 2 WHERE id_cita = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setInt(1, citaId);

            int rowsUpdated = updateStmt.executeUpdate();

            return rowsUpdated > 0; // Devuelve true si se actualizó al menos una fila, de lo contrario, devuelve false

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Devuelve false en caso de error
        }
    }

    private class CancelarCitaSeleccionadaAsync extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... params) {
            int citaId = params[0];
            return cancelarCitaSeleccionada(citaId);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // La actualización se realizó correctamente
                // Actualiza la vista llamando a obtenerCitasPendientesAsync nuevamente
                new ObtenerCitasPendientesAsync().execute(userId);
            } else {
                // Hubo un error en la actualización
                // Puedes mostrar un mensaje de error o manejar la situación según sea necesario
            }
        }
    }


    private List<Notify> obtenerCitasPendientes(int userId) {
        List<Notify> citasPendientes = new ArrayList<>();
        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();

            String sql = "SELECT c.id_cita, t.nombre, c.fec_inic_cita, c.not_p " +
                    "FROM citas c " +
                    "INNER JOIN pacientes p ON c.id_paciente = p.id_paciente " +
                    "LEFT JOIN tratamientos t ON c.id_tratamiento = t.id_tratamiento " +
                    "WHERE p.id_usuario = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int idCita = rs.getInt("id_cita");
                String nombreTratamiento = rs.getString("nombre");
                Date fechaCita = rs.getDate("fec_inic_cita");

                String estadoCitaFromDatabase = rs.getString("not_p");

                String estadoCita = "Pendiente"; // Valor por defecto

                if ("0".equals(estadoCitaFromDatabase)) {
                    estadoCita = "Pendiente"; // Si el valor de la base de datos es "0", establece "Pendiente"
                } else if("1".equals(estadoCitaFromDatabase)){
                    estadoCita = "Confirmada"; // Puedes definir otros estados aquí si es necesario
                } else if("2".equals(estadoCitaFromDatabase)){
                estadoCita = "Cancelada"; // Puedes definir otros estados aquí si es necesario
                }

                 // Asegúrate de que coincida con el nombre real en tu base de datos

                Notify notify = new Notify(idCita, fechaCita, estadoCita, nombreTratamiento);
                citasPendientes.add(notify);
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }

        return citasPendientes;
    }
}
