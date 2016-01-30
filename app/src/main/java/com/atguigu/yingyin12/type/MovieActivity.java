package com.atguigu.yingyin12.type;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.atguigu.yingyin12.R;
import com.atguigu.yingyin12.base.BaseTypeActivity;
import com.atguigu.yingyin12.domain.TopLabel;
import com.atguigu.yingyin12.pager.detailpager.MovieListPager;
import com.atguigu.yingyin12.utils.ApiUtils;
import com.atguigu.yingyin12.utils.ConstantUtiles;
import com.atguigu.yingyin12.utils.VolleyManager;
import com.google.gson.Gson;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * 电影列表
 */
public class MovieActivity extends BaseTypeActivity {

    private static final String TAG = MovieActivity.class.getSimpleName();
    //
    @ViewInject(R.id.movie_tablayout)
    private TabLayout movie_tablayout;

    @ViewInject(R.id.viewpager_movie)
    private ViewPager viewpager_movie;

    @ViewInject(R.id.fl_loading)
    private FrameLayout fl_loading;

    @ViewInject(R.id.iv_loading_circle)
    private ImageView iv_loading_circle;

    @ViewInject(R.id.ib_movie_more)
    private ImageButton ib_movie_more;

    @ViewInject(R.id.ll_movie_pager)
    private LinearLayout ll_movie_pager;

    /**
     * 顶部标签
     */
    private List<TopLabel.InfoEntity.CategoryRecommendEntity> category_recommend;

    /**
     * 影片类型、年份、地域
     */
    public static List<TopLabel.InfoEntity.CategoryEntity> category;
    public static List<TopLabel.InfoEntity.YearEntity> year;
    public static List<TopLabel.InfoEntity.DistrictEntity> district;

    /**
     * 标签适配器
     */
    private MyTabsFromPagerAdapter adapter;


    private Handler handler = new Handler();
    /**
     * 电视、电影、动漫、综艺
     */
    private String[] ds = {"ds", ApiUtils.TAB_DS_URL,ApiUtils.MOVIE_B_URI,ApiUtils.TV_SOURCE_URL};
    private String[] dy = {"dy",ApiUtils.TAB_DY_URI,ApiUtils.MOVIE_B_URI,ApiUtils.TV_SOURCE_URL};
    private String[] dm = {"dm",ApiUtils.TAB_DM_URL,ApiUtils.MOVIE_B_URI,ApiUtils.TV_SOURCE_URL};
    private String[] zy = {"zy",ApiUtils.TAB_DM_URL,ApiUtils.MOVIE_B_URI,ApiUtils.TV_SOURCE_URL};

    private String[] url;

    public MovieActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(MovieActivity.this, R.layout.activity_movie, null);
        x.view().inject(this, view);
        Log.e("TAG", "wgoawohgowghgg");
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.circle_anim);
        animation.setInterpolator(new LinearInterpolator());
        iv_loading_circle.startAnimation(animation);
        ll_base.addView(view);
    }

    @Override
    public void initData() {
        super.initData();

        Log.e("TAG","initData()");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 * 判断数据
                 */
                int i = getIntent().getIntExtra("url", -1);
                if (i == ConstantUtiles.DY) {
                    url = dy;
                } else if (i == ConstantUtiles.DS) {
                    url = ds;
                } else if (i == ConstantUtiles.DM) {
                    url = dm;
                } else if (i == ConstantUtiles.ZY) {
                    url = zy;
                }
                Log.e(TAG, url[0]);
                getDataFromNet();
            }
        }, 2000);

    }

    /**
     * 联网请求数据
     * 标签
     */
    private void getDataFromNet() {

        /**
         * 加载动画和主页面的设置
         */

        fl_loading.setVisibility(View.VISIBLE);
        viewpager_movie.setVisibility(View.GONE);
        ll_movie_pager.setVisibility(View.GONE);

        RequestQueue queue = VolleyManager.getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.GET, url[1], new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //联网请求成功  -- 存储
                Log.e("TEG", "联网成功sdghdfjj");
                isNet = true;
                progressJson(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, "联网错误：" + volleyError);
                isNet = false;
                isNetState();
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
     * 联网状态
     */
    private boolean isNet = false;

    /**
     * 联网失败
     */
    private void isNetState() {

        fl_loading.clearAnimation();
        fl_loading.setVisibility(View.GONE);
        viewpager_movie.setVisibility(View.VISIBLE);
        ll_movie_pager.setVisibility(View.VISIBLE);

        viewpager_movie.removeAllViews();
        viewpager_movie.setAdapter(null);

        viewpager_movie.setAdapter(new MyTabsFromPagerAdapter());
        // 关联ViewPager
        movie_tablayout.setupWithViewPager(viewpager_movie);

    }

    /**
     * 解析json
     *
     * @param json
     */
    private void progressJson(String json) {

        /**
         * 取消动画，主页面显示
         */
        fl_loading.clearAnimation();
        fl_loading.setVisibility(View.GONE);
        viewpager_movie.setVisibility(View.VISIBLE);
        ll_movie_pager.setVisibility(View.VISIBLE);

        Log.e(TAG, "1-1");
        Gson gson = new Gson();

        if (json != null) {
            topLabel = gson.fromJson(json, TopLabel.class);
            /**
             *
             */
            category = topLabel.getInfo().getCategory();
            year = topLabel.getInfo().getYear();
            district = topLabel.getInfo().getDistrict();

            category_recommend = topLabel.getInfo().getCategory_recommend();
            if (category_recommend != null) {
                adapter = new MyTabsFromPagerAdapter();
                viewpager_movie.setAdapter(adapter);
                movie_tablayout.setupWithViewPager(viewpager_movie);

                ib_movie_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewpager_movie.setCurrentItem(viewpager_movie.getCurrentItem() + 1);
                    }
                });
            } else {
                Log.e(TAG, "category_recommend == null");
            }

        }

    }

    private TopLabel topLabel;
    /**
     * 标签页适配器
     */
    class MyTabsFromPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return category_recommend.size() + 1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            if(isNet){
                if (position == 1) {
                    MovieListPager movieListPager = new MovieListPager(MovieActivity.this, dy, ConstantUtiles.JX);
                    view = movieListPager.rootView;
                } else {
                    MovieListPager movieListPager;
                    movieListPager = new MovieListPager(MovieActivity.this, dy, position);
                    view = movieListPager.rootView;
                }
            } else {
                view = View.inflate(MovieActivity.this, R.layout.discover_fail, null);
                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getDataFromNet();
                    }
                });
            }

            container.addView(view);
            return view;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if (position == 0) {
                return "筛选";
            }

            if ("dm".equals(url[0])) {
                return " " + category_recommend.get(position - 1).getName() + "   ";
            } else if ("zy".equals(url[0])) {
                return " " + category_recommend.get(position - 1).getName() + "   ";
            }

            return category_recommend.get(position - 1).getName();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}

