package com.example.myittaroostockinventorymanger.stock_name_fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myittaroostockinventorymanger.R;
import com.example.myittaroostockinventorymanger.local.Stock;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfirmDialogFragment extends DialogFragment {

    @BindView(R.id.btn_yes)
    Button btnYes;
    @BindView(R.id.btn_no)
    Button btnNo;

    private AlertDialog alertDialog;
    private Context context;
    private Stock stock;

    private ConfirmDialogFragment() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.confirm_dialog_fragment, null, false);
        ButterKnife.bind(this, view);

        alertDialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();

        alertDialog.getWindow()
                .setBackgroundDrawableResource(R.drawable.rounded_rectangle_white);

        Bundle bundle = getArguments();

        if (bundle != null) {
            stock = bundle.getParcelable(StockNameFragment.EXTRA_DELETE);
        }

        return alertDialog;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ConfirmDialogFragmentViewModel confirmDialogFragmentViewModel = new ViewModelProvider(requireActivity())
                .get(ConfirmDialogFragmentViewModel.class);

        btnYes.setOnClickListener(v -> {
            confirmDialogFragmentViewModel.deleteStock(stock);
            alertDialog.cancel();
        });

        btnNo.setOnClickListener(v -> {
            alertDialog.cancel();
        });

        confirmDialogFragmentViewModel.getMessage()
                .observe(getParentFragment().getViewLifecycleOwner(), m -> {

                    String message = m.getContentIfNotHandle();

                    if (message != null) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static ConfirmDialogFragment getNewInstance(String key, Stock stock) {
        ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(key, stock);
        confirmDialogFragment.setArguments(bundle);
        return confirmDialogFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
