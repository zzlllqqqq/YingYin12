package com.atguigu.yingyin12.utils;

import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.atguigu.yingyin12.R;

/**
 * Created by admin on 2016/1/29.
 */
public class ImageUtils {

    /**
     * 网络请求图片
     * @param imageurl
     */
    public static void loaderImager(final ImageView imageView, String imageurl) {
        ImageLoader imageLoader = VolleyManager.getImageLoader();
        imageView.setTag(imageurl);
        //直接在这里请求会乱位置
        ImageLoader.ImageListener listener = new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageContainer != null) {

                    if (imageView != null) {
                        if (imageContainer.getBitmap() != null) {
                            imageView.setImageBitmap(imageContainer.getBitmap());
                        } else {
                            imageView.setImageResource(R.drawable.channel_defult_up);
                        }
                    }
                }
            }
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //如果出错，则说明都不显示（简单处理），最好准备一张出错图片
                imageView.setImageResource(R.drawable.channel_defult_up);
            }
        };
        imageLoader.get(imageurl, listener);
    }
}
