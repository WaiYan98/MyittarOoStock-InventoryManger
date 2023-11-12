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
import io.reactivex.Single

class Repository {
    private val dao: Dao

    init {
        dao = LocalDataBaseService.getDataBase()
            .dao()
    }

    fun insertItem(item: Item): Completable {
        return dao.insertItem(item)
    }

    fun insertBatch(batch: Batch): Single<Long> {
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

    fun findItemByName(itemName: String): LiveData<Item> {
        return dao.findItemByName(itemName)
    }

    fun getAllItemIds(): LiveData<List<Long>> {
        return dao.getAllItemIds()
    }

    fun deleteBatchesByItemIds(ids: List<Long>): Completable {
        return dao.deleteBatchesByItemIds(ids)
    }

    fun updateTransaction(transaction: Transaction): Completable {
        return dao.updateTransaction(transaction)
    }

    fun findItemNameByIds(ids: List<Long>): LiveData<List<String>> {
        return dao.findItemNameByIds(ids)
    }

    fun findBatchByItemId(itemId: Long): LiveData<List<Batch>> {
        return dao.findBatchByItemId(itemId)
    }

    fun findBatchByBatchId(batchId: Long): Single<Batch> {
        return dao.findBatchByBatchId(batchId)
    }

    fun getAllTransaction(): LiveData<List<Transaction>> {
        return dao.getAllTransaction()
    }

    fun findBatchWithItemByBatchIds(batchIds: List<Long>): LiveData<List<BatchWithItem>> {
        return dao.findBatchWithItemByBatchIds(batchIds)
    }

    fun findItemById(itemId: Long): LiveData<Item> {
        return dao.findItemById(itemId)
    }

    fun getExpiredBatchRowCount(date: Long): LiveData<Long> {
        return dao.getExpiredBatchRowCount(date)
    }

    fun getOutOfStockRowCount(num: Int): LiveData<Long> {
        return dao.getOutOfStockBatchRowCount(num)
    }

    fun getExpiredBatchWithItem(date: Long): LiveData<List<BatchWithItem>> {
        return dao.getExpiredBatchWithItem(date)
    }

    fun getOutOfStockBatchWithItem(num: Int): LiveData<List<BatchWithItem>> {
        return dao.getOutOfStockBatchWithItem(num)
    }
}