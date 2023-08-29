package com.example.myittaroostockinventorymanger.ui.item_name;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myittaroostockinventorymanger.event.Event;
import com.example.myittaroostockinventorymanger.data.entities.Stock;
import com.example.myittaroostockinventorymanger.data.repository.Repository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ConfirmDialogFragmentViewModel extends ViewModel {

    private MutableLiveData<Event<String>> message = new MutableLiveData<>();

    Repository repository = new Repository();
    private Disposable disposable;

    public void deleteStock(Stock stock) {
        disposable = repository.deleteStock(stock)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    message.setValue(new Event<>("Deleted"));
                }, error -> {
                    message.setValue(new Event<>("Cannot delete"));
                });
    }

    public MutableLiveData<Event<String>> getMessage() {
        return message;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}
