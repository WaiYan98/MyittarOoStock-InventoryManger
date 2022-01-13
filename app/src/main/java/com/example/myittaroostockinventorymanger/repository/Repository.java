package com.example.myittaroostockinventorymanger.repository;

import androidx.lifecycle.LiveData;

import com.example.myittaroostockinventorymanger.local.Batch;
import com.example.myittaroostockinventorymanger.local.Dao;
import com.example.myittaroostockinventorymanger.local.LocalDataBaseService;
import com.example.myittaroostockinventorymanger.local.Stock;
import com.example.myittaroostockinventorymanger.local.StockWithBatch;
import com.example.myittaroostockinventorymanger.local.Transaction;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class Repository {

    private Dao dao;

    public Repository() {
        this.dao = LocalDataBaseService.getDataBase()
                .dao();
    }

    public Completable insertStock(Stock stock) {
        return dao.insertStock(stock);
    }

    public Completable insertBatch(Batch batch) {
        return dao.insertBatch(batch);
    }

    public Completable insertTransaction(Transaction transaction) {
        return dao.insertTransaction(transaction);
    }

    public LiveData<List<Stock>> getAllStock() {
        return dao.getAllStock();
    }

    public LiveData<List<StockWithBatch>> getAllStockWithBatch() {
        return dao.getAllStockWithBatch();
    }

    public Completable deleteStock(Stock stock) {
        return dao.deleteStock(stock);
    }

    public Completable updateStockName(Stock stock) {
        return dao.updateStockName(stock);
    }

    public LiveData<List<String>> getAllStockNames() {
        return dao.getAllStockNames();
    }

    public Observable<Long> findStockIdByName(String stockName) {
        return dao.findStockIdByName(stockName);
    }

    public Completable deleteBatchesById(List<Long> ids) {
        return dao.deleteBatchesByIds(ids);
    }
}
