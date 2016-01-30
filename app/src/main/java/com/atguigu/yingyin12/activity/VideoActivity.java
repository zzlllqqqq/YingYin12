package com.atguigu.yingyin12.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.atguigu.yingyin12.R;
import com.atguigu.yingyin12.domain.VideoInfo;
import com.atguigu.yingyin12.domain.VideoUrlBean;
import com.atguigu.yingyin12.eventbus.VideoPosition;
import com.atguigu.yingyin12.utils.CacheUtils;
import com.atguigu.yingyin12.utils.Utils;
import com.atguigu.yingyin12.utils.VolleyManager;
import com.atguigu.yingyin12.view.VitamioVideoView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;

public class VideoActivity extends Activity implements View.OnClickListener {
    private static final int WHAT_SUCCESS = 1;  //视频准备完成,同步进度条
    private static final int WHAT_DETECTOR = 2; //屏幕控制器
    private static final int WHAT_CLICK_BACK = 3; //点击back键返回
    private static final int WHAT_KB = 4; //更新流量显示

    /**
     * 视频播放
     */
    private VitamioVideoView vvv_video;
    /**
     * 返回
     */
    private ImageButton ib_back_video;
    /**
     * 电影名称
     */
    private TextView tv_video_title;
    /**
     * 电影进度
     */
    private SeekBar sb_video_bar;
    /**
     * 声音所在的布局
     */
    private LinearLayout ll_video_voice;
    /**
     * 调节灯光所在的布局
     */
    private LinearLayout ll_video_light;
    /**
     * 全屏切换按钮
     */
    private ImageButton ib_play_screen;
    /**
     * 视频播放的时长
     */
    private TextView tv_play_time;
    /**
     * 本地视频播放时,切换都下一个
     */
    private ImageButton ib_play_next;
    /**
     * 声音图标
     */
    private ImageView iv_video_voice;
    /**
     * 屏幕中中间的播放按钮
     */
    private ImageView iv_play_click;
    /**
     * 切换暂停和播放的状态
     */
    private ImageButton ib_play_pause;
    /**
     * 声音大小
     */
    private TextView tv_voice_number;
    /**
     * 灯光大小
     */
    private TextView tv_light_number;
    /**
     * 屏幕控制器
     */
    private RelativeLayout rl_controler;
    /**
     * 屏幕中间的快进进度
     */
    private TextView tv_voide_progress;
    /**
     * 切换画质
     */
    private Button btn_video_select;
    /**
     * 本地视频信息
     */
    private ArrayList<VideoInfo> videoInfos;
    /**
     * 视频播放之前的加载提示
     */
    private LinearLayout ll_video_loading;
    /**
     * 视频缓冲时显示缓冲的流量
     */
    private TextView tv_video_flow;
    /**
     * 记录本地视频列表的位置
     */
    private int currentPosition;
    /**
     * 网络播放url
     */
    private Uri uri;
    /**
     * 转换时间格式的工具类
     */
    private Utils utils;

    /**
     * 手势识别器
     */
    private GestureDetector detector;

    /**
     * 屏幕的宽和高
     */
    private int screenWidth;
    private int screenHeight;
    /**
     * 正在缓冲的状体
     */
    private LinearLayout ll_buffer;

    /**
     * 视屏的宽和高
     */
    private float mVideoWidth;
    private float mVideoHeight;
    private long s_KB; //一秒内增加的流量
    private static String key = "position";
    /**
     * 处理消息的handler
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_SUCCESS:
                    setSeekBarAndTime();
                    removeMessages(WHAT_SUCCESS);
                    sendEmptyMessageDelayed(WHAT_SUCCESS, 1000);
                    break;
                case WHAT_DETECTOR:
                    rl_controler.setVisibility(View.GONE);
                    break;
                case WHAT_CLICK_BACK:
                    clickBackNumber = 0;
                    break;
                case WHAT_KB:
                    s_KB = (Long) msg.obj;
                    tv_video_flow.setText("视频加载中(" + s_KB + "K/S)...");
                    break;

                default:
                    break;
            }
        }
    };
    /**
     * 手势识别器的监听
     */
    private android.view.GestureDetector.OnGestureListener detectorListenter = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public void onLongPress(MotionEvent e) { //长按屏幕的时候调用
            super.onLongPress(e);
//            switchPlayVideo();
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) { //双击屏幕的时候调用
            switchScreenType();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) { //点击屏幕的时候调用
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
            switchDetector();
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            return super.onScroll(e1, e2, distanceX, distanceY);
        }

    };
    private boolean isShow;
    /**
     * 监听拖动卡和播放卡
     */
    private MediaPlayer.OnInfoListener mOnInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            switch (what) {
                case MediaPlayer.MEDIA_INFO_BUFFERING_START: //当卡的时候和拖动卡的时候调用
                    ll_buffer.setVisibility(View.VISIBLE);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END: //当卡的时候和拖动卡的时候调用(结束)
                    ll_buffer.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            return true;
        }
    };
    private AudioManager am;
    private float currentVolume;
    private int currentMaxVolume;
    private int brightness;
    /**
     * 注册视频播放完成的监听
     */
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.pause();
            saveAndSendCurrentPosition(true);
            finish();
        }
    };
    /**
     * 设置videoView的缓冲监听
     */
    private MediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            sb_video_bar.setSecondaryProgress(percent);
        }
    };

    /**
     * 切换屏幕控制器的显示状态
     */
    private void switchDetector() {
        if (isShow) {
            rl_controler.setVisibility(View.GONE);
            handler.removeMessages(WHAT_DETECTOR);
            isShow = false;
        } else {
            isShow = true;
            rl_controler.setVisibility(View.VISIBLE);
            handler.removeMessages(WHAT_DETECTOR);
            handler.sendEmptyMessageDelayed(WHAT_DETECTOR, 5000);
        }
    }

    /**
     * 标识当前是否是全屏播放
     */
    private boolean isScreen;
    /**
     * seekBar状态改变的监听
     */
    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                vvv_video.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    /**
     * 切换屏幕的类型(全屏和适应)
     */
    private void switchScreenType() {
        if (isScreen) {
            setDefaultScreen();
        } else {
            setScreenFull();
        }

    }

    /**
     * 设置为全屏播放
     */
    private void setScreenFull() {
        vvv_video.setVideoSize(screenWidth, screenHeight);
        isScreen = true;
        ib_play_screen.setImageResource(R.drawable.ic_player_normal);
    }

    /**
     * 设置默认屏幕播放
     */
    private void setDefaultScreen() {
        /**
         * 屏幕的宽和高
         */
        int width = (int) screenWidth;
        int height = (int) screenHeight;

        if (mVideoWidth > 0 && mVideoHeight > 0) {
            if (mVideoWidth * height < width * mVideoHeight) {
                //Log.i("@@@", "image too wide, correcting");
                width = (int) (height * mVideoWidth / mVideoHeight);
            } else if (mVideoWidth * height > width * mVideoHeight) {
                //Log.i("@@@", "image too tall, correcting");
                height = (int) (width * mVideoHeight / mVideoWidth);
            }
        }
        vvv_video.setVideoSize(width, height);
        isScreen = false;
        ib_play_screen.setImageResource(R.drawable.ic_player_full);

    }

    /**
     * 长按屏幕的时候切换播放状态
     */
    private void switchPlayVideo() {
        if (vvv_video.isPlaying()) {
            vvv_video.pause();
            ib_play_pause.setImageResource(R.drawable.ic_player_playbtn);
            iv_play_click.setVisibility(View.VISIBLE);
            handler.removeMessages(WHAT_SUCCESS);
        } else {
            vvv_video.start();
            ib_play_pause.setImageResource(R.drawable.ic_player_pause);
            iv_play_click.setVisibility(View.GONE);
            handler.sendEmptyMessage(WHAT_SUCCESS);
        }
    }

    /**
     * 设置seekBar的进度和是时间
     */
    private void setSeekBarAndTime() {
        long currentPosition = vvv_video.getCurrentPosition();
        tv_video_title.setText(videoUrlBean.getTitle());
        sb_video_bar.setProgress((int) currentPosition);
        String time = utils.stringForTime((int) currentPosition);
        tv_play_time.setText(time + "/" + videoUrlBean.getDuration());
    }

    /**
     * 设置视频准备好了的监听
     */
    private MediaPlayer.OnPreparedListener mOnPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            ll_video_loading.setVisibility(View.GONE);
            mp.setPlaybackSpeed(1.0f);
            vvv_video.start();
            if (videoUrlBean != null) {
                key = "POSITION";
                int value = CacheUtils.getInstance(VideoActivity.this).getIntValue(key);
                if (value != -1) {
                    vvv_video.seekTo(value);
                    currentPlayPosition = value;
                    Log.e("TAG", "++++++" + value);
                }
            }
            synSeekBarProgress();
            handler.sendEmptyMessageDelayed(WHAT_DETECTOR, 5000);
        }
    };
    private VideoUrlBean videoUrlBean;

    /**
     * 同步视频播放seekBar的进度和总时长
     */

    private void synSeekBarProgress() {
        vvv_video.seekTo(currentPlayPosition);
        //设置总时长和初始化位置
        tv_play_time.setText("00:00:00/" + videoUrlBean.getDuration());
        long duration = vvv_video.getDuration();
        sb_video_bar.setMax((int) duration);

        handler.sendEmptyMessage(WHAT_SUCCESS);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Vitamio.isInitialized(this)) {
            return;
        }
        setContentView(R.layout.activity_video);
        initView();
        initData();
        //获取url并且设置播放地址
        getDataOrUrl();
        setListener();
    }

    /**
     * 初始化对象
     */
    private void initData() {
        utils = new Utils();
        vvv_video.requestFocus();
        detector = new GestureDetector(this, detectorListenter);
        //获取屏幕的宽和高
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
        //初始化声音管理器
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        //获取当前的音量
        currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        //获取当前的最大音量
        currentMaxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        setScreenMode(0);//设置当前调节屏幕亮度的模式为手动调节;1:自动调节,0:手动调节
        brightness = getScreenBrightness();
        setScreenBrightness(100);
        saveScreenBrightness(100);
        tv_light_number.setText((int) ((float) (brightness / 255) * 100) + "%");//设置当前的亮度
        Timer timer = new Timer();
        timer.schedule(task, 1000, 1000);


    }

    /**
     * 设置监听
     */
    private void setListener() {
        vvv_video.setOnPreparedListener(mOnPreparedListener);
        ib_play_pause.setOnClickListener(this);
        btn_video_select.setOnClickListener(this);
        ib_back_video.setOnClickListener(this);
        sb_video_bar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        vvv_video.setOnInfoListener(mOnInfoListener);
        vvv_video.setOnCompletionListener(mOnCompletionListener); //注册视频播放完成的监听
        ib_play_screen.setOnClickListener(this);
        vvv_video.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        iv_play_click.setOnClickListener(this);

    }

    /**
     * 初始化布局
     */
    private void initView() {
        ib_play_pause = (ImageButton) findViewById(R.id.ib_play_pause);
        vvv_video = (VitamioVideoView) findViewById(R.id.vvv_video);
        ib_back_video = (ImageButton) findViewById(R.id.ib_back_video);
        tv_video_title = (TextView) findViewById(R.id.tv_video_title);
        sb_video_bar = (SeekBar) findViewById(R.id.sb_video_bar);
        ib_play_screen = (ImageButton) findViewById(R.id.ib_play_screen);
        ll_video_voice = (LinearLayout) findViewById(R.id.ll_video_voice);
        ll_video_light = (LinearLayout) findViewById(R.id.ll_video_light);
        iv_video_voice = (ImageView) findViewById(R.id.iv_video_voice);
        tv_voice_number = (TextView) findViewById(R.id.tv_voice_number);
        tv_light_number = (TextView) findViewById(R.id.tv_light_number);
        ib_play_next = (ImageButton) findViewById(R.id.ib_play_next);
        ll_video_loading = (LinearLayout) findViewById(R.id.ll_video_loading);
        tv_play_time = (TextView) findViewById(R.id.tv_play_time);
        iv_play_click = (ImageView) findViewById(R.id.iv_play_click);
        btn_video_select = (Button) findViewById(R.id.btn_video_select);
        ll_buffer = (LinearLayout) findViewById(R.id.ll_buffer);
        rl_controler = (RelativeLayout) findViewById(R.id.rl_controler);
        tv_voide_progress = (TextView) findViewById(R.id.tv_voice_progress);
        tv_video_flow = (TextView) findViewById(R.id.tv_video_flow);
    }

    /**
     * 获取播放列表或者网络播放的url
     */
    private void getDataOrUrl() {
        ll_video_loading.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        videoInfos = (ArrayList<VideoInfo>) intent.getSerializableExtra("videoInfos");
        int position = intent.getIntExtra("position", 0);
        String url = intent.getStringExtra("URL");
        if (url != null) {
            ll_video_loading.setVisibility(View.VISIBLE);
            getDataFromNet(url);
            return;
        }
        currentPosition = position;
        if (videoInfos != null && !videoInfos.isEmpty()) {
            VideoInfo videoInfo = videoInfos.get(position);
            String data = videoInfo.getData();
            String title = videoInfo.getTitle();
            tv_video_title.setText(title);
            vvv_video.setVideoPath(data);
            if (position == videoInfos.size() - 1) {
                Toast.makeText(this, "已经是最后一个视频", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * 从网络获取数据
     *
     * @param url
     */
    private void getDataFromNet(String url) {
        RequestQueue queue = VolleyManager.getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parseJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.toString());
            }
        });
        queue.add(request);
    }

    /**
     * 解析json数据
     *
     * @param response
     */
    private void parseJson(JSONObject response) {
        if (response.has("info")) {
            JSONObject object = response.optJSONObject("info");
            videoUrlBean = new VideoUrlBean();
            String duration = object.optString("duration");
            videoUrlBean.setDuration(duration);
            String hightUrl = object.optString("player_url_height");
            videoUrlBean.setPlayer_url_height(hightUrl);
            String title = object.optString("title");
            videoUrlBean.setTitle(title);
            String player_url_super = object.optString("player_url_super");
            videoUrlBean.setPlayer_url_super(player_url_super);
            String player_url = object.optString("player_url");
            videoUrlBean.setPlayer_url(player_url);
        }
        //设置默认超清的播放地址
        vvv_video.setVideoPath(videoUrlBean.getPlayer_url_super());
    }

    private float startY;
    private float startX;
    private float touchMaxRang, touchMaxWidth;
    private boolean isLeftScreen;//用来标识当前触摸的是否是屏幕左半屏
    private boolean isProgress;//用来标识是否是在快进

    /**
     * 把触摸事件传递给手势识别器
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (videoUrlBean == null) {
            return false;
        }
        detector.onTouchEvent(event);
        float eventY = event.getY();
        float eventX = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handler.removeMessages(WHAT_DETECTOR);
                startY = eventY;
                startX = eventX;
                touchMaxRang = Math.min(screenWidth, screenHeight);
                touchMaxWidth = Math.max(screenWidth, screenHeight);

                //判断是否触摸的是左半屏
                if (startX <= Math.max(screenWidth, screenHeight) / 2) {
                    isLeftScreen = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //获取滑动的位移,有可能为负也有可能为正
                float dy = startY - eventY;
                float dx = eventX - startX;
                if (Math.abs(dx) > Math.abs(dy) && Math.abs(dx) > 10) {
                    isProgress = true;
                    tv_voide_progress.setVisibility(View.VISIBLE);
                    ll_video_light.setVisibility(View.GONE);
                    ll_video_voice.setVisibility(View.GONE);
                    float percent = dx / touchMaxWidth / 100;
                    //获取增加或减少的进度
                    float progress = vvv_video.getDuration() * percent;
                    //获取真实的进度
                    progress = progress + vvv_video.getCurrentPosition();
                    if (progress > vvv_video.getDuration()) {
                        progress = vvv_video.getDuration();
                    } else if (progress < 0) {
                        progress = 0;

                    }
                    tv_voide_progress.setText(utils.stringForTime((int) progress));
                    currentPlayPosition = (long) progress;
                    sb_video_bar.setProgress((int) progress);
                    String time = utils.stringForTime((int) progress);
                    tv_play_time.setText(time + "/" + videoUrlBean.getDuration());

                } else if (!isProgress) {
                    //计算dx与touchMaxRang的百分比
                    //太敏感
                    float percent = dy / touchMaxRang;
                    percent = percent / 3;//让敏感度降低3倍
                    if (isLeftScreen) {
                        setLight(percent);
                    } else {
                        setVolume(percent);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                if (isProgress) {
                    vvv_video.seekTo(currentPlayPosition);
                }
                isProgress = false;
                isLeftScreen = false;
                handler.sendEmptyMessageDelayed(WHAT_DETECTOR, 5000);
                ll_video_voice.setVisibility(View.GONE);
                ll_video_light.setVisibility(View.GONE);
                tv_voide_progress.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        return true;
    }


    private long currentPlayPosition;//记录当前播放位置

    /**
     * 动态设置亮度
     *
     * @param percent
     */
    private void setLight(float percent) {
        float slideLight = percent * 255;
        float light = slideLight + brightness;
        if (light > 255) {
            light = 255;
        } else if (light < 0) {
            light = 0;
        }
        float lightPercent = light / 255;
        ll_video_light.setVisibility(View.VISIBLE);
        tv_light_number.setText((int) (lightPercent * 100) + "%");
        setScreenBrightness((int) light);
        saveScreenBrightness((int) light);
        brightness = (int) light;
    }

    /**
     * 动态设置声音
     *
     * @param percent
     */
    private void setVolume(float percent) {
        //与音量连接在一起
        ll_video_voice.setVisibility(View.VISIBLE);
        float slideVolume = percent * currentMaxVolume / 20;
        float volume = (slideVolume + currentVolume) * 100;
        if (volume > currentMaxVolume * 100) {
            volume = currentMaxVolume * 100;
        } else if (volume < 0) {
            volume = 0;
        }
        tv_voice_number.setText((int) ((volume / 100) / currentMaxVolume * 100) + "%");
        currentVolume = volume / 100;
        am.setStreamVolume(AudioManager.STREAM_MUSIC, (int) volume / 100, 0);
        if (volume == 0) {
            iv_video_voice.setImageResource(R.drawable.voice_no_bg);
        } else {
            iv_video_voice.setImageResource(R.drawable.voice_img);
        }
    }

    /**
     * 各种按钮点击监听的回调
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back_video:
                finish();
                break;
            case R.id.ib_play_pause: //切换播放状态
                switchPlayVideo();
                break;
            case R.id.btn_video_select:
                showDefinitionPopuWindow(v); //切换画质
                break;
            case R.id.tv_popu_biao:
                switchPlayDefinition(videoUrlBean.getPlayer_url(), "标清");
                break;
            case R.id.tv_popu_height:
                switchPlayDefinition(videoUrlBean.getPlayer_url_height(), "高清");
                break;
            case R.id.tv_popu_super:
                switchPlayDefinition(videoUrlBean.getPlayer_url_super(), "超清");
                break;
            case R.id.ib_play_screen:
//                switchScreenType();
                switchScreenOrientation();
                break;
            case R.id.iv_play_click:
                vvv_video.start();
                iv_play_click.setVisibility(View.GONE);
                ib_play_pause.setImageResource(R.drawable.ic_player_pause);
                break;
            default:
                break;
        }
    }

    /**
     * 切换视频清晰度
     *
     * @param url
     * @param definition
     */
    private void switchPlayDefinition(String url, String definition) {
        currentPlayPosition = vvv_video.getCurrentPosition();
        vvv_video.setVideoPath(url);
        btn_video_select.setText(definition);
        popupWindow.dismiss();

    }

    private PopupWindow popupWindow;


    /**
     * 显示切换视频清晰度的popuWindow
     *
     * @param view
     */
    private void showDefinitionPopuWindow(View view) {

        View popupView = View.inflate(this, R.layout.popup_window, null);
        popupView.findViewById(R.id.tv_popu_biao).setOnClickListener(this);
        popupView.findViewById(R.id.tv_popu_height).setOnClickListener(this);
        popupView.findViewById(R.id.tv_popu_super).setOnClickListener(this);
        popupWindow = new PopupWindow(popupView, view.getWidth() + 10, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //显示
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);  //设置点击屏幕其它地方弹出框消失
        popupWindow.showAsDropDown(view, 0, 0);
    }

    /**
     * 获得当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    private int getScreenMode() {
        int screenMode = 0;
        try {
            screenMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Exception localException) {

        }
        return screenMode;
    }

    /**
     * 设置当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    private void setScreenMode(int paramInt) {
        try {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 获得当前屏幕亮度值 0--255
     */
    private int getScreenBrightness() {
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception localException) {

        }
        return screenBrightness;
    }

    /**
     * 设置当前屏幕亮度值 0--255
     */
    private void saveScreenBrightness(int paramInt) {
        try {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 保存当前的屏幕亮度值，并使之生效
     */
    private void setScreenBrightness(int paramInt) {
        Window localWindow = getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        float f = paramInt / 255.0F;
        localLayoutParams.screenBrightness = f;
        localWindow.setAttributes(localLayoutParams);
    }

    private int clickBackNumber;//点击back键的次数

    /**
     * 点击两次返回键退出播放
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            clickBackNumber++;
            if (clickBackNumber == 1) {
                Toast.makeText(this, "再按一次退出播放", Toast.LENGTH_SHORT).show();
            }
            handler.sendEmptyMessageDelayed(WHAT_CLICK_BACK, 2000);
            if (clickBackNumber >= 2) {
                //保存播放进度
                saveAndSendCurrentPosition(false);
                return super.onKeyDown(keyCode, event);
            } else {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 保存和发送当前的播放位置
     */
    private void saveAndSendCurrentPosition(boolean isPlayComplete) {
        CacheUtils.getInstance(this).saveInt(key, (int) vvv_video.getCurrentPosition());
        EventBus.getDefault().post(new VideoPosition(vvv_video.getCurrentPosition(), isPlayComplete));
    }

    /**
     * 切换横竖屏
     */
    private void switchScreenOrientation() {
        if (isScreenOriatationPortrait(this)) {// 当屏幕是竖屏时
            // 点击后变横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 设置当前activity为横屏
            // 当横屏时 把除了视频以外的都隐藏
            //隐藏其他组件的代码
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 设置当前activity为竖屏
            //显示其他组件
        }
    }


    /**
     * 返回当前屏幕是否为竖屏。
     *
     * @param context
     * @return 当且仅当当前屏幕为竖屏时返回true, 否则返回false。
     */
    public boolean isScreenOriatationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 实现播放卡的时候显示流量
     *
     * @return
     */
    public long getUidRxBytes() { //获取总的接受字节数，包含Mobile和WiFi等
        PackageManager pm = getPackageManager();
        ApplicationInfo ai = null;
        try {
            ai = pm.getApplicationInfo("com.atguigu.yingshi2345", PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return TrafficStats.getUidRxBytes(ai.uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);
    }

    private long new_KB; //用来记录一秒内增加的流量
    private long old_KB; //用来记录手机从开机到一秒之前的总流量

    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            new_KB = getUidRxBytes() - old_KB;
            old_KB = getUidRxBytes();
            Message msg = new Message();
            msg.what = WHAT_KB;
            msg.obj = new_KB;
            handler.sendMessage(msg);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
