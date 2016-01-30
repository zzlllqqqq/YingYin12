package com.atguigu.yingyin12.netvideo;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.yingyin12.R;
import com.atguigu.yingyin12.bean.MediaItem;
import com.atguigu.yingyin12.utils.Utils;
import com.atguigu.yingyin12.view.VitamioVideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;

public class VitamioVideoPlayerActivity extends BaActivity {

    private static final int PROGRESS = 1;
    private static final int HIDE_MEDIACONTROL = 2;
    private static final int SCREEN_DEFAULT = 3;
    private static final int SCREEN_FULL = 4;
    private static final int FINISH = 5;
    private VitamioVideoView videoview;
    private Uri uri;
    private Utils utils;
    private LinearLayout llTop;
    private TextView tvVideoname;
    private LinearLayout ll_loading;
    private LinearLayout ll_buffering;
    private ImageView ivSystemBattery;
    private TextView tvSystemTime;
    private Button btnVideoVoice;
    private SeekBar seekbarVoice;
    private Button btnSwitchPlayer;
    private LinearLayout llBottom;
    private TextView tvCurrentTime;
    private SeekBar seekbarVideo;
    private TextView tvDuration;
    private Button btnVideoBack;
    private Button btnVideoPre;
    private Button btnVideoStartPause;
    private Button btnVideoNext;
    private Button btnVideoSwitchScreen;
    private boolean isActivityDestory = false;
    private MyReceiver receiver;
    private ArrayList<MediaItem> mediaItems;
    private int position;
    //手势识别器
    //1.定义手势识别器，申明
    //2.实例化手势识别器，重新双击，单击，长按方法
    //3.接收事件onTouchEvent();
    private GestureDetector detector;
    private boolean isHideMediaControl = false;
    //视频原有尺寸
    private int videoWidth;
    private int videoHeiget;
    private int screenWidth;
    private int screenHeight;
    private boolean isScreenFull = false;
    //声音服务
    private AudioManager am;
    //当前音量
    private int currentVolume;
    // 最大音量
    private int maxVolume;
    //是否是静音
    private boolean isMute = false;
    //始坐标
    private float startY;
    // 滑动的区域
    private float touchRang;
    // 当前的音量
    private int mVol;
    private int preCurrentPosition;
    private boolean isNetUrl;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROGRESS:
                    //得到当前进度  当前秒
                    int currentPosition = (int) videoview.getCurrentPosition();
                    //得到当前播放的进度 - 记录前一秒的进度 < 500; 卡
                    int buffering = currentPosition - preCurrentPosition;
                    if (videoview.isPlaying() && buffering < 500 && isNetUrl) {
                        ll_buffering.setVisibility(View.VISIBLE);
                    } else {
                        ll_buffering.setVisibility(View.GONE);
                    }
                    preCurrentPosition = currentPosition;
                    tvCurrentTime.setText(utils.stringForTime(currentPosition));
                    seekbarVideo.setProgress(currentPosition);
                    tvSystemTime.setText(getSystemTime());
                    //设置视频缓冲
                    if (isNetUrl) {
                        int buffer = videoview.getBufferPercentage();
                        int totalBuffer = seekbarVideo.getMax() * buffer;
                        int secondBuffer = totalBuffer / 100;
                        seekbarVideo.setSecondaryProgress(secondBuffer);
                    } else {
                        seekbarVideo.setSecondaryProgress(0);
                    }
                    if (!isActivityDestory) {
                        handler.removeMessages(PROGRESS);
                        handler.sendEmptyMessageDelayed(PROGRESS, 1000);
                    }
                    break;
                case HIDE_MEDIACONTROL:
                    hideMediaControl();
                    break;
                case FINISH:
                    finish();
                    break;
            }
        }
    };

    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findViews();
        getData();
        setListener();
        setData();
    }

    @Override
    protected void onDestroy() {
        isActivityDestory = true;
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void setData() {
        //提供播放的地址
        if (mediaItems != null && mediaItems.size() > 0) {
            MediaItem mediaItem = mediaItems.get(position);
            tvVideoname.setText(mediaItem.getTitle());
            videoview.setVideoPath(mediaItem.getData());
            isNetUrl = utils.isNetUrl(mediaItem.getData());
        } else if (uri != null) {
            videoview.setVideoURI(uri);
            tvVideoname.setText(uri.toString());
            isNetUrl = utils.isNetUrl(uri.toString());
        }
        setButtonStatus();
    }

    private void setButtonStatus() {
        if (mediaItems != null && mediaItems.size() > 0) {
            if (position == 0) {//上一个按钮变灰，并且不可用点击
                btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                btnVideoPre.setEnabled(false);
            } else if (position == mediaItems.size() - 1) {//下一个按钮变灰，并且不可用点击
                btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                btnVideoNext.setEnabled(false);
            } else {
                //把按钮设置默认和可以点击
                btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
                btnVideoPre.setEnabled(true);
                btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
                btnVideoNext.setEnabled(true);
            }
        } else if (uri != null) {//播放源来自，本地文件夹，浏览器，网络
            btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
            btnVideoPre.setEnabled(false);
            btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
            btnVideoNext.setEnabled(false);
        }
    }

    private void setListener() {
        //监听视频的拖动-SeeKBar的状态
        seekbarVideo.setOnSeekBarChangeListener(new VideoOnSeekBarChangeListener());
        //设置准备好的监听
        videoview.setOnPreparedListener(new MyOnPreparedListener());
        //播放完成监听
        videoview.setOnCompletionListener(new MyOnCompletionListener());
        //监听音量的seekBar
        seekbarVoice.setOnSeekBarChangeListener(new VoiceOnSeekBarChangeListener());
        //播放出错的监听
        videoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                showErrorDialog();
                return true;
            }
        });
        //设置卡监听
        videoview.setOnInfoListener(new VideoOnInfoListener());
    }

    private void showErrorDialog() {
        AlertDialog.Builder builder = new  AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("视频播放出错了，请检查网络是否异常，文件是否存在");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    private void getData() {
        Intent intent = getIntent();
        uri = intent.getData();
        mediaItems = (ArrayList<MediaItem>) (intent.getSerializableExtra("videolist"));
        position = intent.getIntExtra("position", 0);
    }

    private void initData() {
        utils = new Utils();
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);
        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (isHideMediaControl) {
                    showMediaControl();
                    handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROL, 5000);
                } else {
                    hideMediaControl();
                    handler.removeMessages(HIDE_MEDIACONTROL);
                }
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                switchVideoSize();
                return super.onDoubleTap(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                startAndPause();
                super.onLongPress(e);
            }
        });

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();

        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

    }

    private void hideMediaControl() {
        llBottom.setVisibility(View.GONE);
        llTop.setVisibility(View.GONE);
        isHideMediaControl = true;
    }

    private void showMediaControl() {
        llTop.setVisibility(View.VISIBLE);
        llBottom.setVisibility(View.VISIBLE);
        isHideMediaControl = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = event.getY();
                touchRang = Math.min(screenWidth, screenHeight);
                mVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                handler.removeMessages(HIDE_MEDIACONTROL);
                break;
            case MotionEvent.ACTION_MOVE:
                float endY = event.getY();
                float distanceY = startY - endY;
                float delta = (distanceY / touchRang) * maxVolume;
                float volume = Math.min(Math.max(mVol + delta, 0), maxVolume);
                if (delta != 0) {
                    updatavolumeProgress((int) volume);
                }
                break;
            case MotionEvent.ACTION_UP:
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROL, 5000);
                break;
        }
        return super.onTouchEvent(event);
    }

    private void updatavolumeProgress(int volume) {
        am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        seekbarVoice.setProgress(volume);
        currentVolume = volume;
    }

    private void findViews() {
        videoview = (VitamioVideoView) findViewById(R.id.videoview);
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        ll_buffering = (LinearLayout) findViewById(R.id.ll_buffering);
        tvVideoname = (TextView) findViewById(R.id.tv_videoname);
        ivSystemBattery = (ImageView) findViewById(R.id.iv_system_battery);
        tvSystemTime = (TextView) findViewById(R.id.tv_system_time);
        btnVideoVoice = (Button) findViewById(R.id.btn_video_voice);
        seekbarVoice = (SeekBar) findViewById(R.id.seekbar_voice);
        btnSwitchPlayer = (Button) findViewById(R.id.btn_switch_player);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tvCurrentTime = (TextView) findViewById(R.id.tv_current_time);
        seekbarVideo = (SeekBar) findViewById(R.id.seekbar_video);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        btnVideoBack = (Button) findViewById(R.id.btn_video_back);
        btnVideoPre = (Button) findViewById(R.id.btn_video_pre);
        btnVideoStartPause = (Button) findViewById(R.id.btn_video_start_pause);
        btnVideoNext = (Button) findViewById(R.id.btn_video_next);
        btnVideoSwitchScreen = (Button) findViewById(R.id.btn_video_switch_screen);

        //隐藏标题栏
        fl_titlebar.setVisibility(View.GONE);

        btnVideoVoice.setOnClickListener(this);
        btnSwitchPlayer.setOnClickListener(this);
        btnVideoBack.setOnClickListener(this);
        btnVideoPre.setOnClickListener(this);
        btnVideoStartPause.setOnClickListener(this);
        btnVideoNext.setOnClickListener(this);
        btnVideoSwitchScreen.setOnClickListener(this);

        seekbarVoice.setMax(maxVolume);
        seekbarVoice.setProgress(currentVolume);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == btnVideoVoice) {
            isMute = !isMute;
            updatavolume(currentVolume);
        } else if (v == btnSwitchPlayer) {
            showSwitchPlayerDialog();
        } else if (v == btnVideoBack) {
            finish();
        } else if (v == btnVideoPre) {
            playPreVideo();
        } else if (v == btnVideoStartPause) {
            startAndPause();
        } else if (v == btnVideoNext) {
            playNextVideo();
        } else if (v == btnVideoSwitchScreen) {
            switchVideoSize();
        }
        handler.removeMessages(HIDE_MEDIACONTROL);
        handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROL, 5000);
    }

    private void showSwitchPlayerDialog() {
        new AlertDialog.Builder(this)
                .setTitle("是否切换播放器")
                .setMessage("当前播放是万能播放器播放视频，是否切换到系统播放器播放")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startSystemPlayer();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void startSystemPlayer() {
        Intent intent = new Intent();
        if (mediaItems != null && mediaItems.size() > 0) {
            Bundle bundle = new Bundle();
            //传递播放列表
            bundle.putSerializable("videolist", mediaItems);
            intent.putExtras(bundle);
            //传递播放位置
            intent.putExtra("position", position);
        } else if (uri != null) {
            //从浏览器、文件夹、QQ空间等打开视频。
            intent.setData(uri);
        }
        startActivity(intent);
        handler.sendEmptyMessageDelayed(FINISH, 1500);
    }

    private void updatavolume(int volume) {
        if (isMute) {
            am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            seekbarVoice.setProgress(0);
        } else {
            am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
            seekbarVoice.setProgress(volume);
            currentVolume = volume;
        }
    }

    private void switchVideoSize() {
        if (isScreenFull) {
            setVideoType(SCREEN_DEFAULT);
        } else {
            setVideoType(SCREEN_FULL);
        }
    }

    private void startAndPause() {
        if (videoview.isPlaying()) {
            videoview.pause();
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_selector);
        } else {
            videoview.start();
            btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
        }
    }

    @Override
    public View getContentView() {
        Vitamio.isInitialized(this);
        return View.inflate(VitamioVideoPlayerActivity.this, R.layout.activity_vitamiovideo_player, null);
    }

    @Override
    protected void clickRightButton() {

    }

    @Override
    protected void clickLeftButton() {

    }


    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            videoWidth = mp.getVideoWidth();
            videoHeiget = mp.getVideoHeight();
            ll_loading.setVisibility(View.GONE);
            videoview.start();
            //设置总时长
            int duration = (int) videoview.getDuration();
            tvDuration.setText(utils.stringForTime(duration));
            seekbarVideo.setMax(duration);
            hideMediaControl();
            setVideoType(SCREEN_DEFAULT);
            handler.sendEmptyMessage(PROGRESS);
        }
    }

    private void setVideoType(int screenDefault) {
        switch (screenDefault) {
            case SCREEN_FULL:
                videoview.setVideoSize(screenWidth, screenHeight);
                isScreenFull = true;
                btnVideoSwitchScreen.setBackgroundResource(R.drawable.btn_video_switch_screen_default_selector);
                break;
            case SCREEN_DEFAULT:
                int mVideoWidth = videoWidth;
                int mVideoHeight = videoHeiget;

                int width = screenWidth;
                int height = screenHeight;
                if (mVideoWidth > 0 && mVideoHeight > 0) {
                    if (mVideoWidth * height < width * mVideoHeight) {
                        width = height * mVideoWidth / mVideoHeight;
                    } else if (mVideoWidth * height > width * mVideoHeight) {
                        height = width * mVideoHeight / mVideoWidth;
                    }
                }
                videoview.setVideoSize(width, height);
                isScreenFull = false;
                btnVideoSwitchScreen.setBackgroundResource(R.drawable.btn_video_switch_screen_full_selector);
                break;
        }
    }

    class VideoOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        //        * @param progress 视频要拖动到的位置
        //        * @param fromUser 当状态改变是由用户引起的时候，这个值为true,默认为false
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                videoview.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);
            setBatteryStatus(level);
        }
    }

    private void setBatteryStatus(int level) {
        if (level <= 0) {
            ivSystemBattery.setImageResource(R.drawable.ic_battery_0);
        } else if (level <= 10) {
            ivSystemBattery.setImageResource(R.drawable.ic_battery_10);
        } else if (level <= 20) {
            ivSystemBattery.setImageResource(R.drawable.ic_battery_20);
        } else if (level <= 40) {
            ivSystemBattery.setImageResource(R.drawable.ic_battery_40);
        } else if (level <= 60) {
            ivSystemBattery.setImageResource(R.drawable.ic_battery_60);
        } else if (level <= 80) {
            ivSystemBattery.setImageResource(R.drawable.ic_battery_80);
        } else if (level <= 100) {
            ivSystemBattery.setImageResource(R.drawable.ic_battery_100);
        } else {
            ivSystemBattery.setImageResource(R.drawable.ic_battery_100);
        }
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            playNextVideo();
        }
    }

    private void playNextVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            position++;
            if (position < mediaItems.size()) {
                MediaItem mediaItem = mediaItems.get(position);
                tvVideoname.setText(mediaItem.getTitle());
                videoview.setVideoPath(mediaItem.getData());
                isNetUrl = utils.isNetUrl(mediaItem.getData());
                ll_loading.setVisibility(View.VISIBLE);
                setButtonStatus();
                if (position == mediaItems.size() - 1) {
                    Toast.makeText(VitamioVideoPlayerActivity.this, "已经最后一个视频了", Toast.LENGTH_SHORT).show();
                }
            } else {
                finish();
            }
        }
    }

    private void playPreVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            position--;
            if (position >= 0) {
                MediaItem mediaItem = mediaItems.get(position);
                tvVideoname.setText(mediaItem.getTitle());
                videoview.setVideoPath(mediaItem.getData());
                isNetUrl = utils.isNetUrl(mediaItem.getData());
                ll_loading.setVisibility(View.VISIBLE);
                setButtonStatus();
            } else {
                position = 0;
            }
        }
    }

    class VoiceOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                seekBar.setProgress(progress);
            }
        }

        //当手一触碰的时候回调这个方法
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            handler.removeMessages(HIDE_MEDIACONTROL);
        }

        //手指离开屏幕的时候，回调这个方法
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROL, 5000);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            currentVolume--;
            updatavolumeProgress(currentVolume);
            handler.removeMessages(HIDE_MEDIACONTROL);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROL, 5000);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            currentVolume++;
            updatavolumeProgress(currentVolume);
            handler.removeMessages(HIDE_MEDIACONTROL);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROL, 5000);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class VideoOnInfoListener implements MediaPlayer.OnInfoListener {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
//            switch (what) {
//                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//                    ll_buffering.setVisibility(View.VISIBLE);
//                    break;
//                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//                    ll_buffering.setVisibility(View.GONE);
//                    break;
//            }
            return true;
        }
    }
}
