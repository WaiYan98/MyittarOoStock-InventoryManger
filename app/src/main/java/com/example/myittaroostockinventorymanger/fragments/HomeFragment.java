package com.example.myittaroostockinventorymanger.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myittaroostockinventorymanger.R;
import com.example.myittaroostockinventorymanger.adapters.StockListRecycleViewAdapter;
import com.example.myittaroostockinventorymanger.util.VerticalSpaceItemDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {

    @BindView(R.id.recy_stock_list)
    RecyclerView recyStockList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        StockListRecycleViewAdapter stockListAdapter = new StockListRecycleViewAdapter(getContext());
        recyStockList.setAdapter(stockListAdapter);
        recyStockList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyStockList.addItemDecoration(new VerticalSpaceItemDecoration(24));
    }
}
