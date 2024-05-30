package com.example.coema.Adapter;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.coema.Listas.Tratamientos;
import com.example.coema.R;

import java.util.List;

public class TratamientosAdapter extends RecyclerView.Adapter<TratamientosAdapter.ViewHolder> {

    private List<Tratamientos> tratamientos;
    private int selectedTratamientosId = -1; // Variable para almacenar el ID del recibo seleccionado
    private Handler handler = new Handler(Looper.getMainLooper());

    public TratamientosAdapter(List<Tratamientos> tratamientos) {
        this.tratamientos = tratamientos;
    }

    public int getSelectedTratamientosId() {
        return selectedTratamientosId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tratamiento, parent, false);
        return new ViewHolder(view);
    }

    public void setTratamientos(List<Tratamientos> tratamientos) {
        this.tratamientos = tratamientos;
        notifyDataSetChanged(); // Notificar cambios en los datos para que el RecyclerView se actualice
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tratamientos tratamiento = tratamientos.get(position);

        holder.idTextView.setText(String.valueOf(tratamiento.getId()));
        holder.nameTextView.setText(tratamiento.getNom());
        holder.detalleTextView.setText(tratamiento.getDet());
        holder.amountTextView.setText(String.valueOf(tratamiento.getPrec()));

        // Configurar el estado del CheckBox
        holder.selectCheckbox.setChecked(tratamiento.getId() == selectedTratamientosId);

        // Escuchar cambios en el estado del CheckBox
        holder.selectCheckbox.setOnCheckedChangeListener(null); // Desactivar el listener temporalmente

        holder.selectCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Actualizar el ID del recibo seleccionado
                selectedTratamientosId = tratamiento.getId();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tratamientos.size();
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
