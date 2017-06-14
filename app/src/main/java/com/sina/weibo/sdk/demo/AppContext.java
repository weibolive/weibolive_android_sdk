package com.sina.weibo.sdk.demo;

import android.app.Application;
import android.content.Context;

/**
 * Created by bj-m-206333a on 2017/6/12.
 */

public class AppContext extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
//        initData();
        context = this;
    }

    public static Context getAppContext() {
        return context;
    }


}
