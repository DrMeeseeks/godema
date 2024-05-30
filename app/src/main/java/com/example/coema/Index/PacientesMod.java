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
import com.example.coema.Listas.Notify;
import com.example.coema.R;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacientesMod extends AppCompatActivity {

    private List<Notify> notifyList;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paciente_layout);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        Button btnConfirmarCita = findViewById(R.id.btnConfirmarCita);
        Button btnCancelarCita = findViewById(R.id.btnCancelarCita);





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
            NotifyAdapter notifyAdapter = new NotifyAdapter(PacientesMod.this, notifyList);
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

            String sql = "SELECT c.nombres, c.apellidos, c.id_paciente, c.fecha_nacimiento " +
                    "FROM pacientes c";

            PreparedStatement stmt = conn.prepareStatement(sql);


            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int idCita = rs.getInt("id_paciente");;
                String nombreTratamiento = rs.getString("nombres") + " " + rs.getString("apellidos");
                Date fechaCita = rs.getDate("fecha_nacimiento");

                String estadoCita = rs.getString("id_paciente");





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
