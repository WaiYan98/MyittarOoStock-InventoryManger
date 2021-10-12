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

    public LiveData<List<Stock>> getAllStockName() {
        return dao.getAllStockName();
    }

    public LiveData<List<StockWithBatch>> getAllStockWithBatch() {
        return dao.getAllStockWithBatch();
    }
}