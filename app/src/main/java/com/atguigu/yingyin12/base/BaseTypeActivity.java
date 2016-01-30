package com.atguigu.yingyin12.base;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atguigu.yingyin12.R;

public class BaseTypeActivity extends Activity {
    /**
     * 总布局
     */
    public LinearLayout ll_base;
    /**
     * 返回主页面
     */
    public ImageButton ib_back;
    /**
     * 搜索按钮
     */
    public ImageButton ib_search;
    /**
     * 过滤按钮
     */
    public ImageButton ib_filter;

    /**
     * 类型名称
     */
    public TextView tv_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_type);
        ll_base = (LinearLayout) findViewById(R.id.ll_base);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_search = (ImageButton) findViewById(R.id.ib_search);
        ib_filter = (ImageButton) findViewById(R.id.ib_filter);
        tv_type = (TextView) findViewById(R.id.tv_type);
        initData();
    }

    public void initData(){}
}
