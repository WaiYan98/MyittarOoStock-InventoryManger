package com.example.myittaroostockinventorymanger.ui.batch

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.databinding.FragmentBatchBinding
import com.example.myittaroostockinventorymanger.databinding.FragmentOutOfDateBatchBinding
import com.example.myittaroostockinventorymanger.util.VerticalSpaceItemDecoration
import java.util.Calendar

class OutOfDateBatchFragment : Fragment(), BatchListRecycleViewAdapter.CallBack {

    private lateinit var binding: FragmentOutOfDateBatchBinding
    private val viewModel: OutOfDateFragmentViewModel by viewModels()
    private lateinit var adapter: BatchListRecycleViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOutOfDateBatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycleView()

        //currentDate+10days
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, 10)
        val currDateAfter10Days = cal.time.time

        viewModel.getOutOfDateBatchWithItem(currDateAfter10Days)
            .observe(viewLifecycleOwner) {

                if (it.isEmpty()) {
                    binding.txtEmptyBatch.visibility = View.VISIBLE
                } else {
                    binding.txtEmptyBatch.visibility = View.GONE
                }

                Log.d("outOfDate", "onViewCreated: $it")
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