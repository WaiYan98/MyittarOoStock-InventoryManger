package com.example.myittaroostockinventorymanger.data.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemBatch(
    var item: Item,
    var batch: Batch,
) : Parcelable