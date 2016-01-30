package com.atguigu.yingyin12.netvideo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atguigu.yingyin12.R;

public abstract class BaActivity extends Activity implements View.OnClickListener {

    public FrameLayout fl_titlebar;
    private Button btn_base_left;
    private Button btn_base_right;
    private TextView tv_base_title;
    private LinearLayout ll_child_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ba);

        initView();
        setOnClickListener();
    }

    private void setOnClickListener() {
        btn_base_left.setOnClickListener(this);
        btn_base_right.setOnClickListener(this);
    }

    private void initView() {
        fl_titlebar = (FrameLayout)findViewById(R.id.fl_titlebar);
        btn_base_left = (Button)findViewById(R.id.btn_base_left);
        btn_base_right = (Button)findViewById(R.id.btn_base_right);
        tv_base_title = (TextView)findViewById(R.id.tv_base_title);
        ll_child_content = (LinearLayout)findViewById(R.id.ll_child_content);

        View view = getContentView();
        if(view != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ll_child_content.addView(view, params);
        }
    }

    //强制要求子类实现该方法，从而完成各自的特性
    public abstract View getContentView();

    //设置点击监听
    @Override
    public void onClick(View v) {
        if(v == btn_base_left) {
            clickLeftButton();
            //Toast.makeText(BaseActivity.this, "letf", Toast.LENGTH_SHORT).show();
        } else if(v == btn_base_right) {
            clickRightButton();
            //Toast.makeText(BaseActivity.this, "right", Toast.LENGTH_SHORT).show();
        }
    }

    //右边按钮的点击事件的具体实现，由孩子实现
    protected abstract void clickRightButton();

    //左边按钮的点击事件的具体实现，由孩子实现
    protected abstract void clickLeftButton();

    //左侧按钮是否隐藏
    public void setLetfButton(int visibility){btn_base_left.setVisibility(visibility); }

    //右侧按钮是否隐藏
    public void setRightButton(int visibility){
        btn_base_right.setVisibility(visibility);
    }

    //设置标题
    public void setTitle(String string){
        tv_base_title.setText(string);
    }
}
