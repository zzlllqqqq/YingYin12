package com.atguigu.yingyin12.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.yingyin12.R;
import com.atguigu.yingyin12.activity.MovieDetailsActivity;
import com.atguigu.yingyin12.domain.HomeDataBean;
import com.atguigu.yingyin12.text.TestWebViewActivity;
import com.atguigu.yingyin12.utils.ConstantUtiles;
import com.atguigu.yingyin12.utils.ImageUtils;
import com.atguigu.yingyin12.view.CircleImageView;
import com.atguigu.yingyin12.view.NoScrollGridView;

import java.util.List;

/**
 * Created by admin on 2016/1/29.
 */
public class HomeListAdapter extends BaseAdapter implements View.OnClickListener {
    private HomeDataBean dataBean;
    private Activity activity;
    private List<HomeDataBean.InfoEntity.Recommend> recommends;
    private List<HomeDataBean.InfoEntity.Focus> focus;
    private GridViewAdapter adapter;

    public HomeListAdapter(Activity activity, HomeDataBean dataBean) {
        this.dataBean = dataBean;
        this.activity = activity;
        recommends = dataBean.getInfo().getRecommend();
    }

    @Override
    public int getCount() {
        return recommends.size() - 1;
    }

    @Override
    public Object getItem(int position) {
        return recommends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        HomeDataBean.InfoEntity.Recommend recommend = recommends.get(position);
        //属于第一类:有大图品和明星
        if (recommend.getActor().size() != 0) {
            return 1;
            //属于第二类:有大图片
        } else if (recommend.getList().size() % 2 != 0) {
            return 2;
        } else {  //没有大图片和明星
            return 3;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int type = getItemViewType(position); //布局类型
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case 3:
                    convertView = View.inflate(activity, R.layout.item_home3, null);
                    break;
                case 2:
                    convertView = View.inflate(activity, R.layout.item_home2, null);
                    holder.iv_up = (ImageView) convertView.findViewById(R.id.iv_up);
                    holder.iv_tag = (ImageView) convertView.findViewById(R.id.iv_tag);
                    holder.tv_up_detail_left = (TextView) convertView.findViewById(R.id.tv_up_detail_left);
                    holder.tv_up_detail_right = (TextView) convertView.findViewById(R.id.tv_up_detail_right);
                    break;
                case 1:
                    convertView = View.inflate(activity, R.layout.item_home1, null);
                    holder.iv_up = (ImageView) convertView.findViewById(R.id.iv_up);
                    holder.iv_tag = (ImageView) convertView.findViewById(R.id.iv_tag);
                    holder.tv_up_detail_left = (TextView) convertView.findViewById(R.id.tv_up_detail_left);
                    holder.tv_up_detail_right = (TextView) convertView.findViewById(R.id.tv_up_detail_right);
                    holder.tv_actor1_name = (TextView) convertView.findViewById(R.id.tv_actor1_name);
                    holder.tv_actor2_name = (TextView) convertView.findViewById(R.id.tv_actor2_name);
                    holder.tv_actor3_name = (TextView) convertView.findViewById(R.id.tv_actor3_name);
                    holder.tv_actor4_name = (TextView) convertView.findViewById(R.id.tv_actor4_name);
                    holder.iv_actor1 = (CircleImageView) convertView.findViewById(R.id.iv_actor1);
                    holder.iv_actor2 = (CircleImageView) convertView.findViewById(R.id.iv_actor2);
                    holder.iv_actor3 = (CircleImageView) convertView.findViewById(R.id.iv_actor3);
                    holder.iv_actor4 = (CircleImageView) convertView.findViewById(R.id.iv_actor4);
                    break;
            }
            holder.gv_detail = (NoScrollGridView) convertView.findViewById(R.id.gv_detail);
            holder.tv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
            holder.tv_item_more = (TextView) convertView.findViewById(R.id.tv_item_more);
            holder.tv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
            holder.tv_item_more = (TextView) convertView.findViewById(R.id.tv_item_more);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeDataBean.InfoEntity.Recommend recommend = recommends.get(position);
        List<HomeDataBean.InfoEntity.Recommend.Actor> actors = recommend.getActor();
        List<HomeDataBean.InfoEntity.Recommend.Deatils> deatilses = recommend.getList();
        switch (type) {
            case 1:
                holder.tv_actor1_name.setText(actors.get(0).getTitle());
                holder.tv_actor2_name.setText(actors.get(1).getTitle());
                holder.tv_actor3_name.setText(actors.get(2).getTitle());
                holder.tv_actor4_name.setText(actors.get(3).getTitle());
                ImageUtils.loaderImager(holder.iv_actor1, actors.get(0).getPic());
                ImageUtils.loaderImager(holder.iv_actor2, actors.get(1).getPic());
                ImageUtils.loaderImager(holder.iv_actor3, actors.get(2).getPic());
                ImageUtils.loaderImager(holder.iv_actor4, actors.get(3).getPic());

                //给明星简介设置点击监听
                holder.iv_actor1.setOnClickListener(this);
                holder.iv_actor2.setOnClickListener(this);
                holder.iv_actor3.setOnClickListener(this);
                holder.iv_actor4.setOnClickListener(this);
                holder.iv_actor1.setTag(actors.get(0).getUrl());
                holder.iv_actor2.setTag(actors.get(1).getUrl());
                holder.iv_actor3.setTag(actors.get(2).getUrl());
                holder.iv_actor4.setTag(actors.get(3).getUrl());
            case 2:
                HomeDataBean.InfoEntity.Recommend.Deatils deatils0 = deatilses.get(0);
                ImageUtils.loaderImager(holder.iv_up, deatils0.getPic());
                holder.tv_up_detail_left.setText(deatils0.getTitle());
                holder.tv_up_detail_right.setText(deatils0.getDescription());
                String tag_name = deatils0.getTag_name();
                addTag(holder.iv_tag, tag_name);
            case 3:
                if ("应用推荐".equals(recommend.getName())) {
                    return convertView;
                }
                holder.tv_item_name.setText(recommend.getName());
                if (recommend.getHot_cate().size() != 0) {
                    holder.tv_item_more.setVisibility(View.VISIBLE);
                    if ("热点聚焦".equals(recommend.getName())) {
                        holder.tv_item_more.setText("看不够?猛戳这");
                    }
                } else {
                    holder.tv_item_more.setVisibility(View.GONE);
                }
                if (type != 3) {
                    deatilses = deatilses.subList(1, deatilses.size());
                }
                adapter = new GridViewAdapter(deatilses, type);
                holder.gv_detail.setAdapter(adapter);
                //给GridView设置点击监听
                holder.gv_detail.setOnItemClickListener(new GridOnItemClickListener(deatilses));

                break;
        }
        return convertView;
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

    static class ViewHolder {


        ImageView iv_up; //大图片
        ImageView iv_tag; //tag图片
        TextView tv_up_detail_left; //对大图片的描述
        TextView tv_up_detail_right; //对大图片的描述_右边
        NoScrollGridView gv_detail, gv_app_detail; //中间详情
        CircleImageView iv_actor1;
        CircleImageView iv_actor2;
        CircleImageView iv_actor3;
        CircleImageView iv_actor4;
        TextView tv_actor1_name;
        TextView tv_actor2_name;
        TextView tv_actor3_name;
        TextView tv_actor4_name;
        TextView tv_item_name;//整个item的描述
        TextView tv_item_more; //更多
    }


    /**
     * 应用详情的adapter
     */
    class GridViewAdapter extends BaseAdapter {

        private final List<HomeDataBean.InfoEntity.Recommend.Deatils> deatilses;


        public GridViewAdapter(List<HomeDataBean.InfoEntity.Recommend.Deatils> deatilses, int type) {
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
            GridViewHolder holder;
            if (convertView == null) {
                holder = new GridViewHolder();
                convertView = View.inflate(activity, R.layout.item_grid_item, null);
                holder.iv_grid_item_icon = (ImageView) convertView.findViewById(R.id.iv_grid_item_icon);
                holder.tv_grid_item_title = (TextView) convertView.findViewById(R.id.tv_grid_item_title);
                holder.tv_grid_item_details = (TextView) convertView.findViewById(R.id.tv_grid_item_details);
                holder.iv_grid_tag_icon = (ImageView) convertView.findViewById(R.id.iv_grid_tag_icon);
                holder.tv_grid_item_fen = (TextView) convertView.findViewById(R.id.tv_grid_item_fen);
                convertView.setTag(holder);
            } else {
                holder = (GridViewHolder) convertView.getTag();
            }


            ImageUtils.loaderImager(holder.iv_grid_item_icon, deatilses.get(position).getPic());
            holder.tv_grid_item_title.setText(deatilses.get(position).getTitle());
            holder.tv_grid_item_details.setText(deatilses.get(position).getDescription());
            String tagName = deatilses.get(position).getTag_name();
            String latest = deatilses.get(position).getLatest();
            String score = deatilses.get(position).getScore();
            if ("".equals(score) || "0".equals(score) || "dsj".equals(tagName)) {
                holder.tv_grid_item_fen.setText(latest);
            } else {
                holder.tv_grid_item_fen.setText(score);
            }
            addTag(holder.iv_grid_tag_icon, tagName);
            return convertView;
        }

    }

    static class GridViewHolder {

        ImageView iv_grid_item_icon;
        TextView tv_grid_item_title;
        TextView tv_grid_item_details;
        ImageView iv_grid_tag_icon; //左上角小图标
        TextView tv_grid_item_fen;//图片右下角的分数,级数等
    }

    /**
     * GridView的点击监听
     */
    class GridOnItemClickListener implements AdapterView.OnItemClickListener {

        private final List<HomeDataBean.InfoEntity.Recommend.Deatils> deatilses;

        public GridOnItemClickListener(List<HomeDataBean.InfoEntity.Recommend.Deatils> deatilses) {
            this.deatilses = deatilses;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//            startWebActivity(deatilses.get(position).getUrl());
            activity.startActivity(new Intent(activity, MovieDetailsActivity.class));
        }

    }


    /**
     * 明星简介的点击监听的回调
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        String url = (String) v.getTag();
        startWebActivity(url);
    }

    /**
     * 携带url启动WebView
     *
     * @param html
     */
    private void startWebActivity(String html) {
        Intent intent = new Intent(activity, TestWebViewActivity.class);
        intent.putExtra(ConstantUtiles.HTML, html);
        activity.startActivity(intent);
    }
}