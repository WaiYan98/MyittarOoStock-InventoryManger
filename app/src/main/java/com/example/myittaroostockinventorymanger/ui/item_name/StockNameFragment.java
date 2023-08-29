package com.example.myittaroostockinventorymanger.ui.item_name;

import android.os.Build;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.myittaroostockinventorymanger.R;
import com.example.myittaroostockinventorymanger.databinding.FragmentStockNameBinding;
import com.example.myittaroostockinventorymanger.ui.ConfirmDialog;
import com.example.myittaroostockinventorymanger.data.entities.Stock;
import com.example.myittaroostockinventorymanger.util.VerticalSpaceItemDecoration;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.ArrayList;
import java.util.List;

public class StockNameFragment extends Fragment implements
        StockNameRecycleViewAdapter.CallBack,
        ConfirmDialog.CallBack {

    MaterialToolbar toolbar;
    private SearchView searchView;
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
    public static final String EXTRA_NUM_OF_STOCK = "EXTRA_NUM_OF_STOCK";

    private Stock stock;
    private List<Long> selectStockIdList = new ArrayList<>();
    private ActionMode actionMode;

    private FragmentStockNameBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStockNameBinding.inflate(inflater, container, false);
        toolbar = getActivity().findViewById(R.id.tool_bar);


        //Bind SearchView form toolBar
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.app_bar_search);

        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type Stock Name");

        changeToolBarName();
        setUpViewModel();
        setUpRecycleView();

        return binding.getRoot();
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
                    binding.refresh.setRefreshing(loading);
                });

        binding.refresh.setOnRefreshListener(() -> {
            viewModel.loadStocks();
        });

        //Search View Find Name list insert to recycleView adapter
        viewModel.getFilterNames()
                .observe(getViewLifecycleOwner(), stockList -> {
                    adapter.insertItem(stockList);
                });

        //to add new Stock
        binding.fabAddItem.setOnClickListener(v -> {
            showAddAndRenameStockDialogFragment(ADD, null);
        });

        viewModel.getContextualActionBarTitle()
                .observe(getViewLifecycleOwner(), title -> {
                    actionMode.setTitle(title);
                });

        viewModel.isShowRenameButton()
                .observe(getViewLifecycleOwner(), isShow -> {
                    actionMode.getMenu().findItem(R.id.edit).setVisible(isShow);
                });

        viewModel.getIsValidDelete()
                .observe(getViewLifecycleOwner(), isValidDelete -> {
                    if (isValidDelete) {
                        showConfirmDialog();
                    }
                });

    }

    private void setUpViewModel() {
        viewModel = new ViewModelProvider(requireActivity())
                .get(StockNameViewModel.class);
    }

    private void setUpRecycleView() {
        adapter = new StockNameRecycleViewAdapter(getContext(), new ArrayList<>());
        adapter.setCallBack(this);
        binding.recyStockName.setAdapter(adapter);
        binding.recyStockName.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyStockName.addItemDecoration(new VerticalSpaceItemDecoration(8));
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


//show popup menu for rename and delete

    private void createPopupMenu(View v) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getContext(), R.style.PopupMenuOverlapAnchor);
        popupMenu = new PopupMenu(contextThemeWrapper, v, Gravity.END);
        popupMenu.inflate(R.menu.contexual_menu);
        popupMenu.show();
    }

    private void popupMenuItemClick() {

//        popupMenu.setOnMenuItemClickListener(item -> {
//
//            switch (item.getItemId()) {
//                case R.id.rename:
//                    // TODO: 2/27/2022 repair rename fun
//                    showAddAndRenameStockDialogFragment(RENAME, stock);
//                    break;
//                case R.id.delete:
//
//                    break;
//            }
//            return false;
//        });
    }

    private void changeToolBarName() {
        toolbar.setTitle("Stock Name List");
    }

    private ActionMode.Callback getCallBack() {

        return new ActionMode.Callback() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.contextual_action_bar, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

//                switch (item.getItemId()) {
//                    case R.id.select_all:
//                        adapter.onClickSelectAll();
//                        break;
//                    case R.id.edit:
//                        showAddAndRenameStockDialogFragment(RENAME, stock);
//                        break;
//                    case R.id.delete:
//                        viewModel.isValidDelete(selectStockIdList);
//                        break;
//
//                }

                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.contextualActionBarClose();
            }
        };
    }

    private void showConfirmDialog() {
        ConfirmDialog confirmDialog = ConfirmDialog.Companion.getNewInstance(EXTRA_NUM_OF_STOCK, selectStockIdList.size());
        confirmDialog.setCallBack(this);
        confirmDialog.setKey(EXTRA_NUM_OF_STOCK);
        confirmDialog.show(getChildFragmentManager(), "");
    }

    @Override
    public void onLongClickItem() {
        actionMode = getActivity().startActionMode(getCallBack());
    }

    @Override
    public void onClickItem(List<Long> selectedStockIdList) {
        setUpContextualBar(selectedStockIdList);
    }

    @Override
    public void onSelectedItemIsOne(Stock stock) {
        this.stock = stock;
    }

    private void setUpContextualBar(List<Long> selectedStockIdList) {
        this.selectStockIdList = selectedStockIdList;
        int num = selectedStockIdList.size();
        viewModel.setContextualActionBarTitle(num);
        viewModel.showAndHideRenameButton(num);

        if (selectedStockIdList.isEmpty()) {
            actionMode.finish();
        }
    }

    @Override
    public void onClickedYes() {
        viewModel.deleteStocks(selectStockIdList, actionMode);
    }
}
