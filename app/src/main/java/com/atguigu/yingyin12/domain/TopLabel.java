package com.atguigu.yingyin12.domain;

import java.util.List;

/**
 * Created by admin on 2016/1/30.
 */
public class TopLabel {


    /**
     * caeFlag :
     * category : [{"name":"全部","py":""},{"name":"爱情","py":"aiqing"},{"name":"喜剧","py":"xiju"},{"name":"恐怖","py":"kongbu"},{"name":"科幻","py":"kehuan"},{"name":"鬼片","py":"guipian"},{"name":"剧情","py":"juqing"},{"name":"动作","py":"dongzuo"},{"name":"搞笑","py":"gaoxiao"},{"name":"青春","py":"qingchun"},{"name":"微电影","py":"weidianying"},{"name":"惊悚","py":"jingsong"},{"name":"战争","py":"zhanzheng"},{"name":"犯罪","py":"fanzui"},{"name":"枪战","py":"qiangzhan"},{"name":"古装","py":"guzhuang"},{"name":"动画","py":"donghua"},{"name":"奇幻","py":"qihuan"},{"name":"艺术","py":"yishu"},{"name":"灾难","py":"zainan"},{"name":"魔幻","py":"mohuan"},{"name":"武侠","py":"wuxia"},{"name":"冒险","py":"maoxian"},{"name":"文艺","py":"wenyi"},{"name":"悬疑","py":"xuanyi"}]
     * category_recommend : [{"act":"getRecommend","cate":"jx","is_show":"1","name":"精选","order_num":"0","type":"cate"},{"act":"getZtData","cate":"dy_tuijian","is_show":"1","name":"每周推荐","order_num":"1","type":"zt"},{"act":"getZtData","cate":"dy_lengmen","is_show":"1","name":"冷门佳片","order_num":"2","type":"zt"},{"act":"getZtData","cate":"dy_paonan","is_show":"1","name":"跑男","order_num":"3","type":"zt"},{"act":"getZtData","cate":"dy_dongzuo","is_show":"1","name":"特工","order_num":"4","type":"zt"},{"act":"getZtData","cate":"dy_xiju","is_show":"1","name":"喜剧","order_num":"5","type":"zt"},{"act":"getZtData","cate":"dy_aiqing","is_show":"1","name":"爱情","order_num":"6","type":"zt"},{"act":"getZtData","cate":"dy_kongbu","is_show":"1","name":"恐怖","order_num":"7","type":"zt"}]
     * channel : dy
     * district : [{"name":"全部","py":""},{"name":"大陆","py":"dalu"},{"name":"香港","py":"xianggang"},{"name":"日本","py":"riben"},{"name":"美国","py":"meiguo"},{"name":"韩国","py":"hanguo"},{"name":"台湾","py":"taiwan"},{"name":"泰国","py":"taiguo"},{"name":"法国","py":"faguo"},{"name":"英国","py":"yingguo"},{"name":"印度","py":"yindu"},{"name":"俄罗斯","py":"eluosi"},{"name":"西班牙","py":"xibanya"},{"name":"加拿大","py":"jianada"},{"name":"新加坡","py":"xinjiapo"}]
     * year : [{"name":"全部","py":""},{"name":"2016","py":"2016"},{"name":"2015","py":"2015"},{"name":"2014","py":"2014"},{"name":"2013","py":"2013"},{"name":"2012","py":"2012"},{"name":"2011","py":"2011"},{"name":"2010-2000","py":"20002010"},{"name":"90年代","py":"19901999"},{"name":"80年代","py":"19801989"},{"name":"70年代","py":"19701979"},{"name":"更早","py":"18001969"}]
     */

    private InfoEntity info;
    /**
     * info : {"caeFlag":"","category":[{"name":"全部","py":""},{"name":"爱情","py":"aiqing"},{"name":"喜剧","py":"xiju"},{"name":"恐怖","py":"kongbu"},{"name":"科幻","py":"kehuan"},{"name":"鬼片","py":"guipian"},{"name":"剧情","py":"juqing"},{"name":"动作","py":"dongzuo"},{"name":"搞笑","py":"gaoxiao"},{"name":"青春","py":"qingchun"},{"name":"微电影","py":"weidianying"},{"name":"惊悚","py":"jingsong"},{"name":"战争","py":"zhanzheng"},{"name":"犯罪","py":"fanzui"},{"name":"枪战","py":"qiangzhan"},{"name":"古装","py":"guzhuang"},{"name":"动画","py":"donghua"},{"name":"奇幻","py":"qihuan"},{"name":"艺术","py":"yishu"},{"name":"灾难","py":"zainan"},{"name":"魔幻","py":"mohuan"},{"name":"武侠","py":"wuxia"},{"name":"冒险","py":"maoxian"},{"name":"文艺","py":"wenyi"},{"name":"悬疑","py":"xuanyi"}],"category_recommend":[{"act":"getRecommend","cate":"jx","is_show":"1","name":"精选","order_num":"0","type":"cate"},{"act":"getZtData","cate":"dy_tuijian","is_show":"1","name":"每周推荐","order_num":"1","type":"zt"},{"act":"getZtData","cate":"dy_lengmen","is_show":"1","name":"冷门佳片","order_num":"2","type":"zt"},{"act":"getZtData","cate":"dy_paonan","is_show":"1","name":"跑男","order_num":"3","type":"zt"},{"act":"getZtData","cate":"dy_dongzuo","is_show":"1","name":"特工","order_num":"4","type":"zt"},{"act":"getZtData","cate":"dy_xiju","is_show":"1","name":"喜剧","order_num":"5","type":"zt"},{"act":"getZtData","cate":"dy_aiqing","is_show":"1","name":"爱情","order_num":"6","type":"zt"},{"act":"getZtData","cate":"dy_kongbu","is_show":"1","name":"恐怖","order_num":"7","type":"zt"}],"channel":"dy","district":[{"name":"全部","py":""},{"name":"大陆","py":"dalu"},{"name":"香港","py":"xianggang"},{"name":"日本","py":"riben"},{"name":"美国","py":"meiguo"},{"name":"韩国","py":"hanguo"},{"name":"台湾","py":"taiwan"},{"name":"泰国","py":"taiguo"},{"name":"法国","py":"faguo"},{"name":"英国","py":"yingguo"},{"name":"印度","py":"yindu"},{"name":"俄罗斯","py":"eluosi"},{"name":"西班牙","py":"xibanya"},{"name":"加拿大","py":"jianada"},{"name":"新加坡","py":"xinjiapo"}],"year":[{"name":"全部","py":""},{"name":"2016","py":"2016"},{"name":"2015","py":"2015"},{"name":"2014","py":"2014"},{"name":"2013","py":"2013"},{"name":"2012","py":"2012"},{"name":"2011","py":"2011"},{"name":"2010-2000","py":"20002010"},{"name":"90年代","py":"19901999"},{"name":"80年代","py":"19801989"},{"name":"70年代","py":"19701979"},{"name":"更早","py":"18001969"}]}
     * notice : success cached
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
        private String caeFlag;
        private String channel;
        /**
         * name : 全部
         * py :
         */

        private List<CategoryEntity> category;
        /**
         * act : getRecommend
         * cate : jx
         * is_show : 1
         * name : 精选
         * order_num : 0
         * type : cate
         */

        private List<CategoryRecommendEntity> category_recommend;
        /**
         * name : 全部
         * py :
         */

        private List<DistrictEntity> district;
        /**
         * name : 全部
         * py :
         */

        private List<YearEntity> year;

        public void setCaeFlag(String caeFlag) {
            this.caeFlag = caeFlag;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public void setCategory(List<CategoryEntity> category) {
            this.category = category;
        }

        public void setCategory_recommend(List<CategoryRecommendEntity> category_recommend) {
            this.category_recommend = category_recommend;
        }

        public void setDistrict(List<DistrictEntity> district) {
            this.district = district;
        }

        public void setYear(List<YearEntity> year) {
            this.year = year;
        }

        public String getCaeFlag() {
            return caeFlag;
        }

        public String getChannel() {
            return channel;
        }

        public List<CategoryEntity> getCategory() {
            return category;
        }

        public List<CategoryRecommendEntity> getCategory_recommend() {
            return category_recommend;
        }

        public List<DistrictEntity> getDistrict() {
            return district;
        }

        public List<YearEntity> getYear() {
            return year;
        }

        public static class CategoryEntity {
            private String name;
            private String py;

            public void setName(String name) {
                this.name = name;
            }

            public void setPy(String py) {
                this.py = py;
            }

            public String getName() {
                return name;
            }

            public String getPy() {
                return py;
            }
        }

        public static class CategoryRecommendEntity {
            private String act;
            private String cate;
            private String is_show;
            private String name;
            private String order_num;
            private String type;

            public void setAct(String act) {
                this.act = act;
            }

            public void setCate(String cate) {
                this.cate = cate;
            }

            public void setIs_show(String is_show) {
                this.is_show = is_show;
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

            public String getAct() {
                return act;
            }

            public String getCate() {
                return cate;
            }

            public String getIs_show() {
                return is_show;
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
        }

        public static class DistrictEntity {
            private String name;
            private String py;

            public void setName(String name) {
                this.name = name;
            }

            public void setPy(String py) {
                this.py = py;
            }

            public String getName() {
                return name;
            }

            public String getPy() {
                return py;
            }
        }

        public static class YearEntity {
            private String name;
            private String py;

            public void setName(String name) {
                this.name = name;
            }

            public void setPy(String py) {
                this.py = py;
            }

            public String getName() {
                return name;
            }

            public String getPy() {
                return py;
            }
        }
    }
}
