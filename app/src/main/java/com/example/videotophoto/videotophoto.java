package com.example.videotophoto;

import android.app.Application;
import android.content.Context;

public class videotophoto extends Application {
    private static videotophoto appContext;

    public videotophoto() { appContext = this; }

    public static Context getAppContext() { return appContext; }

}
