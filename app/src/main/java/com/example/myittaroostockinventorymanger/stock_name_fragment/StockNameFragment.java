package com.example.myittaroostockinventorymanger.stock_name_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myittaroostockinventorymanger.R;
import com.example.myittaroostockinventorymanger.adapters.StockListRecycleViewAdapter;
import com.example.myittaroostockinventorymanger.util.VerticalSpaceItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockNameFragment extends Fragment {

    @BindView(R.id.recy_stock_name)
    RecyclerView recyStockName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_name, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        StockNameRecycleViewAdapter adapter = new StockNameRecycleViewAdapter();
        recyStockName.setAdapter(adapter);
        recyStockName.setLayoutManager(new LinearLayoutManager(getContext()));
        recyStockName.addItemDecoration(new VerticalSpaceItemDecoration(8));

    }
}
