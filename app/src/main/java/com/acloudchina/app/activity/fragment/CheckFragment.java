package com.acloudchina.app.activity.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.acloudchina.app.Constants;
import com.acloudchina.app.R;
import com.acloudchina.app.bean.TvBean;
import com.acloudchina.app.callback.OnFragmentCallBack;
import com.acloudchina.app.callback.OnTvBeanCallBack;
import com.acloudchina.app.http.BaseCallBack;
import com.acloudchina.app.http.OkHttpManager;
import com.acloudchina.app.utils.L;
import com.acloudchina.app.utils.NetUtils;
import com.acloudchina.app.utils.TvUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 验证界面
 */
public class CheckFragment extends Fragment {
    public static final int CHECK_IS_NOT_LEGAL = 0x01; // 网络配置错误 Ip不合法
    public static final int CHECK_SUCCESS = 0x02;
    public static final int CHECK_NOT_CONTRACT = 0x03;
    public static final int PROGRESS_BAR = 0x04;
    public static final int CHECK_NETWORK = 0x05;
    public static final int MAX_CHECK_COUNT = 8;
    private UiHandler mUiHandler;
    private TextView mVerifyTextView;
    private TextView mCheckDownView;
    private ProgressBar mProgressBar;
    private int mProgress;
    private int mCheckCounts;
    private OnFragmentCallBack mCallBack;
    private boolean isNetWorkConnect;//网络已经连接

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
                case PROGRESS_BAR:
                    mProgress += 10;
                    mProgressBar.setProgress(mProgress);
                    sendEmptyMessageDelayed(PROGRESS_BAR, 1000);
                    break;
                case CHECK_IS_NOT_LEGAL:
                    if (mCallBack != null)
                        mCallBack.onCheckIsNotLegal();
                    break;
                case CHECK_SUCCESS:
                    if (mCallBack != null)
                        mCallBack.onCheckSuccess();
                    break;
                case CHECK_NOT_CONTRACT:
                    if (mCallBack != null)
                        mCallBack.onCheckNotContract();
                    break;
                case CHECK_NETWORK:
                    isNetWorkConnect = NetUtils.isNetworkConnected(getContext());
                    if (!isNetWorkConnect) {
                        if (mCheckCounts > MAX_CHECK_COUNT) {
                            mCallBack.onCheckNetWorkError();
                        } else {
                            L.i("check error count:" + mCheckCounts);
                            mCheckCounts++;
                            sendEmptyMessageDelayed(CHECK_NETWORK, 1000);
                        }
                    } else {
                        netWorkConnect();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check, container, false);
        mVerifyTextView = view.findViewById(R.id.id_check_text);
        mCheckDownView = view.findViewById(R.id.id_check_countDown);
        mProgressBar = view.findViewById(R.id.id_check_pb);
        mUiHandler = new UiHandler(this);
        checkIsNetWorkConn();
        return view;
    }

    /**
     * 检查是否有网络
     */
    private void checkIsNetWorkConn() {
        mProgress += 10;
        mProgressBar.setMax(100);
        mProgressBar.setProgress(mProgress);
        mVerifyTextView.setText(String.valueOf("检查网络配置"));
        isNetWorkConnect = NetUtils.isNetworkConnected(getContext());
        if (isNetWorkConnect) {
            mUiHandler.sendEmptyMessage(PROGRESS_BAR);
            checkTvIpIsLegal();
        } else {
            mUiHandler.sendEmptyMessageDelayed(CHECK_NETWORK, 1000);
        }
    }

    /**
     * 当接收到广播
     */
    public void netWorkConnect() {
        mUiHandler.sendEmptyMessage(PROGRESS_BAR);
        checkTvIpIsLegal();
    }

    /**
     * 检查电视是否在合约期
     */
    private void checkTvIpIsLegal() {
        mVerifyTextView.setText(String.valueOf("检查Ip是否合法"));
        L.i("TAG tvId:" + TvUtils.getSerialNum());
        String url = Constants.BASE_URL + "checkTv?" + "id=" + TvUtils.getSerialNum();
        OkHttpManager.getInstance().getRequest(url, new OnTvBeanCallBack<TvBean>() {
            @Override
            protected void onFailure(Call call, IOException e) {
                mUiHandler.sendEmptyMessageDelayed(CHECK_IS_NOT_LEGAL, 3000);
                super.onFailure(call, e);
            }

            @Override
            protected void onSuccess(Call call, Response response, TvBean bean) {
                L.i("TvVerify:" + bean.toString());
                if (bean.isIscontract()) {
                    if (bean.isIslegal()) {
                        //合约期内且合法
                        mUiHandler.sendEmptyMessageDelayed(CHECK_SUCCESS, 3000);
                    } else {
                        //合约期内不合法
                        mUiHandler.sendEmptyMessageDelayed(CHECK_IS_NOT_LEGAL, 3000);
                    }
                } else {
                    //合约期外
                    mUiHandler.sendEmptyMessageDelayed(CHECK_NOT_CONTRACT, 3000);
                }
                super.onSuccess(call, response, bean);
            }
        });
    }
}
