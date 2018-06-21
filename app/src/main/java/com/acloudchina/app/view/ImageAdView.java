package com.acloudchina.app.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.acloudchina.app.R;
import com.acloudchina.app.callback.OnAdPlayCallBack;
import com.acloudchina.app.utils.L;
import com.bumptech.glide.Glide;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 图片广告
 */
public class ImageAdView extends FrameLayout {

    private ImageView mAdImageView;
    private CountDownView mCountDownView;
    private Timer mTimer;
    private int mCount;
    private OnImageAdCallBack mCallBack;
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            mCount--;
            if (mCount >= 0) {
                L.i("mCount：" + mCount);
                mCountDownView.setText(mCount);
            } else {
                mTimer.cancel();
                if (mCallBack != null) {
                    mCallBack.onImageAdPlayFinish();
                }
            }
        }
    };

    public ImageAdView(@NonNull Context context) {
        this(context, null);
    }

    public ImageAdView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageAdView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.view_ad_image, this);
        mAdImageView = view.findViewById(R.id.id_image_ad);
        mCountDownView = view.findViewById(R.id.id_image_countDownView);
    }

    /**
     * 设置显示的图片
     *
     * @param url
     */
    public void setImageView(String url) {
        L.i("setImageView");
        Glide.with(getContext()).load(url).into(mAdImageView);
        startCountDown();
    }


    /**
     * 开启倒计时
     */
    public void startCountDown() {
        if (mCountDownView != null) {
            mCount = 10;
            mTimer = new Timer();
            mTimer.schedule(mTimerTask, 0, 1000);
            mCountDownView.startCountDown(10 * 1000);
        }
    }

    /**
     * 设置图片广告的播放回调
     *
     * @param callBack
     */
    public void setOnImageAdCallBack(OnImageAdCallBack callBack) {
        mCallBack = callBack;
    }

    public interface OnImageAdCallBack {
        void onImageAdPlayFinish();
    }

}
