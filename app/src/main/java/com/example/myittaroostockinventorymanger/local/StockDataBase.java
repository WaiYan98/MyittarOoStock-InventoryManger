package com.example.myittaroostockinventorymanger.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {
        Stock.class,
        Batch.class,
        Transaction.class},
        version = 1)
@TypeConverters({DateLongTypeConverter.class})
public abstract class StockDataBase extends RoomDatabase {

    public abstract Dao dao();
}
