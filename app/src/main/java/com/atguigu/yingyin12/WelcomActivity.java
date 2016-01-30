package com.atguigu.yingyin12;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class WelcomActivity extends Activity {

    /**
     * 版本号
     */
    private TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        tv_version = (TextView) findViewById(R.id.tv_version);
        getVersion();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switchMainActivity();
            }
        }, 2000);
    }

    /**
     * 切换到mainActivity
     */
    private void switchMainActivity() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
        overridePendingTransition(R.anim.right_in_anim, R.anim.left_out_anim);
    }

    /**
     * 获取版本号
     */
    private void getVersion() {
        int currentVersionCode = 0;
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String appVersionName = info.versionName; // 版本名
            currentVersionCode = info.versionCode; // 版本号
            tv_version.setText("V" + currentVersionCode);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch blockd
            e.printStackTrace();
        }
    }


}
