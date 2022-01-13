package com.example.myittaroostockinventorymanger.pojo

import com.example.myittaroostockinventorymanger.local.Batch
import com.example.myittaroostockinventorymanger.local.Stock

data class StockBatch(
        val stock: Stock,
        val batch: Batch,
        var isSelected: Boolean
) {
}