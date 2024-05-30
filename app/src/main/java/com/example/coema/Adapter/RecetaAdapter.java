package com.example.coema.Adapter;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.coema.Listas.Receta;
import com.example.coema.Listas.Tratamientos;
import com.example.coema.R;

import java.util.List;

public class RecetaAdapter extends RecyclerView.Adapter<RecetaAdapter.ViewHolder> {

    private List<Receta> recetas;
    private int selectedRecetaId = -1; // Variable para almacenar el ID del recibo seleccionado
    private Handler handler = new Handler(Looper.getMainLooper());

    public RecetaAdapter(List<Receta> recetas) {
        this.recetas = recetas;
    }

    public int getSelectedRecetaId() {
        return selectedRecetaId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recetas, parent, false);
        return new ViewHolder(view);
    }

    public void setRecetas(List<Receta> recetas) {
        this.recetas = recetas;
        notifyDataSetChanged(); // Notificar cambios en los datos para que el RecyclerView se actualice
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Receta receta = recetas.get(position);

        holder.idTextView.setText(String.valueOf(receta.getId()));
        holder.nameTextView.setText(receta.getNombre());
        holder.dosisTextView.setText(receta.getDosis());
        holder.medicamentoTextView.setText(String.valueOf(receta.getMedicamento()));

        // Configurar el estado del CheckBox
        holder.selectCheckbox.setChecked(receta.getId() == selectedRecetaId);

        // Escuchar cambios en el estado del CheckBox
        holder.selectCheckbox.setOnCheckedChangeListener(null); // Desactivar el listener temporalmente

        holder.selectCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Actualizar el ID del recibo seleccionado
                selectedRecetaId = receta.getId();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return recetas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView dosisTextView;
        public TextView medicamentoTextView;
        public TextView idTextView;
        public CheckBox selectCheckbox;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            dosisTextView = itemView.findViewById(R.id.dosisTextView);
            medicamentoTextView = itemView.findViewById(R.id.medicamentoTextView);
            selectCheckbox = itemView.findViewById(R.id.selectCheckbox);
            idTextView = itemView.findViewById(R.id.idTextView);
        }
    }
}
