package com.example.myittaroostockinventorymanger.batch_fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.example.myittaroostockinventorymanger.MainActivity;
import com.example.myittaroostockinventorymanger.R;
import com.example.myittaroostockinventorymanger.local.Stock;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.internal.ToolbarUtils;

import java.util.ArrayList;
import java.util.Arrays;
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
    private AddNewBatchViewModel addNewBatchViewModel;
    private List<String> stockNameList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);
        ButterKnife.bind(this);

        addNewBatchViewModel = new ViewModelProvider(this)
                .get(AddNewBatchViewModel.class);

        addNewBatchViewModel.getAllStockNames()
                .observe(this, stockNameList -> {
//                    this.stockNameList = stockNameList;
                    setUpAutoCompleteTextView(stockNameList);
                });


        toolBar.setNavigationOnClickListener(v -> {
            goToMainActivity();
        });




    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setUpAutoCompleteTextView(List<String> stockNameList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, stockNameList);
        actStockName.setAdapter(adapter);
    }


}