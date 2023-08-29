package com.example.myittaroostockinventorymanger.ui.batch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myittaroostockinventorymanger.ui.MainActivity;
import com.example.myittaroostockinventorymanger.R;
import com.example.myittaroostockinventorymanger.data.entities.StockBatch;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AddNewAndUpdateBatchActivity extends AppCompatActivity {

    MaterialToolbar toolBar;
    AutoCompleteTextView actStockName;
    EditText edtAmount;
    EditText edtCostPrice;
    EditText edtSalePrice;
    TextInputLayout txtInputLayout;

    private AddNewAndUpdateBatchViewModel addNewAndUpdateBatchViewModel;
    private List<String> stockNameList = new ArrayList<String>();

    private String option;
    private StockBatch stockBatch;
    private Long batchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        toolBar = findViewById(R.id.tool_bar);
        actStockName = findViewById(R.id.act_stock_name);
        edtAmount = findViewById(R.id.edt_amount);
        edtCostPrice = findViewById(R.id.edt_cost_price);
        edtSalePrice = findViewById(R.id.edt_sale_price);
        txtInputLayout = findViewById(R.id.txt_input_layout);

        setUpViewModel();

        //test
//        edtDate.listen();


//stockBatch batchId is always 0 so I request batchId from batchFragment for testing
        // TODO: 2/5/2022 need to check later
        Intent intent = getIntent();

        if (intent != null) {
            option = intent.getStringExtra(BatchFragment.Companion.getEXTRA_OPTION());
            stockBatch = intent.getParcelableExtra(BatchFragment.Companion.getEXTRA_STOCK_BATCH());
            batchId = intent.getLongExtra(BatchFragment.Companion.getEXTRA_BATCH_ID(), 0);
        }

        if (option.equals(BatchFragment.Companion.getUPDATE())) {

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
            String expDate = dateFormat.format(stockBatch.getBatch().getExpDate());

            actStockName.setText(stockBatch.getStock().getName());
//            edtDate.setText(expDate);
            edtAmount.setText(String.valueOf(stockBatch.getBatch().getTotalStock()));
            edtCostPrice.setText(String.valueOf(stockBatch.getBatch().getOriginalPrice()));
            edtSalePrice.setText(String.valueOf(stockBatch.getBatch().getSalePrice()));
        }

        addNewAndUpdateBatchViewModel.getAllStockNames()
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

                // TODO: 8/25/2023   handle edtExpDate
                addNewAndUpdateBatchViewModel.onClickSave(option, batchId,
                        actStockName, null
                        , edtAmount, edtCostPrice
                        , edtSalePrice, true);
            }

            return false;
        });

        addNewAndUpdateBatchViewModel.getMessage()
                .observe(this, mEvent -> {
                    String notify = mEvent.getContentIfNotHandle();

                    if (notify != null) {
                        Toast.makeText(this, notify, Toast.LENGTH_SHORT).show();
                    }
                });

        addNewAndUpdateBatchViewModel.getNavigateToMainActivity()
                .observe(this, navigateEvent -> {

                    Boolean navigate = navigateEvent.getContentIfNotHandle();

                    if (navigate != null) {
                        goToMainActivity();
                    }
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
        addNewAndUpdateBatchViewModel = new ViewModelProvider(this)
                .get(AddNewAndUpdateBatchViewModel.class);
    }

}