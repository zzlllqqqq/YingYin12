package com.atguigu.yingyin12.domain;

import java.util.List;

/**
 * Created by admin on 2016/1/30.
 */
public class MovieSource {




    private InfoEntity info;


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
        private String caeFlag;
        private String channel;


        private List<ListEntity> list;

        public void setCaeFlag(String caeFlag) {
            this.caeFlag = caeFlag;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public void setList(List<ListEntity> list) {
            this.list = list;
        }

        public String getCaeFlag() {
            return caeFlag;
        }

        public String getChannel() {
            return channel;
        }

        public List<ListEntity> getList() {
            return list;
        }

        public static class ListEntity {
            private String floor;
            private String name;
            private String order_num;
            private String type;
            /**
             * catch_type : 2
             * column : 3
             * description :
             * id : 128968
             * latest : 8.9分
             * media : dy
             * order_num : 1
             * pic : http://imgwx2.2345.com/dianyingimg/img/1/42/s128968.jpg?1438238256
             * pic_type : 3
             * tag_name : sb
             * title : 蚁人
             * url : http://dianying.2345.com/m/detail/128968.html
             */

            private List<listentity> list;

            public void setFloor(String floor) {
                this.floor = floor;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setOrder_num(String order_num) {
                this.order_num = order_num;
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setList(List<listentity> list) {
                this.list = list;
            }

            public String getFloor() {
                return floor;
            }

            public String getName() {
                return name;
            }

            public String getOrder_num() {
                return order_num;
            }

            public String getType() {
                return type;
            }

            public List<listentity> getList() {
                return list;
            }

            public static class listentity {
                private String catch_type;
                private String column;
                private String description;
                private String id;
                private String latest;
                private String media;
                private String order_num;
                private String pic;
                private String pic_type;
                private String tag_name;
                private String title;
                private String url;

                public void setCatch_type(String catch_type) {
                    this.catch_type = catch_type;
                }

                public void setColumn(String column) {
                    this.column = column;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public void setLatest(String latest) {
                    this.latest = latest;
                }

                public void setMedia(String media) {
                    this.media = media;
                }

                public void setOrder_num(String order_num) {
                    this.order_num = order_num;
                }

                public void setPic(String pic) {
                    this.pic = pic;
                }

                public void setPic_type(String pic_type) {
                    this.pic_type = pic_type;
                }

                public void setTag_name(String tag_name) {
                    this.tag_name = tag_name;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getCatch_type() {
                    return catch_type;
                }

                public String getColumn() {
                    return column;
                }

                public String getDescription() {
                    return description;
                }

                public String getId() {
                    return id;
                }

                public String getLatest() {
                    return latest;
                }

                public String getMedia() {
                    return media;
                }

                public String getOrder_num() {
                    return order_num;
                }

                public String getPic() {
                    return pic;
                }

                public String getPic_type() {
                    return pic_type;
                }

                public String getTag_name() {
                    return tag_name;
                }

                public String getTitle() {
                    return title;
                }

                public String getUrl() {
                    return url;
                }
            }
        }
    }
}
