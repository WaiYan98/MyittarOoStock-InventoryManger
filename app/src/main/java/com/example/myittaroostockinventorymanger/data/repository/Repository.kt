package com.example.myittaroostockinventorymanger.data.repository

import androidx.lifecycle.LiveData
import com.example.myittaroostockinventorymanger.data.entities.Batch
import com.example.myittaroostockinventorymanger.data.entities.BatchWithItem
import com.example.myittaroostockinventorymanger.data.entities.Item
import com.example.myittaroostockinventorymanger.data.entities.Transaction
import com.example.myittaroostockinventorymanger.data.local.Dao
import com.example.myittaroostockinventorymanger.data.local.LocalDataBaseService
import io.reactivex.Completable
import io.reactivex.Observable

class Repository {
    private val dao: Dao

    init {
        dao = LocalDataBaseService.getDataBase()
            .dao()
    }

    fun insertItem(item: Item): Completable {
        return dao.insertItem(item)
    }

    fun insertBatch(batch: Batch): Completable {
        return dao.insertBatch(batch)
    }

    fun insertTransaction(transaction: Transaction): Completable {
        return dao.insertTransaction(transaction)
    }

    val allItem: LiveData<List<Item>> get() = dao.getAllItems()
    val allBatchWithItem: LiveData<List<BatchWithItem>>
        get() = dao.getAllBatchWithItem()

    fun deleteItem(item: Item): Completable {
        return dao.deleteItem(item)
    }

    fun updateItemName(item: Item): Completable {
        return dao.updateItemName(item)
    }

    fun updateBatch(batch: Batch): Completable {
        return dao.updateBatch(batch)
    }

    val allItemNames: LiveData<List<String>>
        get() = dao.getAllItemNames()

    fun findItemIdByName(stockName: String): Observable<Long> {
        return dao.findItemIdByName(stockName)
    }

    fun deleteBatchesById(ids: List<Long>): Completable {
        return dao.deleteBatchesByIds(ids)
    }

    fun deleteItemsById(ids: List<Long>): Completable {
        return dao.deleteItemByIds(ids)
    }

    fun searchItems(queryText: String): LiveData<List<Item>> {
        return dao.searchItems(queryText)
    }

    fun getItemById(id: Long): LiveData<Item> {
        return dao.getItemById(id)
    }

    fun findBatchWithItemByIds(batchId: Long): LiveData<BatchWithItem> {
        return dao.findItemWithBatchById(batchId)
    }

    fun findBatchWithItemByIds(idList: List<Long>): LiveData<List<BatchWithItem>> {
        return dao.findBatchWithItemByIds(idList)
    }

    fun findIdListByQueryText(queryText: String): LiveData<List<Long>> {
        return dao.findIdListByQueryText(queryText)
    }
}