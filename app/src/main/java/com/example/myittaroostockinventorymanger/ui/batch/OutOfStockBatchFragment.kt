package com.example.myittaroostockinventorymanger.ui.batch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.databinding.FragmentBatchBinding
import com.example.myittaroostockinventorymanger.databinding.FragmentOutOfStockBatchBinding
import com.example.myittaroostockinventorymanger.util.VerticalSpaceItemDecoration

class OutOfStockBatchFragment : Fragment(), BatchListRecycleViewAdapter.CallBack {

    private lateinit var binding: FragmentOutOfStockBatchBinding
    private val viewModel: OutOfStockFragmentViewModel by viewModels()
    private lateinit var adapter: BatchListRecycleViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOutOfStockBatchBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycleView()

        viewModel.getOutOfStockBatchWithItem(10)
            .observe(viewLifecycleOwner) {
                Log.d("outOfStock", "onViewCreated: $it")
                if (it.isEmpty()) {
                    binding.txtEmptyBatch.visibility = View.VISIBLE
                } else {
                    binding.txtEmptyBatch.visibility = View.GONE
                }

                adapter.insertItem(it)
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

    override fun getHolder(holder: BatchListRecycleViewAdapter.ViewHolder) {
        holder.linearLayoutBatch.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.warning_card_color
            )
        )
    }

    override fun onLongClicked() {

    }

    override fun onItemsSelected(selectedBatchIdList: MutableList<Long>) {

    }

    override fun onSelectedItemIsOne(batchId: Long) {

    }
}