package com.acloudchina.app.activity.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acloudchina.app.R;
import com.acloudchina.app.bean.AdBean;
import com.acloudchina.app.callback.OnFragmentCallBack;
import com.acloudchina.app.db.DbService;
import com.acloudchina.app.view.CountDownView;
import com.acloudchina.app.view.VideoSurfaceView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 广告的界面
 */
public class AdFragment extends Fragment {
    private static final int AD_PLAY_FINISGH = 0x01;
    private VideoSurfaceView mSurfaceView;
    private CountDownView mCountDownView;
    private Timer mTimer;
    private int mCount;
    private OnFragmentCallBack mCallBack;
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            mCount--;
            if (mCount >= 0) {
                mCountDownView.setText(mCount);
            } else {
                mTimer.cancel();
                mUiHandler.sendEmptyMessage(AD_PLAY_FINISGH);

            }
        }
    };
    private UiHandler mUiHandler;

    private class UiHandler extends Handler {
        private WeakReference<Fragment> mWeakReference;

        UiHandler(Fragment fragment) {
            mWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mWeakReference.get() == null)
                return;
            switch (msg.what) {
                case AD_PLAY_FINISGH:
                    if (mSurfaceView != null)
                        mSurfaceView.release();
                    if (mCallBack != null)
                        mCallBack.onAdPlayFinish();
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentCallBack) {
            mCallBack = (OnFragmentCallBack) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCallBack != null) {
            mCallBack = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad, container, false);
        mSurfaceView = view.findViewById(R.id.id_my_surfaceView);
        mCountDownView = view.findViewById(R.id.id_my_countDownView);
        init();
        return view;
    }

    /**
     *
     */
    private void init() {
        mUiHandler = new UiHandler(this);
        List<AdBean> adList = DbService.getInstance(getContext()).queryTypeIdAdList("1");


        mCount = 10;
        mTimer = new Timer();
        //每隔一秒执行一次
        mTimer.schedule(mTimerTask, 0, 1000);
        mCountDownView.startCountDown(10000);
    }

}
