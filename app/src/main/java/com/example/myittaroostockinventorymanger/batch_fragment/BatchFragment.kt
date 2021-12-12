package com.example.myittaroostockinventorymanger.batch_fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

class BatchFragment : Fragment() {

    @BindView(R.id.recy_batch_list)
    lateinit var recyBatchList: RecyclerView

    @BindView(R.id.fab_add_batch)
    lateinit var fabAddBatch: FloatingActionButton;

    lateinit var adapter: BatchListRecycleViewAdapter

    lateinit var toolbar: MaterialToolbar;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_batch, container, false);
        ButterKnife.bind(this, view)

        toolbar = activity?.findViewById(R.id.tool_bar)!!

        setToolbarTitle()

        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycleView()

        fabAddBatch.setOnClickListener {

            goToAddNewBatchActivity()
        }

        val batchViewModel = ViewModelProvider(this)
                .get(BatchViewModel::class.java)

        batchViewModel.getAllStockWithBatches()
                ?.observe(viewLifecycleOwner) {

                    //covert nested list into one list
                    var stockBatchList = ListCreator.createStockBatchList(it)

                    adapter.insertItem(stockBatchList)
                }


    }

    private fun setUpRecycleView() {
        adapter = BatchListRecycleViewAdapter(context, arrayListOf())
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

}