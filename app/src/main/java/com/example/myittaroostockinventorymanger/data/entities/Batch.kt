package com.example.myittaroostockinventorymanger.data.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity
data class Batch(
        @ColumnInfo(name = "stock_id")
        var stockId: Long,

        @ColumnInfo(name = "original_price")
        var originalPrice: Double,

        @ColumnInfo(name = "sale_price")
        var salePrice: Double,

        @ColumnInfo(name = "total_stock")
        var totalStock: Int,

        @ColumnInfo(name = "exp_date")
        var expDate: Date
) : Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "batch_id")
    var batchId: Long = 0

}
