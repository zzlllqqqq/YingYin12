package com.atguigu.yingyin12.domain;

/**
 * Created by admin on 2016/1/30.
 */
public class VideoUrlBean {
    private String title; //网络视频名称
    private String duration; //播放时常
    private String player_url;  //标清播放地址
    private String player_url_height;  //高清播放地址
    private String player_url_super; //超清播放地址

    public VideoUrlBean(String title, String duration, String player_url, String player_url_height, String player_url_super) {
        this.title = title;
        this.duration = duration;
        this.player_url = player_url;
        this.player_url_height = player_url_height;
        this.player_url_super = player_url_super;
    }

    public VideoUrlBean() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPlayer_url() {
        return player_url;
    }

    public void setPlayer_url(String player_url) {
        this.player_url = player_url;
    }

    public String getPlayer_url_height() {
        return player_url_height;
    }

    public void setPlayer_url_height(String player_url_height) {
        this.player_url_height = player_url_height;
    }

    public String getPlayer_url_super() {
        return player_url_super;
    }

    public void setPlayer_url_super(String player_url_super) {
        this.player_url_super = player_url_super;
    }

    @Override
    public String toString() {
        return "VideoUrlBean{" +
                "title='" + title + '\'' +
                ", duration='" + duration + '\'' +
                ", player_url='" + player_url + '\'' +
                ", player_url_height='" + player_url_height + '\'' +
                ", player_url_super='" + player_url_super + '\'' +
                '}';
    }
}
