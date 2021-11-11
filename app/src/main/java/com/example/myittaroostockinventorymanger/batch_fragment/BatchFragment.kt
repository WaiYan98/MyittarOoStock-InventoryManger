package com.example.myittaroostockinventorymanger.batch_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.util.VerticalSpaceItemDecoration
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BatchFragment : Fragment() {

    @BindView(R.id.recy_batch_list)
    lateinit var recyBatchList: RecyclerView

    @BindView(R.id.fab_add_batch)
    lateinit var fabAddBatch: FloatingActionButton;

    lateinit var adapter: BatchListRecycleViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_batch, container, false);
        ButterKnife.bind(this, view)
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycleView()

    }

    private fun setUpRecycleView() {
        adapter = BatchListRecycleViewAdapter(context)
        recyBatchList.adapter = adapter
        recyBatchList.layoutManager = LinearLayoutManager(context)
        recyBatchList.addItemDecoration(VerticalSpaceItemDecoration(8))
    }
}