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
        version = 3,
        exportSchema = false)
@TypeConverters({DateLongTypeConverter.class})
public abstract class Database extends RoomDatabase {

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE 'stock' ADD COLUMN 'isSelected' INTEGER NOT NULL DEFAULT(0)");
        }
    };


    public abstract Dao dao();

}
