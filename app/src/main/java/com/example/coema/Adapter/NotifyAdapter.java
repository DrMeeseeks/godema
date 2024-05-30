package com.example.coema.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coema.Index.NotifyMod;
import com.example.coema.Listas.Notify;

import com.example.coema.R;

import java.text.SimpleDateFormat;
import java.util.List;


public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.NotifyViewHolder> {
    private List<Notify> notifyList;
    private Context context;

    public NotifyAdapter(Context context, List<Notify> notifyList) {
        this.context = context;
        this.notifyList = notifyList;
    }

    @NonNull
    @Override
    public NotifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notify, parent, false);
        return new NotifyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyViewHolder holder, int position) {
        final Notify notify = notifyList.get(position);
        holder.bind(notify);

        // Agregar un oyente para el cambio de estado del CheckBox
        holder.checkboxCita.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (context instanceof NotifyMod) {
                        ((NotifyMod) context).setCitaSeleccionadaId(notify.getIdCita());
                    }
                } else {
                    if (context instanceof NotifyMod) {
                        ((NotifyMod) context).setCitaSeleccionadaId(-1);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifyList.size();
    }

    public class NotifyViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkboxCita;
        private TextView txtEstadoCita;
        private TextView txtTratamiento;
        private TextView txtFecha;

        public NotifyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkboxCita = itemView.findViewById(R.id.checkboxCita);
            txtEstadoCita = itemView.findViewById(R.id.txtEstadoCita);
            txtTratamiento = itemView.findViewById(R.id.txtTratamiento);
            txtFecha = itemView.findViewById(R.id.txtFecha);
        }

        public void bind(Notify notify) {
            checkboxCita.setChecked(false); // No estoy seguro de si necesitas establecer esto en falso
            txtEstadoCita.setText(notify.getEstadoCita()); // Establecer el estado de la cita desde Notify
            txtTratamiento.setText(notify.getNombreTratamiento()); // Establecer el nombre del tratamiento desde Notify
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String fechaFormatted = sdf.format(notify.getFechaCita());
            txtFecha.setText(fechaFormatted);
        }
    }
}

