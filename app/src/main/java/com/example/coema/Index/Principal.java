package com.example.coema.Index;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coema.Login.IniciarSesion;
import com.example.coema.Login.IniciarSesionAdmin;
import com.example.coema.R;
import com.example.coema.Registro.RegistroPacientes;


public class Principal extends AppCompatActivity {

    Button btnNoAdminInic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);
        btnNoAdminInic=(Button) findViewById(R.id.btnIniciarSesion);
        registerForContextMenu(btnNoAdminInic);
        btnNoAdminInic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Muestra el menú contextual cuando se mantiene presionado el botón
                openContextMenu(v);
                return true; // Devuelve true para indicar que se ha manejado el evento
            }
        });
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contextual, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.adminInic) {
            // Realiza alguna acción cuando se seleccione el elemento de menú
            Intent intent = new Intent(Principal.this, IniciarSesionAdmin.class);
            startActivity(intent);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }
    public void iniciarSesion(View view){
        Intent intent = new Intent(this, IniciarSesion.class);
        Bundle bundle=new Bundle();
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void redRegistrar(View view){
        Intent intent = new Intent(this, RegistroPacientes.class);
        Bundle bundle=new Bundle();
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void redIniciarSesionAdmin(View view){
        Intent intent = new Intent(this, IniciarSesionAdmin.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
