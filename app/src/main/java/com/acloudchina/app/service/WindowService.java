package com.acloudchina.app.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.acloudchina.app.bean.AdBean;
import com.acloudchina.app.db.DbService;
import com.acloudchina.app.utils.L;
import com.acloudchina.app.view.ImageAdView;
import com.acloudchina.app.view.VideoAdView;

import java.util.Timer;
import java.util.TimerTask;

public class WindowService extends Service implements VideoAdView.OnVideoAdCallBack, ImageAdView.OnImageAdCallBack {
    private WindowManager mWindowManager;
    private View mWindowView;
    private AdBean mAdBean;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mAdBean= intent.getParcelableExtra("AdBean");
        L.i("WindowService onStartCommand");
        addWindowView();
        addWindow();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeWindow();
    }

    /**
     * 添加悬浮窗View
     */
    private void addWindowView() {
        int contentId = Integer.valueOf(mAdBean.getContentId());
        if (contentId == 1) {
            ImageAdView  imageAdView= new ImageAdView(this);
            imageAdView.setOnImageAdCallBack(this);
            imageAdView.setImageView(mAdBean.getUrl());
            mWindowView = imageAdView;
        } else if (contentId == 2) {
            VideoAdView videoAdView = new VideoAdView(this);
            videoAdView.setOnVideoAdCallBack(this);
            videoAdView.setVideoPath(mAdBean.getUrl());
            mWindowView = videoAdView;
        }
    }

    /**
     * 添加悬浮窗
     */
    private void addWindow() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // 靠手机屏幕的左边居中显示
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        if (Build.VERSION.SDK_INT >= 26) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.width = 300;
        params.height = 300;
        mWindowManager.addView(mWindowView, params);
        L.i("addWindow");
    }

    /**
     * 关闭悬浮窗
     */
    public void closeWindow() {
        if (mWindowView != null) {
            mWindowManager.removeViewImmediate(mWindowView);
            mWindowView = null;
        }
    }

    /**
     * 当视频广告播放完的时候回调
     */
    @Override
    public void onVideoAdPlayFinish() {
        L.i("onVideoAdPlayFinish");
        stopSelf();
    }

    /**
     * 当图片广告播放完的时候回调
     */
    @Override
    public void onImageAdPlayFinish() {
        L.i("onImageAdPlayFinish");
        stopSelf();
    }
}
