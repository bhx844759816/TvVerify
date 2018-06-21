package com.acloudchina.app.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.acloudchina.app.Constants;
import com.acloudchina.app.activity.CheckActivity;
import com.acloudchina.app.bean.AdBean;
import com.acloudchina.app.callback.OnDownLandCallBack;
import com.acloudchina.app.callback.OnHttpCallBack;
import com.acloudchina.app.db.DbService;
import com.acloudchina.app.http.OkHttpManager;
import com.acloudchina.app.utils.DateUtil;
import com.acloudchina.app.utils.L;
import com.acloudchina.app.utils.TvUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


/**
 * 后台服务
 */
public class BackService extends Service {
    private List<AdBean> mOpenTvAdList;
    private int mDownLandIndex;

    private class BackBinder extends Binder {
        public BackService getBackService() {
            return BackService.this;
        }

        /**
         * 重新设置所有的闹钟
         */
        public void setAdAlarm() {

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new BackBinder();
    }

    @Override
    public void onCreate() {
        mOpenTvAdList = new ArrayList<>();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        pullAdInfo();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 拉取广告列表信息
     */
    private void pullAdInfo() {
        String url = Constants.BASE_URL + "getadlist?" + "id=123";
        OkHttpManager.getInstance().getRequest(url, new OnHttpCallBack<List<AdBean>>() {
            @Override
            protected void onSuccess(Call call, Response response, List<AdBean> list) {
                //更新广告列表
                updateLocalData(list);
                mDownLandIndex = 0;
                //查询开机广告
                List<AdBean> adList = DbService.getInstance(getApplicationContext()).queryTypeIdAdList("0");
                //添加未下载的开机广告
                for (AdBean adBean : adList) {
                    if (TextUtils.isEmpty(adBean.getSavePath())) {
                        mOpenTvAdList.add(adBean);
                    }
                }
            }
        });
    }

    /**
     * 更新本地数据
     */
    private void updateLocalData(List<AdBean> list) {
        //获取广告列表
        for (AdBean adBean : list) {
            AdBean adBean_local = DbService.getInstance(getApplicationContext()).queryAdBean(adBean.getId());
            if (adBean_local != null) {
                DbService.getInstance(getApplicationContext()).updateAdBean(adBean);
            } else {
                DbService.getInstance(getApplicationContext()).insertAdBean(adBean);
            }
        }
    }

    /**
     * 下载开机广告
     */
    private void downLandAD() {
        AdBean adBean = mOpenTvAdList.get(mDownLandIndex);
        String url = adBean.getUrl();
//      String type = adBean.getUrl().substring();
        OkHttpManager.getInstance().asynDownloadFile(adBean.getUrl(), Constants.AD_SAVE_PATH + "",
                new OnDownLandCallBack() {
                    @Override
                    protected void onSuccess(Call call, Response response, Object o) {
                        super.onSuccess(call, response, o);
                    }
                });
    }

}
