package com.example.myittaroostockinventorymanger.stock_name_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myittaroostockinventorymanger.R;
import com.example.myittaroostockinventorymanger.adapters.StockListRecycleViewAdapter;
import com.example.myittaroostockinventorymanger.repository.Repository;
import com.example.myittaroostockinventorymanger.util.VerticalSpaceItemDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockNameFragment extends Fragment {

    @BindView(R.id.recy_stock_name)
    RecyclerView recyStockName;
    @BindView(R.id.fab_add_item)
    FloatingActionButton fabAddItem;
    private StockNameViewModel viewModel;
    private StockNameRecycleViewAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_name, container, false);
        ButterKnife.bind(this, view);
        viewModel = new ViewModelProvider(requireActivity())
                .get(StockNameViewModel.class);

        setUpRecycleView();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getAllStockName()
                .observe(getViewLifecycleOwner(), stockList -> {
                    adapter.insertItem(stockList);
                });

        fabAddItem.setOnClickListener(v -> {

        });

    }

    public void setUpRecycleView() {
        adapter = new StockNameRecycleViewAdapter(new ArrayList<>());
        recyStockName.setAdapter(adapter);
        recyStockName.setLayoutManager(new LinearLayoutManager(getContext()));
        recyStockName.addItemDecoration(new VerticalSpaceItemDecoration(8));
    }
}
