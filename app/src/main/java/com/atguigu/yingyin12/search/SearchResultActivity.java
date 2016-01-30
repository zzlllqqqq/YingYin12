package com.atguigu.yingyin12.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.atguigu.yingyin12.domain.SearchResultDetailBean;
import com.atguigu.yingyin12.utils.ApiUtils;
import com.atguigu.yingyin12.utils.CacheUtils;
import com.atguigu.yingyin12.utils.ImageUtils;
import com.atguigu.yingyin12.utils.VolleyManager;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends Activity implements View.OnClickListener {

    private ImageButton ib_back;
    private ImageButton ib_delete;
    private EditText et_search;
    private ImageButton ib_search;
    private ListView listview_result_content;

    private String TAG=SearchResultActivity.class.getSimpleName();

    //搜索所需的路径
    private String url;
    //搜索的结果数据
    private SearchResultDetailBean detailBean;
    //搜索结果所对应的数据集合
    private List<SearchResultDetailBean.InfoEntity.ListEntity> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化视图
        initView();

        //联网请求数据
        getSearchDataFromNet();

        //设置适配器
        //设置监听
        setLisener();


    }

    private void setLisener() {
        ib_back.setOnClickListener(this);
        ib_delete.setOnClickListener(this);
    }

    /**
     * 初始化视图的方法
     */
    private void initView() {
        setContentView(R.layout.activity_search_result);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_delete = (ImageButton) findViewById(R.id.ib_delete);
        et_search = (EditText) findViewById(R.id.et_search);
        ib_search = (ImageButton) findViewById(R.id.ib_search);

        listview_result_content = (ListView) findViewById(R.id.listview_result_content);

        ib_back.setVisibility(View.VISIBLE);
        et_search.setVisibility(View.VISIBLE);
        ib_search.setVisibility(View.VISIBLE);
        ib_delete.setVisibility(View.VISIBLE);

        Intent intent=getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
        String reslut=intent.getStringExtra("search");

        //初始化edtext
        et_search.setText(reslut);
        Log.e("TAG", "SearchResultActivity      " + reslut);

    }

    /**
     * 联网请求数据
     */
    private void getSearchDataFromNet() {
        RequestQueue queue = VolleyManager.getRequestQueue();

        url = ApiUtils.SEARCH_RESULT__URL;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //联网请求成功  -- 存储
                CacheUtils.getInstance(SearchResultActivity.this).saveString(url, s);

                progresSearchJson(s);
                Log.e(TAG, "联网成功-----" + s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e(TAG, "联网错误：----" + volleyError);
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
     * 解析数据的方法
     * @param json
     */
    private void progresSearchJson(String json) {
        //解析数据(json)
        Gson gson = new Gson();
        detailBean = gson.fromJson(json, SearchResultDetailBean.class);

        list=detailBean.getInfo().getList();

        //设置适配
        listview_result_content.setAdapter(new ResultAdapter());
        Log.e(TAG, detailBean.getInfo().getList().get(0).getTitle());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_back:

                startActivity(new Intent(this,SearchActivity.class));
                finish();
                break;
            case R.id.ib_delete:
                et_search.setText("");
                finish();
                break;
            case R.id.ib_search:
                if (et_search.length() == 0) {
                    Toast.makeText(SearchResultActivity.this, "请输入搜索词", Toast.LENGTH_SHORT).show();
                }
        }
    }


    private class ResultAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder holder;
            if (convertView==null){
                holder=new MyViewHolder();
                convertView=View.inflate(SearchResultActivity.this,R.layout.item_search_content,null);
                holder.item_icon_search= (ImageView) convertView.findViewById(R.id.item_icon_search);
                holder.item_title_search= (TextView) convertView.findViewById(R.id.item_title_search);
                holder.item_media_search= (TextView) convertView.findViewById(R.id.item_media_search);
                holder.item_score_search= (TextView) convertView.findViewById(R.id.item_score_search);
                holder.item_year_search= (TextView) convertView.findViewById(R.id.item_year_search);
                holder.item_type_search= (TextView) convertView.findViewById(R.id.item_type_search);
                holder.item_actor_search= (TextView) convertView.findViewById(R.id.item_actor_search);

                convertView.setTag(holder);
            }else {
                holder= (MyViewHolder) convertView.getTag();
            }

            //准备数据
            SearchResultDetailBean.InfoEntity.ListEntity listEntity=list.get(position);
            //联网请求图片
            ImageUtils.loaderImager(holder.item_icon_search, listEntity.getPic());

            holder.item_title_search.setText(listEntity.getTitle());
            holder.item_score_search.setText(listEntity.getScore());
            holder.item_year_search.setText(listEntity.getYear());

            if ("tv".equals(listEntity.getMedia())){
                holder.item_media_search.setText("电视剧");
                holder.item_media_search.setBackgroundResource(R.drawable.dianshiju);
                holder.item_type_search.setText(listEntity.getLatest());
                holder.item_actor_search.setText(listEntity.getType()+"/"+listEntity.getActor());

            }else if ("dy".equals(listEntity.getMedia())){
                holder.item_media_search.setText("电影");
                holder.item_media_search.setBackgroundResource(R.drawable.dianying);
                holder.item_type_search.setText(listEntity.getType());
                holder.item_actor_search.setText(listEntity.getActor());

            }else if ("dm".equals(listEntity.getMedia())){
                holder.item_media_search.setText("动漫");
                holder.item_media_search.setBackgroundResource(R.drawable.dongman);
                holder.item_type_search.setText(listEntity.getType());

            }else {
                holder.item_media_search.setText("综艺");
                holder.item_media_search.setBackgroundResource(R.drawable.zongyi);
                holder.item_actor_search.setText(listEntity.getLatest());

            }

            return convertView;
        }
    }

    static class MyViewHolder{
        ImageView item_icon_search;
        TextView item_title_search;
        TextView item_media_search;
        TextView item_score_search;
        TextView item_year_search;
        TextView item_type_search;
        TextView item_actor_search;

    }
}
