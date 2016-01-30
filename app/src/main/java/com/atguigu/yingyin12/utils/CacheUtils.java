package com.atguigu.yingyin12.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by admin on 2016/1/29.
 */
public class CacheUtils {
    private static SharedPreferences sp;
    private static CacheUtils cacheUtils;

    public static CacheUtils getInstance(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("atguigu", Context.MODE_PRIVATE);
            cacheUtils = new CacheUtils();
        }
        return cacheUtils;
    }


    public void save(String key, boolean value) {
        sp.edit().putBoolean(key, value).commit();
    }

    public boolean getSavedValue(String key) {
        return sp.getBoolean(key, false);
    }

    public void saveString(String key, String value) {
        //判断存储卡是否挂载
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File file = Environment.getExternalStorageDirectory();
                String fileName = MD5Encoder.encode(key);
                file = new File(file + "/YingShi2345", fileName);
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(value.getBytes());
                fos.flush();
                fos.close();
            } else {
                sp.edit().putString(key, value).commit();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据key值获取string类型的数据(默认为"")
     * @param key
     * @return
     */
    public String getStringValue(String key) {
        String result = "";
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File file = Environment.getExternalStorageDirectory();
                String fileName = MD5Encoder.encode(key);
                file = new File(file + "/yingshi2345", fileName);
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(file);
                    byte[] buffer = new byte[1024];
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    int length = -1;
                    while ((length = fis.read(buffer)) != -1) {
                        baos.write(buffer, 0, length);
                    }
                    fis.close();
                    baos.close();
                    result = baos.toString();
                }
            } else {
                result =  sp.getString(key, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 保存int类型的值
     * @param key
     * @param value
     */
    public void saveInt(String key, int value) {
        sp.edit().putInt(key, value).commit();
    }

    /**
     * 根据Key值获取int类型的值(默认为-1)
     * @param key
     * @return
     */
    public int getIntValue(String key) {
        return sp.getInt(key, -1);
    }
    /**
     * 从sp中读取字符串
     * @param context
     * @param key
     * @return
     */
    public static String getStringFromCache(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String string = sp.getString(key, "");
        return string;
    }

    public static void setBooleanToCache(Context context,String key,boolean isGuide){
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, isGuide).commit();
    }

    public static boolean getBooleanFromCache(Context context,String key){
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean isGuide = sp.getBoolean(key, false);
        return isGuide;
    }


    /**
     * 保存字符串到sp
     * @param context
     * @param key
     * @param value
     */
    public static void setStringToCache(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }
}
