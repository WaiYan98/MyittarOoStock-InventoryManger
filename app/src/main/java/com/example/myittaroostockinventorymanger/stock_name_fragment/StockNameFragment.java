package com.example.myittaroostockinventorymanger.stock_name_fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.myittaroostockinventorymanger.R;
import com.example.myittaroostockinventorymanger.local.Stock;
import com.example.myittaroostockinventorymanger.util.VerticalSpaceItemDecoration;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockNameFragment extends Fragment implements
        StockNameRecycleViewAdapter.CallBack {

    @BindView(R.id.recy_stock_name)
    RecyclerView recyStockName;
    @BindView(R.id.fab_add_item)
    FloatingActionButton fabAddItem;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    SearchView searchView;
    MaterialToolbar toolbar;
    private StockNameViewModel viewModel;
    private StockNameRecycleViewAdapter adapter;
    private AddAndRenameStockDialogFragment addAndRenameStockDialogFragment;
    private PopupMenu popupMenu;
    private List<Stock> stockList = new ArrayList<>();

    public static final String ADD = "ADD";
    public static final String RENAME = "RENAME";
    public static final String EXTRA_OPTION = "EXTRA_OPTION";
    public static final String EXTRA_STOCK = "EXTRA_STOCK";
    public static final String EXTRA_DELETE = "EXTRA_DELETE";

    private Stock stock;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_name, container, false);
        ButterKnife.bind(this, view);
        toolbar = getActivity().findViewById(R.id.tool_bar);

        //Bind SearchView form toolBar
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.app_bar_search);

        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type Stock Name");

        changeToolBarName();
        setUpViewModel();
        setUpRecycleView();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String newText) {

                viewModel.filterName(stockList, newText);
                return false;
            }
        });

        viewModel.getAllStocks()
                .observe(getViewLifecycleOwner(), stockList -> {
                    this.stockList = stockList;
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
            viewModel.loadStocks();
        });

        //Search View Find Name list insert to recycleView adapter
        viewModel.getFilterNames()
                .observe(getViewLifecycleOwner(), stockList -> {
                    adapter.insertItem(stockList);
                });

        //to add new Stock
        fabAddItem.setOnClickListener(v -> {
            showAddAndRenameStockDialogFragment(ADD, null);
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

    private void showAddAndRenameStockDialogFragment(String option, Stock stockForRename) {
        addAndRenameStockDialogFragment = AddAndRenameStockDialogFragment.getNewInstance(EXTRA_OPTION, EXTRA_STOCK,
                option, stockForRename);
        addAndRenameStockDialogFragment.show(getChildFragmentManager(), "");
    }

    private void setUpConfirmDialogFragment(Stock stock) {
        ConfirmDialogFragment confirmDialogFragment = ConfirmDialogFragment.getNewInstance(EXTRA_DELETE, stock);
        confirmDialogFragment.show(getChildFragmentManager(), "");
    }

    //recycle view onLongClick stock
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
                    showAddAndRenameStockDialogFragment(RENAME, stock);
                    break;
                case R.id.delete:
                    setUpConfirmDialogFragment(stock);
                    break;
            }
            return false;
        });
    }

    private void changeToolBarName() {
        toolbar.setTitle("Stock Name List");
    }

}
