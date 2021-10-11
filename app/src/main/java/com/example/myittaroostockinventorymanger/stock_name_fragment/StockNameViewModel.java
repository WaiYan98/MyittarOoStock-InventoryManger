package com.example.myittaroostockinventorymanger.stock_name_fragment;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myittaroostockinventorymanger.local.Stock;
import com.example.myittaroostockinventorymanger.repository.Repository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class StockNameViewModel extends ViewModel {

    private LiveData<List<Stock>> mutStockList;
    private Repository repository;

    public StockNameViewModel() {
        repository = new Repository();
    }

    public void onClickAddFab() {

    }

    public Completable insertStock(Stock stock) {
        return repository.insertStock(stock);
    }

    public LiveData<List<Stock>> getAllStockName() {

        if (mutStockList == null) {
            mutStockList = new MutableLiveData<>();
            loadStockName();
        }

        return mutStockList;
    }

    private void loadStockName() {
        mutStockList = repository.getAllStockName();
    }
}
