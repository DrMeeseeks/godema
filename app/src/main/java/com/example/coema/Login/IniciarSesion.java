package com.example.coema.Login;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coema.Conection.DatabaseConnection;
import com.example.coema.Conection.GlobalVariables;
import com.example.coema.Index.ActMenuAdmin;
import com.example.coema.Index.ActMenuOdonto;
import com.example.coema.Perfil.PacientePrincipal;
import com.example.coema.Perfil.PerfilPaciente;
import com.example.coema.R;
import com.example.coema.Registro.RegistroPacientes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class IniciarSesion extends AppCompatActivity {

    EditText txtCorreo, txtContra;

    TextView txtRegistrate; // Agrega la referencia al TextView "¿Eres administrador?"

    Button btnIniSe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_sesion);
        asignarReferencias();

        //new DatabaseConnectionTask().execute();
    }

    private void asignarReferencias() {
        txtCorreo = findViewById(R.id.txtEmailR);
        txtContra = findViewById(R.id.txtContraR);
        btnIniSe = (Button) findViewById(R.id.btnIniciarSesion);
        txtRegistrate = findViewById(R.id.txtRegistrate);



        txtRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iReg = new Intent(IniciarSesion.this, RegistroPacientes.class);
                startActivity(iReg);
            }
        });

        // Configura el clic en el TextView "¿Eres administrador?"


        btnIniSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObtenerDatosDeTablaAsyncTask task = new ObtenerDatosDeTablaAsyncTask(txtCorreo.getText().toString(), txtContra.getText().toString());
                task.execute();
            }
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
            Connection connection = null;
            try {
                // Obtener una conexión a la base de datos
                connection = DatabaseConnection.getConnection();

                if (connection != null) {
                    // Realizar las operaciones de consulta aquí
                    String query = "SELECT id_usuario, id_rol FROM usuarios WHERE nombre_usuario = ? AND contrasena = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, correo);
                    preparedStatement.setString(2, contra);

                    ResultSet rs = preparedStatement.executeQuery();
                    if (rs.next()) {
                        int idUsuario = rs.getInt("id_usuario");
                        GlobalVariables.getInstance().setUserId(idUsuario);
                        int idRol = rs.getInt("id_rol");
                        Log.d("IniciarSesion", "idUsuario almacenado: " + idUsuario);
                        return idRol; // Devuelve el ID del rol del usuario
                    }
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Manejar excepciones aquí (puedes mostrar un mensaje de error en onPostExecute)
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer idRol) {
            super.onPostExecute(idRol);
            if (idRol != null) {
                Intent intent = null;
                if (idRol == 1) {
                    // Administrador
                    intent = new Intent(IniciarSesion.this, ActMenuAdmin.class);
                } else if (idRol == 2) {
                    // Doctor
                    intent = new Intent(IniciarSesion.this, ActMenuOdonto.class);
                } else if (idRol == 3) {
                    // Paciente
                    intent = new Intent(IniciarSesion.this, PacientePrincipal.class);
                }

                if (intent != null) {
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "SE HA LOGEADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                } else {
                    // Rol no válido, mostrar el Toast aquí
                    Toast.makeText(getApplicationContext(), "ROL NO VÁLIDO", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Usuario no encontrado, mostrar el Toast aquí
                Toast.makeText(getApplicationContext(), "USUARIO NO ENCONTRADO", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
