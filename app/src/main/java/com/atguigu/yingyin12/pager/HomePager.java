package com.atguigu.yingyin12.pager;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.atguigu.yingyin12.R;
import com.atguigu.yingyin12.adapter.HomeListAdapter;
import com.atguigu.yingyin12.base.BasePager;
import com.atguigu.yingyin12.domain.HomeDataBean;
import com.atguigu.yingyin12.search.SearchActivity;
import com.atguigu.yingyin12.text.TestWebViewActivity;
import com.atguigu.yingyin12.type.MovieActivity;
import com.atguigu.yingyin12.utils.ApiUtils;
import com.atguigu.yingyin12.utils.CacheUtils;
import com.atguigu.yingyin12.utils.ConstantUtiles;
import com.atguigu.yingyin12.utils.ImageUtils;
import com.atguigu.yingyin12.utils.VolleyManager;
import com.atguigu.yingyin12.view.NoScrollGridView;
import com.google.gson.Gson;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by admin on 2016/1/30.
 */
public class HomePager extends BasePager implements View.OnClickListener {

    public HomePager(Activity activity) {
        super(activity);
        initHomeView();
    }


    private static final int AUTO = 1;
    /**
     * 主布局
     */
    private LinearLayout view;
    /**
     * 全网搜索
     */
    private TextView tv_home_search;
    /**
     * json数据对应的实体类
     */
    private HomeDataBean dataBean;
    /**
     * 首界面的布局
     */
    private ListView lv_home;
    /**
     * 首界面ListView的adapter
     */
    private HomeListAdapter adapter;
    /**
     * 轮播图的数据集合
     */
    private List<HomeDataBean.InfoEntity.Focus> focus;

    /**
     * listView底部应用推荐的数据对象
     */
    private HomeDataBean.InfoEntity.Recommend recommendFooter;
    /**
     * 头部的Viewpager
     */
    private ViewPager vp_home_header;
    /**
     * 底部布局的ViewPager
     */
    private ViewPager vp_home_footer;
    /**
     * 顶部轮播图的点
     */
    private LinearLayout ll_tab_detail_point;
    /**
     * 底部ViewPager的点
     */
    private LinearLayout ll_home_footer_point;
    /**
     * 顶部轮播图红点的位置
     */
    private int prePoint;

    private ImageOptions imageOptions;

    /**
     * 轮播图的描述
     */
    /**
     * 标识是否已经发送轮播图自动播放的消息
     */
    private boolean isAuto;
    /**
     * 标识底部ViewPager的点的从前的位置
     */
    private int preFooterPoint;
    /**
     * 获取保存的首页的Json数据
     */
    private String value;
    /**
     * 下拉刷新的控件
     */
    private SwipeRefreshLayout srl_home;
    /**
     * handler
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AUTO:
                    vp_home_header.setCurrentItem((vp_home_header.getCurrentItem() + 1) % focus.size());
                    isAuto = true;
                    handler.removeMessages(AUTO);
                    handler.sendEmptyMessageDelayed(AUTO, 3000);
                    break;
                default:
                    break;
            }
        }
    };
    private TextView tv_tab_detail_title;
    /**
     * 标识头部的ViewPager是否是拖拽引起的状态改变
     */
    private boolean isDragging;
    /**
     * 顶部ViewPager的状态改变的监听
     */
    private ViewPager.OnPageChangeListener headerOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position != prePoint) {
                ll_tab_detail_point.getChildAt(position).setEnabled(true);
                ll_tab_detail_point.getChildAt(prePoint).setEnabled(false);
                prePoint = position;
                tv_tab_detail_title.setText(focus.get(prePoint).getTitle());
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                handler.removeMessages(AUTO);
                isDragging = true;
            } else {
                isDragging = false;
                handler.sendEmptyMessageDelayed(AUTO, 3000);
            }
            enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);
        }
    };

    /**
     * @param enable
     */
    protected void enableDisableSwipeRefresh(boolean enable) {
        if (srl_home != null) {
            srl_home.setEnabled(enable);
        }
    }

    /**
     * 底部Viewpager的监听
     */
    private ViewPager.OnPageChangeListener footerOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position != preFooterPoint) {
                ll_home_footer_point.getChildAt(position).setEnabled(true);
                ll_home_footer_point.getChildAt(preFooterPoint).setEnabled(false);
                preFooterPoint = position;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    /**
     * 下拉刷新的监听
     */
    private SwipeRefreshLayout.OnRefreshListener homeOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            getDataFromNet();
        }
    };
    private RadioGroup rg_home_header;
    /**
     * 头部的5个RadioButton在的RadioGroup的监听
     */
    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            Intent intent = new Intent(activity, MovieActivity.class);
            switch (checkedId) {
                case R.id.rb_home_movie:
                    //电影
                    intent.putExtra("url", ConstantUtiles.DY);
                    activity.startActivity(intent);
                    break;
                case R.id.rb_home_teleplay:
                    //电视
                    intent.putExtra("url", ConstantUtiles.DS);
                    activity.startActivity(intent);
                    break;
                case R.id.rb_home_variety:
                    //综艺
                    intent.putExtra("url", ConstantUtiles.ZY);
                    activity.startActivity(intent);
                    break;
                case R.id.rb_home_anime:
                    intent.putExtra("url", ConstantUtiles.DM);
                    activity.startActivity(intent);
                    break;
                case R.id.rb_home_zhibo:
                    break;
                default:
                    break;
            }
            activity.startActivity(intent);

        }
    };

//        public HomePager(Activity activity) {
//            super(activity);
//            initHomeView();
//        }

    /**
     * 初始化布局文件
     */
    private void initHomeView() {
        //加载页面主布局
        view = (LinearLayout) View.inflate(activity, R.layout.home_layout, null);
        tv_home_search = (TextView) view.findViewById(R.id.tv_home_search);
        lv_home = (ListView) view.findViewById(R.id.lv_home);
        srl_home = (SwipeRefreshLayout) view.findViewById(R.id.srl_home);

        //加载头部布局
        View viewHeader = View.inflate(activity, R.layout.home_header, null);
        vp_home_header = (ViewPager) viewHeader.findViewById(R.id.vp_home_header);
        tv_tab_detail_title = (TextView) viewHeader.findViewById(R.id.tv_tab_detail_title);
        ll_tab_detail_point = (LinearLayout) viewHeader.findViewById(R.id.ll_tab_detail_point);
        rg_home_header = (RadioGroup) viewHeader.findViewById(R.id.rg_home_header);
        //加载底部布局
        View viewFooter = View.inflate(activity, R.layout.home_footer, null);
        vp_home_footer = (ViewPager) viewFooter.findViewById(R.id.vp_home_footer);
        ll_home_footer_point = (LinearLayout) viewFooter.findViewById(R.id.ll_home_footer_point);
        lv_home.addHeaderView(viewHeader);
        lv_home.addFooterView(viewFooter);
        rootView.addView(this.view);

    }


    /**
     * 程序入口
     */
    @Override
    public void initData() {
        super.initData();
        value = CacheUtils.getInstance(activity).getStringValue(ConstantUtiles.HOME_KEY);
        if (!"".equals(value)) {
            parseJson(value);
        }
        getDataFromNet();

    }


    /**
     * 从网络获取数据
     */
    private void getDataFromNet() {
        RequestQueue queue = VolleyManager.getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.GET, ApiUtils.HOME_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseJson(response);
                srl_home.setRefreshing(false);
                CacheUtils.getInstance(activity).saveString(ConstantUtiles.HOME_KEY, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.toString());
                srl_home.setRefreshing(false);
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String data = new String(response.data, "UTF-8");
                    return Response.success(data, HttpHeaderParser.parseCacheHeaders(response));

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return super.parseNetworkResponse(response);
            }
        };
        queue.add(request);
    }

    /**
     * 解析json数据
     *
     * @param response
     */
    private void parseJson(String response) {
        Gson gson = new Gson();
        dataBean = gson.fromJson(response, HomeDataBean.class);
        focus = dataBean.getInfo().getFocus();
        List<HomeDataBean.InfoEntity.Recommend> recommends = dataBean.getInfo().getRecommend();
        recommendFooter = recommends.get(recommends.size() - 1);
        //顶部轮播图的圆点
        addHeaderPoint();
        //底部Viewpager的圆点
        addFooterPoint();

        vp_home_header.setAdapter(new HeaderPagerAdapter());
        vp_home_footer.setAdapter(new FooterPagerAdapter());
        tv_tab_detail_title.setText(focus.get(prePoint).getTitle());
        //发送自动循环播放的消息
        if (!isAuto) {
            handler.sendEmptyMessageDelayed(AUTO, 3000);
        }
        tv_home_search.setOnClickListener(this);
        vp_home_header.addOnPageChangeListener(headerOnPageChangeListener);
        vp_home_footer.addOnPageChangeListener(footerOnPageChangeListener);
        //下拉刷新的监听
        srl_home.setOnRefreshListener(homeOnRefreshListener);
        //radioGroup的监听
        rg_home_header.setOnCheckedChangeListener(mOnCheckedChangeListener);

        // 顶部刷新的样式
        srl_home.setColorScheme(android.R.color.holo_red_light, android.R.color.holo_green_light,
                android.R.color.holo_blue_bright, android.R.color.holo_orange_light);
        if (adapter == null) {
            adapter = new HomeListAdapter(activity, dataBean);
        }
        lv_home.setAdapter(adapter);

    }

    /**
     * 添加底部圆点
     */
    private void addFooterPoint() {
        ll_home_footer_point.removeAllViews();
        for (int i = 0; i < recommendFooter.getList().size() / 8; i++) {
            ImageView imageView = new ImageView(activity);
            //点的个数
            imageView.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(7), DensityUtil.dip2px(7));
            params.leftMargin = DensityUtil.dip2px(4);
            params.rightMargin = DensityUtil.dip2px(4);
            preFooterPoint = 0;
            if (i == preFooterPoint) {
                imageView.setEnabled(true);
            } else {
                imageView.setEnabled(false);
            }
            imageView.setLayoutParams(params);
            ll_home_footer_point.addView(imageView);
        }
    }

    /**
     * 添加顶部轮播图的圆点
     */
    private void addHeaderPoint() {
        ll_tab_detail_point.removeAllViews();
        for (int i = 0; i < focus.size(); i++) {
            ImageView imageView = new ImageView(activity);
            //点的个数
            imageView.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(7), DensityUtil.dip2px(7));
            params.leftMargin = DensityUtil.dip2px(4);
            params.rightMargin = DensityUtil.dip2px(4);
            prePoint = 0;
            if (i == prePoint) {
                imageView.setEnabled(true);
            } else {
                imageView.setEnabled(false);
            }
            imageView.setLayoutParams(params);
            ll_tab_detail_point.addView(imageView);
        }
    }


    /**
     * 标题栏按钮的点击回调
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_home_search:
                Intent intent = new Intent(activity, SearchActivity.class);
                activity.startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 头部VIewPager适配器
     */
    class HeaderPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return focus.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View view = View.inflate(activity, R.layout.home_top_pic, null);
            ImageView iv_home_top_pic = (ImageView) view.findViewById(R.id.iv_home_top_pic);
            ImageView iv_home_top_tag = (ImageView) view.findViewById(R.id.iv_home_top_tag);
            ImageUtils.loaderImager(iv_home_top_pic, focus.get(position).getPic());
            addTag(iv_home_top_tag, focus.get(position).getTag_name());
            container.addView(view);
            iv_home_top_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, TestWebViewActivity.class);
                    intent.putExtra(ConstantUtiles.HTML, focus.get(position).getUrl());
                    activity.startActivity(intent);
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


    }


    /**
     * 底部VIewPager适配器
     */
    class FooterPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(activity, R.layout.item_grid_app_view, null);
            NoScrollGridView gridView = (NoScrollGridView) view.findViewById(R.id.gv_app_detail);
            List<HomeDataBean.InfoEntity.Recommend.Deatils> deatilses;
            if (position == 0) {
                deatilses = recommendFooter.getList().subList(0, 8);
            } else {
                deatilses = recommendFooter.getList().subList(8, recommendFooter.getList().size());
            }
            gridView.setAdapter(new GridViewAppAdapter(deatilses));
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


    }

    /**
     * 应用推荐GridView的Adapter
     */
    class GridViewAppAdapter extends BaseAdapter {


        private final List<HomeDataBean.InfoEntity.Recommend.Deatils> deatilses;

        public GridViewAppAdapter(List<HomeDataBean.InfoEntity.Recommend.Deatils> deatilses) {
            this.deatilses = deatilses;
        }

        @Override
        public int getCount() {
            return deatilses.size();
        }

        @Override
        public Object getItem(int position) {
            return deatilses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GridViewAppHolder holder;
            if (convertView == null) {
                holder = new GridViewAppHolder();
                convertView = View.inflate(activity, R.layout.item_grid_app, null);
                holder.iv_grid_item_app_icon = (ImageView) convertView.findViewById(R.id.iv_grid_item_app_icon);
                holder.tv_grid_item_app_title = (TextView) convertView.findViewById(R.id.tv_grid_item_app_title);
                convertView.setTag(holder);
            } else {
                holder = (GridViewAppHolder) convertView.getTag();
            }
            ImageUtils.loaderImager(holder.iv_grid_item_app_icon, deatilses.get(position).getPic());

            holder.tv_grid_item_app_title.setText(deatilses.get(position).getTitle());

            return convertView;
        }
    }

    static class GridViewAppHolder {
        ImageView iv_grid_item_app_icon;
        TextView tv_grid_item_app_title;
    }


    /**
     * 添加tag图标
     *
     * @param imageView
     * @param tag_name
     */

    private void addTag(ImageView imageView, String tag_name) {
        if (!"0".equals(tag_name)) {
            imageView.setVisibility(View.VISIBLE);
            if ("rq".equals(tag_name)) {
                imageView.setImageResource(R.drawable.ic_tag_renqi);
            } else if ("dsj".equals(tag_name)) {
                imageView.setImageResource(R.drawable.ic_tag_dianshiju);
            } else if ("dy".equals(tag_name)) {
                imageView.setImageResource(R.drawable.ic_tag_dianying);
            } else if ("zt".equals(tag_name)) {
                imageView.setImageResource(R.drawable.ic_tag_zhuanti);
            } else if ("zy".equals(tag_name)) {
                imageView.setImageResource(R.drawable.ic_tag_zongyi);
            } else if ("sb".equals(tag_name)) {
                imageView.setImageResource(R.drawable.ic_tag_shoubo);
            } else if ("xf".equals(tag_name)) {
                imageView.setImageResource(R.drawable.ic_tag_xinfan);
            } else if ("xj".equals(tag_name)) {
                imageView.setImageResource(R.drawable.ic_tag_xinju);
            } else if ("yg".equals(tag_name)) {
                imageView.setImageResource(R.drawable.ic_tag_yugao);
            } else if ("gq".equals(tag_name)) {
                imageView.setImageResource(R.drawable.ic_tag_gaoqing);
            } else if ("db".equals(tag_name)) {
                imageView.setImageResource(R.drawable.ic_tag_dubo);
            } else if ("dm".equals(tag_name)) {
                imageView.setImageResource(R.drawable.ic_tag_dongman);
            }
        } else {
            imageView.setVisibility(View.GONE);
        }


    }
}

