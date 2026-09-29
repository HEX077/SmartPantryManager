package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class ExpiryDatePickerDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public interface SaveDateListener {
        void didFinishDatePickerDialog(Calendar selectedTime);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar selectedTime = Calendar.getInstance();
        selectedTime.set(year, month, day);
        SaveDateListener activity = (SaveDateListener) getActivity();
        if (activity != null) {
            activity.didFinishDatePickerDialog(selectedTime);
        }
    }
}

