package com.atguigu.yingyin12;

import android.app.Application;

import com.atguigu.yingyin12.utils.VolleyManager;

import org.xutils.x;

/**
 * Created by admin on 2016/1/25.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        VolleyManager.init(this);
    }
}
