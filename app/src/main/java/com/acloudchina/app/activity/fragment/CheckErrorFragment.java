package com.acloudchina.app.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.acloudchina.app.R;
import com.acloudchina.app.callback.OnFragmentCallBack;

import java.lang.ref.WeakReference;

public class CheckErrorFragment extends Fragment {
    public static final int COUNT_DOWN = 0x01;
    private TextView mErrorMsg;
    private TextView mCountDown;
    private UiHandler mUiHandler;
    private int mCount;
    private String errorMsg;
    private OnFragmentCallBack mCallBack;

    /**
     * 主线程Handler
     */
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
                case COUNT_DOWN:
                    if (mCount > 0) {
                        mCount--;
                        mCountDown.setText(String.valueOf(mCount + "s"));
                        sendEmptyMessageDelayed(COUNT_DOWN, 1000);
                    } else {
                        mCallBack.netWorkSetting();
                    }
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
    public void onDetach() {
        super.onDetach();
        if (mCallBack != null) {
            mCallBack = null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUiHandler != null)
            mUiHandler.mWeakReference.clear();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_error, container, false);
        mErrorMsg = view.findViewById(R.id.id_error_msg);
        mCountDown = view.findViewById(R.id.id_error_countDown);
        mUiHandler = new UiHandler(this);
        startCountDown();
        mErrorMsg.setText(errorMsg);
        return view;
    }

    public void setErrorMsg(String msg) {
        errorMsg = msg;
    }

    /**
     * 开始倒计时
     */
    private void startCountDown() {
        mCount = 5;
        mErrorMsg.setText(String.valueOf(mCount));
        mUiHandler.sendEmptyMessageDelayed(COUNT_DOWN, 1000);
    }

    /**
     * 取消倒计时
     */
    public void cancelCountDown() {
        mUiHandler.removeMessages(COUNT_DOWN);
    }
}
