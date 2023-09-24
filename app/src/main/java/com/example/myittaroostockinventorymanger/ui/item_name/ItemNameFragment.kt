package com.example.myittaroostockinventorymanger.ui.item_name

import android.os.Bundle
import android.view.ActionMode
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.data.entities.Item
import com.example.myittaroostockinventorymanger.databinding.FragmentItemNameBinding
import com.example.myittaroostockinventorymanger.event.Event
import com.example.myittaroostockinventorymanger.ui.ConfirmDialog
import com.example.myittaroostockinventorymanger.ui.ConfirmDialog.Companion.getNewInstance
import com.example.myittaroostockinventorymanger.util.VerticalSpaceItemDecoration

class ItemNameFragment : Fragment(), ItemNameRecycleViewAdapter.CallBack, ConfirmDialog.CallBack,
    MenuProvider {
    private lateinit var toolbar: Toolbar
    private lateinit var searchView: SearchView
    private lateinit var adapter: ItemNameRecycleViewAdapter
    private lateinit var addAndUpdateItemDialogFragment: AddAndUpdateItemDialogFragment
    private lateinit var popupMenu: PopupMenu
    private var itemList: List<Item> = ArrayList()
    private lateinit var item: Item
    private var selectItemIdList: List<Long> = ArrayList()
    private lateinit var actionMode: ActionMode
    private lateinit var binding: FragmentItemNameBinding

    private val viewModel: ItemNameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemNameBinding.inflate(inflater, container, false)
        toolbar = requireActivity().findViewById(R.id.tool_bar_main)

        setUpRecycleView()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                return false
//            }
//
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            override fun onQueryTextChange(newText: String): Boolean {
//                viewModel.filterName(itemList, newText)
//                return false
//            }
//        })

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)



        viewModel.getAllItems()
            .observe(viewLifecycleOwner)
            { itemList: List<Item> ->
                adapter.insertItem(itemList)
            }

        viewModel.getSearchItems()
            .observe(viewLifecycleOwner) {
                adapter.insertItem(it)
            }

        viewModel.getMessage()
            .observe(viewLifecycleOwner)
            { message: Event<String?> ->
                val notify = message.contentIfNotHandle
                if (notify != null) {
                    Toast.makeText(context, notify, Toast.LENGTH_SHORT).show()
                }
            }

        viewModel.getLoading()
            .observe(viewLifecycleOwner)
            { loading: Boolean? ->
                binding.refresh.isRefreshing = loading!!
            }

        binding.refresh.setOnRefreshListener { viewModel.loadItems() }

//        //Search View Find Name list insert to recycleView adapter
//        viewModel.filterNames()
//            .observe(viewLifecycleOwner)
//            { itemList: List<Item?>? ->
//                adapter.insertItem(
//                    itemList
//                )
//            }

        //to add new Stock
        binding.fabAddItem.setOnClickListener {
            val action =
                ItemNameFragmentDirections.actionItemNameFragmentToAddAndUpdateItemDialogFragment()
            findNavController().navigate(action)
//            showAddAndRenameStockDialogFragment(
//                ADD, null
//            )
        }

        viewModel.getContextualActionBarTitle()
            .observe(viewLifecycleOwner)
            { title: String? -> actionMode.title = title }

        viewModel.isShowRenameButton()
            .observe(viewLifecycleOwner)
            { isShow: Boolean? ->
                actionMode.menu.findItem(R.id.edit).isVisible = isShow!!
            }

        viewModel.getIsValidDelete()
            .observe(viewLifecycleOwner)
            { isValidDelete: Boolean ->
                if (isValidDelete) {
                    showConfirmDialog()
                }
            }
    }

    private fun setUpRecycleView() {
        adapter = ItemNameRecycleViewAdapter()
        adapter.setCallBack(this)
        binding.recyStockName.adapter = adapter
        binding.recyStockName.layoutManager = LinearLayoutManager(context)
        binding.recyStockName.addItemDecoration(VerticalSpaceItemDecoration(8))
    }

//    private fun showAddAndRenameStockDialogFragment(option: String, itemForRename: Item?) {
//        addAndUpdateItemDialogFragment = AddAndUpdateItemDialogFragment.getNewInstance(
//            EXTRA_OPTION, EXTRA_STOCK,
//            option, itemForRename
//        )
//        addAndUpdateItemDialogFragment.show(childFragmentManager, "")
//    }

    //show popup menu for rename and delete
    private fun createPopupMenu(v: View) {
        val contextThemeWrapper = ContextThemeWrapper(context, R.style.PopupMenuOverlapAnchor)
        popupMenu = PopupMenu(contextThemeWrapper, v, Gravity.END)
        popupMenu.inflate(R.menu.contexual_menu)
        popupMenu.show()
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

    private val callBack: ActionMode.Callback
        get() = object : ActionMode.Callback {
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
////
////                }

                when (item.itemId) {

                    R.id.select_all -> adapter.onClickSelectAll()


                    R.id.edit ->
                        findNavController()
                            .navigate(
                                ItemNameFragmentDirections.actionItemNameFragmentToAddAndUpdateItemDialogFragment(
                                    this@ItemNameFragment.item.itemId
                                )
                            )

                    R.id.delete -> findNavController()
                        .navigate(
                            ItemNameFragmentDirections.actionItemNameFragmentToConfirmDialogFragment(
                                selectItemIdList.toLongArray()
                            )
                        )
                }
                return true
            }

            override fun onDestroyActionMode(mode: ActionMode) {
                adapter.contextualActionBarClose()
            }
        }

    private fun showConfirmDialog() {
        val confirmDialog = getNewInstance(EXTRA_NUM_OF_STOCK, selectItemIdList.size)
        confirmDialog.setCallBack(this)
        confirmDialog.setKey(EXTRA_NUM_OF_STOCK)
        confirmDialog.show(childFragmentManager, "")
    }

    override fun onLongClickItem() {
        actionMode = requireActivity().startActionMode(callBack)!!
    }

    override fun onClickItem(selectedItemIdList: List<Long>) {
        setUpContextualBar(selectedItemIdList)

    }

    override fun onSelectedItemIsOne(item: Item) {
        this.item = item
    }

    private fun setUpContextualBar(selectedStockIdList: List<Long>) {
        selectItemIdList = selectedStockIdList
        val num = selectedStockIdList.size
        viewModel.setContextualActionBarTitle(num)
        viewModel.showAndHideRenameButton(num)
        if (selectedStockIdList.isEmpty()) {
            actionMode.finish()
        }
    }

    override fun onClickedYes() {
        viewModel.deleteStocks(selectItemIdList, actionMode)
    }


    companion object {
        const val ADD = "ADD"
        const val RENAME = "RENAME"
        const val EXTRA_OPTION = "EXTRA_OPTION"
        const val EXTRA_STOCK = "EXTRA_STOCK"
        const val EXTRA_DELETE = "EXTRA_DELETE"
        const val EXTRA_NUM_OF_STOCK = "EXTRA_NUM_OF_STOCK"
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_app_bar_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

        return when (menuItem.itemId) {

            R.id.menu_search_view -> {

                val searchView = menuItem.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        val queryText = "%$newText%"
                        viewModel.searchItemsFromDb(queryText)
                            .observe(viewLifecycleOwner) {
                                adapter.insertItem(it)
                            }
                        return false
                    }

                })

                true
            }

            else -> false
        }

    }

}