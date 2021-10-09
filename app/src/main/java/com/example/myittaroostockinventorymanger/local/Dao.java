package com.example.myittaroostockinventorymanger.local;

import androidx.room.Insert;

import io.reactivex.Completable;

@androidx.room.Dao
public interface Dao {

    /**
     * @param product return type is void for testing;
     *                Change completable later;
     */

    @Insert
    void insertProduct(Product... product);

    @Insert
    void insertBatch(Batch... batch);

    @Insert
    void insertTransaction(Transaction... transaction);

}
