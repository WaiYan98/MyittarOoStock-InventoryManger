package com.example.myittaroostockinventorymanger.stock_name_fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

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
    private AddAndRenameStockViewModel addAndRenameStockViewModel;
    private AlertDialog alertDialog;
    private Context context;

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

        Log.d("tag", "onCreateView: ");


        addAndRenameStockViewModel = new ViewModelProvider(requireActivity())
                .get(AddAndRenameStockViewModel.class);

        btnSave.setOnClickListener(v -> {

            String stockName = edtStockName.getText().toString();
            addAndRenameStockViewModel.onClickSave(new Stock(stockName));
        });

        btnCancel.setOnClickListener(v -> {
            alertDialog.cancel();
        });

        addAndRenameStockViewModel.getStock()
                .observe(getParentFragment().getViewLifecycleOwner(), s -> {

                    Stock stock = s.getContentIfNotHandle();

                    if (stock != null) {

                        if (option.equals(StockNameFragment.ADD)) {
                            callBack.onClickSave(stock);
                        } else {
                            callBack.onClickRename(stock);
                        }
                    }
                    alertDialog.cancel();
                });

        addAndRenameStockViewModel.getMessage()
                .observe(getParentFragment().getViewLifecycleOwner(), message -> {

                    String notify = message.getContentIfNotHandle();

                    if (notify != null) {
                        Toast.makeText(context, notify, Toast.LENGTH_SHORT).show();
                    }

                });

        return super.onCreateView(inflater, container, savedInstanceState);
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
