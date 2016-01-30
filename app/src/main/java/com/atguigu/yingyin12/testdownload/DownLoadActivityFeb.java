package com.atguigu.yingyin12.testdownload;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.yingyin12.R;

import java.io.File;

public class DownLoadActivityFeb extends Activity {
    private static final int PROCESSING = 1;
    private static final int FAILURE = -1;

    private TextView resultView;
    private Button downloadButton;
    private Button stopButton;
    private ProgressBar progressBar;

    private Handler handler = new UIHandler();
    private boolean isState = false;

    private final class UIHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROCESSING:
                    progressBar.setProgress(msg.getData().getInt("size"));
                    float num = (float) progressBar.getProgress() / (float) progressBar.getMax();
                    int result = (int) (num * 100);
                    resultView.setText(result + "%");
                    if (progressBar.getProgress() == progressBar.getMax()) {
                        Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_LONG).show();
                    }
                    break;
                case FAILURE:
                    Toast.makeText(getApplicationContext(), "出错", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load_activity_feb);

        resultView = (TextView) findViewById(R.id.resultView);
        downloadButton = (Button) findViewById(R.id.downloadbutton);
        stopButton = (Button) findViewById(R.id.stopbutton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        startDownLoad();

        ButtonClickListener listener = new ButtonClickListener();



        downloadButton.setOnClickListener(listener);
        stopButton.setOnClickListener(listener);
    }



    private final class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.downloadbutton:
                    startDownLoad();
                    downloadButton.setEnabled(false);
                    stopButton.setEnabled(true);
                    break;
                case R.id.stopbutton:

                    if(isState) {
                        Toast.makeText(getApplicationContext(), "Now thread is Stopping!!", Toast.LENGTH_LONG).show();
                        downloadButton.setEnabled(true);
                        stopButton.setEnabled(false);
                        isState = false;
                        exit();
                    } else {
                        startDownLoad();
                        Toast.makeText(getApplicationContext(), "1-1", Toast.LENGTH_LONG).show();
                        downloadButton.setEnabled(true);
                        stopButton.setEnabled(true);
                    }

                    break;
            }
        }


    }

    private void startDownLoad() {
        String path = "http://192.168.1.153:8080/321_video_test%20-%203.mp4";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //File savDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
            File savDir = Environment.getExternalStorageDirectory();
            download(path, savDir);
        } else {
            Toast.makeText(getApplicationContext(), "SD卡不存在！！！", Toast.LENGTH_LONG).show();
        }
        downloadButton.setEnabled(false);
        stopButton.setEnabled(true);
        isState = true;
    }

    private DownloadTask task;

    private void exit() {
        if (task != null)
            task.exit();
    }

    private void download(String path, File savDir) {
        task = new DownloadTask(path, savDir);
        new Thread(task).start();
    }

    private final class DownloadTask implements Runnable {
        private String path;
        private File saveDir;
        private FileDownloader loader;

        public DownloadTask(String path, File saveDir) {
            this.path = path;
            this.saveDir = saveDir;
        }

        public void exit() {
            if (loader != null)
                loader.exit();
        }

        DownloadProgressListener downloadProgressListener = new DownloadProgressListener() {
            @Override
            public void onDownloadSize(int size) {
                Message msg = new Message();
                msg.what = PROCESSING;
                msg.getData().putInt("size", size);
                handler.sendMessage(msg);
            }
        };

        public void run() {
            try {
                loader = new FileDownloader(getApplicationContext(), path, saveDir, 3);
                progressBar.setMax(loader.getFileSize());
                loader.download(downloadProgressListener);
            } catch (Exception e) {
                e.printStackTrace();
                handler.sendMessage(handler.obtainMessage(FAILURE));
            }
        }
    }
}
