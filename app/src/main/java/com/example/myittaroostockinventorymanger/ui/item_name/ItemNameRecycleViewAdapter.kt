package com.example.myittaroostockinventorymanger.ui.item_name

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        holder.txtStockName.text = currentItem.name

        showItemImage(currentItem.imagePath, holder)
        Log.d("myTag", "onBindViewHolder: ${currentItem.imagePath}")

        holder.linearLayoutItemName.setOnLongClickListener { v: View? ->
            isSelectedMode = true
            checkSelectAndUnselectItem(currentItem)
            callBack.onLongClickItem()
            callBack.onClickItem(selectedItemIdList)
            holder.linearLayoutItemView.isSelected = selectedItemIdList.contains(currentItem.itemId)

            if (selectedItemIdList.size == 1) {
                callBack.onSelectedItemIsOne(currentItem.itemId)
            }
            true
        }

        holder.linearLayoutItemName.setOnClickListener { v: View? ->
            if (isSelectedMode) {
                //For test
                checkSelectAndUnselectItem(currentItem)
                callBack.onClickItem(selectedItemIdList)
                holder.linearLayoutItemView.isSelected =
                    selectedItemIdList.contains(currentItem.itemId)
                if (selectedItemIdList.size == 1) {

                    callBack.onSelectedItemIsOne(selectedItemIdList[0])

                }
                notifyItemChanged(position)
            }
        }

        holder.linearLayoutItemView.isSelected = selectedItemIdList.contains(currentItem.itemId)

    }

    //if imagePath empty show default image
    // TODO: drawable to uri String
    private fun showItemImage(imagePath: String, holder: ViewHolder) {

        if (imagePath.isEmpty()) {
            Glide
                .with(context)
                .load(R.drawable.no_img)
                .circleCrop()
                .into(holder.itemImageView)
        } else {
            Glide
                .with(context)
                .load(imagePath)
                .circleCrop()
                .into(holder.itemImageView)
        }


    }


    /*
    check selectedItemIdList contain this
     @param currentItem
    If not have this id in this list add currentIte.itemId to selectedItemIdList
    If contain remove form list.
     */
    private fun checkSelectAndUnselectItem(currentItem: Item) {
        val itemId = currentItem.itemId
        if (!selectedItemIdList.contains(itemId)) {
            selectedItemIdList.add(itemId)
            Log.d("tag", "checkSelectAndUnselectItem: select $itemId $selectedItemIdList")
        } else {
            selectedItemIdList.remove(itemId)
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
        var linearLayoutItemView: LinearLayout
        var itemImageView: ImageView

        init {
            linearLayoutItemName = itemView.findViewById(R.id.linear_layout_stock_name)
            cardViewItem = itemView.findViewById(R.id.card_view_item)
            txtStockName = itemView.findViewById(R.id.txt_stock_name)
            linearLayoutItemView = itemView.findViewById(R.id.linear_layout_item_view)
            itemImageView = itemView.findViewById(R.id.item_image_view)
        }
    }

    private fun List<Item>.itemToIdList(): List<Long> = this.map {
        it.itemId
    }

    interface CallBack {
        fun onLongClickItem()
        fun onClickItem(selectedStockIdList: List<Long>)
        fun onSelectedItemIsOne(id: Long)
    }
}