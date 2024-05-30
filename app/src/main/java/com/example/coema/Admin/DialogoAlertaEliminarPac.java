package com.example.coema.Admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogoAlertaEliminarPac extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=
                new AlertDialog.Builder(getActivity());

        builder.setMessage("Se eliminó al paciente seleccionado")
                .setTitle("REALIZADO")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = getActivity().getIntent();
                        Bundle bundle=new Bundle();
                        Toast.makeText(getContext(), "Eliminado exitosamente", Toast.LENGTH_LONG).show();

                        getActivity().finish();
                        getActivity().overridePendingTransition(0,0);
                        startActivity(intent);
                        getActivity().overridePendingTransition(0,0);
                        dialogInterface.cancel();

                    }
                });
        return builder.create();
    }
}
