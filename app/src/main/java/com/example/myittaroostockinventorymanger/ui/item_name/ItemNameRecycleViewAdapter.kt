package com.example.myittaroostockinventorymanger.ui.item_name

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myittaroostockinventorymanger.Application
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.data.entities.Item
import java.util.Locale

class ItemNameRecycleViewAdapter() : RecyclerView.Adapter<ItemNameRecycleViewAdapter.ViewHolder>() {
    private val context: Context
    private var itemList: List<Item> = listOf()
    private lateinit var callBack: CallBack
    private var isSelectedMode = false
    private var selectedItemIdList: MutableList<Long> = ArrayList()
    private var itemPosition: MutableList<Int> = mutableListOf()

    init {
        context = Application.getContext()
    }

    fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_stock_name, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = itemList[position]
        val initialWord = currentItem.name[0].toString().uppercase(Locale.getDefault())
        holder.txtStockName.text = currentItem.name

        holder.linearLayoutItemName.setOnLongClickListener { v: View? ->
            isSelectedMode = true
            checkSelectAndUnselectItem(currentItem, holder, position)
            callBack.onLongClickItem()
            callBack.onClickItem(selectedItemIdList)
            holder.linearLayoutItemView.isSelected = selectedItemIdList.contains(currentItem.itemId)

            if (selectedItemIdList.size == 1) {
                callBack.onSelectedItemIsOne(currentItem)
            }
            true
        }

        holder.linearLayoutItemName.setOnClickListener { v: View? ->
            if (isSelectedMode) {
                //For test
                checkSelectAndUnselectItem(currentItem, holder, position)
                callBack.onClickItem(selectedItemIdList)
                holder.linearLayoutItemView.isSelected =
                    selectedItemIdList.contains(currentItem.itemId)
                if (selectedItemIdList.size == 1) {
//solve for selected many items and rename it left one item
                        val item = itemList[itemPosition[0]]

                        callBack.onSelectedItemIsOne(item)

                        Log.d("tag", "onBindViewHolder: $itemPosition,$item")

                }
                notifyItemChanged(position)
            }
        }

        holder.linearLayoutItemView.isSelected = selectedItemIdList.contains(currentItem.itemId)

        holder.txtNameInitialWord.text = initialWord

        holder.linearLayoutItemView.isSelected
    }


    /*
    check selectedItemIdList contain this
     @param currentItem
     @param holder
    If not have this id in this list add currentIte.itemId to selectedItemIdList
    If contain remove form list.
     */
    private fun checkSelectAndUnselectItem(
        currentItem: Item,
        holder: ViewHolder,
        position: Int
    ) {
        val itemId = currentItem.itemId
        if (!selectedItemIdList.contains(itemId)) {
            selectedItemIdList.add(itemId)
            itemPosition.add(position)

            Log.d("tag", "checkSelectAndUnselectItem: select $itemId $selectedItemIdList")
        } else {
            selectedItemIdList.remove(itemId)
            itemPosition.remove(position)
            Log.d("tag", "checkSelectAndUnselectItem: deselect $itemId $selectedItemIdList")
        }
    }


    fun onClickSelectAll() {
        val itemIdList = itemList.itemToIdList()

        if (itemIdList == selectedItemIdList) {
            selectedItemIdList.clear()
        } else {
            selectedItemIdList = itemIdList as MutableList<Long>
        }

        callBack.onClickItem(selectedItemIdList)
        notifyDataSetChanged()
    }

    fun contextualActionBarClose() {
        selectedItemIdList.clear()
        itemPosition.clear()
        isSelectedMode = false
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    /**
     * @param itemList To update data by observer
     */
    fun insertItem(itemList: List<Item>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var linearLayoutItemName: LinearLayout
        var cardViewItem: CardView
        var txtStockName: TextView
        var txtNameInitialWord: TextView
        var linearLayoutItemView: LinearLayout

        init {
            linearLayoutItemName = itemView.findViewById(R.id.linear_layout_stock_name)
            cardViewItem = itemView.findViewById(R.id.card_view_item)
            txtStockName = itemView.findViewById(R.id.txt_stock_name)
            txtNameInitialWord = itemView.findViewById(R.id.txt_name_initial_word)
            linearLayoutItemView = itemView.findViewById(R.id.linear_layout_item_view)
        }
    }

    private fun List<Item>.itemToIdList(): List<Long> = this.map {
        it.itemId
    }

    interface CallBack {
        fun onLongClickItem()
        fun onClickItem(selectedStockIdList: List<Long>)
        fun onSelectedItemIsOne(item: Item)
    }
}