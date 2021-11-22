package com.example.myittaroostockinventorymanger.batch_fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.myittaroostockinventorymanger.MainActivity;
import com.example.myittaroostockinventorymanger.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.msa.dateedittext.DateEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewBatchActivity extends AppCompatActivity {

    @BindView(R.id.tool_bar)
    MaterialToolbar toolBar;
    @BindView(R.id.act_stock_name)
    AutoCompleteTextView actStockName;
    @BindView(R.id.edt_amount)
    EditText edtAmount;
    @BindView(R.id.edt_cost_price)
    EditText edtCostPrice;
    @BindView(R.id.edt_sale_price)
    EditText edtSalePrice;
    @BindView(R.id.edt_date)
    DateEditText edtDate;
    private AddNewBatchViewModel addNewBatchViewModel;
    private List<String> stockNameList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);
        ButterKnife.bind(this);

        setUpViewModel();

        //test

        edtDate.listen();

        addNewBatchViewModel.getAllStockNames()
                .observe(this, stockNameList -> {
                    setUpAutoCompleteTextView(stockNameList);
                });

        actStockName.setOnTouchListener((v, event) -> {
            actStockName.showDropDown();
            return false;
        });


        toolBar.setNavigationOnClickListener(v -> {
            goToMainActivity();
        });

        toolBar.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.save) {
                addNewBatchViewModel.onClickSave("12/31/2010");
            }

            return false;
        });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //AutoCompleteTextView for stock name
    private void setUpAutoCompleteTextView(List<String> stockNameList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.simple_drop_down_layout, R.id.txt_name, stockNameList);
        actStockName.setAdapter(adapter);
    }

    private void setUpViewModel() {
        addNewBatchViewModel = new ViewModelProvider(this)
                .get(AddNewBatchViewModel.class);
    }

}