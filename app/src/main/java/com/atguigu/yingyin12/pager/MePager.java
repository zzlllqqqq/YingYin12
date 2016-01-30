package com.atguigu.yingyin12.pager;

import android.app.Activity;
import android.view.View;

import com.atguigu.yingyin12.R;
import com.atguigu.yingyin12.base.BasePager;

/**
 * Created by admin on 2016/1/29.
 */
public class MePager extends BasePager {

    private View view;

    public MePager(Activity activity) {
        super(activity);
        view = View.inflate(activity, R.layout.me_layout, null);
        rootView.addView(view);
    }

    @Override
    public void initData() {
        super.initData();
    }
}
