package com.example.myittaroostockinventorymanger.ui.selling

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myittaroostockinventorymanger.Application
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.databinding.FragmentSellingBinding
import com.example.myittaroostockinventorymanger.util.ImageShower

class SellingFragment : Fragment(), SellingBatchListRecycleViewAdapter.ItemClickCallBack {

    private lateinit var adapter: SellingBatchListRecycleViewAdapter
    private lateinit var binding: FragmentSellingBinding
    private val context = Application.getContext()
    private val viewModel: SellingFragmentViewModel by viewModels()
    private var sellingBatchId: Long = 0
    private var qty = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSellingBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()

        viewModel.findSellingItemNames()

        viewModel.getSellingItemNameList()
            .observe(viewLifecycleOwner) {
                setUpAutoCompleteTextView(it)
            }

        viewModel.getSellingBatchList()
            .observe(viewLifecycleOwner) {
                Log.d("batchList", "onViewCreated: $it")
                adapter.insertData(it)
            }

        viewModel.getMessage()
            .observe(viewLifecycleOwner) {
                val notify = it.contentIfNotHandle

                if (notify != null) {
                    Toast.makeText(context, notify, Toast.LENGTH_SHORT).show()
                }
            }

        //Submission qty and update batch
        viewModel.getValidBatch()
            .observe(viewLifecycleOwner) {
                val batch = it.contentIfNotHandle
                if (batch != null) {
                    viewModel.updateBatch(batch, qty)
                    Log.d("isValid", "onViewCreated: $batch")
                }
            }

        //add transaction In
        viewModel.getValidTransaction()
            .observe(viewLifecycleOwner) {
                val transaction = it.contentIfNotHandle
                if (transaction != null) {
                    viewModel.insertTransaction(transaction)
                    Log.d("isValid", "onViewCreated: ${transaction}")
                }
            }

        viewModel.getIsReturnBack()
            .observe(viewLifecycleOwner) {
                val isReturn = it.contentIfNotHandle
                if (isReturn != null && isReturn) {
                    findNavController().popBackStack()
                }
            }

        //to update img view
        binding.actItemName.onItemClickListener =
            OnItemClickListener { p0, p1, p2, p3 ->
                val itemName = binding.actItemName.text.toString()
                viewModel.getItemByNames(itemName)
                    .observe(viewLifecycleOwner) {
                        ImageShower.showImage(context, it.imagePath, binding.imgItem)
                        viewModel.findBatchByItemId(it.itemId)
                    }
            }

        binding.btnSale.setOnClickListener {

            //if qty is empty  string Check for numberFormatException
            qty = if (binding.edtQuty.text!!.isNotEmpty()) {
                binding.edtQuty.text.toString().toInt()
            } else {
                0
            }
            viewModel.onClickSale(sellingBatchId, qty)
        }

    }

    private fun setUpAdapter() {
        adapter = SellingBatchListRecycleViewAdapter()
        adapter.setCallBack(this)
        binding.recyBatchList
            .apply {
                adapter = this@SellingFragment.adapter
                layoutManager = LinearLayoutManager(context)
            }

    }

    private fun setUpAutoCompleteTextView(sellingItemNameList: List<String>) {
        binding.actItemName
            .apply {
                val adapter = ArrayAdapter(
                    context,
                    R.layout.simple_drop_down_layout,
                    R.id.txt_name,
                    sellingItemNameList
                )
                setAdapter(adapter)
                threshold = 1
            }
    }

    override fun onClickItem(id: Long) {
        this.sellingBatchId = id
    }

}