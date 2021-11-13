
package com.example.myittaroostockinventorymanger.stock_name_fragment;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myittaroostockinventorymanger.event.Event;
import com.example.myittaroostockinventorymanger.local.Stock;
import com.example.myittaroostockinventorymanger.repository.Repository;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StockNameViewModel extends ViewModel {

    private LiveData<List<Stock>> mutStockList;
    private MutableLiveData<Event<String>> message;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<List<Stock>> mutFilterNames;
    private Repository repository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public StockNameViewModel() {
        repository = new Repository();
        message = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
        mutFilterNames = new MutableLiveData<>();
    }

    public void insertStock(Stock stock) {
        Disposable disposable = repository.insertStock(stock)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    message.setValue(new Event<>("Added"));
                }, error -> {
                    message.setValue(new Event<>("unexpected error"));
                });
        compositeDisposable.add(disposable);
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
        mutStockList = repository.getAllStock();
        isLoading.setValue(false);
        message.setValue(new Event<>("updated"));
    }

    public LiveData<Event<String>> getMessage() {
        return message;
    }

    public LiveData<Boolean> getLoading() {
        return isLoading;
    }

    public void deleteStock(Stock stock) {
        Disposable disposable = repository.deleteStock(stock)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    message.setValue(new Event<>("Deleted"));
                }, error -> {
                    message.setValue(new Event<>("Cannot delete"));
                });
        compositeDisposable.add(disposable);
    }

    public void updateStockName(Stock stock) {
        Disposable disposable = repository.updateStockName(stock)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    message.setValue(new Event<>("Renamed"));
                }, error -> {
                    message.setValue(new Event<>("Cannot Renamed"));
                });
        compositeDisposable.add(disposable);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void filterName(List<Stock> stockList, String text) {
        List<Stock> resultList = stockList.stream()
                .filter(stock -> stock.getName().toUpperCase().startsWith(text.toUpperCase()))
                .collect(Collectors.toList());
        mutFilterNames.setValue(resultList);
    }

    public LiveData<List<Stock>> getFilterNames() {
        return mutFilterNames;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
