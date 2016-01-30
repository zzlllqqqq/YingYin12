package com.atguigu.yingyin12.base;

import android.app.Activity;
import android.view.View;

/**
 * Created by admin on 2016/1/30.
 */
public abstract class BaseDetailPager {

    /**
     * 传入上下文
     */
    public Activity activity;
    /**
     * 根布局
     */
    public View rootView;
    public BaseDetailPager(Activity activity) {
        this.activity = activity;
        rootView = initView();

        initData();
    }


    /**
     * 初始化根布局
     */
    public abstract View initView();

    /**
     * 留给子类实现的方法
     */

    public void initData() {

    }
}
