package com.atguigu.yingyin12.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.atguigu.yingyin12.R;
import com.atguigu.yingyin12.adapter.MoviePrimaryRecycleAdapter;
import com.atguigu.yingyin12.domain.MovieDetailsBean;
import com.atguigu.yingyin12.eventbus.VideoPosition;
import com.atguigu.yingyin12.testdownload.DownLoadActivityFeb;
import com.atguigu.yingyin12.utils.ApiUtils;
import com.atguigu.yingyin12.utils.CacheUtils;
import com.atguigu.yingyin12.utils.ImageUtils;
import com.atguigu.yingyin12.utils.Utils;
import com.atguigu.yingyin12.utils.VolleyManager;
import com.atguigu.yingyin12.view.CircleImageView;
import com.atguigu.yingyin12.view.NoScrollListView;
import com.atguigu.yingyin12.view.ObservableScrollView;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MovieDetailsActivity extends Activity implements View.OnClickListener {
    /**
     * 返回按钮
     */
    private ImageButton ib_back;
    /**
     * 视屏标题
     */
    private TextView tv_type;
    /**
     * 收藏
     */
    private ImageButton ib_collect;
    /**
     * 反馈意见
     */
    private ImageButton ib_idea;

    private RecyclerView rv_details;

    /**
     * 电影详细信息的bean类
     */
    private MovieDetailsBean movieDetailsBean;
    /**
     * 演员列表
     */
    private List<MovieDetailsBean.Info.ActorList> actor_list;
    /**
     * 推荐列表
     */
    private List<MovieDetailsBean.Info.RecommendList> recommend_list;


    /**
     * 电影图标
     */
    private ImageView iv_actor_icon;
    /**
     * 电影主演
     */
    private TextView tv_details_primary;
    /**
     * 电影地区
     */
    private TextView tv_details_area;
    /**
     * 电影来源
     */
    private TextView tv_details_resource;
    /**
     * 播放按钮
     */
    private Button btn_details_play;
    /**
     * 离线按钮
     */
    private Button btn_details_off_line;
    /**
     * 主演列表
     */
    private RecyclerView rv_details_primary;
    /**
     * 评论游客图标
     */
    private CircleImageView iv_details_custom_icon;
    /**
     * 评论游客的姓名
     */
    private TextView tv_details_custom_name;
    /**
     * 评论游客的短评
     */
    private TextView tv_details_short;
    /**
     * 查看剩余的评论
     */
    private TextView tv_details_short_number;
    /**
     * 评论时间
     */
    private TextView tv_details_short_time;
    /**
     * 简介下面的箭头
     */
    private ImageView iv_vd_jq_arrow;
    /**
     * 评论回复个数
     */
    private TextView tv_details_short_reply;
    /**
     * 电影简介
     */
    private TextView tv_details_jianjie;
    /**
     * 推荐列表
     */
    private NoScrollListView lv_details_list;
    private String actor;
    private MovieDetailsBean.Info.CommentInfo comment_info;
    private MovieDetailsBean.Info info;
    private PopupWindow popupWindow;
    private View popupView;
    private TextView tv_pre_position;
    private ObservableScrollView sv_details;
    private static String key = "position";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_movie_details_brief);
        initView();
        setListener();
        //注册EventBus
        EventBus.getDefault().register(this);
        String movie = CacheUtils.getInstance(MovieDetailsActivity.this).getStringValue("movie");
        if (!"".equals(movie)) {
            parseJson(movie);
        }
        getDataFromNet();
    }

    /**
     * s设置监听
     */
    private void setListener() {
        tv_details_jianjie.setOnClickListener(this);
        btn_details_play.setOnClickListener(this);
        btn_details_off_line.setOnClickListener(this);
        iv_vd_jq_arrow.setOnClickListener(this);
        sv_details.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                if (y == 0 && !popupWindow.isShowing()) {
                    int value = CacheUtils.getInstance(MovieDetailsActivity.this).getIntValue(key);
                    if (value != -1) {
                        showPreviousPosition(value, false);
                    }
                } else {
                    popupWindow.dismiss();
                }
            }
        });
    }

    /**
     * 初始化视图
     */
    private void initView() {
        sv_details = (ObservableScrollView) findViewById(R.id.sv_details);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        tv_type = (TextView) findViewById(R.id.tv_type);
        ib_collect = (ImageButton) findViewById(R.id.ib_collect);
        ib_idea = (ImageButton) findViewById(R.id.ib_idea);
        iv_actor_icon = (ImageView) findViewById(R.id.iv_actor_icon);
        tv_details_area = (TextView) findViewById(R.id.tv_details_area);
        tv_details_primary = (TextView) findViewById(R.id.tv_details_primary);
        tv_details_resource = (TextView) findViewById(R.id.tv_details_resource);
        btn_details_play = (Button) findViewById(R.id.btn_details_play);
        btn_details_off_line = (Button) findViewById(R.id.btn_details_off_line);
        rv_details_primary = (RecyclerView) findViewById(R.id.rv_details_primary);
        iv_details_custom_icon = (CircleImageView) findViewById(R.id.iv_details_custom_icon);
        tv_details_custom_name = (TextView) findViewById(R.id.tv_details_custom_name);
        tv_details_short_time = (TextView) findViewById(R.id.tv_details_short_time);
        tv_details_short_reply = (TextView) findViewById(R.id.tv_details_short_reply);
        lv_details_list = (NoScrollListView) findViewById(R.id.lv_details_list);
        tv_details_jianjie = (TextView) findViewById(R.id.tv_details_jianjie);
        tv_details_short_number = (TextView) findViewById(R.id.tv_details_short_number);
        tv_details_short = (TextView) findViewById(R.id.tv_details_short);
        iv_vd_jq_arrow = (ImageView) findViewById(R.id.iv_vd_jq_arrow);

    }

    /**
     * 从网络获取数据
     */
    private void getDataFromNet() {
        RequestQueue queue = VolleyManager.getRequestQueue();
        StringRequest request = new StringRequest(Request.Method.GET, ApiUtils.HALIBOTE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CacheUtils.getInstance(MovieDetailsActivity.this).saveString("movie", response);
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.toString());
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
        movieDetailsBean = new Gson().fromJson(response, MovieDetailsBean.class);
        info = movieDetailsBean.getInfo();
        actor_list = info.getActor_list();
        actor = info.getActor();
        recommend_list = info.getRecommend_list();
        comment_info = info.getComment_info();
        setViewData();
    }

    /**
     * 给视图添加数据
     */
    private void setViewData() {
        ImageUtils.loaderImager(iv_actor_icon, info.getPic());

        //设置top
        tv_details_primary.setText("主演:" + info.getActor());
        tv_details_area.setText("地区:" + info.getRegion() + "/" + info.getYear());
        tv_details_resource.setText("来源" + info.getSource());
        tv_type.setText(info.getTitle() + " 死亡圣器");
        tv_details_jianjie.setText(info.getDescription());
        //设置短评
        ImageUtils.loaderImager(iv_details_custom_icon, comment_info.getUser().getPic());
        tv_details_custom_name.setText(comment_info.getUser().getName());
        tv_details_short.setText(comment_info.getContent());
        tv_details_short_time.setText("时间" + comment_info.getShowTime());
        tv_details_short_number.setText("查看剩余的" + comment_info.getTotal() + "条评论");
        //设置主演
        rv_details_primary.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_details_primary.setHasFixedSize(true);
        rv_details_primary.setLayoutManager(layout);
        rv_details_primary.setAdapter(new MoviePrimaryRecycleAdapter(this, actor_list));

        //设置推荐列表
        lv_details_list.setAdapter(new MovieListAdapter());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int value = CacheUtils.getInstance(MovieDetailsActivity.this).getIntValue(key);
                if (value != -1) {
                    showPreviousPosition(value, false);
                }
            }
        }, 500);

    }

    /**
     * 点击监听的回调
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_details_jianjie:
                setTextLines();
                break;
            case R.id.btn_details_off_line:
                startActivity(new Intent(this, DownLoadActivityFeb.class));
                break;
            case R.id.btn_details_play:
                Intent intent = new Intent(this, VideoActivity.class);
                intent.putExtra("URL", ApiUtils.PLAYER_URL);
                startActivity(intent);
                break;
            case R.id.iv_vd_jq_arrow:
                setTextLines();
                break;
            case R.id.ib_back:
                finish();
                break;
            default:
                break;
        }
    }

    private boolean isCurrentLines3;

    /**
     * 动态设置简介的TextView的文本行数
     */
    private void setTextLines() {
        if (isCurrentLines3) {
            tv_details_jianjie.setMaxLines(Integer.MAX_VALUE);
            isCurrentLines3 = false;
            iv_vd_jq_arrow.setImageResource(R.drawable.vd_jq_arrow_up);
        } else {
            tv_details_jianjie.setMaxLines(3);
            isCurrentLines3 = true;
            iv_vd_jq_arrow.setImageResource(R.drawable.vd_jq_arrow_down);
        }
    }


    class MovieListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return recommend_list.size();
        }

        @Override
        public Object getItem(int position) {
            return recommend_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(MovieDetailsActivity.this, R.layout.move_detail_list_view_item, null);
                holder.iv_item_logo = (ImageView) convertView.findViewById(R.id.iv_item_logo);
                holder.tv_item_tilte = (TextView) convertView.findViewById(R.id.tv_item_tilte);
                holder.tv_item_type_and_actor = (TextView) convertView.findViewById(R.id.tv_item_type_and_actor);
                holder.tv_item_score = (TextView) convertView.findViewById(R.id.tv_item_score);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            MovieDetailsBean.Info.RecommendList list = recommend_list.get(position);
            ImageUtils.loaderImager(holder.iv_item_logo, list.getPic());
            holder.tv_item_tilte.setText(list.getTitle());
            holder.tv_item_type_and_actor.setText(list.getType() + "/" + list.getActor());
            holder.tv_item_score.setText(list.getScore());
            return convertView;
        }
    }

    class ViewHolder {
        ImageView iv_item_logo;
        TextView tv_item_tilte;
        TextView tv_item_score;
        TextView tv_item_type_and_actor;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解注册EventBus
        EventBus.getDefault().unregister(this);
    }

    /**
     * 注册EventBus的回调方法
     *
     * @param event
     */
    public void onEventMainThread(VideoPosition event) {
        long position = event.getPosition();
        boolean complete = event.isPlayComplete();
        showPreviousPosition(position, complete);
    }

    /**
     * 显示播放记录的popuWindow
     *
     * @param position
     * @param isPlayComplete
     */
    private void showPreviousPosition(long position, boolean isPlayComplete) {
        popupView = View.inflate(this, R.layout.video_details_popu, null);
        tv_pre_position = (TextView) popupView.findViewById(R.id.tv_pre_position);
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(btn_details_play, 0, 0);
        if (isPlayComplete) {
            tv_pre_position.setText("上次播放完毕");
        } else {
            tv_pre_position.setText("上次播放到" + new Utils().stringForTime((int) position));
        }
        //显示
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);  //设置点击屏幕其它地方弹出框消失
    }


}