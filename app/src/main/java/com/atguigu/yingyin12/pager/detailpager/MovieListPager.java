package com.atguigu.yingyin12.pager.detailpager;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.atguigu.yingyin12.R;
import com.atguigu.yingyin12.activity.MovieDetailsActivity;
import com.atguigu.yingyin12.adapter.RecycleAdapter;
import com.atguigu.yingyin12.base.BaseDetailPager;
import com.atguigu.yingyin12.domain.MovieSource;
import com.atguigu.yingyin12.domain.TopLabel;
import com.atguigu.yingyin12.domain.ZYChannerBean;
import com.atguigu.yingyin12.type.MovieActivity;
import com.atguigu.yingyin12.utils.ApiUtils;
import com.atguigu.yingyin12.utils.CacheUtils;
import com.atguigu.yingyin12.utils.ConstantUtiles;
import com.atguigu.yingyin12.utils.ImageUtils;
import com.atguigu.yingyin12.utils.VolleyManager;
import com.atguigu.yingyin12.view.MyGridView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by admin on 2016/1/30.
 */
public class MovieListPager extends BaseDetailPager {


    /**
     * 统一URl
     */
    private final String[] dy;

    /**
     * 判断当前标签
     */
    private final int i;

    /**
     * recycle使用实现筛选
     */
    private RecyclerView rl_menu_recycle1;
    private RecyclerView rl_menu_recycle2;
    private RecyclerView rl_menu_recycle3;

    /**
     * 筛选栏数据，类型+年份+地域
     */
    public static List<TopLabel.InfoEntity.CategoryEntity> category;
    public static List<TopLabel.InfoEntity.YearEntity> year;
    public static List<TopLabel.InfoEntity.DistrictEntity> district;


    private static final String TAG = MovieListPager.class.getSimpleName();




    private MySourceBAdapter adapter;

    @ViewInject(R.id.container)
    private SwipeRefreshLayout container;

    @ViewInject(R.id.ll_source_content)
    private ListView ll_source_content;

    @ViewInject(R.id.ll_load_error)
    private LinearLayout ll_load_error;

    @ViewInject(R.id.iv_refresh_feb)
    private ImageView iv_refresh_feb;

    public MovieListPager(Activity activity, String[] dy, int i) {
        super(activity);
        category = MovieActivity.category;
        year = MovieActivity.year;
        district = MovieActivity.district;
        this.dy = dy;
        this.i = i;
        /**
         * 请求数据
         */
        String value = CacheUtils.getInstance(activity).getStringValue("movie1");
//        if (!"".equals(value)) {
//            progressJson(value);
//        }
        getDataFromNet("");
    }

    @Override
    public View initView() {
        View viewC = View.inflate(activity, R.layout.movie_list, null);
        x.view().inject(this, viewC);
        return viewC;
    }


    @Override
    public void initData() {
        super.initData();

    }

    /**
     * 解析json  volley
     */
    private void getDataFromNet(String url) {
        /**
         * 分类型url
         */
        final String urlNew;
        if (url.equals("")) {
            if (i == ConstantUtiles.JX) {
                urlNew = dy[2];
            } else {
                urlNew = dy[3];
            }
        } else {
            urlNew = url;
        }

        RequestQueue queue = VolleyManager.getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.GET, urlNew, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //联网请求成功  -- 存储
                Log.e(TAG, "联网成功" + s);
                CacheUtils.getInstance(activity).saveString("movie1",s);
                ll_load_error.setVisibility(View.GONE);
                progressJson(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, "联网错误：" + volleyError);
                ll_load_error.setVisibility(View.VISIBLE);
                iv_refresh_feb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDataFromNet(urlNew);
                    }
                });
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
     * 页面数据 ,除jingxuan之外的页面
     */
    private List<ZYChannerBean.InfoEntity.ListEntity> zyData;

    /**
     * 精选页面数据
     */
    private List<MovieSource.InfoEntity.ListEntity> movieData;

    /**
     * 解析json
     *
     * @param json
     */
    private void progressJson(String json) {
        Log.e(TAG, "1-1");
        Gson gson = new Gson();

        if (i == ConstantUtiles.JX) {
            MovieSource movieSource = gson.fromJson(json, MovieSource.class);
            movieData = movieSource.getInfo().getList();
        } else {
            ZYChannerBean bean = gson.fromJson(json, ZYChannerBean.class);
            zyData = bean.getInfo().getList();
        }
        adapter = new MySourceBAdapter();
        ll_source_content.setAdapter(adapter);


        /**
         *
         */
        container.setOnRefreshListener(new MyOnRefreshListener());
        container.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }


    /**
     * 刷新(新特性之一)
     */
    class MyOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    container.setRefreshing(false);
                    Toast.makeText(activity, "哒哒哒...", Toast.LENGTH_SHORT).show();
                }
            }, 5000);
        }
    }

    /**
     * listView适配器
     */
    class MySourceBAdapter extends BaseAdapter {
        @Override
        public int getCount() {

            if (i == ConstantUtiles.JX) {
                return movieData.size();
            } else {
                /**
                 * 不是精选页面，所有的GridView数据保存在一个item中
                 */
                return 1;
            }

        }

        @Override
        public Object getItem(int position) {
            if (i == ConstantUtiles.JX) {
                return movieData.get(position);
            }
            return zyData.get(position);

        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(activity, R.layout.movie_source_b, null);
                ll_grid_feb = (LinearLayout) convertView.findViewById(R.id.ll_grid_feb);

                holder.gl_source_content = (MyGridView) convertView.findViewById(R.id.gl_source_content);
                holder.tv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
                holder.tv_item_more = (TextView) convertView.findViewById(R.id.tv_item_more);
                holder.ll_recycle_feb = (LinearLayout) convertView.findViewById(R.id.ll_recycle_feb);
                holder.iv = (ImageView) convertView.findViewById(R.id.iv);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            /**
             *
             */
            if (i == 0) {
                rl_menu_recycle1 = (RecyclerView) holder.ll_recycle_feb.findViewById(R.id.rl_menu_recycle1);
                rl_menu_recycle2 = (RecyclerView) holder.ll_recycle_feb.findViewById(R.id.rl_menu_recycle2);
                rl_menu_recycle3 = (RecyclerView) holder.ll_recycle_feb.findViewById(R.id.rl_menu_recycle3);
                setRecycleView();

                holder.ll_recycle_feb.setVisibility(View.VISIBLE);
                holder.tv_item_name.setVisibility(View.GONE);
                holder.tv_item_more.setVisibility(View.GONE);
                holder.iv.setVisibility(View.GONE);
                holder.gl_source_content.setAdapter(new MyGridAdapter(null));
                holder.gl_source_content.setOnItemClickListener(new MyGridOnItemClickListener(null));
            } else if (i == ConstantUtiles.JX) {

                holder.ll_recycle_feb.setVisibility(View.GONE);
                MovieSource.InfoEntity.ListEntity listEntity = movieData.get(position);
                List<MovieSource.InfoEntity.ListEntity.listentity> innerData = listEntity.getList();
                holder.tv_item_name.setText(listEntity.getName());
                holder.tv_item_more.setVisibility(View.GONE);
                holder.iv.setVisibility(View.VISIBLE);
                holder.gl_source_content.setAdapter(new MyGridAdapter(innerData));
                holder.gl_source_content.setOnItemClickListener(new MyGridOnItemClickListener(innerData));

            } else {
                holder.iv.setVisibility(View.GONE);
                holder.ll_recycle_feb.setVisibility(View.GONE);
                holder.tv_item_name.setVisibility(View.GONE);
                holder.tv_item_more.setVisibility(View.GONE);
                holder.gl_source_content.setAdapter(new MyGridAdapter(null));
                holder.gl_source_content.setOnItemClickListener(new MyGridOnItemClickListener(null));
            }



            /**
             *
             */
            return convertView;
        }



    }

    //    List<MovieSource.InfoEntity.ListEntity.listentity> innerData;
    LinearLayout ll_grid_feb;

    /**
     * 设置recycle
     */
    private void setRecycleView() {
        RecycleAdapter adapter;
        adapter = new RecycleAdapter(activity, 1);
//        GridLayoutManager manager = new GridLayoutManager(activity,category.size(),GridLayoutManager.HORIZONTAL,false);
//        rl_menu_recycle1.setLayoutManager(manager);
        rl_menu_recycle1.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        rl_menu_recycle1.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecycleAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Toast.makeText(activity, category.get(postion).getName(), Toast.LENGTH_LONG).show();
                getDataFromNet(ApiUtils.HAPPY_DY_URL);
            }
        });
        adapter = new RecycleAdapter(activity, 2);
        rl_menu_recycle2.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        rl_menu_recycle2.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecycleAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Toast.makeText(activity, year.get(postion).getName(), Toast.LENGTH_LONG).show();
                getDataFromNet(ApiUtils.DM_SOURCE_URL);
            }
        });
        adapter = new RecycleAdapter(activity, 3);
        rl_menu_recycle3.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        rl_menu_recycle3.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecycleAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Toast.makeText(activity, district.get(postion).getName(), Toast.LENGTH_LONG).show();
                getDataFromNet(ApiUtils.ZY_SOURCE_URL);

            }
        });
    }

    /**
     * listView的加载类
     */
    static class ViewHolder {
        MyGridView gl_source_content;
        TextView tv_item_name;
        TextView tv_item_more;
        LinearLayout ll_recycle_feb;
        ImageView iv;
    }

    /**
     * gridView的适配器
     */
    class MyGridAdapter extends BaseAdapter {
        private final List<MovieSource.InfoEntity.ListEntity.listentity> innerData;

        public MyGridAdapter(List<MovieSource.InfoEntity.ListEntity.listentity> innerData) {
            this.innerData = innerData;
        }

        @Override
        public int getCount() {
            if (i == ConstantUtiles.JX) {
                return innerData.size();
            }
            return zyData.size();
        }

        @Override
        public Object getItem(int position) {
            if (i == ConstantUtiles.JX) {
                return innerData.get(position);
            }
            return zyData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            ViewGridHolder holder = null;
            if (convertView == null) {
                holder = new ViewGridHolder();
                convertView = View.inflate(activity, R.layout.item_grid_movie, null);
                x.view().inject(this, convertView);
                holder.iv_grid_item = (ImageView) convertView.findViewById(R.id.iv_grid_item);
                holder.tv_grid_item = (TextView) convertView.findViewById(R.id.tv_grid_item);

                convertView.setTag(holder);
            } else {
                holder = (ViewGridHolder) convertView.getTag();
            }

            if (i == ConstantUtiles.JX && innerData != null) {
                MovieSource.InfoEntity.ListEntity.listentity child = innerData.get(position);
                holder.tv_grid_item.setText(child.getTitle());
                //x.image().bind(holder.iv_grid_item, child.getPic());
//            Picasso.with(activity)
//                    .load(child.getPic())
//                    .into(holder.iv_grid_item);
//                Glide.with(activity).load(child.getPic()).into(holder.iv_grid_item);

                ImageUtils.loaderImager(holder.iv_grid_item, child.getPic());
            } else {
                ZYChannerBean.InfoEntity.ListEntity child = zyData.get(position);
                holder.tv_grid_item.setText(child.getTitle());
                Picasso.with(activity).load(child.getPic()).into(holder.iv_grid_item);
            }


            return convertView;
        }
    }


    static class ViewGridHolder {
        ImageView iv_grid_item;
        TextView tv_grid_item;
    }

    /**
     * 点击监听
     */
    class MyGridOnItemClickListener implements AdapterView.OnItemClickListener {

        private final List<MovieSource.InfoEntity.ListEntity.listentity> innerData;

        public MyGridOnItemClickListener(List<MovieSource.InfoEntity.ListEntity.listentity> innerData) {
            this.innerData = innerData;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (i == ConstantUtiles.JX && innerData != null) {
                MovieSource.InfoEntity.ListEntity.listentity lisTentity = innerData.get(position);
                String WebViewUrl = lisTentity.getUrl();

                Toast.makeText(activity, lisTentity.getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, MovieDetailsActivity.class);
                intent.putExtra("WebViewUrl", WebViewUrl);
                activity.startActivity(intent);
            } else {
                Toast.makeText(activity, "资源下架", Toast.LENGTH_SHORT).show();
            }

        }
    }

}

