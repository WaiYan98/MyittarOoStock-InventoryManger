package com.example.myittaroostockinventorymanger.pojo

import android.os.Parcelable
import com.example.myittaroostockinventorymanger.local.Batch
import com.example.myittaroostockinventorymanger.local.Stock
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StockBatch(
        var stock: Stock,
        var batch: Batch,
        var isSelected: Boolean
):Parcelable