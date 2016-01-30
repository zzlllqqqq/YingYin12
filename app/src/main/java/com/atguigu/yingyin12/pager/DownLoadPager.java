package com.atguigu.yingyin12.pager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.yingyin12.R;
import com.atguigu.yingyin12.base.BasePager;
import com.atguigu.yingyin12.bean.MediaItem;
import com.atguigu.yingyin12.netvideo.VitamioVideoPlayerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by admin on 2016/1/29.
 */
public class DownLoadPager extends BasePager {

    private View view;
    private ListView lv_videolist;
    private TextView tv_nomedia;
    /**
     * 多媒体列表-视频
     */
    private ArrayList<MediaItem> mediaItems;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (mediaItems != null && mediaItems.size() > 0) {
                //有数据
                tv_nomedia.setVisibility(View.GONE);
                // 5.ListView设置适配器
                lv_videolist.setAdapter(new VideoListAdapter());
            } else {
                //没有数据
                tv_nomedia.setVisibility(View.VISIBLE);
            }
        }
    };
    private ImageOptions imageOptions;

    public DownLoadPager(Activity activity) {
        super(activity);
        view = View.inflate(activity, R.layout.activity_video_list, null);
        rootView.addView(view);
    }

    @Override
    public void initData() {
        super.initData();
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
                .setRadius(DensityUtil.dip2px(5))
                        // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true)
                        // 加载中或错误图片的ScaleType
                        //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.mipmap.ic_launcher)
                .setFailureDrawableId(R.mipmap.ic_launcher)
                .build();


        lv_videolist = (ListView) view.findViewById(R.id.lv_videolist);
        tv_nomedia = (TextView) view.findViewById(R.id.tv_nomedia);

        //1.视频的读取是到数据库-ContentProvider-开机和sdcard插入好的时候会发广播-媒体扫描器-插入到数据库
        //2.视频的读取第二种，自己扫描
        //3.可能视频很多，加载时间比较长-子线程里面加载视频
        //4.用hanlder切换到主线程
        //5.ListView设置适配器
        getData();

        //设置点击某一条事件
        lv_videolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.parse(mediaItems.get(position).getData()), "video/*");
//                mActivity.startActivity(intent);

                Intent intent = new Intent(activity, VitamioVideoPlayerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("videolist", mediaItems);//传递的是播放列表
                intent.putExtras(bundle);
                //传递播放位置
                intent.putExtra("position", position);
                intent.setAction(Intent.ACTION_VIEW);
                activity.startActivity(intent);
            }
        });
    }


    private void getData() {
        mediaItems = new ArrayList<MediaItem>();
        RequestParams params = new RequestParams("http://api.m.mtime.cn/PageSubArea/TrailerList.api");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });

    }

    /**
     * 解析和处理数据
     * @param json
     */
    private void processData(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);//对象
            JSONArray jsonArray = jsonObject.optJSONArray("trailers");
            if (jsonArray != null) {

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                    if (jsonObj != null) {
                        MediaItem mediaItem = new MediaItem();

                        String imageUrl = jsonObj.optString("coverImg");
                        mediaItem.setImageurl(imageUrl);

                        String title = jsonObj.optString("videoTitle");
                        mediaItem.setTitle(title);

                        String movieName = jsonObj.optString("movieName");
                        mediaItem.setTitle2(movieName);

                        String url = jsonObj.optString("url");
                        mediaItem.setData(url);

                        String hUrl = jsonObj.optString("hightUrl");
                        mediaItem.setHdata(hUrl);
                        mediaItems.add(mediaItem);
                    }
                }
            }
            handler.sendEmptyMessage(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class VideoListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mediaItems.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(activity, R.layout.netvideolist_item, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_videolist = (ImageView) convertView.findViewById(R.id.iv_videolist);
                viewHolder.tv_media_name = (TextView) convertView.findViewById(R.id.tv_media_name);
                viewHolder.tv_media_time = (TextView) convertView.findViewById(R.id.tv_media_time);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //更加位置得到对应的数据
            MediaItem mediaItem = mediaItems.get(position);
            viewHolder.tv_media_name.setText(mediaItem.getTitle());
            viewHolder.tv_media_time.setText(mediaItem.getTitle2());

            //设置加载图片
            x.image().bind(viewHolder.iv_videolist, mediaItem.getImageurl(), imageOptions);

            return convertView;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

    }

    static class ViewHolder {
        ImageView iv_videolist;
        TextView tv_media_name;
        TextView tv_media_time;
    }


}
