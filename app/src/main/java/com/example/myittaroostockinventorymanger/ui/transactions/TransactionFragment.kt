package com.example.myittaroostockinventorymanger.ui.transactions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myittaroostockinventorymanger.data.entities.BatchWithItem
import com.example.myittaroostockinventorymanger.data.entities.Transaction
import com.example.myittaroostockinventorymanger.databinding.FragmentTransactionBinding
import com.example.myittaroostockinventorymanger.util.VerticalSpaceItemDecoration
import java.util.Date

class TransactionFragment : Fragment() {

    private lateinit var binding: FragmentTransactionBinding
    private lateinit var adapter: TransactionRecyclerViewAdapter
    private val viewModel: TransactionFragmentViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        viewModel.getAllTransaction().observe(viewLifecycleOwner) {
            //sorted
            viewModel.sortedTransaction(it)
        }

        viewModel.getSortedTransaction()
            .observe(viewLifecycleOwner) {
                adapter.insertData(it)
            }

    }

    private fun setUpRecyclerView() {
        adapter = TransactionRecyclerViewAdapter(requireContext())
        binding.recyTransaction.apply {
            adapter = this@TransactionFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(VerticalSpaceItemDecoration(16))
        }
    }

}