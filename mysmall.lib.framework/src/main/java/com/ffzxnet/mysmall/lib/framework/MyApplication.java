package com.ffzxnet.mysmall.lib.framework;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * 创建者： feifan.pi 在 2017/4/10.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
