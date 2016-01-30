package com.atguigu.yingyin12.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by admin on 2016/1/30.
 */
public class VitamioVideoView extends VideoView {
    private MediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener;

    public VitamioVideoView(Context context) {
        super(context);
    }

    public VitamioVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VitamioVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }


    /**
     * 根据提供的参数设置播放视频的尺寸
     * @param width
     * @param height
     */

    public void setVideoSize(float width, float height) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = (int) width;
        params.height= (int) height;
        setLayoutParams(params);
    }

    public void setOnSeekCompleteListener(MediaPlayer.OnSeekCompleteListener listener) {
        mOnSeekCompleteListener = listener;
    }



}