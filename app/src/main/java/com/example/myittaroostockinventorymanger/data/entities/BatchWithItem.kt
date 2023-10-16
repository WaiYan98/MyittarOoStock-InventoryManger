package com.example.myittaroostockinventorymanger.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class BatchWithItem(
    @Embedded
    val batch: Batch,
    @Relation(
        parentColumn = "item_id",
        entityColumn = "item_id",
    )
    val item: Item
)
