package com.example.myittaroostockinventorymanger.ui.batch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myittaroostockinventorymanger.Application
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.data.entities.BatchWithItem
import com.example.myittaroostockinventorymanger.util.AutoNumGenerator
import java.text.SimpleDateFormat
import java.util.Locale

class BatchListRecycleViewAdapter() :
    RecyclerView.Adapter<BatchListRecycleViewAdapter.ViewHolder>() {

    private val batchWithItem: MutableList<BatchWithItem> = mutableListOf()
    private var isSelectedMode = false
    private val selectedBatchIdList: MutableList<Long> = mutableListOf()
    private lateinit var callBack: CallBack
    private var holder: ViewHolder? = null
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
        this.holder = holder
        val (batch, item) = batchWithItem[position]
        val curBatchId = batch.batchId
        val df = SimpleDateFormat("dd/MM/yy")
        val cardView = holder.cardViewItemNameContainer
        changeCardViewColor(cardView)
        holder.txtStockName.text = item.name
        holder.txtNameInitialWord.text = item.name.substring(0, 1).uppercase(Locale.getDefault())
        holder.txtCostPrice.text = batch.originalPrice.toString()
        holder.txtSalePrice.text = batch.salePrice.toString()
        holder.txtExpDate.text = df.format(batch.expDate)
        holder.txtAmount.text = batch.quantity.toString()

        //when batch item onLongClicked show contextual action mode
        holder.linearLayoutBatch.setOnLongClickListener { v: View? ->
            callBack.onLongClicked()
            checkSelectAndDeSelectBatches(curBatchId)
            //change color for selected and deselected batch
            holder.linearLayoutBatch.isSelected = isSelectedBatch(curBatchId)
            callBack.onItemsSelected(selectedBatchIdList)
            isSelectedMode = true
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

    //    public void selectAllItems() {
    //
    //        boolean isAllSelected = false;
    //
    //        for (ItemBatch itemBatch : batchWithItem) {
    //
    //            if (!itemBatch.isSelected()) {
    //                isAllSelected = false;
    //                break;
    //            } else {
    //                isAllSelected = true;
    //            }
    //        }
    //
    //        if (isAllSelected) {
    //            importSelectedFalse();
    //            this.selectedBatchIdList.clear();
    //        } else {
    //            importSelectedTrue();
    //            insertAllBatchId();
    //        }
    //        callBack.onItemsSelected(selectedBatchIdList);
    //
    //        notifyDataSetChanged();
    //    }
    //    private void insertAllBatchId() {
    //
    //        selectedBatchIdList.clear();
    //
    //        for (ItemBatch itemBatch : batchWithItem) {
    //            this.selectedBatchIdList.add(itemBatch.getBatch().getBatchId());
    //        }
    //    }
    //    private void importSelectedTrue() {
    //        for (ItemBatch itemBatch : batchWithItem) {
    //            itemBatch.setSelected(true);
    //        }
    //    }
    //    private ItemBatch findStockBatchByBatchId(Long batchId) {
    //        for (ItemBatch itemBatch : batchWithItem) {
    //            if (batchId == itemBatch.getBatch().getBatchId()) {
    //                return itemBatch;
    //            }
    //        }
    //        return null;
    //    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var linearLayoutBatch: LinearLayout
        var cardViewItemNameContainer: CardView
        var txtStockName: TextView
        var txtCostPrice: TextView
        var txtSalePrice: TextView
        var txtExpDate: TextView
        var txtAmount: TextView
        var txtNameInitialWord: TextView

        init {
            linearLayoutBatch = itemView.findViewById(R.id.linear_layout_batch)
            cardViewItemNameContainer = itemView.findViewById(R.id.card_view_item_name_container)
            txtStockName = itemView.findViewById(R.id.txt_stock_name)
            txtCostPrice = itemView.findViewById(R.id.txt_cost_price)
            txtSalePrice = itemView.findViewById(R.id.txt_sale_price)
            txtExpDate = itemView.findViewById(R.id.txt_exp_date)
            txtAmount = itemView.findViewById(R.id.txt_amount)
            txtNameInitialWord = itemView.findViewById(R.id.txt_name_initial_word)
        }
    }

    fun insertItem(batchWithItem: List<BatchWithItem>) {
        this.batchWithItem.clear()
        this.batchWithItem.addAll(batchWithItem)
        notifyDataSetChanged()
    }

    //Assign allStockBatches isSelected property to false
    //    private void importSelectedFalse() {
    //
    //        for (ItemBatch itemBatch : batchWithItem) {
    //
    //        }
    //
    //    }
    //for change card view color by random
    private fun changeCardViewColor(cardView: CardView) {
        val randomNum = AutoNumGenerator.generateNum()
        val context = Application.getContext()
        when (randomNum) {
            1 -> cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_red))

            2 -> cardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.light_green
                )
            )

            3 -> cardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.light_blue
                )
            )

            4 -> cardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.light_orange
                )
            )

            5 -> cardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.light_purple
                )
            )
        }
    }

    interface CallBack {
        fun onLongClicked()
        fun onItemsSelected(selectedBatchIdList: MutableList<Long>)
        fun onSelectedItemIsOne(batchId: Long)
    }
}