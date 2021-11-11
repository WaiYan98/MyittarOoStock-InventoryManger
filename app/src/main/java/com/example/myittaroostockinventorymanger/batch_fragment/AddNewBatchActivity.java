package com.example.myittaroostockinventorymanger.batch_fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;

import com.example.myittaroostockinventorymanger.MainActivity;
import com.example.myittaroostockinventorymanger.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.internal.ToolbarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewBatchActivity extends AppCompatActivity {

    @BindView(R.id.tool_bar)
    MaterialToolbar toolBar;
    @BindView(R.id.edt_stock_name)
    EditText edtStockName;
    @BindView(R.id.edt_amount)
    EditText edtAmount;
    @BindView(R.id.edt_cost_price)
    EditText edtCostPrice;
    @BindView(R.id.edt_sale_price)
    EditText edtSalePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);
        ButterKnife.bind(this);

        toolBar.setNavigationOnClickListener(v -> {
            goToMainActivity();
        });

    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}