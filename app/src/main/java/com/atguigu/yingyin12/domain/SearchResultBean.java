package com.atguigu.yingyin12.domain;

import java.util.List;

/**
 * Created by admin on 2016/1/30.
 */
public class SearchResultBean {

    /**
     * info : [{"actor":"林继东 苗圃 徐囡楠","id":"47796","media":"tv","title":"我的铁血金戈梦"},{"actor":"海顿 翁虹 赵恒煊","id":"47659","media":"tv","title":"我是赵传奇"},{"actor":"宋慧乔 窦骁 邬君梅","id":"135019","media":"dy","title":"我是女王"},{"actor":"高利虹 纪子墨 何明翰","id":"158633","media":"dy","title":"我的美女总监"},{"actor":"杨幂 鹿晗 朱亚文","id":"151662","media":"dy","title":"我是证人"},{"actor":"宋芸桦 王大陆 刘德华","id":"151762","media":"dy","title":"我的少女时代（2015）"},{"actor":"","id":"64767","media":"dm","title":"我叫白小飞"},{"actor":"郝蕾 于震 于洋","id":"45776","media":"tv","title":"我的二哥二嫂"},{"actor":"黄志忠 左小青 吴刚","id":"46780","media":"tv","title":"我的绝密生涯"},{"actor":"沙溢 梁静 田雨","id":"47820","media":"tv","title":"我的博士老公"}]
     * notice : success cached
     * status : 200
     */

    private String notice;
    private String status;
    /**
     * actor : 林继东 苗圃 徐囡楠
     * id : 47796
     * media : tv
     * title : 我的铁血金戈梦
     */

    private List<InfoEntity> info;

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInfo(List<InfoEntity> info) {
        this.info = info;
    }

    public String getNotice() {
        return notice;
    }

    public String getStatus() {
        return status;
    }

    public List<InfoEntity> getInfo() {
        return info;
    }

    public static class InfoEntity {
        private String actor;
        private String id;
        private String media;
        private String title;

        public void setActor(String actor) {
            this.actor = actor;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setMedia(String media) {
            this.media = media;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getActor() {
            return actor;
        }

        public String getId() {
            return id;
        }

        public String getMedia() {
            return media;
        }

        public String getTitle() {
            return title;
        }
    }
}
