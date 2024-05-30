package com.example.coema.Adapter;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.coema.Fragments.RecyclerViewItemClickListener;
import com.example.coema.Listas.CitasOd;
import com.example.coema.Listas.CitasPac;
import com.example.coema.R;

import java.util.List;

public class CitasPacAdapter extends RecyclerView.Adapter<CitasPacAdapter.ViewHolder> {



    private RecyclerViewItemClickListener clickListener;
    private List<CitasPac> citas;
    private int selectedCitasId = -1; // Variable para almacenar el ID del recibo seleccionado
    private Handler handler = new Handler(Looper.getMainLooper());

    private List<String> listaDeValores;
    //private OnItemClickListener listener;
    public CitasPacAdapter(List<CitasPac> citas, RecyclerViewItemClickListener listener) {
        this.citas = citas;
        this.clickListener = listener;
        //OnItemClickListener listener;
    }


    public int getSelectedCitasId() {
        return selectedCitasId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_citas, parent, false);
        return new ViewHolder(view);
    }

    public void setTratamientos(List<CitasPac> citas) {
        this.citas = citas;
        notifyDataSetChanged(); // Notificar cambios en los datos para que el RecyclerView se actualice
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CitasPac cita = citas.get(holder.getAdapterPosition());
        /*final String elemento = listaDeValores.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(elemento);
                }
            }
        });*/

        holder.txtNombre.setText(cita.getNom());
        holder.txtTratamiento.setText(cita.getTrat());
        holder.txtFecha.setText(String.valueOf(cita.getFec()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(holder.getAdapterPosition(),cita.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return citas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNombre;
        public TextView txtTratamiento;
        public TextView txtFecha;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtTratamiento = itemView.findViewById(R.id.txtTratamiento);
            txtFecha = itemView.findViewById(R.id.txtFecha);
        }
    }
}
