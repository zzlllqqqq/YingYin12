package com.atguigu.yingyin12.eventbus;

/**
 * Created by admin on 2016/1/30.
 */
public class VideoPosition {
    private long position; //用来传递当前的播放位置
    private boolean isPlayComplete; //用来记录是否播放完全
    public VideoPosition(long position,boolean isPlayComplete) {
        this.position = position;
        this.isPlayComplete = isPlayComplete;
    }

    public long getPosition() {
        return position;
    }

    public boolean isPlayComplete() {
        return isPlayComplete;
    }
}
