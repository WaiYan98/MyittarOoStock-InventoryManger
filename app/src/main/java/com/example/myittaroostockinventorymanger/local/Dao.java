package com.example.myittaroostockinventorymanger.local;

import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@androidx.room.Dao
public interface Dao {

    /**
     * @param stock return type is void for testing;
     *              Change completable later;
     */

    @Insert
    void insertStock(Stock... stock);

    @Insert
    void insertBatch(Batch... batch);

    @Insert
    void insertTransaction(Transaction... transaction);

    @Query("SELECT * FROM Stock")
    List<Stock> getAllStockName();

    @Query("SELECT * FROM Stock")
    List<StockWithBatch> getAllStockWithBatch();

}
