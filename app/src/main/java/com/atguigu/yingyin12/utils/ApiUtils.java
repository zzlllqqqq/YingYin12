package com.atguigu.yingyin12.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by admin on 2016/1/29.
 */
public class ApiUtils {
    /**
     * 主界面的url
     */

    public static final String TAB_DM_URL = "http://api.v.2345.com/api.php?ctl=channel&act=getCategory&channel=dm&search=&encode=1&from=app&api_ver=v4.2&vcode=4.4.4&sign=DVZXAlZVA1UDClRXXVBUAQQEUFBYVgkEAVQAAA5TX1I=";
    public static final String TAB_ZY_URL = "http://api.v.2345.com/api.php?ctl=channel&act=getCategory&channel=zy&search=&encode=1&from=app&api_ver=v4.2&vcode=4.4.4&sign=WFZXVwcHUgVVBANUDVQGVQEBAAUHBwBSVVcDAA5TCQA=";

    //频道下载专区的url
    //频道下载专区——电影
    //频道下载专区——电视剧
    //频道下载专区——动漫
    //频道下载专区——综艺：
    //vip专区

    /**
     * 电视剧精选列表
     */
    //public static final String TV_SOURCE_URL= "http://api.v.2345.com/api.php?ctl=channel&act=getFilterData&category=&district=&year=&page=1&perpage=21&channel=tv&from=app&api_ver=v4.2&vcode=4.4.4&sign=W1JTVgVbUVZVAlZTXABQXFUEAQ5RVAlWBwICVAdXAQU=";


    //哈利波特电影界面

    public static final String HALIBOTE_URL = "http://api.v.2345.com/?ctl=dyDetail&id=161133&api_ver=v4.3&vcode=4.4.4&sign=AQ0KCllWUgBVB1EAXVADVlcHBFAFV1JcVAZTVVJRDwQ=";
    //哈利波特电影播放地址
    public static final String PLAYER_URL = "http://api.v.2345.com/?ctl=dyDetail&id=161133&act=getPlayLink&playSource=bestv&playId=0&api_ver=v4.3&vcode=4.4.4&sign=WgEAV1RTVQUEBgEBXlABAAYEAw9WXQVdVVYNWAdbXAU=";
    //频道影视金曲区


    //搜索区
    //搜索文本的基础路径
    //public static final String SEARCH_BASE__URL1 = "http://api.v.2345.com/?ctl=search&act=think&word=";
    //public static final String SEARCH_BASE__URL2 ="&vcode=4.4.4&api_ver=v3.1&sign=DFAEBQRXAVEFUQcGDARQXVIAV1MFBwJWB1RUUVFUXFA=";
    //搜索结果的基础路径

    /**
     * 检查当前网络是否可用
     */
    public static boolean isNetConnected(Context context) {
        boolean connected = false;
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取active的NetworkInfo对象
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            // 得到是否连接状态
            connected = networkInfo.isConnected();
        }
        return connected;
    }

    public static final String TV_SOURCE_URL= "http://api.v.2345.com/api.php?ctl=channel&act=getFilterData&district=tv_meiguo&channel=tv&page=1&from=app&api_ver=v4.2&vcode=4.4.4&sign=DlJWUFdaVFQLVVVUD1RaBwUHUABZUAIGAlQNU1NbW1E=";
    public static final String DM_SOURCE_URL= "http://api.v.2345.com/api.php?ctl=channel&act=getFilterData&category=dm_gaoxiao&channel=dm&page=1&from=app&api_ver=v4.2&vcode=4.4.4&sign=CgEFBgNXVFYGUABUXFEDXAcCVANRXAJQU1VUAgQGWgI=";
    public static final String ZY_SOURCE_URL= "http://api.v.2345.com/api.php?ctl=channel&act=getFilterData&category=&district=&station=&page=1&perpage=21&channel=zy&from=app&api_ver=v4.2&vcode=4.4.4&sign=AQUABFYBBFcBUAMCXQIABFdRVwAABQFSVQBXUFVRWgI=";
    public static final String HAPPY_DY_URL = "http://api.v.2345.com/api.php?ctl=channel&act=getZtData&zt=dy_xiju&channel=dy&page=1&from=app&api_ver=v4.2&vcode=4.4.4&sign=CQBUUFVSAQcHA1YEDFMDVVRRDFVTUAZcBgYCUwBbXww=";


    //主界面
    public static String HOME_URL = "http://api.v.2345.com/?ctl=index&api_ver=v4.3&vcode=4.4.4&sign=CgYDBAJXAlEEBlQFXANXXFUBUVBQUQJdV1UGBwUEDgY=";
    public static final String CHANNER_URI = "http://api.v.2345.com/api.php?ctl=channel&api_ver=v4.3&vcode=4.4.4&sign=CgYDBAJXAlEEBlQFXANXXFUBUVBQUQJdV1UGBwUEDgY=";
    public static final String TAB_DY_URI = "http://api.v.2345.com/api.php?ctl=channel&act=getCategory&channel=dy&search=&encode=1&from=app&api_ver=v4.2&vcode=4.4.4&sign=C1BUBVACBFkAV1IDDl9aVFVVBlVTBQBdVFFTAg4EXQA=";
    public static final String MOVIE_B_URI = "http://api.v.2345.com/api.php?ctl=channel&act=getRecommend&cate=jx&channel=dy&from=app&api_ver=v4.2&vcode=4.4.4&sign=CQ0BBAVSAlABCghTWVRaAFMIVAFUXVRQDlINWAAEWFA=";

    public static final String TAB_DS_URL="http://api.v.2345.com/api.php?ctl=channel&act=getCategory&channel=tv&search=&encode=1&from=app&api_ver=v4.2&vcode=4.4.4&sign=WldWVlZaU1VXBQYDWl5aAVIIUVcCXAQGBgAGWFVVDQc=";

    //频道下载专区的url
    public static final String CHANNER_DownLoad__URL = "http://api.v.2345.com/api.php?ctl=dlRecommend&from=app&api_ver=v4.2&vcode=4.4.4&sign=DwxTVFJaUwdVVlNQCV5WAwRSAFJUBwlTBFUHUAJVDgM=";
    //频道下载专区——电影
    public static final String CHANNER_DownLoad_DY_URL = "http://api.v.2345.com/api.php?ctl=dlRecommend&act=getFilterData&category=&district=&year=&page=1&perpage=21&channel=dy&from=app&api_ver=v4.2&vcode=4.4.4&sign=WgRWBVFUAlFRClUJW1NUUQ4FVw9RAAIDVwEMBw9SWFE=";
    //频道下载专区——电视剧
    public static final String CHANNER_DownLoad_TV_URL = "http://api.v.2345.com/api.php?ctl=dlRecommend&act=getFilterData&category=&district=&year=&page=1&perpage=21&channel=tv&from=app&api_ver=v4.2&vcode=4.4.4&sign=W1JTVgVbUVZVAlZTXABQXFUEAQ5RVAlWBwICVAdXAQU=";
    //频道下载专区——动漫
    public static final String CHANNER_DownLoad_DM_URL = "http://api.v.2345.com/api.php?ctl=dlRecommend&act=getFilterData&category=&district=&year=&page=1&perpage=21&channel=dm&from=app&api_ver=v4.2&vcode=4.4.4&sign=XwwGAwNXAFYCUFUCXgBaXQ8IVlBZUgRUAQYAV1YEAQQ=";
    //频道下载专区——综艺：
    public static final String CHANNER_DownLoad_ZY_URL = "http://api.v.2345.com/api.php?ctl=dlRecommend&act=getFilterData&category=&district=&year=&page=1&perpage=21&channel=zy&from=app&api_ver=v4.2&vcode=4.4.4&sign=AQUABFYBBFcBUAMCXQIABFdRVwAABQFSVQBXUFVRWgI=";

    //频道vip专区
    public static final String CHANNER_VIP_HOME_URL = "http://api.v.2345.com/api.php?ctl=custom&act=getArticle&id=4&type=jingxuan&api_ver=v4.3&vcode=4.4.4&sign=W1dUAVkHD1AFAwYJXlIBAA4HVwBUUgBdVFABWAAHXww=";

    //频道影视金曲区
    public static final String CHANNER_JINQU_URL = "http://api.v.2345.com/api.php?ctl=custom&act=getCategory&id=26&api_ver=v4.3&vcode=4.4.4&sign=WwYEC1cBVVAHAAQEWVADVA9RAAJYVwIDDlADV1RbCVI=";
    //频道影视金曲区详情页面
    public static final String CHANNER_JINQU_DETAIL_URL = "http://api.v.2345.com/api.php?ctl=custom&act=getArticle&id=26&type=dianying&api_ver=v4.3&vcode=4.4.4&sign=AQMCAVdRUVhVCwFUXVRSUAJTUwdQXQJUVFUBUwVWWAY=";

    //影院热映区
    public static final String CHANNER_REYING_URL = "http://api.v.2345.com/api.php?ctl=ztDyList&month=0&year=0&vcode=4.4.4&api_ver=v3&sign=DgcFB1EHBVMFBgMIClMBBAYDVwZUUVVQB1QDU1JSD1E%3D";

    //专题列表
    public static final String CHANNER_ZHUANTI_URL ="http://api.v.2345.com/api.php?ctl=custom&act=getList&page%22%20+%20%22=1&api_ver=v4.3&vcode=4.4.4&sign=DgMABlBRA1MEClUHDQIEBFJVUwRVUQRSVAYHAFVbW1c=%22);http://api.v.2345.com/api.php?ctl=custom&act=getList&page%22%20+%20%22=1&api_ver=v4.3&vcode=4.4.4&sign=DgMABlBRA1MEClUHDQIEBFJVUwRVUQRSVAYHAFVbW1c=";
    ;


    //搜索区
    public static final String CHANNER_SEARCH_URL = "http://api.v.2345.com/?ctl=search&act=hotword&vcode=4.4.4&api_ver=v3.1&sign=DAIDUAQFA1kFBwIJClNWV1IEDQ5VUlYGAQJXWQ4DCVY=";
    //搜索文本的基础路径
    //public static final String SEARCH_BASE__URL1 = "http://api.v.2345.com/?ctl=search&act=think&word=";
    //public static final String SEARCH_BASE__URL2 ="&vcode=4.4.4&api_ver=v3.1&sign=DFAEBQRXAVEFUQcGDARQXVIAV1MFBwJWB1RUUVFUXFA=";
    public static final String SEARCH_URL ="http://api.v.2345.com/?ctl=search&act=think&word=%E6%88%91&vcode=4.4.4&api_ver=v3.1&sign=X1ULA1NVUlJVAAcHDQBSVAUEVwAFBwcGBFdRV1JSAAY=";
    //搜索结果的基础路径
    public static final String SEARCH_RESULT__URL="http://api.v.2345.com/?ctl=search&short_video=1&word=wo&perpage=20&encode=1&page=1&from=app&api_ver=v4.2&vcode=4.4.4&sign=W1YHBlgGAVEEAlZUCwBQBwYGBQdVAlZVAVpTUVFTXQA=";




}

