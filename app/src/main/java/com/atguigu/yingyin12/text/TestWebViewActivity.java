package com.atguigu.yingyin12.text;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.atguigu.yingyin12.R;
import com.atguigu.yingyin12.utils.ConstantUtiles;

public class TestWebViewActivity extends Activity {
    private WebView web_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        web_view = (WebView) findViewById(R.id.web_view);
        Intent intent = getIntent();
        String html = intent.getStringExtra(ConstantUtiles.HTML);
        WebSettings settings = web_view.getSettings();
        settings.setJavaScriptEnabled(true);
        web_view.loadUrl(html);
    }
}
