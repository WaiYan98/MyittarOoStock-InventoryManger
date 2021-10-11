package com.example.myittaroostockinventorymanger.stock_name_fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myittaroostockinventorymanger.R;

public class AddNewStockDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.add_new_stock_dialog_fragment, null, false);

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();

        return alertDialog;
    }
}
