package com.example.coema.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.coema.Fragments.RecyclerViewItemClickListener;
import com.example.coema.Listas.CitasOd;
import com.example.coema.R;

import java.util.List;

public class RadiografiasAdapter extends RecyclerView.Adapter<RadiografiasAdapter.ViewHolder> {
    private List<byte[]> listaDeDatos;


    public RadiografiasAdapter(List<byte[]> datos) {
        this.listaDeDatos = datos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_radiografias, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        byte[] datosBytea = listaDeDatos.get(position);
        Bitmap imagen = BitmapFactory.decodeByteArray(datosBytea, 0, datosBytea.length);
        holder.imagenView.setImageBitmap(imagen);
    }

    @Override
    public int getItemCount() {
        return listaDeDatos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenView;

        public ViewHolder(View itemView) {
            super(itemView);
            imagenView = itemView.findViewById(R.id.imageView);
        }
    }

}
