package com.example.myittaroostockinventorymanger.stock_name_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.myittaroostockinventorymanger.R;
import com.example.myittaroostockinventorymanger.local.Stock;
import com.example.myittaroostockinventorymanger.util.VerticalSpaceItemDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockNameFragment extends Fragment implements AddNewStockDialogFragment.CallBack {

    @BindView(R.id.recy_stock_name)
    RecyclerView recyStockName;
    @BindView(R.id.fab_add_item)
    FloatingActionButton fabAddItem;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    private StockNameViewModel viewModel;
    private StockNameRecycleViewAdapter adapter;
    private AddNewStockDialogFragment dialogFragment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_name, container, false);
        ButterKnife.bind(this, view);

        setUpViewModel();
        setUpRecycleView();
        setUpDialogFragment();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getAllStockName()
                .observe(getViewLifecycleOwner(), stockList -> {
                    adapter.insertItem(stockList);
                });

        viewModel.getMessage()
                .observe(getViewLifecycleOwner(), message -> {

                    String notify = message.getContentIfNotHandle();

                    if (notify != null) {
                        Toast.makeText(getContext(), notify, Toast.LENGTH_SHORT).show();
                    }
                });

        viewModel.getLoading()
                .observe(getViewLifecycleOwner(), loading -> {
                    refresh.setRefreshing(loading);
                });

        refresh.setOnRefreshListener(() -> {
            viewModel.loadStockName();
        });

        fabAddItem.setOnClickListener(v -> {
            dialogFragment.show(getFragmentManager(), "");
        });

    }

    private void setUpViewModel() {
        viewModel = new ViewModelProvider(requireActivity())
                .get(StockNameViewModel.class);
    }

    private void setUpRecycleView() {
        adapter = new StockNameRecycleViewAdapter(new ArrayList<>());
        recyStockName.setAdapter(adapter);
        recyStockName.setLayoutManager(new LinearLayoutManager(getContext()));
        recyStockName.addItemDecoration(new VerticalSpaceItemDecoration(8));
    }

    private void setUpDialogFragment() {
        dialogFragment = AddNewStockDialogFragment.getNewInstance();
        dialogFragment.setCallBack(this);
    }

    @Override
    public void onClickSave(Stock stock) {
        viewModel.insertStock(stock);
    }
}
