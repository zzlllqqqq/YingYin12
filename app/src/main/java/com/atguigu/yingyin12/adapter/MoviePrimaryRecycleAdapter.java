package com.atguigu.yingyin12.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atguigu.yingyin12.R;
import com.atguigu.yingyin12.domain.MovieDetailsBean;
import com.atguigu.yingyin12.utils.ImageUtils;
import com.atguigu.yingyin12.view.CircleImageView;

import java.util.List;

/**
 * Created by admin on 2016/1/29.
 */
public class MoviePrimaryRecycleAdapter extends RecyclerView.Adapter<MoviePrimaryRecycleAdapter.ViewHolder> {

    private Context context;
    //电影主演
    private List<MovieDetailsBean.Info.ActorList> actor_list;


    public MoviePrimaryRecycleAdapter(Context context, List<MovieDetailsBean.Info.ActorList> actor_list) {
        this.context = context;
        this.actor_list = actor_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_recycle_view_primary, null);
        return new MoviePrimaryRecycleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageUtils.loaderImager(holder.iv_actor_icon, actor_list.get(position).getPic());
        holder.tv_actor_name.setText(actor_list.get(position).getActor());
    }

    @Override
    public int getItemCount() {
        return actor_list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView iv_actor_icon;
        TextView tv_actor_name;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_actor_icon = (CircleImageView) itemView.findViewById(R.id.iv_actor_primary_icon);
            tv_actor_name = (TextView) itemView.findViewById(R.id.tv_actor_name);
        }
    }
}
