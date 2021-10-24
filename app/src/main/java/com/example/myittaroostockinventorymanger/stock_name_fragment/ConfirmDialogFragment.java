package com.example.myittaroostockinventorymanger.stock_name_fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myittaroostockinventorymanger.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfirmDialogFragment extends DialogFragment {

    @BindView(R.id.btn_yes)
    Button btnYes;
    @BindView(R.id.btn_no)
    Button btnNo;

    private ConfirmDialogFragment.CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.confirm_dialog_fragment, null, false);
        ButterKnife.bind(this, view);

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();

        alertDialog.getWindow()
                .setBackgroundDrawableResource(R.drawable.rounded_rectangle_white);

        btnYes.setOnClickListener(v -> {
            callBack.onClickYes();
            alertDialog.cancel();
        });

        btnNo.setOnClickListener(v -> {
            alertDialog.cancel();
        });

        return alertDialog;

    }

    public interface CallBack {
        void onClickYes();
    }
}
