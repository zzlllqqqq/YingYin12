package com.atguigu.yingyin12.bean;

import java.io.Serializable;

/**
 * Created by admin on 2016/1/22.
 */
public class MediaItem implements Serializable{

    private String title;

    private String data;//普清

    private String hdata;//高清


    private long duration;

    private  long size;

    private String title2;

    private String imageurl;

    private String artist;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getHdata() {
        return hdata;
    }

    public void setHdata(String hdata) {
        this.hdata = hdata;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "MediaItem{" +
                "title='" + title + '\'' +
                ", data='" + data + '\'' +
                ", hdata='" + hdata + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", imageurl='" + imageurl + '\'' +
                '}';
    }
}
