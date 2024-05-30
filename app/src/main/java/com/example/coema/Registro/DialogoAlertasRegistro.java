/*
package com.example.coema.Registro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.coema.Listas.Paciente;
import com.example.coema.Modelos.DAOPaciente;
import com.example.coema.R;

import java.util.ArrayList;



public class DialogoAlertasRegistro extends DialogFragment {
    TextView txtNom, txtCor, txtContra, txtFecha;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=
                new AlertDialog.Builder(getActivity());

        builder.setMessage("Revise si se ha registrado correctamente ")
                .setTitle("CUIDADO")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtNom = getActivity().findViewById(R.id.txtNomte);
                        txtCor = getActivity().findViewById(R.id.txtEmailR);
                        txtContra = getActivity().findViewById(R.id.txtContraR);
                        txtFecha = getActivity().findViewById(R.id.txtFechaR);


                        DAOPaciente daoPaciente = new DAOPaciente(getContext());
                        daoPaciente.openBD();

                        int foto = getArguments().getInt("idFoto");
                        String nombre  = getArguments().getString("nombre");
                        String correo = getArguments().getString("correo");
                        String contra = getArguments().getString("contra");
                        String fecha = getArguments().getString("fecha");
                        String sexo = getArguments().getString("sexo");
                        ArrayList<Paciente> listaPacientes = (ArrayList<Paciente>) getArguments().getSerializable("listaPacientes");

                        Paciente p = new Paciente(foto, nombre, correo, contra, fecha, sexo);

                        daoPaciente.registrarPaciente(p);
                        listaPacientes = daoPaciente.getPaciente();
                        Toast.makeText(getContext(), "Registrado Exitosamente", Toast.LENGTH_LONG).show();
                        limpiar();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Arregle sus datos", Toast.LENGTH_LONG).show();
                        dialogInterface.cancel();
                    }
                });
        return builder.create();
    }

        public void limpiar(){
            txtNom.setText("");
            txtCor.setText("");
            txtFecha.setText("");
            txtContra.setText("");
        }
    }
*/
