package com.example.coema.Perfil;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Bitmap;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import com.example.coema.Conection.DatabaseConnection;
import com.example.coema.Conection.GlobalVariables;
import com.example.coema.Listas.Paciente;
import com.example.coema.Login.IniciarSesion;
import com.example.coema.R;
import com.example.coema.Registro.RegistroDescansoEdit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class PerfilPaciente extends AppCompatActivity {

    EditText editTextFirstName, editTextLastName,  editTextBirthdate, editTextHistorial,
            editTextIdentification, editTextGender, editTextAddress, editTextPhoneNumber;
    Button btnSave, btnSelect;

    private ImageView imageView;

    private Bitmap selectedImageBitmap;

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    Integer idActual= GlobalVariables.getInstance().getUserId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);
        asignarReferencias();
        PerfilPaciente.ObtenerDatosDeTablaAsyncTask task = new PerfilPaciente.ObtenerDatosDeTablaAsyncTask(idActual);
        task.execute();


    }


    public void asignarReferencias(){
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextBirthdate = findViewById(R.id.editTextBirthdate);
        editTextIdentification = findViewById(R.id.editTextIdentification);
        editTextGender = findViewById(R.id.editTextGender);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextHistorial = findViewById(R.id.editTextDocumento);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        imageView = (ImageView) findViewById(R.id.imPerfil);
        btnSave = findViewById(R.id.btnSave);
        btnSelect = findViewById(R.id.btnSeleccionarImagen);


        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnclickButtonSelectImage();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerfilPaciente.GuardarCambiosAsyncTask task = new PerfilPaciente.GuardarCambiosAsyncTask(idActual);
                task.execute();
            }
        });

    }


    private class ObtenerDatosDeTablaAsyncTask extends AsyncTask<Void, Void, Void> {

        private final Integer idUsuario;

        public ObtenerDatosDeTablaAsyncTask(Integer idUsuario) {
            this.idUsuario=idUsuario;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Connection connection;
            try {
                connection = DatabaseConnection.getConnection();
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM pacientes WHERE id_usuario='" + idUsuario+"'" );
                if (rs.next()) {
                    // El usuario se ha autenticado correctamente
                    String nom=rs.getString("nombres");
                    String ape=rs.getString("apellidos");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String fecNac=sdf.format(rs.getDate("fecha_nacimiento"));
                    String cor=rs.getString("correo");
                    String con=rs.getString("contrasena");
                    String gen=rs.getString("sexo");
                    String tel=rs.getString("telefono");

                    byte[] imageData = rs.getBytes("foto");


                    String his=rs.getString("historial_medico");



                    if (his!=null){
                        editTextHistorial.setText(his);
                    }
                    editTextFirstName.setText(nom);
                    editTextLastName.setText(ape);
                    editTextIdentification.setText(con);
                    editTextBirthdate.setText(fecNac);
                    editTextGender.setText(gen);
                    editTextPhoneNumber.setText(tel);
                    editTextAddress.setText(cor);
                    if(imageData!=null){
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

        public class GuardarCambiosAsyncTask extends AsyncTask<Void, Void, String> {
            private final Integer idUsuario;

            public GuardarCambiosAsyncTask(Integer idUsuario) {
                this.idUsuario=idUsuario;
            }
            @Override
            protected String doInBackground(Void... voids) {
                Connection connection;
                try {
                    connection = DatabaseConnection.getConnection();
                    if (connection != null) {
                        String nombre=editTextFirstName.getText().toString();
                        String apellido=editTextLastName.getText().toString();
                        String correo=editTextAddress.getText().toString();
                        String contra=editTextIdentification.getText().toString();
                        String fecNac=editTextBirthdate.getText().toString();
                        String sexo=editTextGender.getText().toString();
                        String telefono=editTextPhoneNumber.getText().toString();
                        String historial=editTextHistorial.getText().toString();

                        // Convierte el Bitmap en un formato que necesites (por ejemplo, bytes)
                        String updateQuery = "UPDATE pacientes SET nombres = ?, apellidos = ?, correo = ?, " +
                                "contrasena = ?, telefono = ?, fecha_nacimiento = to_date(?,'yyyy-MM-dd'), sexo = ? WHERE id_usuario = ?;" +
                                "UPDATE usuarios SET nombre_usuario = ?, contrasena = ? WHERE id_usuario = ?;";
                        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                        preparedStatement.setString(1, nombre);
                        preparedStatement.setString(2, apellido);
                        preparedStatement.setString(3, correo);
                        preparedStatement.setString(4, contra);
                        preparedStatement.setString(5, telefono);
                        preparedStatement.setString(6, fecNac);
                        preparedStatement.setString(7, sexo);
                        preparedStatement.setInt(8, idUsuario);
                        preparedStatement.setString(9, correo);
                        preparedStatement.setString(10, contra);
                        preparedStatement.setInt(11, idUsuario);

                        preparedStatement.executeUpdate();
                        preparedStatement.close();

                        if (historial.equals("")){
                            String updateQuery2 = "UPDATE pacientes SET historial_medico = ? WHERE id_usuario = ?";
                            PreparedStatement preparedStatement2 = connection.prepareStatement(updateQuery2);
                            preparedStatement2.setString(1, historial);
                            preparedStatement2.setInt(2, idUsuario);

                            preparedStatement2.executeUpdate();
                            preparedStatement2.close();
                        }
                        if (selectedImageBitmap != null){
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            String updateQuery3 = "UPDATE pacientes SET foto = ? WHERE id_usuario = ?";
                            PreparedStatement preparedStatement3 = connection.prepareStatement(updateQuery3);
                            preparedStatement3.setBytes(1, byteArray);
                            preparedStatement3.setInt(2, idUsuario);

                            preparedStatement3.executeUpdate();
                            preparedStatement3.close();
                        }
                    }else {
                        // Manejo de error si la conexión es nula
                        return "Error: No se pudo establecer una conexión a la base de datos.";
                    }


                }catch(Exception e){
                    e.printStackTrace();

                }
                return null;
            }
            @Override
            protected void onPostExecute(String errorMessage) {
                super.onPostExecute(errorMessage);

                if (errorMessage != null) {
                    // Mostrar un mensaje de error
                    Toast.makeText(PerfilPaciente.this, errorMessage, Toast.LENGTH_SHORT).show();
                } else {
                    // Los cambios se guardaron correctamente
                    Toast.makeText(PerfilPaciente.this, "Cambios guardados correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        }
        /*@Override
        protected void onPostExecute(Integer idPaciente) {
            super.onPostExecute(idPaciente);

            if (idPaciente != null) {
                // Haz lo que necesites con idPaciente
                Intent iRegistrar = new Intent(PerfilPaciente.this, PerfilPaciente.class);
                Bundle bundle = new Bundle();
                bundle.putInt("data", idPaciente);
                iRegistrar.putExtras(bundle);
                startActivity(iRegistrar);
                Toast.makeText(getApplicationContext(), "SE HA LOGEADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();

            }
        }*/

    /*private Paciente buscarDato() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            Statement st = connection.createStatement();
            Paciente pa= new Paciente();
            ResultSet rs = st.executeQuery("SELECT * FROM pacientes WHERE id_paciente='" + idPaciente+"'" );
            if (rs.next()) {
            // El usuario se ha autenticado correctamente
                String nom=rs.getString("nombres");
                pa.setNombre(nom);
                String ape=rs.getString("apellidos");
                pa.setApellido(ape);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fecNac=sdf.format(rs.getDate("fecha_nacimiento"));
                pa.setFecNac(fecNac);
                String cor=rs.getString("correo");
                pa.setCorreo(cor);
                String con=rs.getString("contrasena");
                pa.setContra(con);
                String gen=rs.getString("sexo");
                pa.setSexo(gen);
                String tel=rs.getString("telefono");
                pa.setTelefono(tel);

                Log.d("TAG", "Nombre: " + nom);
                Log.d("TAG", "Apellido: " + ape);
            }
            return pa;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }*/

    //obtener Foto
    private void OnclickButtonSelectImage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar una opción");
        builder.setItems(new CharSequence[]{"Cámara", "Galería"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        checkCameraPermission();
                        break;
                    case 1:
                        openGallery();
                        break;
                }
            }
        });
        builder.show();

    }
    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Utiliza ActivityOptionsCompat para configurar las opciones de transición si es necesario
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        /*} else {
            Toast.makeText(this, "La cámara no está disponible", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_GALLERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bundle extras = data.getExtras();
                selectedImageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(selectedImageBitmap);
            } else if (requestCode == REQUEST_GALLERY) {
                Uri selectedImageUri = data.getData();
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    imageView.setImageBitmap(selectedImageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}