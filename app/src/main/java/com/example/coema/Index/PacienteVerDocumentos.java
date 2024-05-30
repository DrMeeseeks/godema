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

import com.example.coema.Adapter.RadiografiasAdapter;
import com.example.coema.Conection.DatabaseConnection;
import com.example.coema.Conection.GlobalVariables;
import com.example.coema.Perfil.PerfilPaciente;
import com.example.coema.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PacienteVerDocumentos extends AppCompatActivity {
    Integer codigo;
    List<byte[]> listaDeDatos = new ArrayList<>();

    ImageView dialogImageView;
    RadiografiasAdapter adaptador;
    private Bitmap selectedImage;
    Integer idActual= GlobalVariables.getInstance().getUserId();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paciente_ver_documento);
        recuperarData();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        PacienteVerDocumentos.ObtenerDatosDeTablaAsyncTask task = new PacienteVerDocumentos.ObtenerDatosDeTablaAsyncTask();
        task.execute();
        adaptador = new RadiografiasAdapter(listaDeDatos);
        recyclerView.setAdapter(adaptador);
    }
    private void recuperarData() {
        Bundle bundle= getIntent().getExtras();
        if (bundle==null){
            codigo=null;
        }else{
            codigo=(int)bundle.getSerializable("clave_valor");
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
                    String consultaSQL = "SELECT foto FROM radiografias WHERE id_cita="+codigo+"and id_paciente="+idActual;
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
            if(fotos.isEmpty()){
                Toast.makeText(PacienteVerDocumentos.this, "No hay radiografias", Toast.LENGTH_SHORT).show();
            }else {
                listaDeDatos.addAll(fotos);
                adaptador.notifyDataSetChanged();
            }
        }


    }
}
