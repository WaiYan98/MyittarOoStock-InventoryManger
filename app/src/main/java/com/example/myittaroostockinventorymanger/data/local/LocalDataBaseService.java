package com.example.myittaroostockinventorymanger.data.local;

import androidx.room.Room;

import com.example.myittaroostockinventorymanger.Application;

public class LocalDataBaseService {

    private static StockDataBase INSTANCE;

    public static StockDataBase getDataBase() {

        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(Application.getContext(), StockDataBase.class, "stock_db")
                    .addMigrations(StockDataBase.MIGRATION_1_2)
                    .build();
        }
        return INSTANCE;
    }
}
