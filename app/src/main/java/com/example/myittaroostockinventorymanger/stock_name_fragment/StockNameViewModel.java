package com.example.myittaroostockinventorymanger.stock_name_fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myittaroostockinventorymanger.event.Event;
import com.example.myittaroostockinventorymanger.local.Stock;
import com.example.myittaroostockinventorymanger.repository.Repository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StockNameViewModel extends ViewModel {

    private LiveData<List<Stock>> mutStockList;
    private MutableLiveData<Event<String>> message;
    private MutableLiveData<Boolean> isLoading;
    private Repository repository;
    private Disposable disposable;

    public StockNameViewModel() {
        repository = new Repository();
        message = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
    }

    public void insertStock(Stock stock) {
        disposable = repository.insertStock(stock)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    message.setValue(new Event<>("Added"));
                }, error -> {
                    message.setValue(new Event<>("unexpected error"));
                });
    }

    public LiveData<List<Stock>> getAllStockName() {

        if (mutStockList == null) {
            mutStockList = new MutableLiveData<>();
            loadStockName();
        }

        return mutStockList;
    }

    public void loadStockName() {
        isLoading.setValue(true);
        mutStockList = repository.getAllStockName();
        isLoading.setValue(false);
        message.setValue(new Event<>("updated"));
    }

    public LiveData<Event<String>> getMessage() {
        return message;
    }

    public LiveData<Boolean> getLoading(){
        return isLoading;
    }
}
