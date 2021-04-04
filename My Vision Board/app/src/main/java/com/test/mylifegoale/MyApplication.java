package com.test.mylifegoale;

import android.app.Application;

public class MyApplication extends Application {
    private static MyApplication mInstance;

    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        MyApplication myApplication;
        synchronized (MyApplication.class) {
            myApplication = mInstance;
        }
        return myApplication;
    }
}
