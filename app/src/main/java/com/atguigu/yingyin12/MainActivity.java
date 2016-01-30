package com.atguigu.yingyin12;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.atguigu.yingyin12.adapter.MainPagerAdapter;
import com.atguigu.yingyin12.base.BasePager;
import com.atguigu.yingyin12.pager.ChannelePager;
import com.atguigu.yingyin12.pager.DownLoadPager;
import com.atguigu.yingyin12.pager.HomePager;
import com.atguigu.yingyin12.pager.MePager;
import com.atguigu.yingyin12.view.MainViewPager;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    /**
     * 框架ViewPager
     */
    private MainViewPager vp_main;
    /**
     * 主界面RadioGroup
     */
    private RadioGroup rg_main;
    /**
     * 盛放ViewPager子View的集合
     */
    private ArrayList<BasePager> pagers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        setListener();
    }

    /**
     * 用于设置监听
     */
    private void setListener() {
        rg_main.setOnCheckedChangeListener(new RadioOnCheckedChangeListener());
        vp_main.addOnPageChangeListener(new PagerOnPagerChangeListener());

    }

    /**
     * ViewPager的监听类
     */
    class PagerOnPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            pagers.get(position).initData();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
    /**
     * RadioGroup的监听类
     */
    class RadioOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_home:
                    vp_main.setCurrentItem(0,false);
                    break;
                case R.id.rb_channel:
                    vp_main.setCurrentItem(1,false);
                    break;
                case R.id.rb_download:
                    vp_main.setCurrentItem(2,false);
                    break;
                case R.id.rb_me:
                    vp_main.setCurrentItem(3,false);
                    break;
            }
        }
    }

    /**
     * 初始化视图组件
     */
    private void initView() {
        vp_main = (MainViewPager) findViewById(R.id.vp_main);
        rg_main = (RadioGroup) findViewById(R.id.rg_main);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        pagers = new ArrayList<>();
        HomePager homePage = new HomePager(this);
        pagers.add(homePage); //主界面
        pagers.add(new ChannelePager(this)); //频道
        pagers.add(new DownLoadPager(this)); //离线
        pagers.add(new MePager(this)); //我的影视大全
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(this, pagers);

        vp_main.setAdapter(mainPagerAdapter);
        //设置默认界面为主界面
        vp_main.setCurrentItem(0);
        homePage.initData();
        rg_main.check(0);
    }
}
