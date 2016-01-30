package com.atguigu.yingyin12.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.atguigu.yingyin12.R;
import com.atguigu.yingyin12.domain.SearchBean;
import com.atguigu.yingyin12.search.SearchActivity;

import java.util.List;

/**
 * Created by admin on 2016/1/30.
 */
public class SearchAdapter extends BaseAdapter {

    private static final String TAG = "SUCCESS";
    //上下文
    private Activity activity;
    //搜索区所需要的数据
    private SearchBean bean;
    private List<SearchBean.InfoEntity.ListEntity> list;


    public SearchAdapter(SearchActivity activity, SearchBean bean) {
        this.activity=activity;
        this.bean=bean;

        list=bean.getInfo().getList();

        Log.e(TAG, list.size() + "++++++++++");
    }

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
        String tag=list.get(position).getTag();

        ViewHorder horder;
        if (convertView==null){
            horder=new ViewHorder();
            convertView=View.inflate(activity, R.layout.item_search,null);

            horder.tv_title_item_search= (TextView) convertView.findViewById(R.id.tv_title_item_search);
            horder.tv_tag_item_search= (ImageButton) convertView.findViewById(R.id.tv_tag_item_search);
            horder.tv_media_item_search= (TextView) convertView.findViewById(R.id.tv_media_item_search);

            if ("grq".equals(tag)){
                horder.tv_tag_item_search.setVisibility(View.VISIBLE);

            }else if ("sb".equals(tag)){
                horder.tv_tag_item_search.setVisibility(View.VISIBLE);
                horder.tv_tag_item_search.setImageResource(R.drawable.shoubo);
            }

            convertView.setTag(horder);

        }else {
            horder= (ViewHorder) convertView.getTag();
        }

        //准备数据集合
        SearchBean.InfoEntity.ListEntity listEntity=list.get(position);

        if ("tv".equals(listEntity.getMedia())){
            horder.tv_media_item_search.setText("电视剧");

        }else if ("dy".equals(listEntity.getMedia())){
            horder.tv_media_item_search.setText("电影");
        }else {
            horder.tv_media_item_search.setText("综艺");
        }
        horder.tv_title_item_search.setText(listEntity.getTitle());



        return convertView;
    }

    static class ViewHorder{
        TextView tv_title_item_search;
        ImageButton tv_tag_item_search;
        TextView tv_media_item_search;
    }
}

