package com.example.myittaroostockinventorymanger.stock_name_fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myittaroostockinventorymanger.R;
import com.example.myittaroostockinventorymanger.local.Stock;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAndRenameStockDialogFragment extends DialogFragment {

    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.edt_stock_name)
    EditText edtStockName;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    private CallBack callBack;
    private String option;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private AddAndRenameStockDialogFragment() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.stock_dialog_fragment, null, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();

        if (bundle != null) {

            option = bundle.getString(StockNameFragment.EXTRA_KEY);
        }

        changeTitleAndBtn();


        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();

        alertDialog.getWindow().
                setBackgroundDrawableResource(R.drawable.rounded_rectangle_white);

        btnSave.setOnClickListener(v -> {
            try {

                if (option.equals(StockNameFragment.ADD)) {
                    onClickBtnSave();
                    alertDialog.cancel();
                } else {
                    onClickBtnRename();
                    alertDialog.cancel();
                }

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

    public void onClickBtnRename() {

        String stockName = edtStockName.getText().toString();

        if (!stockName.equals("")) {

            callBack.onClickRename(new Stock(stockName));
        } else {
            throw new IllegalArgumentException("Name cannot be empty");
        }
    }

    public static AddAndRenameStockDialogFragment getNewInstance(String key, String option) {
        AddAndRenameStockDialogFragment addAndRenameStockDialogFragment = new AddAndRenameStockDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(key, option);
        addAndRenameStockDialogFragment.setArguments(bundle);
        return addAndRenameStockDialogFragment;
    }

    public interface CallBack {

        void onClickSave(Stock stock);

        void onClickRename(Stock stock);
    }

    private void changeTitleAndBtn() {
        if (option.equals(StockNameFragment.ADD)) {
            txtTitle.setText("Add New Stock Name");
            btnSave.setText("Save");
        } else {
            txtTitle.setText("Rename the Stock");
            btnSave.setText("Rename");
        }
    }
}
