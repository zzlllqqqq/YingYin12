package com.atguigu.yingyin12.domain;

import java.io.Serializable;

/**
 * Created by admin on 2016/1/30.
 */
public class VideoInfo implements Serializable {

    private String title; //视频标题
    private long size; // 视频大小
    private String data; // 视频本地播放地址
    private long duration; //视频时长
    private String artist; //艺术家
    private String heightUri;
    private String videoName;
    private String coverImg;
    private String summary;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getHeightUri() {
        return heightUri;
    }

    public void setHeightUri(String heightUri) {
        this.heightUri = heightUri;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "title='" + title + '\'' +
                ", size=" + size +
                ", data='" + data + '\'' +
                ", heightUri='" + heightUri + '\'' +
                ", videoName='" + videoName + '\'' +
                ", coverImg='" + coverImg + '\'' +
                '}';
    }

    public VideoInfo(String title, long size, String data, long duration) {
        this.title = title;
        this.size = size;
        this.data = data;
        this.duration = duration;
    }

    public VideoInfo(String title, long size, String data, long duration, String artist) {
        this.title = title;
        this.size = size;
        this.data = data;
        this.duration = duration;
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public VideoInfo() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

}