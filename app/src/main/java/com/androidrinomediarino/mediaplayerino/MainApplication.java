package com.androidrinomediarino.mediaplayerino;

import android.app.Application;
import android.content.Context;

/**
 * Created by wilson on 2016-11-21.
 */

public class MainApplication extends Application {
    private static MainApplication instance;

    public static MainApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
