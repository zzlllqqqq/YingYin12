package com.atguigu.yingyin12.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atguigu.yingyin12.R;

public abstract class BaseActivity extends Activity implements View.OnClickListener {

    private Button btn_left;
    private TextView tv_title;
    private Button btn_right;
    private LinearLayout ll_child_content;
    protected FrameLayout fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        setContentView(R.layout.activity_base);
        btn_left = (Button) findViewById(R.id.btn_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        btn_right = (Button) findViewById(R.id.btn_right);
        ll_child_content = (LinearLayout) findViewById(R.id.ll_child_content);
        fl = (FrameLayout) findViewById(R.id.fl);

        //添加子布局-子View
        View child = getChildView();//从子页面来的
        if (child != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, -1);
            ll_child_content.addView(child, params);
        }

        // 设置点击事件-并且要有孩子具体实现
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
    }

    /**
     * 让孩子实现自己特有的效果
     *
     * @return
     */
    protected abstract View getChildView();

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_left:// 左边按钮
                clickLeftButton();
                break;
            case R.id.btn_right:// 右边按钮
                clickRightButton();
                break;

            default:
                break;
        }



    }

//	private OnClickListener mOnClickListener = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			switch (v.getId()) {
//			case R.id.btn_left:// 左边按钮
//				clickLeftButton();
//				break;
//			case R.id.btn_right:// 右边按钮
//				clickRightButton();
//				break;
//
//			default:
//				break;
//			}
//
//		}
//	};

    /**
     * 左边按钮的点击事件
     */
    protected abstract void clickLeftButton();

    /**
     * 设置标题，丁计委
     *
     * @param text
     */
    protected void setTilte(String text) {
        tv_title.setText(text);
    }

    /**
     * 设置左边按钮是否隐藏和显示
     *
     * @param visible
     */
    protected void setLeftButton(int visible) {
        btn_left.setVisibility(visible);
    }

    /**
     * 设置右边按钮是否隐藏和显示
     *
     * @param visible
     */
    protected void setRightButton(int visible) {
        btn_right.setVisibility(visible);
    }

    /**
     * 右边按钮的点击事件
     */

    protected abstract void clickRightButton();

}

