package com.example.coema.Perfil;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.coema.Index.ListarTratamientos;
import com.example.coema.Index.NotifyMod;
import com.example.coema.Index.PacienteVerCita;
import com.example.coema.R;
import com.example.coema.Registro.RegistroCitas;

import java.util.ArrayList;

public class PacientePrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paciente_principal);
//        obtenerDatos();


        // Puedes agregar aquí la lógica para manejar la entrada del usuario y la funcionalidad de guardado en la base de datos.
        mostrarNotificacion();
    }

    private void mostrarNotificacion() {
        // Crea una intención para abrir la actividad actual (PerfilPaciente)
        Intent intent = new Intent(this, PerfilPaciente.class);

        NotificationCompat.Builder builder; // Declaración de builder

        // Debes crear un canal de notificación si la versión de Android es 8 o superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "mi_canal_id";
            CharSequence channelName = "Mi Canal de Notificación";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            // Inicializa builder con el canal
            builder = new NotificationCompat.Builder(this, channelId);
        } else {
            // Si la versión es anterior a Android 8, utiliza el constructor sin canal
            builder = new NotificationCompat.Builder(this);
        }

        builder.setSmallIcon(R.drawable.ic_info)
                .setContentTitle("Bienvenido a la actividad Perfil")
                .setContentText("Esta es una notificación de ejemplo")
                .setAutoCancel(true);

        // Puedes agregar vibración a la notificación
        long[] pattern = {0, 1000, 1000};
        builder.setVibrate(pattern);

        // Crea una PendingIntent para abrir la actividad al hacer clic en la notificación
        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        builder.setContentIntent(pendingIntent);

        // Muestra la notificación
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(0, builder.build());
    }



    public void registrarCita(View view) {
        Intent intent = new Intent(this, RegistroCitas.class);
        startActivity(intent);
    }
    public void perfilPaciente(View view) {
        Intent intent = new Intent(this, PerfilPaciente.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void verCitas(View view) {
        Intent intent = new Intent(this, PacienteVerCita.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void confirmarCita(View view){
        Intent intent = new Intent(this, NotifyMod.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void verTratamientos(View view) {
        Intent intent = new Intent(this, ListarTratamientos.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivity(intent);
    }




    /*public void salirPrincipalPaciente(View view) {
        SharedPreferences sharedPref = this.getSharedPreferences("correo_electronico", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, ActPrincipal.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivity(intent);
    }    */

    public void nosotrosPrincipalPaciente(View view) {
        Intent intent = new Intent(this, ActivityInfo.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        startActivity(intent);
    }



}

