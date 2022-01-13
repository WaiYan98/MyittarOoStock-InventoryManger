package com.example.myittaroostockinventorymanger.batch_fragment

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.get
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.example.myittaroostockinventorymanger.dialog.ConfirmDialog
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.pojo.StockBatch
import com.example.myittaroostockinventorymanger.util.ListCreator
import com.example.myittaroostockinventorymanger.util.VerticalSpaceItemDecoration
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BatchFragment : Fragment(), BatchListRecycleViewAdapter.CallBack, ConfirmDialog.CallBack {

    private lateinit var selectedBatchIdList: MutableList<Long>
    private lateinit var searchView: SearchView

    @BindView(R.id.swipe_refresh_loading)
    lateinit var swipeRefreshLoading: SwipeRefreshLayout

    @BindView(R.id.recy_batch_list)
    lateinit var recyBatchList: RecyclerView

    @BindView(R.id.fab_add_batch)
    lateinit var fabAddBatch: FloatingActionButton;

    lateinit var adapter: BatchListRecycleViewAdapter

    lateinit var toolbar: MaterialToolbar;

    private val batchViewModel: BatchViewModel by lazy { setUpViewModel() }
    private lateinit var stockBatchList: List<StockBatch>

    private lateinit var actionMode: ActionMode
    private lateinit var confirmDialog: ConfirmDialog

    private val EXTRA_DELETE: String = "EXTRA_DELETE"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_batch, container, false);
        ButterKnife.bind(this, view)

        toolbar = activity?.findViewById(R.id.tool_bar)!!
        val menuItem = toolbar.menu.findItem(R.id.app_bar_search)
        searchView = menuItem.actionView as SearchView

        setToolbarTitle()
        setUpRecycleView()

        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {

                batchViewModel.searchBatchByName(stockBatchList, newText)

                return false
            }
        })

        fabAddBatch.setOnClickListener {

            goToAddNewBatchActivity()
        }

        batchViewModel.getAllStockWithBatches()
                ?.observe(viewLifecycleOwner) {

                    //covert nested list into one list
                    stockBatchList = ListCreator.createStockBatchList(it)

                    adapter.insertItem(stockBatchList)
                }

        batchViewModel.getSearchResult()
                .observe(viewLifecycleOwner) {
                    adapter.insertItem(it)
                }

        swipeRefreshLoading.setOnRefreshListener {
            batchViewModel.loadStockWithBatches()
        }

        batchViewModel.isLoading()
                .observe(viewLifecycleOwner) {
                    swipeRefreshLoading.isRefreshing = it
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

        batchViewModel.isShowRenameButton()
                .observe(viewLifecycleOwner) {
                    actionMode.menu.findItem(R.id.edit).isVisible = it
                }
    }

    private fun setUpRecycleView() {
        adapter = BatchListRecycleViewAdapter(context, arrayListOf())
        adapter.setCallBack(this)
        recyBatchList.adapter = adapter
        recyBatchList.layoutManager = LinearLayoutManager(context)
        recyBatchList.addItemDecoration(VerticalSpaceItemDecoration(8))
    }

    private fun goToAddNewBatchActivity() {
        val intent: Intent = Intent(context, AddNewBatchActivity::class.java)
        startActivity(intent)
    }

    //to change toolbar title
    private fun setToolbarTitle() {
        toolbar.title = "Batch List"
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

                    R.id.edit -> Log.d("tag", "onActionItemClicked: ")

                    R.id.select_all -> adapter.selectAllItems()

                }

                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                adapter.contextualActionBarClose()
                Log.d("tag", "onDestroyActionMode: ")
            }
        }
    }

    override fun onLongClicked() {
        actionMode = activity?.startActionMode(getCallBack())!!
    }

    override fun onItemsSelected(selectedBatchIdList: MutableList<Long>) {
        setUpContextualBarSelection(selectedBatchIdList)
    }

    private fun setUpConfirmDialog() {
        confirmDialog = ConfirmDialog.getNewInstance(EXTRA_DELETE, selectedBatchIdList.size)
        confirmDialog.setCallBack(this)
        confirmDialog.setKey(EXTRA_DELETE)
    }

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


//    override fun onLongClicked(view: View, stockBatch: StockBatch) {
//        this.stockBatch = stockBatch
//
//        //contextual action bar
//        val actionMode = activity?.startActionMode(getCallBack())
//        actionMode?.title = "1 selected"
//
//    }

    //To show rename and delete PopupMenu
//    private fun createPopupMenu(view: View) {
//        val contextThemeWrapper = ContextThemeWrapper(context, R.style.PopupMenuOverlapAnchor)
//        popupMenu = PopupMenu(contextThemeWrapper, view, Gravity.END)
//        popupMenu.inflate(R.menu.contexual_menu)
//        popupMenu.show()
//    }

//    private fun popupMenuOnItemClick() {
//
//        popupMenu.setOnMenuItemClickListener { menuItem ->
//
//            when (menuItem.itemId) {
//                R.id.rename -> Log.d("tag", "popupMenuOnItemClick:")
//                R.id.delete -> ""
//            }
//
//            false
//        }
//    }
}