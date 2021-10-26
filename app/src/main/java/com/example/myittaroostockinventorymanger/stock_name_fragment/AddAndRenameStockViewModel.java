package com.example.myittaroostockinventorymanger.stock_name_fragment;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myittaroostockinventorymanger.event.Event;
import com.example.myittaroostockinventorymanger.local.Stock;

public class AddAndRenameStockViewModel extends ViewModel {

    private MutableLiveData<Event<Stock>> mutStock = new MutableLiveData<>();
    private MutableLiveData<Event<String>> message = new MutableLiveData<>();

    public void onClickSave(Stock stock) {

        String stockName = stock.getName();

        if (!TextUtils.isEmpty(stockName)) {
            mutStock.setValue(new Event<>(stock));
        } else {
            message.setValue(new Event<>("Empty name cannot be saved"));
        }

    }

    public LiveData<Event<Stock>> getStock() {
        return mutStock;
    }

    public LiveData<Event<String>> getMessage() {
        return message;
    }
}
