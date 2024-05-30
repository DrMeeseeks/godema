package com.example.coema.Index;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coema.Adapter.ReceiptAdapter;
import com.example.coema.Conection.DatabaseConnection;
import com.example.coema.Modelos.Receipt;
import com.example.coema.R;
import com.example.coema.Registro.RegistroPagoEdit;
import com.example.coema.Registro.RegitroPago;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MenuRecibosActivity extends AppCompatActivity {

    private Button editHistoryButton;
    private Button addPaymentButton;
    private RecyclerView recyclerView;
    private ReceiptAdapter adapter;
    private List<Receipt> receipts = new ArrayList<>(); // Lista para almacenar los recibos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_recibos);

        // Encuentra las referencias a los botones
        editHistoryButton = findViewById(R.id.editHistoryButton);
        addPaymentButton = findViewById(R.id.addPaymentButton);

        // Configura el RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReceiptAdapter(receipts); // Pasa la lista de recibos al adaptador
        recyclerView.setAdapter(adapter);

        // Configura los listeners de los botones
        editHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén el ID del recibo seleccionado directamente desde el adaptador
                long selectedReceiptId = adapter.getSelectedReceiptId();

                if (selectedReceiptId != -1) {
                    // Abre la actividad de edición y pasa los datos seleccionados
                    Intent intent = new Intent(MenuRecibosActivity.this, RegistroPagoEdit.class);
                    intent.putExtra("selectedReceiptId", selectedReceiptId);
                    startActivity(intent);
                } else {
                    // Mostrar un mensaje de que ningún recibo está seleccionado
                    Toast.makeText(MenuRecibosActivity.this, "Selecciona un recibo primero", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agrega aquí la lógica para el botón "Agregar Pago"
                // Por ejemplo, puedes abrir la actividad RegistroPagoActivity.
                Intent intent = new Intent(MenuRecibosActivity.this, RegitroPago.class);
                startActivity(intent);
            }
        });

        // Iniciar una tarea asincrónica para obtener datos de la base de datos
        new ObtenerDatosDeTablaAsyncTask().execute();
    }

    // Tarea asincrónica para obtener datos de la tabla
    private class ObtenerDatosDeTablaAsyncTask extends AsyncTask<Void, Void, List<Receipt>> {

        @Override
        protected List<Receipt> doInBackground(Void... voids) {
            List<Receipt> receipts = new ArrayList<>();

            try {
                // Obtener una conexión a la base de datos
                Connection conn = DatabaseConnection.getConnection();

                if (conn != null) {
                    // Ejecutar una consulta SQL para obtener datos
                    String consultaSQL = "SELECT id, name, date, amount FROM receipt";
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(consultaSQL);

                    // Procesar los resultados de la consulta y agregarlos a la lista
                    while (resultSet.next()) {
                        long id = resultSet.getLong("id");
                        String name = resultSet.getString("name");
                        String date = resultSet.getString("date");
                        double amount = resultSet.getDouble("amount");

                        receipts.add(new Receipt(id, name, date, amount));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return receipts;
        }

        @Override
        protected void onPostExecute(List<Receipt> receiptList) {
            super.onPostExecute(receiptList);

            // Actualiza el adaptador con la lista de recibos obtenida de la base de datos
            receipts.addAll(receiptList);
            adapter.notifyDataSetChanged();
        }
    }
}
