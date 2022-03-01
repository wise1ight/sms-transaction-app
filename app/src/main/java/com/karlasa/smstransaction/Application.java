package com.karlasa.smstransaction;

import android.content.Context;

/**
 * Created by kuvh on 2017-03-15.
 */

public class Application extends android.app.Application {

    private static Application instance;

    public static Context getContext() { return instance; }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
