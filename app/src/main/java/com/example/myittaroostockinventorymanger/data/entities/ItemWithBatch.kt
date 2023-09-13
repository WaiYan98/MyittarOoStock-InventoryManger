package com.example.myittaroostockinventorymanger.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ItemWithBatch(
    @Embedded var item: Item,
    @Relation(
        parentColumn = "item_id",
        entityColumn = "item_id"
    ) var batchList: List<Batch>
)