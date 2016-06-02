package com.antilo0p.porkskin;

import android.app.Application;
import android.content.Context;

/**
 * Created by rigre on 31/05/2016.
 */
public class PorkSkinApp extends android.app.Application {
    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}
