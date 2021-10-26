package com.example.myittaroostockinventorymanger.stock_name_fragment;

import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
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

public class StockNameFragment extends Fragment implements AddAndRenameStockDialogFragment.CallBack,
        StockNameRecycleViewAdapter.CallBack, ConfirmDialogFragment.CallBack {

    @BindView(R.id.recy_stock_name)
    RecyclerView recyStockName;
    @BindView(R.id.fab_add_item)
    FloatingActionButton fabAddItem;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    private StockNameViewModel viewModel;
    private StockNameRecycleViewAdapter adapter;
    private AddAndRenameStockDialogFragment addAndRenameStockDialogFragment;
    private PopupMenu popupMenu;

    public static final String ADD = "ADD";
    public static final String EXTRA_KEY = "EXTRA_KEY";

    private Stock stock;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_name, container, false);
        ButterKnife.bind(this, view);

        setUpViewModel();
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
            showAddAndRenameStockDialogFragment(ADD);
        });
    }

    private void setUpViewModel() {
        viewModel = new ViewModelProvider(requireActivity())
                .get(StockNameViewModel.class);
    }

    private void setUpRecycleView() {
        adapter = new StockNameRecycleViewAdapter(new ArrayList<>());
        adapter.setCallBack(this);
        recyStockName.setAdapter(adapter);
        recyStockName.setLayoutManager(new LinearLayoutManager(getContext()));
        recyStockName.addItemDecoration(new VerticalSpaceItemDecoration(8));
    }

    private void showAddAndRenameStockDialogFragment(String option) {
        addAndRenameStockDialogFragment = AddAndRenameStockDialogFragment.getNewInstance(EXTRA_KEY, option);
        addAndRenameStockDialogFragment.show(getChildFragmentManager(), "");
        addAndRenameStockDialogFragment.setCallBack(this);
    }

    private void setUpConfirmDialogFragment() {
        ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment();
        confirmDialogFragment.show(getChildFragmentManager(), "");
        confirmDialogFragment.setCallBack(this);
    }

    @Override
    public void onClickSave(Stock stock) {
        viewModel.insertStock(stock);
    }

    @Override
    public void onClickRename(Stock stock) {
        //for test
        stock.setStockId(this.stock.getStockId());
        viewModel.updateStockName(stock);
    }

    @Override
    public void onLongClickItem(View v, Stock stock) {
        //for test
        this.stock = stock;
        createPopupMenu(v);
        popupMenuItemClick();
    }

//show popup menu for rename and delete

    private void createPopupMenu(View v) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getContext(), R.style.PopupMenuOverlapAnchor);
        popupMenu = new PopupMenu(contextThemeWrapper, v, Gravity.END);
        popupMenu.inflate(R.menu.contexual_menu);
        popupMenu.show();
    }

    private void popupMenuItemClick() {

        popupMenu.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {
                case R.id.rename:
                    showAddAndRenameStockDialogFragment("");
                    break;
                case R.id.delete:
                    setUpConfirmDialogFragment();
                    break;
            }
            return false;
        });
    }


    @Override
    public void onClickYes() {
        viewModel.deleteStock(this.stock);
    }
}
