package com.example.myittaroostockinventorymanger;

import android.content.Context;

public class Application extends android.app.Application {

    private static Context INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public static Context getContext() {
        return INSTANCE;
    }
}
