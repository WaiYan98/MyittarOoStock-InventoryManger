package com.example.myittaroostockinventorymanger.ui.selling

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.data.entities.Batch
import java.text.FieldPosition
import java.text.SimpleDateFormat

class SellingBatchListRecycleViewAdapter :
    Adapter<SellingBatchListRecycleViewAdapter.ViewHolder>() {

    private var batchList: List<Batch> = listOf()
    private lateinit var callBack: ItemClickCallBack
    private var selectedPosition = RecyclerView.NO_POSITION

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val basePrice: TextView
        val sellingPrice: TextView
        val quantity: TextView
        val expDate: TextView
        val constraintLayoutView: ConstraintLayout

        init {
            basePrice = itemView.findViewById(R.id.txt_base_price)
            sellingPrice = itemView.findViewById(R.id.txt_selling_price)
            quantity = itemView.findViewById(R.id.txt_quantity)
            expDate = itemView.findViewById(R.id.txt_exp_date)
            constraintLayoutView = itemView.findViewById(R.id.constraint_layout_view)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_selling_batch_list, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return batchList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentBatch = batchList[position]

        val sf = SimpleDateFormat("dd/MM/YYYY")

        holder.basePrice.text = currentBatch.basePrice.toString()
        holder.sellingPrice.text = currentBatch.sellingPrice.toString()
        holder.quantity.text = currentBatch.quantity.toString()
        holder.expDate.text = sf.format(currentBatch.expDate)

        holder.constraintLayoutView.setOnClickListener {

            if (selectedPosition != position) {
                selectedPosition = position
                notifyDataSetChanged()
            }
            callBack.onClickItem(currentBatch.batchId)
        }

        holder.constraintLayoutView.isSelected = selectedPosition == position

    }

    fun insertData(batchList: List<Batch>) {
        this.batchList = batchList
        notifyDataSetChanged()
    }

    fun setCallBack(callBack: ItemClickCallBack) {
        this.callBack = callBack
    }

    interface ItemClickCallBack {
        fun onClickItem(id: Long)

    }

}