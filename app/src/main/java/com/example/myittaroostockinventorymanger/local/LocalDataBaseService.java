package com.example.myittaroostockinventorymanger.local;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myittaroostockinventorymanger.Application;

public class LocalDataBaseService {

    private static StockDataBase INSTANCE;

    public static StockDataBase getDataBase() {

        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(Application.getContext(), StockDataBase.class, "stock_db")
                    .build();
        }
        return INSTANCE;
    }
}
