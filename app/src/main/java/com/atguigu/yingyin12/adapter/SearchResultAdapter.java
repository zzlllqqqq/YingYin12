package com.atguigu.yingyin12.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atguigu.yingyin12.R;
import com.atguigu.yingyin12.domain.SearchResultBean;
import com.atguigu.yingyin12.search.SearchActivity;

import java.util.List;

/**
 * Created by admin on 2016/1/30.
 */
public class SearchResultAdapter extends BaseAdapter {
    //上下文
    private Activity activity;
    //搜索结果所需要的数据
    private SearchResultBean resultBean;
    private List<SearchResultBean.InfoEntity> list;


    public SearchResultAdapter(SearchActivity activity, SearchResultBean resultBean) {
        this.activity=activity;
        this.resultBean=resultBean;
        list=resultBean.getInfo();

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
        ResultHolder holder;
        if (convertView==null){
            holder= new ResultHolder();
            convertView=View.inflate(activity, R.layout.item_search_result,null);
            holder.tv_title_result= (TextView) convertView.findViewById(R.id.tv_title_result);
            holder.tv_actor_result= (TextView) convertView.findViewById(R.id.tv_actor_result);
            holder.tv_type_result= (TextView) convertView.findViewById(R.id.tv_type_result);

            convertView.setTag(holder);
        }else {
            holder= (ResultHolder) convertView.getTag();
        }

        //准备数据
        SearchResultBean.InfoEntity infoEntity=list.get(position);

        holder.tv_title_result.setText(infoEntity.getTitle());
        if (position==0){
            holder.tv_actor_result.setVisibility(View.VISIBLE);
            holder.tv_actor_result.setText(infoEntity.getActor());
        }


        if ("tv".equals(infoEntity.getMedia())){
            holder.tv_type_result.setText("电视剧");

        }else if ("dy".equals(infoEntity.getMedia())){
            holder.tv_type_result.setText("电影");
        }else if ("dm".equals(infoEntity.getMedia())){
            holder.tv_type_result.setText("动漫");
        }else {
            holder.tv_type_result.setText("综艺");
        }

        return convertView;
    }

    static class ResultHolder{
        TextView tv_title_result;
        TextView tv_actor_result;
        TextView tv_type_result;
    }
}

