package com.example.coema.Adapter;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.coema.Listas.Paciente;
import com.example.coema.Listas.Tratamientos;
import com.example.coema.R;

import java.util.List;

public class PacienteAdapter extends RecyclerView.Adapter<PacienteAdapter.ViewHolder> {

    private List<Paciente> paciente;
    private int selectedPacienteId = -1; // Variable para almacenar el ID del recibo seleccionado
    private Handler handler = new Handler(Looper.getMainLooper());

    public PacienteAdapter(List<Paciente> paciente) {
        this.paciente = paciente;
    }

    public int getSelectedPacienteId() {
        return selectedPacienteId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paciente, parent, false);
        return new ViewHolder(view);
    }

    public void setTratamientos(List<Paciente> paciente) {
        this.paciente = paciente;
        notifyDataSetChanged(); // Notificar cambios en los datos para que el RecyclerView se actualice
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Paciente pacientes = paciente.get(position);

        holder.idTextView.setText(String.valueOf(pacientes.getId()));
        holder.nameTextView.setText(pacientes.getNombre());
        holder.detalleTextView.setText(pacientes.getApellido());
        holder.amountTextView.setText(String.valueOf(pacientes.getCorreo()));

        // Configurar el estado del CheckBox
        holder.selectCheckbox.setChecked(pacientes.getId() == selectedPacienteId);

        // Escuchar cambios en el estado del CheckBox
        holder.selectCheckbox.setOnCheckedChangeListener(null); // Desactivar el listener temporalmente

        holder.selectCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Actualizar el ID del recibo seleccionado
                selectedPacienteId = pacientes.getId();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return paciente.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView detalleTextView;
        public TextView amountTextView;
        public TextView idTextView;
        public CheckBox selectCheckbox;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            detalleTextView = itemView.findViewById(R.id.detalleTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            selectCheckbox = itemView.findViewById(R.id.selectCheckbox);
            idTextView = itemView.findViewById(R.id.idTextView);
        }
    }
}
