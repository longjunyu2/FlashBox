package com.aof.flashbox.application;

import android.app.Application;

public class FlashBoxApplication extends Application {

    private static FlashBoxApplication instance;

    public static FlashBoxApplication getInstance() {
        return instance;
    }

    public FlashBoxApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
