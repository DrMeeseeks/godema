package com.example.coema.Adapter;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.coema.Listas.Descanso;
import com.example.coema.Listas.Tratamientos;
import com.example.coema.R;

import java.util.List;

public class DescansoAdapter extends RecyclerView.Adapter<DescansoAdapter.ViewHolder> {

    private List<Descanso> descansos;
    private int selectedDescansoId = -1; // Variable para almacenar el ID del recibo seleccionado
    private Handler handler = new Handler(Looper.getMainLooper());

    public DescansoAdapter(List<Descanso> descansos) {
        this.descansos = descansos;
    }

    public int getSelectedDescansoId() {
        return selectedDescansoId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_descanso, parent, false);
        return new ViewHolder(view);
    }

    public void setDescanso(List<Descanso> descansos) {
        this.descansos = descansos;
        notifyDataSetChanged(); // Notificar cambios en los datos para que el RecyclerView se actualice
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Descanso descanso = descansos.get(position);

        holder.idTextView.setText(String.valueOf(descanso.getId()));
        holder.nameTextView.setText(descanso.getNombre());
        holder.detalleTextView.setText(descanso.getTratamiento());
        holder.amountTextView.setText(String.valueOf(descanso.getDescanso()));

        // Configurar el estado del CheckBox
        holder.selectCheckbox.setChecked(descanso.getId() == selectedDescansoId);

        // Escuchar cambios en el estado del CheckBox
        holder.selectCheckbox.setOnCheckedChangeListener(null); // Desactivar el listener temporalmente

        holder.selectCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Actualizar el ID del recibo seleccionado
                selectedDescansoId = descanso.getId();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return descansos.size();
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
