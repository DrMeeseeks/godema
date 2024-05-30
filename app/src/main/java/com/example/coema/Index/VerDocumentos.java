package com.example.coema.Index;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coema.Adapter.CitasAdapter;
import com.example.coema.Adapter.RadiografiasAdapter;
import com.example.coema.Conection.DatabaseConnection;
import com.example.coema.Listas.CitasOd;
import com.example.coema.Listas.Tratamientos;
import com.example.coema.Perfil.PerfilPaciente;
import com.example.coema.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VerDocumentos extends AppCompatActivity {
    Integer codigo;
    List<byte[]> listaDeDatos = new ArrayList<>();
    Button buttonTomarFoto, buttonAdjuntarRadiografia;

    ImageView dialogImageView;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    RadiografiasAdapter adaptador;
    private Bitmap selectedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_documento);
        recuperarData();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        VerDocumentos.ObtenerDatosDeTablaAsyncTask task = new VerDocumentos.ObtenerDatosDeTablaAsyncTask();
        task.execute();
        adaptador = new RadiografiasAdapter(listaDeDatos);
        recyclerView.setAdapter(adaptador);
        buttonAdjuntarRadiografia = findViewById(R.id.buttonAdjuntarRadiografia);
        buttonAdjuntarRadiografia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogPersonalizado();
            }
        });
    }
    private void recuperarData() {
        Bundle bundle= getIntent().getExtras();
        if (bundle==null){
            codigo=null;
        }else{
            codigo=(int)bundle.getSerializable("clave_valor");
        }
    }
    private void mostrarDialogPersonalizado() {
        // Obtén el LayoutInflater del contexto actual
        LayoutInflater inflater = LayoutInflater.from(this);

        // Infla el diseño de tu diálogo personalizado
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);

        // Declaración de botones y ImageView
        buttonTomarFoto = dialogView.findViewById(R.id.buttonTomarFoto);
        dialogImageView= dialogView.findViewById(R.id.dialogImageView);

        buttonTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implementa la lógica para tomar una foto (utiliza CameraX o la API de la cámara de Android)
                // Guarda la imagen tomada en 'selectedImage' (Bitmap)
                OnclickButtonSelectImage();
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setPositiveButton("Ingresar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selectedImage != null) {
                            // La imagen se ha seleccionado o tomado, haz lo que necesites con ella
                            // Puedes mostrar un mensaje de éxito o realizar alguna acción
                            VerDocumentos.GuardarCambiosAsyncTask task = new VerDocumentos.GuardarCambiosAsyncTask(codigo);
                            task.execute();
                            Toast.makeText(getApplicationContext(), "Se guardo la imagen correctamente.", Toast.LENGTH_SHORT).show();

                        } else {
                            // No se ha seleccionado ninguna imagen, muestra un mensaje de error
                            Toast.makeText(getApplicationContext(), "No se ha seleccionado ninguna imagen.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }
    public class GuardarCambiosAsyncTask extends AsyncTask<Void, Void, String> {
        private final Integer idCita;

        public GuardarCambiosAsyncTask(Integer idCita) {
            this.idCita = idCita;
        }

        @Override
        protected String doInBackground(Void... voids) {
            Connection connection;
            try {
                connection = DatabaseConnection.getConnection();
                if (connection != null) {
                    // Resto de tu código...

                    if (selectedImage != null) {
                        // Convierte la imagen en un formato de bytes
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        // Actualiza la base de datos con la imagen en formato bytea
                        String updateQuery3 = "INSERT INTO radiografias (foto,id_cita) VALUES(?,?)";
                        PreparedStatement preparedStatement3 = connection.prepareStatement(updateQuery3);
                        preparedStatement3.setBytes(1, byteArray);
                        preparedStatement3.setInt(2, idCita);

                        preparedStatement3.executeUpdate();
                        preparedStatement3.close();
                    }
                } else {
                    return "Error: No se pudo establecer una conexión a la base de datos.";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }




    private class ObtenerDatosDeTablaAsyncTask extends AsyncTask<Void, Void, List<byte[]>> {

        @Override
        protected List<byte[]> doInBackground(Void... voids) {
            List<byte[]> fotos = new ArrayList<>();
            try {
                // Obtener una conexión a la base de datos
                Connection conn = DatabaseConnection.getConnection();

                if (conn != null) {
                    // Ejecutar una consulta SQL para obtener datos
                    String consultaSQL = "SELECT foto FROM radiografias WHERE id_cita="+codigo;
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(consultaSQL);

                    // Procesar los resultados de la consulta y agregarlos a la lista
                    while (resultSet.next()) {
                        byte[] datos = resultSet.getBytes("foto");
                        fotos.add(datos);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return fotos;
        }

        @Override
        protected void onPostExecute(List<byte[]> fotos) {
            super.onPostExecute(fotos);

            // Actualiza el adaptador con la lista de recibos obtenida de la base de datos
            listaDeDatos.addAll(fotos);
            adaptador.notifyDataSetChanged();
        }


    }

    private void OnclickButtonSelectImage() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
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
                selectedImage = (Bitmap) extras.get("data");
                dialogImageView.setImageBitmap(selectedImage);
            } else if (requestCode == REQUEST_GALLERY) {
                Uri selectedImageUri = data.getData();
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    dialogImageView.setImageBitmap(selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
