package com.atguigu.yingyin12.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.atguigu.yingyin12.R;

/**
 * Created by admin on 2016/1/29.
 */
public class BasePager {
    /**
     * 传入上下文
     */
    public Activity activity;
    /**
     * 根布局
     */
    public FrameLayout rootView;
    public BasePager(Activity activity) {
        this.activity = activity;
        rootView = initView();
    }

    /**
     * 初始化根布局
     */
    private FrameLayout initView() {
        FrameLayout view = (FrameLayout) View.inflate(activity, R.layout.root_view, null);
        return view;
    }

    /**
     * 留给子类实现的方法
     */

    public void initData() {

    }
}

