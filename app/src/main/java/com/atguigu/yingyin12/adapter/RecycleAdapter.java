package com.atguigu.yingyin12.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atguigu.yingyin12.R;
import com.atguigu.yingyin12.domain.TopLabel;
import com.atguigu.yingyin12.type.MovieActivity;

import java.util.List;

/**
 * Created by admin on 2016/1/30.
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder>  {

    private final Context context;
    private List<TopLabel.InfoEntity.CategoryEntity> category;
    private  List<TopLabel.InfoEntity.YearEntity> year;
    private  List<TopLabel.InfoEntity.DistrictEntity> district;
    private MyItemClickListener mItemClickListener;
    private final int i;

    public RecycleAdapter(Context context,int i) {
        this.context = context;
        this.i = i;
        category = MovieActivity.category;
        year = MovieActivity.year;
        district = MovieActivity.district;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.text_recycle, null);

        return new ViewHolder(view,mItemClickListener);
    }

    /**
     * 设置Item点击监听
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        category = MovieActivity.category;
        year = MovieActivity.year;
        district = MovieActivity.district;

        if(i == 1) {
            holder.tv.setText(category.get(position).getName());
        } else if(i == 2) {
            holder.tv.setText(year.get(position).getName());
        } else if (i == 3) {
            holder.tv.setText(district.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {

        category = MovieActivity.category;
        year = MovieActivity.year;
        district = MovieActivity.district;

        if(i == 1) {
            return category.size();
        } else if(i == 2) {
            return year.size();
        } else if (i == 3) {
            return district.size();
        }
        return 0;
    }


    public interface MyItemClickListener {
        void onItemClick(View view, int postion);
    }

    /**
     *
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mItemClickListener;
        TextView tv;

        public ViewHolder(final View itemView, MyItemClickListener mItemClickListener) {
            super(itemView);
            this.mItemClickListener = mItemClickListener;
            this.tv = (TextView) itemView.findViewById(R.id.tv_recycle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }

    }
}
