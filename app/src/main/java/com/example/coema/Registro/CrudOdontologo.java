package com.example.coema.Registro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coema.Conection.DatabaseConnection;
import com.example.coema.Index.ActMenuAdmin;
import com.example.coema.Perfil.PerfilPaciente;
import com.example.coema.R;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class CrudOdontologo extends AppCompatActivity {

    EditText edtCodAlu, edtApePatAlu, edtApeMatAlu, edtNombreAlu, edtDateAlu, edtTelefonoAlu, edtUsuarioAlu, edtContraseñaAlu;
    Button btnBuscar, btnModificar, btnEliminar, btnLimpiar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crud_odontologo);
        edtCodAlu = (EditText) findViewById(R.id.edtCodAlu);
        edtApePatAlu = (EditText) findViewById(R.id.edtApePatAlu);
        edtApeMatAlu = (EditText) findViewById(R.id.edtApeMatAlu);
        edtNombreAlu = (EditText) findViewById(R.id.edtNomAlu);
        edtUsuarioAlu = (EditText) findViewById(R.id.edtUsuAlu);
        edtContraseñaAlu = (EditText) findViewById(R.id.edtContAlu);

        // Obtener los datos ingresados por el usuario desde la actividad anterior

        btnBuscar = (Button) findViewById(R.id.btnBuscador);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                CrudOdontologo.BuscarOdoTask task = new CrudOdontologo.BuscarOdoTask();
                task.execute();}
        });

        btnModificar = (Button) findViewById(R.id.btnModificar);
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                CrudOdontologo.EditarOdoTask task = new CrudOdontologo.EditarOdoTask();
                task.execute();}
        });

        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CrudOdontologo.EliminarOdoTask task = new CrudOdontologo.EliminarOdoTask();
                task.execute();}
        });

        btnLimpiar = (Button) findViewById(R.id.btnLimpiar);
        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { limpiar();}
        });


    }

    private class InsertarOdoTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            String nombreUsuario = params[0];
            String contrasena = params[1];

            int resultado = -1;

            try {
                Connection connection = DatabaseConnection.getConnection();
                if (connection != null) {
                    String sqlUsuario = "INSERT INTO usuarios (nombre_usuario, contrasena, id_rol) VALUES (?, ?, ?)";
                    PreparedStatement pstUsuario = connection.prepareStatement(sqlUsuario, PreparedStatement.RETURN_GENERATED_KEYS);
                    pstUsuario.setString(1, nombreUsuario);
                    pstUsuario.setString(2, contrasena);
                    pstUsuario.setInt(3, 3);

                    int rowsAffectedUsuario = pstUsuario.executeUpdate();

                    if (rowsAffectedUsuario > 0) {
                        ResultSet generatedKeys = pstUsuario.getGeneratedKeys();
                        int idUsuario = -1;
                        if (generatedKeys.next()) {
                            idUsuario = generatedKeys.getInt(1);
                        }

                        // Convertir la cadena de fecha en un objeto java.sql.Date


                        String sqlPaciente = "INSERT INTO doctores (id_doctor, nombres, apellidos, especialidad, correo, contrasena, id_usuario) VALUES (?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement pstPaciente = connection.prepareStatement(sqlPaciente);
                        pstPaciente.setString(1, edtCodAlu.getText().toString());
                        pstPaciente.setString(2, edtNombreAlu.getText().toString());
                        pstPaciente.setString(3, edtApePatAlu.getText().toString());
                        pstPaciente.setString(4, edtApeMatAlu.getText().toString());
                        pstPaciente.setString(5, edtUsuarioAlu.getText().toString());
                        pstPaciente.setString(6, edtContraseñaAlu.getText().toString());
                        pstPaciente.setInt(9, idUsuario);

                        int rowsAffectedPaciente = pstPaciente.executeUpdate();

                        pstPaciente.close();

                        if (rowsAffectedPaciente > 0) {
                            resultado = idUsuario;
                        }
                    }

                    pstUsuario.close();

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(Integer idUsuario) {
            if (idUsuario != -1) {
                mostrarToast("Usuario registrado exitosamente");
            } else {
                mostrarToast("No se pudo registrar el usuario");
            }
        }

        private void mostrarToast(String mensaje) {
            Toast.makeText(CrudOdontologo.this, mensaje, Toast.LENGTH_SHORT).show();
        }
    }

    private class BuscarOdoTask extends AsyncTask<String, Void, Integer> {
        private ResultSet rs; // Declarar rs como una variable de instancia

        @Override
        protected Integer doInBackground(String... params) {
            Connection connection;
            int idUsuario = -1;
            try {
                connection = DatabaseConnection.getConnection();
                Statement st = connection.createStatement();
                rs = st.executeQuery("SELECT d.id_doctor, d.nombres, d.apellidos, u.nombre_usuario, u.contrasena, e.nombreespecialidad FROM doctores d, especialidadesodontologicas e, usuarios u WHERE e.especialidadid=d.especialidad and d.id_usuario = u.id_usuario and d.id_doctor='" + edtCodAlu.getText().toString() + "'");
                if (rs.next()) {
                    // El usuario se ha autenticado correctamente
                    idUsuario = Integer.parseInt(rs.getString("id_doctor"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return idUsuario;
        }

        @Override
        protected void onPostExecute(Integer idUsuario) {
            if (idUsuario != -1) {
                mostrarToast("Usuario buscado exitosamente");
                try {
                    // Realiza las actualizaciones de la interfaz de usuario aquí
                    String id = rs.getString("id_doctor");
                    String nom = rs.getString("nombres");
                    String ape = rs.getString("apellidos");
                    String apema = rs.getString("nombreespecialidad");
                    String cor = rs.getString("nombre_usuario");
                    String con = rs.getString("contrasena");

                    edtCodAlu.setText(id);
                    edtNombreAlu.setText(nom);
                    edtApePatAlu.setText(ape);
                    edtApeMatAlu.setText(apema);
                    edtUsuarioAlu.setText(cor);
                    edtContraseñaAlu.setText(con);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                mostrarToast("No se pudo encontrar el usuario");
            }
        }

        private void mostrarToast(String mensaje) {
            Toast.makeText(CrudOdontologo.this, mensaje, Toast.LENGTH_SHORT).show();
        }
    }

    private class EditarOdoTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            int resultado = -1;

            try {
                Connection connection = DatabaseConnection.getConnection();
                if (connection != null) {
                    String id=edtCodAlu.getText().toString();
                    String nombre=edtNombreAlu.getText().toString();
                    String apellido=edtApePatAlu.getText().toString();
                    String especialidad=edtApeMatAlu.getText().toString();
                    String correo=edtUsuarioAlu.getText().toString();
                    String contra=edtContraseñaAlu.getText().toString();

                    // Convierte el Bitmap en un formato que necesites (por ejemplo, bytes)
                    String updateQuery = "UPDATE doctores SET id_doctor = ?, nombres = ?, apellidos = ?, especialidad = (select especialidadid from especialidadesodontologicas where nombreespecialidad = ?) WHERE id_doctor = '"+id+"';" +
                            "UPDATE usuarios SET nombre_usuario = ?, contrasena = ? WHERE id_usuario = (select id_usuario from doctores where id_doctor= '"+id+"');";
                    PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                    preparedStatement.setString(1, id);
                    preparedStatement.setString(2, nombre);
                    preparedStatement.setString(3, apellido);
                    preparedStatement.setString(4, especialidad);
                    preparedStatement.setString(5, correo);
                    preparedStatement.setString(6, contra);

                    preparedStatement.executeUpdate();
                    preparedStatement.close();

                    return 1;

                }else {
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(Integer idUsuario) {
            if (idUsuario != -1) {
                mostrarToast("Usuario editado exitosamente");
            } else {
                mostrarToast("No se pudo editar el usuario");
            }
        }

        private void mostrarToast(String mensaje) {
            Toast.makeText(CrudOdontologo.this, mensaje, Toast.LENGTH_SHORT).show();
        }
    }

    private class EliminarOdoTask extends AsyncTask<String, Void, Integer> {
        protected Integer doInBackground(String... params) {
            int resultado = -1;
            ResultSet rs;
            int idUsuario=-1;
            try {
                Connection connection = DatabaseConnection.getConnection();
                if (connection != null) {
                    connection.setAutoCommit(false);  // Iniciar la transacción

                        Statement st = connection.createStatement();
                        rs = st.executeQuery("SELECT id_usuario from doctores WHERE id_doctor='" + edtCodAlu.getText().toString() + "'");
                        if (rs.next()) {
                            // El usuario se ha autenticado correctamente
                            idUsuario = rs.getInt("id_usuario");
                        }

                        // Primera consulta: Eliminar el doctor
                        String sqlDoctor = "DELETE FROM doctores WHERE id_doctor = ?";
                        PreparedStatement pstDoctor = connection.prepareStatement(sqlDoctor);
                        pstDoctor.setString(1, edtCodAlu.getText().toString());
                        pstDoctor.executeUpdate();

                        // Segunda consulta: Eliminar el usuario
                        String sqlUsuario = "DELETE FROM usuarios WHERE id_usuario = ?";
                        PreparedStatement pstUsuario = connection.prepareStatement(sqlUsuario);
                        pstUsuario.setInt(1, idUsuario);
                        pstUsuario.executeUpdate();



                        connection.commit();  // Confirmar la transacción
                        resultado = 1;  // Éxito

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(Integer idUsuario) {
            if (idUsuario != -1) {
                mostrarToast("Usuario eliminado exitosamente");
            } else {
                mostrarToast("No se pudo eliminar el usuario");
            }
        }

        private void mostrarToast(String mensaje) {
            Toast.makeText(CrudOdontologo.this, mensaje, Toast.LENGTH_SHORT).show();
        }
    }



    public void limpiar(){
                edtCodAlu.setText("");
                edtApePatAlu.setText("");
                edtApeMatAlu.setText("");
                edtNombreAlu.setText("");
                edtUsuarioAlu.setText("");
                edtContraseñaAlu.setText("");
    }


    public void RegresarSitio(View view){
        Intent iRegresar = new Intent(this, ActMenuAdmin.class);
        startActivity(iRegresar);
    }
}