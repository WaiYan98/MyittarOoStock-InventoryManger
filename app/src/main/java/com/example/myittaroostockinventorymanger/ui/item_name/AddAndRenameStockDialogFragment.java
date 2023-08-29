package com.example.myittaroostockinventorymanger.ui.item_name;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myittaroostockinventorymanger.R;
import com.example.myittaroostockinventorymanger.data.entities.Stock;
;

public class AddAndRenameStockDialogFragment extends DialogFragment {

    TextView txtTitle;
    EditText edtStockName;
    Button btnSave;
    Button btnCancel;

    private String option;
    private AddAndRenameStockViewModel addAndRenameStockViewModel;
    private AlertDialog alertDialog;
    private Context context;
    private Stock stock;

    private AddAndRenameStockDialogFragment() {

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.stock_dialog_fragment, null, false);

        txtTitle = view.findViewById(R.id.txt_title);
        edtStockName = view.findViewById(R.id.edt_stock_name);
        btnSave = view.findViewById(R.id.btn_save);
        btnCancel = view.findViewById(R.id.btn_cancel);

        Bundle bundle = getArguments();

        if (bundle != null) {

            option = bundle.getString(StockNameFragment.EXTRA_OPTION);
            stock = bundle.getParcelable(StockNameFragment.EXTRA_STOCK);
        }

        changeTitleAndBtn();

        if (!option.equals(StockNameFragment.ADD)) {
            edtStockName.setText(stock.getName());
        }


        alertDialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();

        alertDialog.getWindow().
                setBackgroundDrawableResource(R.drawable.rounded_rectangle_white);

        return alertDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        addAndRenameStockViewModel = new ViewModelProvider(requireActivity())
                .get(AddAndRenameStockViewModel.class);

        btnSave.setOnClickListener(v -> {

            String stockName = edtStockName.getText().toString();

            if (option.equals(StockNameFragment.ADD)) {
                addAndRenameStockViewModel.onClickBtn(new Stock(stockName), option);
            } else {
                this.stock.setName(stockName);
                addAndRenameStockViewModel.onClickBtn(this.stock, option);
            }
        });

        btnCancel.setOnClickListener(v -> {
            alertDialog.cancel();
        });

        //add and rename stock to database
        addAndRenameStockViewModel.getStock()
                .observe(getParentFragment().getViewLifecycleOwner(), s -> {

                    Stock stock = s.getContentIfNotHandle();
                    String option = addAndRenameStockViewModel.getOption();

                    if (stock != null) {
                        if (option.equals(StockNameFragment.ADD)) {
                            addAndRenameStockViewModel.insertStock(stock);
                        } else {
                            addAndRenameStockViewModel.updateStockName(stock);
                        }
                    }
                    alertDialog.cancel();
                });

        //to show message update,added or error
        addAndRenameStockViewModel.getMessage()
                .observe(getParentFragment().getViewLifecycleOwner(), message -> {

                    String notify = message.getContentIfNotHandle();

                    if (notify != null) {
                        Toast.makeText(context, notify, Toast.LENGTH_SHORT).show();
                    }
                });

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    public static AddAndRenameStockDialogFragment getNewInstance(String key1, String key2,
                                                                 String option,
                                                                 Stock stockForRename) {
        AddAndRenameStockDialogFragment addAndRenameStockDialogFragment = new AddAndRenameStockDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(key1, option);
        bundle.putParcelable(key2, stockForRename);
        addAndRenameStockDialogFragment.setArguments(bundle);
        return addAndRenameStockDialogFragment;
    }

    //change dialogFragment title and button
    private void changeTitleAndBtn() {
        if (option.equals(StockNameFragment.ADD)) {
            txtTitle.setText("Add New Stock Name");
            btnSave.setText("Save");
        } else {
            txtTitle.setText("Rename the Stock");
            btnSave.setText("Rename");
        }
    }

    //this need context for toast
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
