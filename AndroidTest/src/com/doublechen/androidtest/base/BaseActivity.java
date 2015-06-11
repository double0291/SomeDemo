package com.doublechen.androidtest.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {
    public BaseApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = BaseApplication.mApp;
    }
}
