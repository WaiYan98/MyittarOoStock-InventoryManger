package com.example.myittaroostockinventorymanger.ui.transactions

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myittaroostockinventorymanger.Application
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.data.entities.BatchWithItem
import com.example.myittaroostockinventorymanger.data.entities.Transaction
import com.example.myittaroostockinventorymanger.databinding.ItemTransactionBinding
import com.example.myittaroostockinventorymanger.util.ImageShower
import java.text.SimpleDateFormat

class TransactionRecyclerViewAdapter(val context: Context) :
    RecyclerView.Adapter<TransactionRecyclerViewAdapter.ViewHolder>() {


    private var transactionList = listOf<Transaction>()
    private lateinit var binding: ItemTransactionBinding

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        //sorted
//        transactionList.apply {
//            sortedWith(Comparator<Transaction> { p0, p1 ->
//                p1.date.compareTo(p0.date)
//            })
//        }
        Log.d("sort", "onBindViewHolder: $transactionList")

        val currentTransaction = transactionList[position]

        bindData(currentTransaction)
    }

    private fun bindData(curTransaction: Transaction) {

        val sf = SimpleDateFormat("dd/MM/YYYY")
        val sfTime = SimpleDateFormat("hh:mm a")

        binding.txtDate.text = sf.format(curTransaction.date)
        binding.txtTime.text = sfTime.format(curTransaction.date)

        if (curTransaction.itemIn != 0) {
            binding.cardViewTail.visibility = View.VISIBLE
            binding.txtProfitColumn.visibility = View.INVISIBLE
            binding.txtProfit.visibility = View.INVISIBLE
            binding.txtHeader.text = "In"
            binding.txtInOutColumn.text = "In"
            binding.txtInOut.text = curTransaction.itemIn.toString()
            binding.cardViewHeader.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.card_header_blue
                )
            )
        }

        if (curTransaction.itemOut != 0) {
            binding.txtProfit.visibility = View.VISIBLE
            binding.txtProfitColumn.visibility = View.VISIBLE
            binding.cardViewTail.visibility = View.INVISIBLE
            binding.txtHeader.text = "Out"
            binding.txtInOutColumn.text = "Out"
            binding.txtInOut.text = curTransaction.itemOut.toString()
            binding.txtProfit.text = curTransaction.profit.toString()
            binding.cardViewHeader.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.card_header_green
                )
            )

        }

        binding.txtItemName.text = curTransaction.itemName

        ImageShower.showImage(
            context,
            curTransaction.imagePath,
            binding.imgItem
        )
    }

    fun insertData(transactionList: List<Transaction>) {
        this.transactionList = transactionList
        notifyDataSetChanged()
    }

}