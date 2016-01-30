package com.atguigu.yingyin12.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.atguigu.yingyin12.adapter.SearchAdapter;
import com.atguigu.yingyin12.adapter.SearchResultAdapter;
import com.atguigu.yingyin12.domain.SearchBean;
import com.atguigu.yingyin12.domain.SearchResultBean;
import com.atguigu.yingyin12.utils.ApiUtils;
import com.atguigu.yingyin12.utils.CacheUtils;
import com.atguigu.yingyin12.utils.VolleyManager;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity implements View.OnClickListener {

    private ImageButton ib_back;
    private ImageButton ib_delete;
    private EditText et_search;
    private ImageButton ib_search;
    private ListView listview_search;

    private RelativeLayout rl_search;
    private ListView listview_result;

    private LinearLayout ll_search_record;
    private LinearLayout ll_record;
    private TextView clear_record;

    private String TAG = SearchActivity.class.getSimpleName();

    //联网请求的url
    private String url;
    //搜索的数据
    private SearchBean bean;
    private SearchAdapter adapter;

    //搜索结果的数据
    private SearchResultBean resultBean;
    private String str = null;
    private SearchResultAdapter resultAdapter;


    //搜索有关的数据
    //每次输入的字符数组长度
    private List<String> searchList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化视图
        intView();

        //联网请求数据
        getDataFromNet();

        //设置适配器
        //设置监听
        setListener();

    }

    /**
     * 设置监听的方法
     */
    private void setListener() {

        //Button的点击事件
        ib_back.setOnClickListener(this);
        ib_search.setOnClickListener(this);
        ib_delete.setOnClickListener(this);


        //设置listview_search的item的点击事件
        listview_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
                Toast.makeText(getApplication(), (adapter.getItem(position).getClass().getName()), Toast.LENGTH_SHORT).show();
            }
        });

        //给搜索框添加改变的监听
        et_search.addTextChangedListener(new SearchTextChangedListener());
        //给搜索框添加软盘的搜索键的监听
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) et_search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    SearchActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                    //跳转搜索内容的activity
                    doMySearch();
                }
                return false;
            }
        });

    }

    /**
     * 搜索结果数据的方法
     */
    private void searchData() {

    }

    /**
     * doMySearch()方法将根据关键字查询数据库或从网络上查询数据信息
     *
     */
    private void doMySearch() {

        //跳转搜索内容的activity
        Intent intent = new Intent(this,SearchResultActivity.class);
        intent.putExtra("search", et_search.getText().toString());
        Log.e("TAG", "doMySearch()" + et_search.getText().toString());
        startActivity(intent);

        //第一个参数为启动时动画效果，第二个参数为退出时动画效果
        //  overridePendingTransition(R.anim.search, R.anim.searchreult);


    }

    private void intView() {
        setContentView(R.layout.activity_search);

        rl_search = (RelativeLayout) findViewById(R.id.rl_search);
        listview_result = (ListView) findViewById(R.id.listview_result);

        ib_back = (ImageButton) findViewById(R.id.ib_back);
        et_search = (EditText) findViewById(R.id.et_search);
        ib_search = (ImageButton) findViewById(R.id.ib_search);
        listview_search = (ListView) findViewById(R.id.listview_search);
        ib_delete = (ImageButton) findViewById(R.id.ib_delete);

        ib_back.setVisibility(View.VISIBLE);
        ib_search.setVisibility(View.VISIBLE);
        et_search.setVisibility(View.VISIBLE);

        ll_search_record = (LinearLayout) findViewById(R.id.ll_search_record);
        clear_record = (TextView) findViewById(R.id.clear_record);
        ll_record = (LinearLayout) findViewById(R.id.ll_record);


    }


    /**
     * 联网请求数据
     */
    private void getDataFromNet() {

        RequestQueue queue = VolleyManager.getRequestQueue();

        url = ApiUtils.CHANNER_SEARCH_URL;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //联网请求成功  -- 存储
                CacheUtils.getInstance(SearchActivity.this).saveString(url, s);


                progressJson(s);
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
     * 解析数据
     *
     * @param json
     */
    private void progressJson(String json) {
        //解析数据(json)
        Gson gson = new Gson();
        bean = gson.fromJson(json, SearchBean.class);

        //设置适配
        if (adapter == null) {
            adapter = new SearchAdapter(this, bean);
            listview_search.setAdapter(adapter);
        }
        adapter.notifyDataSetInvalidated();

        Log.e(TAG, bean.getInfo().getList().get(0).getTitle());

    }

    /**
     * 点击事件的处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;

            case R.id.ib_search:
                if (et_search.length() == 0) {
                    Toast.makeText(SearchActivity.this, "请输入搜索词", Toast.LENGTH_SHORT).show();
                }else {
                    //跳转搜索内容的activity
                    doMySearch();
                    listview_result.setVisibility(View.GONE);
                    listview_search.setVisibility(View.VISIBLE);
                    finish();
//                    et_search.setText("");
                }
                break;

            case R.id.ib_delete:
                if (" ".equals(et_search.getText())||et_search.length()==0) {
                    ib_delete.setVisibility(View.GONE);

                }
                et_search.setText("");
                ib_delete.setVisibility(View.GONE);
                listview_result.setVisibility(View.GONE);
                rl_search.setVisibility(View.VISIBLE);

                break;
            case R.id.clear_record://清空记录

                break;

        }
    }

    /**
     * 输入框的改变监听
     */
    private class SearchTextChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.length()<1||"".equals(et_search.getText())){

                listview_search.setVisibility(View.VISIBLE);
                listview_result.setVisibility(View.GONE);
                ib_delete.setVisibility(View.GONE);
            }else {

                ib_delete.setVisibility(View.VISIBLE);
                listview_result.setVisibility(View.VISIBLE);
                rl_search.setVisibility(View.GONE);
                listview_search.setVisibility(View.GONE);
            }

            getResultDataFromNet();

            Log.e(TAG, "onTextChanged");
        }

        @Override
        public void afterTextChanged(Editable s) {
          /*  if (!"".equals(et_search.getText())) {

                ib_delete.setVisibility(View.VISIBLE);
                listview_result.setVisibility(View.VISIBLE);
                rl_search.setVisibility(View.GONE);

            }else {
                rl_search.setVisibility(View.VISIBLE);
            }

            try {
                //编码
                str = URLEncoder.encode(et_search.getText().toString(), "UTF-8");
                //解码
                //URLDecoder.decode()
                Log.e(TAG, str + "+++++++++++++++++++++++++++++");

                getResultDataFromNet();


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/


        }
    }

    //数据签名集合

    /**
     * 联网请求搜索结果数据
     */
    private void getResultDataFromNet() {

        RequestQueue queue = VolleyManager.getRequestQueue();

        url = ApiUtils.SEARCH_URL;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //联网请求成功  -- 存储
                CacheUtils.getInstance(SearchActivity.this).saveString(url, s);

                progresResultJson(s);
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
     * 解析搜索结果数据
     *
     * @param json
     */
    private void  progresResultJson(String json) {
        //解析数据(json)
        Gson gson = new Gson();
        resultBean = gson.fromJson(json, SearchResultBean.class);

        //设置适配
        if (resultAdapter == null) {
            resultAdapter = new SearchResultAdapter(this, resultBean);
            listview_result.setAdapter(resultAdapter);
        }
        resultAdapter.notifyDataSetInvalidated();

        Log.e(TAG, bean.getInfo().getList().get(0).getTitle());

    }


}
