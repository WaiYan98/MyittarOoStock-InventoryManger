package com.example.myittaroostockinventorymanger.batch_fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.local.Batch
import com.example.myittaroostockinventorymanger.local.StockWithBatch
import com.example.myittaroostockinventorymanger.pojo.StockBatch
import com.example.myittaroostockinventorymanger.util.ListCreator
import com.example.myittaroostockinventorymanger.util.VerticalSpaceItemDecoration
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BatchFragment : Fragment(), BatchListRecycleViewAdapter.CallBack {

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

    private lateinit var popupMenu: PopupMenu
    private lateinit var stockBatch: StockBatch

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


    }

    private fun setUpRecycleView() {
        adapter = BatchListRecycleViewAdapter(context, arrayListOf())
        recyBatchList.adapter = adapter
        recyBatchList.layoutManager = LinearLayoutManager(context)
        recyBatchList.addItemDecoration(VerticalSpaceItemDecoration(8))
        adapter.setCallBack(this)
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

    override fun onLongClicked(view: View, stockBatch: StockBatch) {
        this.stockBatch = stockBatch
        createPopupMenu(view)
        popupMenuOnItemClick()
    }

    //To show rename and delete PopupMenu
    private fun createPopupMenu(view: View) {
        val contextThemeWrapper = ContextThemeWrapper(context, R.style.PopupMenuOverlapAnchor)
        popupMenu = PopupMenu(contextThemeWrapper, view, Gravity.END)
        popupMenu.inflate(R.menu.contexual_menu)
        popupMenu.show()
    }

    private fun popupMenuOnItemClick() {

        popupMenu.setOnMenuItemClickListener { menuItem ->

            when (menuItem.itemId) {
                R.id.rename -> Log.d("tag", "popupMenuOnItemClick:")
                R.id.delete -> ""
            }

            false
        }
    }
}