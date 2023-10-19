package com.example.myittaroostockinventorymanger.data.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(indices = [Index(value = ["name"], unique = true)])
data class Item(
    @ColumnInfo("name")
    var name: String,
    @ColumnInfo("image_path")
    var imagePath: String
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_id")
    var itemId: Long = 0
}