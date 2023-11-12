package com.example.myittaroostockinventorymanger.ui.batch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myittaroostockinventorymanger.Application
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.data.entities.BatchWithItem
import com.example.myittaroostockinventorymanger.util.ImageShower
import java.text.SimpleDateFormat

class BatchListRecycleViewAdapter() :
    RecyclerView.Adapter<BatchListRecycleViewAdapter.ViewHolder>() {

    private val batchWithItem: MutableList<BatchWithItem> = mutableListOf()
    private var isSelectedMode = false
    private val selectedBatchIdList: MutableList<Long> = mutableListOf()
    private lateinit var callBack: CallBack
    val context = Application.getContext()
    fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view =
            layoutInflater.inflate(R.layout.item_stock_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (batch, item) = batchWithItem[position]
        val curBatchId = batch.batchId
        val df = SimpleDateFormat("dd/MM/yy")

        callBack.getHolder(holder)

        //To show Image
        ImageShower.showImage(context, item.imagePath, holder.imgViewItem)

        holder.txtStockName.text = item.name
        holder.txtCostPrice.text = batch.basePrice.toString()
        holder.txtSalePrice.text = batch.sellingPrice.toString()
        holder.txtExpDate.text = df.format(batch.expDate)
        holder.txtAmount.text = batch.quantity.toString()

        //when batch item onLongClicked show contextual action mode
        holder.linearLayoutBatch.setOnLongClickListener { v: View? ->
            callBack.getHolder(holder)
            callBack.onLongClicked()
            checkSelectAndDeSelectBatches(curBatchId)
            //change color for selected and deselected batch
            holder.linearLayoutBatch.isSelected = isSelectedBatch(curBatchId)
            callBack.onItemsSelected(selectedBatchIdList)
            isSelectedMode = true
            if (selectedBatchIdList.size == 1) {
                callBack.onSelectedItemIsOne(selectedBatchIdList[0])
            }
            true
        }

        //to insert selected Batch id to selectedBatchIdList
        holder.linearLayoutBatch.setOnClickListener { v: View? ->
            if (isSelectedMode) {
                checkSelectAndDeSelectBatches(curBatchId)
                holder.linearLayoutBatch.isSelected = isSelectedBatch(curBatchId)
                callBack.onItemsSelected(selectedBatchIdList)
                if (selectedBatchIdList.size == 1) {
                    callBack.onSelectedItemIsOne(selectedBatchIdList[0])
                }
                notifyItemChanged(position)
            }
        }
        holder.linearLayoutBatch.isSelected = isSelectedBatch(curBatchId)
    }

    private fun isSelectedBatch(id: Long): Boolean {
        return selectedBatchIdList.contains(id)
    }

    override fun getItemCount(): Int {
        return batchWithItem.size
    }

    //when item click itemId save to selectedList and id is equal remove from list
    private fun checkSelectAndDeSelectBatches(id: Long) {
        if (!selectedBatchIdList.contains(id)) {
            selectedBatchIdList.add(id)
        } else {
            selectedBatchIdList.remove(id)
        }
    }

    //if tap actionbar close button following logic work
    fun contextualActionBarClose() {
        isSelectedMode = false
        selectedBatchIdList.clear()
        notifyDataSetChanged()
    }

    fun selectAllBatch() {
        val batchIdList = batchWithItem.toBatchIdList()

        if (batchIdList == selectedBatchIdList) {
            selectedBatchIdList.clear()
        } else {
            selectedBatchIdList.clear()
            selectedBatchIdList.addAll(batchIdList)
        }
        callBack.onItemsSelected(selectedBatchIdList)
        notifyDataSetChanged()
    }

    private fun List<BatchWithItem>.toBatchIdList(): List<Long> = this.map { it.batch.batchId }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var linearLayoutBatch: LinearLayout
        var txtStockName: TextView
        var txtCostPrice: TextView
        var txtSalePrice: TextView
        var txtExpDate: TextView
        var txtAmount: TextView
        var imgViewItem: ImageView

        init {
            linearLayoutBatch = itemView.findViewById(R.id.linear_layout_batch)
            txtStockName = itemView.findViewById(R.id.txt_stock_name)
            txtCostPrice = itemView.findViewById(R.id.txt_cost_price)
            txtSalePrice = itemView.findViewById(R.id.txt_sale_price)
            txtExpDate = itemView.findViewById(R.id.txt_exp_date)
            txtAmount = itemView.findViewById(R.id.txt_amount)
            imgViewItem = itemView.findViewById(R.id.image_view_item)
        }
    }

    fun insertItem(batchWithItem: List<BatchWithItem>) {
        this.batchWithItem.clear()
        this.batchWithItem.addAll(batchWithItem)
        notifyDataSetChanged()
    }


    interface CallBack {

        fun getHolder(holder: ViewHolder)
        fun onLongClicked()
        fun onItemsSelected(selectedBatchIdList: MutableList<Long>)
        fun onSelectedItemIsOne(batchId: Long)

    }
}