package com.t_robop.yuusuke.a01_spica_android;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class MyApplication extends Application {
    private static MyApplication instance = null;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Fabric.with(this, new Crashlytics());
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)  // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);

    }

    public static MyApplication getInstance() {
        return instance;
    }
}