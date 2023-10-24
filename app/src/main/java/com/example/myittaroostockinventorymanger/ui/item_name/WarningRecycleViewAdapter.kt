package com.example.myittaroostockinventorymanger.ui.item_name

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myittaroostockinventorymanger.Application
import com.example.myittaroostockinventorymanger.R
import com.example.myittaroostockinventorymanger.data.entities.BatchWithItem
import com.example.myittaroostockinventorymanger.util.ImageShower
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class WarningRecycleViewAdapter :
    RecyclerView.Adapter<WarningRecycleViewAdapter.ViewHolder>() {

    private var batchWithItemList: List<BatchWithItem> = listOf()
    private val context: Context = Application.getContext()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var imgItem: ImageView
        lateinit var txtItemName: TextView
        lateinit var txtExpDate: TextView

        init {
            imgItem = itemView.findViewById(R.id.img_item)
            txtItemName = itemView.findViewById(R.id.txt_item_name)
            txtExpDate = itemView.findViewById(R.id.txt_exp_date)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_warning, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return batchWithItemList.size
    }

    // TODO: image missing
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (batch, item) = batchWithItemList[position]
        val simpleDateFormat = SimpleDateFormat("dd/MM/YYYY")
        ImageShower.showImage(context, item.imagePath, holder.imgItem)
        holder.txtItemName.text = item.name
        holder.txtExpDate.text = simpleDateFormat.format(batch.expDate)

        Log.d("testtest", "onBindViewHolder:${item.name} ${item.imagePath} ${batch.batchId}")
    }

    fun insertData(batchWithItemList: List<BatchWithItem>) {
        this.batchWithItemList = batchWithItemList
        notifyDataSetChanged()
    }


}