package com.example.coema.Registro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coema.Conection.DatabaseConnection;
import com.example.coema.Index.ListarTratamientos;
import com.example.coema.Listas.Descanso;
import com.example.coema.R;
import com.example.coema.Registro.CrudOdontologo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class RegistroDescanso extends AppCompatActivity {
    EditText edtPaciente, edtTratamiento, edtDetalle;
    Button btnAgregar;

    Integer idCita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_descanso);
        recuperarData();
        edtPaciente = (EditText) findViewById(R.id.pacienteEditText);
        edtTratamiento = (EditText) findViewById(R.id.tratCitEditText);
        edtDetalle = (EditText) findViewById(R.id.descansoEditText);
        editTextNoEditable();
        RegistroDescanso.ObtenerDatosDeTablaAsyncTask task = new RegistroDescanso.ObtenerDatosDeTablaAsyncTask();
        task.execute();

        // Obtener los datos ingresados por el usuario desde la actividad anterior

        btnAgregar = (Button) findViewById(R.id.addDescansoButton);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                RegistroDescanso.InsertarDatosAsyncTask task = new RegistroDescanso.InsertarDatosAsyncTask();
                task.execute();}

        });


    }

    private void editTextNoEditable(){
        edtPaciente.setEnabled(false);
        edtPaciente.setFocusable(false);
        edtPaciente.setClickable(false);
        edtPaciente.setCursorVisible(false);
        edtTratamiento.setEnabled(false);
        edtTratamiento.setFocusable(false);
        edtTratamiento.setClickable(false);
        edtTratamiento.setCursorVisible(false);
    }
    private void recuperarData() {
        Bundle bundle= getIntent().getExtras();
        if (bundle==null){
            idCita=null;
        }else{
            idCita=(int)bundle.getInt("clave_valor");
        }
    }

    private class ObtenerDatosDeTablaAsyncTask extends AsyncTask<Void, Void, Descanso> {
        @Override
        protected Descanso doInBackground(Void... voids) {
            Connection connection;
            try {
                connection = DatabaseConnection.getConnection();
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery("SELECT p.nombres, p.apellidos, t.nombre FROM citas c, pacientes p, tratamientos t " +
                        "WHERE t.id_tratamiento=c.id_tratamiento and c.id_paciente=p.id_paciente and id_cita="+ idCita);
                Descanso d = new Descanso();
                if (rs.next()) {
                    // El usuario se ha autenticado correctamente
                    String nom=rs.getString("nombres")+" "+rs.getString("apellidos");
                    String tra=rs.getString("nombre");
                    d.setNombre(nom);
                    d.setTratamiento(tra);
                }
                return d;

            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Descanso d) {
            super.onPostExecute(d);

            if (d==null) {
                Toast.makeText(getApplicationContext(), "Error al encontrar un dato", Toast.LENGTH_SHORT).show();
            } else {
                edtPaciente.setText(d.getNombre());
                edtTratamiento.setText(d.getTratamiento());
            }
        }
    }

    private class InsertarDatosAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                Connection conn = DatabaseConnection.getConnection();

                if (conn != null) {
                    if(!edtDetalle.getText().toString().equals("")) {
                        String consultaSQL = "INSERT INTO descanso_medico (id_cita, detalle) VALUES (?, ?)";
                        PreparedStatement preparedStatement = conn.prepareStatement(consultaSQL);
                        preparedStatement.setInt(1, idCita);
                        preparedStatement.setString(2, edtDetalle.getText().toString());

                        int filasAfectadas = preparedStatement.executeUpdate();
                        return filasAfectadas > 0;
                    }
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
                edtDetalle.setText("");
            } else {
                Toast.makeText(getApplicationContext(), "Error al realizar el registro.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
