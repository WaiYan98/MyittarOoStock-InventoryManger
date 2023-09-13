package com.example.myittaroostockinventorymanger.data.local;

import androidx.room.Room;

import com.example.myittaroostockinventorymanger.Application;

public class LocalDataBaseService {

    private static Database INSTANCE;

    public static Database getDataBase() {

        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(Application.getContext(), Database.class, "stock_db")
                    .addMigrations(Database.MIGRATION_1_2)
                    .build();
        }
        return INSTANCE;
    }
}
