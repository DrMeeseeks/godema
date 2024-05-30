package com.example.coema.Login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coema.Conection.DatabaseConnection;
import com.example.coema.Perfil.PerfilPaciente;
import com.example.coema.R;
import com.example.coema.Registro.RegistroPacientes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IniciarSesionAdmin extends AppCompatActivity {

    EditText txtCorreo, txtContra;

    TextView txtRegistrate;

    Button btnIniSe;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_sesion_admin);
        asignarReferencias();

        //new DatabaseConnectionTask().execute();
    }


    private void asignarReferencias(){
        txtCorreo = findViewById(R.id.txtEmailR);
        txtContra = findViewById(R.id.txtContraR);
        btnIniSe = (Button) findViewById(R.id.btnIniciarSesion);
        txtRegistrate = findViewById(R.id.txtRegistrate1);

        txtRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iReg = new Intent(IniciarSesionAdmin.this, RegistroPacientes.class);
                startActivity(iReg);
            }
        });
        btnIniSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObtenerDatosDeTablaAsyncTask task = new ObtenerDatosDeTablaAsyncTask(txtCorreo.getText().toString(), txtContra.getText().toString());
                task.execute();            }
        });
    }

    private class ObtenerDatosDeTablaAsyncTask extends AsyncTask<Void, Void, Integer> {
        private String correo, contra;

        public ObtenerDatosDeTablaAsyncTask(String correo, String contra) {
            this.correo = correo;
            this.contra = contra;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                // Obtener una conexión a la base de datos
                Connection connection = DatabaseConnection.getConnection();

                if (connection != null) {
                    // Realizar las operaciones de consulta y cierre de la conexión aquí
                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery("SELECT id_paciente FROM pacientes where correo='" + correo + "' and " +
                            "contrasena='" + contra + "'");

                    if (rs.next()) {
                        // El usuario se ha autenticado correctamente
                        return rs.getInt("id_paciente");
                    }
                    rs.close();
                    st.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Manejar excepciones aquí (puedes mostrar un mensaje de error en onPostExecute)
            }
            // Usuario no encontrado
            return null;
        }

        @Override
        protected void onPostExecute(Integer idPaciente) {
            super.onPostExecute(idPaciente);

            if (idPaciente != null) {
                // El usuario se ha autenticado correctamente
                Intent iRegistrar = new Intent(IniciarSesionAdmin.this, PerfilPaciente.class);
                Bundle bundle = new Bundle();
                bundle.putInt("data", idPaciente);
                iRegistrar.putExtras(bundle);
                startActivity(iRegistrar);
                Toast.makeText(getApplicationContext(), "SE HA LOGEADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
            } else {
                // Usuario no encontrado, mostrar el Toast aquí
                Toast.makeText(getApplicationContext(), "USUARIO NO ENCONTRADO", Toast.LENGTH_SHORT).show();
            }
        }
    }
}