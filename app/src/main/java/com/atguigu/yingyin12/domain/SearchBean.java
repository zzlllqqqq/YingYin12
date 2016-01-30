package com.atguigu.yingyin12.domain;

import java.util.List;

/**
 * Created by admin on 2016/1/30.
 */
public class SearchBean {

    private InfoEntity info;
    /**
     * info : {"list":[{"id":"40886","media":"tv","order_num":"1","tag":"grq","title":"秦时明月"},{"id":"128968","media":"dy","order_num":"2","tag":"","title":"蚁人"},{"id":"47225","media":"tv","order_num":"3","tag":"grq","title":"煮妇神探"},{"id":"47701","media":"tv","order_num":"4","tag":"sb","title":"少帅"},{"id":"45644","media":"tv","order_num":"5","tag":"","title":"美丽的秘密"},{"id":"21918","media":"tv","order_num":"6","tag":"","title":"神探夏洛克"},{"id":"144246","media":"dy","order_num":"7","tag":"","title":"陪安东尼度过漫长岁月"},{"id":"20786","media":"zy","order_num":"8","tag":"","title":"奔跑吧兄弟第三季"},{"id":"136855","media":"dy","order_num":"9","tag":"","title":"一个勺子"},{"id":"21673","media":"zy","order_num":"10","tag":"","title":"了不起的挑战"}]}
     * notice : success
     * status : 200
     */

    private String notice;
    private String status;

    public void setInfo(InfoEntity info) {
        this.info = info;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public InfoEntity getInfo() {
        return info;
    }

    public String getNotice() {
        return notice;
    }

    public String getStatus() {
        return status;
    }

    public static class InfoEntity {
        /**
         * id : 40886
         * media : tv
         * order_num : 1
         * tag : grq
         * title : 秦时明月
         */

        private List<ListEntity> list;

        public void setList(List<ListEntity> list) {
            this.list = list;
        }

        public List<ListEntity> getList() {
            return list;
        }

        public static class ListEntity {
            private String id;
            private String media;
            private String order_num;
            private String tag;
            private String title;

            public void setId(String id) {
                this.id = id;
            }

            public void setMedia(String media) {
                this.media = media;
            }

            public void setOrder_num(String order_num) {
                this.order_num = order_num;
            }

            public void setTag(String tag) {
                this.tag = tag;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getId() {
                return id;
            }

            public String getMedia() {
                return media;
            }

            public String getOrder_num() {
                return order_num;
            }

            public String getTag() {
                return tag;
            }

            public String getTitle() {
                return title;
            }
        }
    }
}
