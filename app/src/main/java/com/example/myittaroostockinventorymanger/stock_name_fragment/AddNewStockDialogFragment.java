package com.example.myittaroostockinventorymanger.stock_name_fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myittaroostockinventorymanger.R;
import com.example.myittaroostockinventorymanger.local.Stock;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewStockDialogFragment extends DialogFragment {

    @BindView(R.id.edt_stock_name)
    EditText edtStockName;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private AddNewStockDialogFragment() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.add_new_stock_dialog_fragment, null, false);
        ButterKnife.bind(this, view);

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();

        alertDialog.getWindow().
                setBackgroundDrawableResource(R.drawable.rounded_rectangle_white);

        btnSave.setOnClickListener(v -> {
            try {
                onClickBtnSave();
                alertDialog.cancel();
            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> {
            alertDialog.cancel();
        });

        return alertDialog;
    }

    public void onClickBtnSave() {

        String stockName = edtStockName.getText().toString();

        if (!stockName.equals("")) {

            callBack.onClickSave(new Stock(stockName));
        } else {
            throw new IllegalArgumentException("Name cannot be empty");
        }
    }

    public static AddNewStockDialogFragment getNewInstance() {
        AddNewStockDialogFragment addNewStockDialogFragment = new AddNewStockDialogFragment();
        return addNewStockDialogFragment;
    }

    public interface CallBack {

        void onClickSave(Stock stock);
    }
}
