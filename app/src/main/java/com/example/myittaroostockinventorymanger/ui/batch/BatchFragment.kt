package com.example.myittaroostockinventorymanger.ui.batch

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myittaroostockinventorymanger.ui.ConfirmDialog
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.databinding.FragmentBatchBinding
import com.example.myittaroostockinventorymanger.data.entities.ItemBatch
import com.example.myittaroostockinventorymanger.util.ListCreator
import com.example.myittaroostockinventorymanger.util.VerticalSpaceItemDecoration

class BatchFragment : Fragment(),
    BatchListRecycleViewAdapter.CallBack, ConfirmDialog.CallBack, MenuProvider {

    private var batchId: Long = 0
    private lateinit var selectedBatchIdList: MutableList<Long>
    private lateinit var searchView: SearchView

    lateinit var adapter: BatchListRecycleViewAdapter

    lateinit var toolbar: Toolbar;

    private val batchViewModel: BatchViewModel by lazy { setUpViewModel() }
    private lateinit var itemBatchList: List<ItemBatch>

    private lateinit var actionMode: ActionMode
    private lateinit var confirmDialog: ConfirmDialog

    private lateinit var binding: FragmentBatchBinding

    companion object {
        val EXTRA_STOCK_BATCH = "EXTRA_STOCK_BATCH"
        val EXTRA_OPTION = "EXTRA_OPTION"
        val EXTRA_BATCH_ID = "EXTRA_BATCH_ID"
        val UPDATE = "UPDATE"
        val ADD_NEW = "ADD_NEW"
    }

    private val EXTRA_DELETE: String = "EXTRA_DELETE"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBatchBinding.inflate(inflater, container, false)

//        val menuItem = toolbar.menu.findItem(R.id.menu_search_view)
//        searchView = menuItem.actionView as SearchView

        setUpRecycleView()

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//
//                batchViewModel.searchBatchByName(itemBatchList, newText)
//
//                return false
//            }
//        })

//        val batch1 = Batch(1, 200.0, 300.0, 10, Date())
//        val batch2 = Batch(2, 200.0, 300.0, 10, Date())
//        val batch3 = Batch(4, 200.0, 300.0, 10, Date())
//        batchViewModel.insertBatch(batch1)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//
//            }
//        batchViewModel.insertBatch(batch2)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//
//            }
//        batchViewModel.insertBatch(batch3)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//
//            }

        binding.fabAddBatch.setOnClickListener {
            findNavController().navigate(BatchFragmentDirections.actionBatchFragmentToAddAndUpdateBatchFragment())
        }

        batchViewModel.getAllBatchWithItem()
            ?.observe(viewLifecycleOwner) {
                adapter.insertItem(it)
            }

//        batchViewModel.getSearchResult()
//            .observe(viewLifecycleOwner) {
//                adapter.insertItem(it)
//            }

        binding.swipeRefreshLoading.setOnRefreshListener {
            batchViewModel.loadStockWithBatches()
        }

        batchViewModel.isLoading()
            .observe(viewLifecycleOwner) {
                binding.swipeRefreshLoading.isRefreshing = it
            }

        batchViewModel.getMessage()
            .observe(viewLifecycleOwner) {

                val message = it?.contentIfNotHandle

                if (message != null) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }

        batchViewModel.getContextualActionBarTitle()
            .observe(viewLifecycleOwner) {
                actionMode.title = it
            }

        //manage rename button hide or show
        batchViewModel.isShowRenameButton()
            .observe(viewLifecycleOwner) {
                actionMode.menu.findItem(R.id.edit).isVisible = it
            }
    }

    private fun setUpRecycleView() {
        adapter =
            BatchListRecycleViewAdapter()
        adapter.setCallBack(this)
        binding.recyBatchList.adapter = adapter
        binding.recyBatchList.layoutManager = LinearLayoutManager(context)
        binding.recyBatchList.addItemDecoration(VerticalSpaceItemDecoration(8))
    }

    private fun goToAddNewAndUpdateBatchActivity(
        option: String,
        itemBatch: ItemBatch?,
        batchId: Long
    ) {
        val intent: Intent = Intent(context, AddAndUpdateBatchFragment::class.java)
        intent.putExtra(EXTRA_OPTION, option)
        intent.putExtra(EXTRA_STOCK_BATCH, itemBatch)
        intent.putExtra(EXTRA_BATCH_ID, batchId)
        startActivity(intent)
    }

    private fun setUpViewModel() = ViewModelProvider(this).get(BatchViewModel::class.java)

    //get actionModeCallBack for Contextual action bar
    private fun getCallBack(): ActionMode.Callback {

        return object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode?.menuInflater?.inflate(R.menu.contextual_action_bar, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {

                setUpConfirmDialog()

                when (item?.itemId) {

                    R.id.delete -> confirmDialog.show(childFragmentManager, "")

                    R.id.edit -> findNavController()
                        .navigate(
                            BatchFragmentDirections.actionBatchFragmentToAddAndUpdateBatchFragment(
                                batchId
                            )
                        )

                    R.id.select_all -> adapter.selectAllBatch()

                }

                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                adapter.contextualActionBarClose()
                Log.d("tag", "onDestroyActionMode: ")
            }
        }
    }

    //When onLongClick to start action mode
    override fun onLongClicked() {
        actionMode = activity?.startActionMode(getCallBack())!!
    }

    //to select batch list item
    override fun onItemsSelected(selectedBatchIdList: MutableList<Long>) {
        setUpContextualBarSelection(selectedBatchIdList)
    }

    override fun onSelectedItemIsOne(batchId: Long) {
        this.batchId = batchId
    }

    private fun setUpConfirmDialog() {
        confirmDialog = ConfirmDialog.getNewInstance(EXTRA_DELETE, selectedBatchIdList.size)
        confirmDialog.setCallBack(this)
        confirmDialog.setKey(EXTRA_DELETE)
    }

    //This callBack is to delete selectedBatches
    override fun onClickedYes() {
        batchViewModel.deleteBatches(selectedBatchIdList, actionMode)
    }

    /**
     *     to set contextual bar title and edit button visible or invisible
     *     and If no selected item action mod close
     */
    private fun setUpContextualBarSelection(selectedBatchIdList: MutableList<Long>) {
        this.selectedBatchIdList = selectedBatchIdList
        val num = selectedBatchIdList.size
        batchViewModel.setContextualActionBarTitle(num)
        batchViewModel.showRenameButton(num)

        if (selectedBatchIdList.isEmpty()) {
            actionMode.finish()
        }
    }

    //create menu with menuProvider
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_app_bar_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

        when (menuItem.itemId) {

            R.id.menu_search_view -> {
                val searchView = menuItem.actionView as SearchView
                searchView.setOnQueryTextListener(object : OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        val queryText = "%$newText%"
                        batchViewModel.searchFromDb(queryText)
                            .observe(viewLifecycleOwner) {
                                adapter.insertItem(it)
                            }
                        return false
                    }

                })

            }
        }
        return false
    }
}