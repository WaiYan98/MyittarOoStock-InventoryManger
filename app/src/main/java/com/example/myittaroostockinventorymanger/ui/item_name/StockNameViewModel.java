
package com.example.myittaroostockinventorymanger.ui.item_name;

import android.os.Build;
import android.view.ActionMode;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myittaroostockinventorymanger.event.Event;
import com.example.myittaroostockinventorymanger.data.entities.Stock;
import com.example.myittaroostockinventorymanger.data.repository.Repository;

import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class StockNameViewModel extends ViewModel {

    private LiveData<List<Stock>> mutStockList;
    private MutableLiveData<Event<String>> message;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<List<Stock>> mutFilterNames;
    private MutableLiveData<String> contextualActionBarTitle;
    private Repository repository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<Boolean> isShowRenameButton;
    private MutableLiveData<Boolean> isValidDelete;

    public StockNameViewModel() {
        repository = new Repository();
        message = new MutableLiveData<>();
        isLoading = new MutableLiveData<>(false);
        mutFilterNames = new MutableLiveData<>();
        contextualActionBarTitle = new MutableLiveData<>();
        isShowRenameButton = new MutableLiveData<>();
        isValidDelete = new MutableLiveData<>();
    }

    public LiveData<List<Stock>> getAllStocks() {

        if (mutStockList == null) {
            mutStockList = new MutableLiveData<>();
            loadStocks();
        }

        return mutStockList;
    }

    public void loadStocks() {
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

    //for SearchView to find Name
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

    public void setContextualActionBarTitle(int num) {
        if (num > 1) {
            contextualActionBarTitle.setValue(num + " Items Selected");
        } else {
            contextualActionBarTitle.setValue(num + " Item Selected");
        }
    }

    public LiveData<String> getContextualActionBarTitle() {
        return contextualActionBarTitle;
    }

    public void showAndHideRenameButton(int num) {
        isShowRenameButton.setValue(num < 2);
    }

    public LiveData<Boolean> isShowRenameButton() {
        return isShowRenameButton;
    }

    public void deleteStocks(List<Long> selectStockIdList, ActionMode actionMode) {

        repository.deleteStocksByIds(selectStockIdList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    message.setValue(new Event("Deleted " + selectStockIdList.size() + " items"));
                    actionMode.finish();
                }, error -> {
                    error.printStackTrace();
                });
    }

    public void isValidDelete(List<Long> selectStockIdList) {
        if (!selectStockIdList.isEmpty()) {
            isValidDelete.setValue(true);
        } else {
            isValidDelete.setValue(false);
            message.setValue(new Event("Please select item"));
        }
    }

    public LiveData<Boolean> getIsValidDelete() {
        return isValidDelete;
    }
}
