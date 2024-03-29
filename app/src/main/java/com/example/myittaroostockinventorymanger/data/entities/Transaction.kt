package com.example.myittaroostockinventorymanger.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Transaction")
data class Transaction(
    @ColumnInfo(name = "item_name") var itemName: String,
    @ColumnInfo(name = "image_path") var imagePath: String,
    @ColumnInfo(name = "item_in") var itemIn: Int,
    @ColumnInfo(name = "item_out") var itemOut: Int,
    @ColumnInfo(name = "profit") var profit: Double,
    @ColumnInfo(name = "date") var date: Date
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    var transactionId: Long = 0
}