package com.example.coema.Index;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coema.Login.IniciarSesionAdmin;
import com.example.coema.Perfil.PerfilPaciente;
import com.example.coema.R;

public class ActMenuOdonto extends AppCompatActivity {

    Button btnVerCitas, btnVerDocumentos, btnVerReceta, btnVerDescanso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.odontologo_principal);
        btnVerCitas=(Button)findViewById(R.id.btnVerCitas);
        btnVerReceta=(Button)findViewById(R.id.btnReceta);
        btnVerDescanso=(Button)findViewById(R.id.btnDescanso);
        btnVerDocumentos=(Button)findViewById(R.id.btnVerDocumentos);
        btnVerDocumentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActMenuOdonto.this, VerCitas.class);
                intent.putExtra("opcion", 1);
                startActivity(intent);
            }
        });
        btnVerReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActMenuOdonto.this, VerCitas.class);
                intent.putExtra("opcion", 2);
                startActivity(intent);
            }
        });
        btnVerDescanso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActMenuOdonto.this, VerCitas.class);
                intent.putExtra("opcion", 3);
                startActivity(intent);
            }
        });
        btnVerCitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iVerCitas = new Intent(ActMenuOdonto.this, ListarCitas.class);
                Bundle bundle = new Bundle();
                iVerCitas.putExtras(bundle);
                startActivity(iVerCitas);          }
        });
    }


}
