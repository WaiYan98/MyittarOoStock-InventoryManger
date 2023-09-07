package com.example.myittaroostockinventorymanger.ui.item_name;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myittaroostockinventorymanger.event.Event;
import com.example.myittaroostockinventorymanger.data.entities.Stock;
import com.example.myittaroostockinventorymanger.data.repository.Repository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddAndRenameItemViewModel extends ViewModel {

    private Repository repository = new Repository();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<Event<Stock>> mutStock = new MutableLiveData<>();
    private MutableLiveData<Event<String>> message = new MutableLiveData<>();
    private String option;

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

    //Check the stock is empty or not
    public void onClickBtn(Stock stock, String option) {

        String stockName = stock.getName();

        if (!TextUtils.isEmpty(stockName)) {
            this.option = option;
            mutStock.setValue(new Event<>(stock));
        } else {
            message.setValue(new Event<>("Empty name cannot be saved"));
        }

    }

    //get the check stock to update or insert
    public LiveData<Event<Stock>> getStock() {
        return mutStock;
    }

    public LiveData<Event<String>> getMessage() {
        return message;
    }

    public String getOption() {
        return option;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
