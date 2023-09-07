package com.example.myittaroostockinventorymanger.ui.item_name

import android.os.Build
import android.os.Bundle
import android.view.ActionMode
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.data.entities.Stock
import com.example.myittaroostockinventorymanger.databinding.FragmentItemNameBinding
import com.example.myittaroostockinventorymanger.event.Event
import com.example.myittaroostockinventorymanger.ui.ConfirmDialog
import com.example.myittaroostockinventorymanger.ui.ConfirmDialog.Companion.getNewInstance
import com.example.myittaroostockinventorymanger.util.VerticalSpaceItemDecoration
import com.google.android.material.appbar.MaterialToolbar

class ItemNameFragment : Fragment(), ItemNameRecycleViewAdapter.CallBack, ConfirmDialog.CallBack {
    private lateinit var toolbar: MaterialToolbar
    private lateinit var searchView: SearchView
    private lateinit var viewModel: ItemNameViewModel
    private lateinit var adapter: ItemNameRecycleViewAdapter
    private lateinit var addAndUpdateItemDialogFragment: AddAndUpdateItemDialogFragment
    private lateinit var popupMenu: PopupMenu
    private var stockList: List<Stock> = ArrayList()
    private lateinit var stock: Stock
    private var selectStockIdList: List<Long> = ArrayList()
    private lateinit var actionMode: ActionMode
    private lateinit var binding: FragmentItemNameBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemNameBinding.inflate(inflater, container, false)
        toolbar = requireActivity().findViewById(R.id.tool_bar)


        //Bind SearchView form toolBar
        val menuItem = toolbar.menu.findItem(R.id.app_bar_search)
        searchView = menuItem.actionView as SearchView
        searchView.queryHint = "Type Stock Name"

        changeToolBarName()
        setUpViewModel()
        setUpRecycleView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.filterName(stockList, newText)
                return false
            }
        })
        viewModel.getAllItems()
            .observe(viewLifecycleOwner) { stockList: List<Stock> ->
                this.stockList = stockList
                adapter.insertItem(stockList)
            }
        viewModel.getMessage()
            .observe(viewLifecycleOwner) { message: Event<String?> ->
                val notify = message.contentIfNotHandle
                if (notify != null) {
                    Toast.makeText(context, notify, Toast.LENGTH_SHORT).show()
                }
            }
        viewModel.getLoading()
            .observe(viewLifecycleOwner) { loading: Boolean? ->
                binding.refresh.isRefreshing = loading!!
            }
        binding.refresh.setOnRefreshListener { viewModel.loadItems() }

        //Search View Find Name list insert to recycleView adapter
        viewModel.filterNames()
            .observe(viewLifecycleOwner) { stockList: List<Stock?>? ->
                adapter.insertItem(
                    stockList
                )
            }

        //to add new Stock
        binding.fabAddItem.setOnClickListener {
            showAddAndRenameStockDialogFragment(
                ADD, null
            )
        }
        viewModel.getContextualActionBarTitle()
            .observe(viewLifecycleOwner) { title: String? -> actionMode!!.title = title }
        viewModel.isShowRenameButton()
            .observe(viewLifecycleOwner) { isShow: Boolean? ->
                actionMode!!.menu.findItem(R.id.edit).isVisible = isShow!!
            }
        viewModel.getIsValidDelete()
            .observe(viewLifecycleOwner) { isValidDelete: Boolean ->
                if (isValidDelete) {
                    showConfirmDialog()
                }
            }
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(requireActivity())
            .get(ItemNameViewModel::class.java)
    }

    private fun setUpRecycleView() {
        adapter = ItemNameRecycleViewAdapter(context, ArrayList())
        adapter.setCallBack(this)
        binding.recyStockName.adapter = adapter
        binding.recyStockName.layoutManager = LinearLayoutManager(context)
        binding.recyStockName.addItemDecoration(VerticalSpaceItemDecoration(8))
    }

    private fun showAddAndRenameStockDialogFragment(option: String, stockForRename: Stock?) {
        addAndUpdateItemDialogFragment = AddAndUpdateItemDialogFragment.getNewInstance(
            EXTRA_OPTION, EXTRA_STOCK,
            option, stockForRename
        )
        addAndUpdateItemDialogFragment.show(childFragmentManager, "")
    }

    private fun setUpConfirmDialogFragment(stock: Stock) {
        val confirmDialogFragment = ConfirmDialogFragment.getNewInstance(EXTRA_DELETE, stock)
        confirmDialogFragment.show(childFragmentManager, "")
    }

    //show popup menu for rename and delete
    private fun createPopupMenu(v: View) {
        val contextThemeWrapper = ContextThemeWrapper(context, R.style.PopupMenuOverlapAnchor)
        popupMenu = PopupMenu(contextThemeWrapper, v, Gravity.END)
        popupMenu!!.inflate(R.menu.contexual_menu)
        popupMenu!!.show()
    }

    private fun popupMenuItemClick() {

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

    private fun changeToolBarName() {
        toolbar!!.title = "Stock Name List"
    }

    private val callBack: ActionMode.Callback
        private get() = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                mode.menuInflater.inflate(R.menu.contextual_action_bar, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {

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
                return true
            }

            override fun onDestroyActionMode(mode: ActionMode) {
                adapter.contextualActionBarClose()
            }
        }

    private fun showConfirmDialog() {
        val confirmDialog = getNewInstance(EXTRA_NUM_OF_STOCK, selectStockIdList.size)
        confirmDialog.setCallBack(this)
        confirmDialog.setKey(EXTRA_NUM_OF_STOCK)
        confirmDialog.show(childFragmentManager, "")
    }

    override fun onLongClickItem() {
        actionMode = requireActivity().startActionMode(callBack)!!
    }

    override fun onClickItem(selectedStockIdList: List<Long>) {
        setUpContextualBar(selectedStockIdList)
    }

    override fun onSelectedItemIsOne(stock: Stock) {
        this.stock = stock
    }

    private fun setUpContextualBar(selectedStockIdList: List<Long>) {
        selectStockIdList = selectedStockIdList
        val num = selectedStockIdList.size
        viewModel.setContextualActionBarTitle(num)
        viewModel.showAndHideRenameButton(num)
        if (selectedStockIdList.isEmpty()) {
            actionMode!!.finish()
        }
    }

    override fun onClickedYes() {
        viewModel.deleteStocks(selectStockIdList, actionMode)
    }

    companion object {
        const val ADD = "ADD"
        const val RENAME = "RENAME"
        const val EXTRA_OPTION = "EXTRA_OPTION"
        const val EXTRA_STOCK = "EXTRA_STOCK"
        const val EXTRA_DELETE = "EXTRA_DELETE"
        const val EXTRA_NUM_OF_STOCK = "EXTRA_NUM_OF_STOCK"
    }
}