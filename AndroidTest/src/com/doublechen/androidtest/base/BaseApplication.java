package com.doublechen.androidtest.base;

import android.app.Application;

public class BaseApplication extends Application {
    public static BaseApplication mApp;

    @Override
    public void onCreate() {
        mApp = this;
        super.onCreate();
    }
}
