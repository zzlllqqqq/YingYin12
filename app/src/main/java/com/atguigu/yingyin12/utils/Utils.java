package com.atguigu.yingyin12.utils;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by admin on 2016/1/22.
 */
public class Utils {

    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;

    public Utils() {
        // 转换成字符串的时间
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

    }

    /**
     * 把毫秒转换成：1:20:30这里形式
     * @param timeMs
     * @return
     */
    public String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;

        int minutes = (totalSeconds / 60) % 60;

        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds)
                    .toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    /**
     * 判断是否是网络资源
     * @param url
     * @return
     */
    public boolean isNetUrl(String url) {
        boolean result = false;
        if (url != null) {
            if (url.toLowerCase().contains("http") || url.toLowerCase().contains("mms") || url.toLowerCase().contains("rtsp")) {
                result = true;
            }

        }
        return result;

    }

}
