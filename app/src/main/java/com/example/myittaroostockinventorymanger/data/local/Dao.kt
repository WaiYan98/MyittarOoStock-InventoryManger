package com.example.myittaroostockinventorymanger.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.myittaroostockinventorymanger.data.entities.Batch
import com.example.myittaroostockinventorymanger.data.entities.Item
import com.example.myittaroostockinventorymanger.data.entities.ItemWithBatch
import com.example.myittaroostockinventorymanger.data.entities.Transaction
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface Dao {
    /**
     * @param item return type is void for testing;
     * Change completable later;
     */
    @Insert
    fun insertItem(item: Item): Completable

    @Insert
    fun insertBatch(batch: Batch): Completable

    @Insert
    fun insertTransaction(transaction: Transaction?): Completable

    @Query("SELECT * FROM Item")
    fun getAllItems(): LiveData<List<Item>>

    @Query("SELECT * FROM Item")
    @androidx.room.Transaction
    fun getAllItemWithBatch(): LiveData<List<ItemWithBatch>>

    @Delete
    fun deleteItem(item: Item): Completable

    @Update
    fun updateItemName(item: Item): Completable

    @Update
    fun updateBatch(batch: Batch): Completable


    @Query("SElECT name FROM Item")
    fun getAllItemNames(): LiveData<List<String>>

    @Query("SELECT item_id FROM Item WHERE Item.name = :itemName")
    fun findItemIdByName(itemName: String): Observable<Long>

    @Query("DELETE FROM Batch WHERE batch_id IN(:ids)")
    fun deleteBatchesByIds(ids: List<Long>): Completable

    @Query("DELETE FROM Item WHERE item_id IN(:ids)")
    fun deleteItemByIds(ids: List<Long>): Completable

    @Query("SELECT * FROM Item WHERE name LIKE :queryText")
    fun searchItems(queryText: String): LiveData<List<Item>>

    @Query("SELECT * FROM Item WHERE item_id = :id")
    fun getItemById(id: Long): LiveData<Item>
}