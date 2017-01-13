package com.pl.universalcopy;

import android.app.Application;

/**
 * Created by penglu on 2016/10/26.
 */

public class UCXPApp extends Application {
    private static UCXPApp instance;

    public static UCXPApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
