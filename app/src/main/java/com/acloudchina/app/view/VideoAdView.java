package com.acloudchina.app.view;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.acloudchina.app.App;
import com.acloudchina.app.R;
import com.acloudchina.app.callback.OnAdPlayCallBack;
import com.acloudchina.app.utils.L;
import com.danikula.videocache.HttpProxyCacheServer;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 视频的广告
 */
public class VideoAdView extends FrameLayout implements SurfaceHolder.Callback {

    private SurfaceView mSurfaceView;
    private CountDownView mCountDownView;
    private MediaPlayer mMediaPlayer;
    private OnVideoAdCallBack mCallBack;
    private Timer mTimer;
    private int mCount;
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            mCount--;
            if (mCount >= 0) {
                mCountDownView.setText(mCount);
            } else {
                mTimer.cancel();
            }
        }
    };

    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            startPlay();
        }
    };

    private MediaPlayer.OnCompletionListener mCompleteListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            if (mCallBack != null) {
                mCallBack.onVideoAdPlayFinish();
            }
        }
    };

    public VideoAdView(@NonNull Context context) {
        this(context, null);
    }

    public VideoAdView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoAdView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.view_ad_video, this);
        mSurfaceView = view.findViewById(R.id.id_video_surfaceView);
        mCountDownView = view.findViewById(R.id.id_video_countDownView);
        mCountDownView.setVisibility(GONE);
        mSurfaceView.getHolder().addCallback(this);
    }

    /**
     * 设置Video视频
     *
     * @param url
     */
    public void setVideoPath(String url) {
        //设置视频
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.reset();
        // 设置声音效果
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(mPreparedListener);
        mMediaPlayer.setOnCompletionListener(mCompleteListener);
        try {
            HttpProxyCacheServer proxy = App.getProxy(getContext());
            String proxy_url = proxy.getProxyUrl(url);
            mMediaPlayer.setDataSource(getContext(), Uri.parse(proxy_url));
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置倒计时
     *
     * @param count 当前的倒计时次数
     */
    public void setCountDown(int count) {
        if (mCountDownView != null) {
            mCountDownView.setText(count);
        }
    }

    /**
     * 开始倒计时
     */
    public void startPlay() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
        if (mCountDownView != null) {
            mCountDownView.setVisibility(VISIBLE);
            mCountDownView.startCountDown(10 * 1000);
            mTimer = new Timer();
            mTimer.schedule(mTimerTask, 0, 1000);
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Surface surface = holder.getSurface();
        if (surface != null && mMediaPlayer != null) {
            mMediaPlayer.setSurface(surface);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setSurface(null);
        }
    }

    public void setOnVideoAdCallBack(OnVideoAdCallBack callBack) {
        mCallBack = callBack;

    }

    public interface OnVideoAdCallBack {
        void onVideoAdPlayFinish();
    }
}
