package com.example.myittaroostockinventorymanger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.internal.ToolbarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewItemActivity extends AppCompatActivity {

    @BindView(R.id.tool_bar)
    MaterialToolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);
        ButterKnife.bind(this);

    }


}