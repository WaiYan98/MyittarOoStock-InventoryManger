package com.example.myittaroostockinventorymanger.data.local;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myittaroostockinventorymanger.data.entities.Batch;
import com.example.myittaroostockinventorymanger.data.entities.Item;
import com.example.myittaroostockinventorymanger.data.entities.Transaction;

@androidx.room.Database(entities = {
        Item.class,
        Batch.class,
        Transaction.class},
        version = 1,
        exportSchema = false)
@TypeConverters({DateLongTypeConverter.class})
public abstract class Database extends RoomDatabase {

    public abstract Dao dao();

}
