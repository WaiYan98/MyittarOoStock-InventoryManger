package com.example.myittaroostockinventorymanger.data.entities

import android.os.Parcelable
import com.example.myittaroostockinventorymanger.data.entities.Batch
import com.example.myittaroostockinventorymanger.data.entities.Stock
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StockBatch(
    var stock: Stock,
    var batch: Batch,
    var isSelected: Boolean
) : Parcelable