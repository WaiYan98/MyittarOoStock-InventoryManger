package com.example.myittaroostockinventorymanger.local;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

@androidx.room.Dao
public interface Dao {

    /**
     * @param stock return type is void for testing;
     *              Change completable later;
     */

    @Insert
    Completable insertStock(Stock stock);

    @Insert
    Completable insertBatch(Batch batch);

    @Insert
    Completable insertTransaction(Transaction transaction);

    @Query("SELECT * FROM Stock")
    LiveData<List<Stock>> getAllStock();

    @Query("SELECT * FROM Stock")
    LiveData<List<StockWithBatch>> getAllStockWithBatch();

    @Delete
    Completable deleteStock(Stock stock);

    @Update
    Completable updateStockName(Stock stock);

    @Query("SElECT name FROM Stock")
    LiveData<List<String>> getAllStockNames();

    @Query("SELECT stock_id FROM Stock WHERE Stock.name = :stockName")
    Observable<Long> findStockIdByName(String stockName);

    @Query("DELETE FROM Batch WHERE batch_id IN(:ids)")
    Completable deleteBatchesByIds(List<Long> ids);
}
