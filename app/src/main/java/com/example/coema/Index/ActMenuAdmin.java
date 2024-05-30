package com.example.coema.Index;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coema.Fragments.IListarPacienteAdmin;
import com.example.coema.Index.MenuRecibosActivity;
import com.example.coema.Listas.Paciente;
import com.example.coema.R;
import com.example.coema.Registro.CrudOdontologo;
import com.example.coema.Registro.RegistroOdontologo;
import com.example.coema.Registro.RegistroTratamientos;

public class ActMenuAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_principal);
    }

    // Método que se llama cuando se hace clic en la imagen de "Recibo de Pagos"

    public void onListarPacientesClick(View view) {
        // Iniciar la actividad MenuRecibosActivity
        Intent intent = new Intent(this, ListarPaciente.class);
        startActivity(intent);
    }
    public void onReciboDePagosClick(View view) {
        // Iniciar la actividad MenuRecibosActivity
        Intent intent = new Intent(this, MenuRecibosActivity.class);
        startActivity(intent);
    }

    public void listarPacientesAdmin(View view) {
        // Iniciar la actividad MenuRecibosActivity
        Intent intent = new Intent(this, PacientesMod.class);
        startActivity(intent);
    }
    public void onTratamientosClick(View view) {
        // Iniciar la actividad MenuRecibosActivity
        Intent intent = new Intent(this, ListarTratamientos.class);
        startActivity(intent);
    }
    public void verDoctor(View view) {
        // Iniciar la actividad MenuRecibosActivity
        Intent intent = new Intent(this, RegistroOdontologo.class);
        startActivity(intent);
    }
    public void verCitasA(View view) {
        // Iniciar la actividad MenuRecibosActivity
        Intent intent = new Intent(this, VerCitas.class);
        startActivity(intent);
    }
}
