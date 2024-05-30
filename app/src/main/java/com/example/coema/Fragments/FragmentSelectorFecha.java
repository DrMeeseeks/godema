package com.example.coema.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;


public class FragmentSelectorFecha extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private DatePickerDialog.OnDateSetListener listener;

    public static FragmentSelectorFecha newInstance(DatePickerDialog.OnDateSetListener listener){
        FragmentSelectorFecha fragmento = new FragmentSelectorFecha();
        fragmento.establecerListener(listener);
        return fragmento;
    }

    private void establecerListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar calendario = Calendar.getInstance();
        int a = calendario.get(Calendar.YEAR);
        int m = calendario.get(Calendar.MONTH);
        int d = calendario.get(Calendar.DAY_OF_MONTH);



        return  new DatePickerDialog(getActivity(), listener, a, m, d);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int a, int m, int d) {

    }


}
