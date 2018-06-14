package com.sw.hc.fit;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;


public class App extends MultiDexApplication {


    private static App sInstance;
    private static String appVersion;


    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

    }


    public static Typeface getCustomFont(Context context) {
        return Typeface.createFromAsset(context.getApplicationContext().getAssets(), "fonts/CoconPro-Regular.ttf");
    }

    public static App getInstance() {
        return sInstance;
    }


    public static String getAppVersion() {

        appVersion = "";

        int appCode  = 0;
        try {
            appVersion = sInstance.getPackageManager().getPackageInfo(sInstance.getPackageName(),
                    0).versionName;
            appCode = sInstance.getPackageManager().getPackageInfo(sInstance.getPackageName(),
                    0).versionCode;
        } catch (NameNotFoundException e) {

            e.printStackTrace();

        }

        appVersion = "" + appVersion + " (" + appCode + ")";

        return appVersion;
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }


}