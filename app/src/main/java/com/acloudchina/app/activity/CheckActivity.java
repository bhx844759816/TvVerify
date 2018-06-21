package com.acloudchina.app.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.acloudchina.app.Constants;
import com.acloudchina.app.R;
import com.acloudchina.app.activity.fragment.AdFragment;
import com.acloudchina.app.activity.fragment.CheckErrorFragment;
import com.acloudchina.app.activity.fragment.CheckFragment;
import com.acloudchina.app.bean.AdBean;
import com.acloudchina.app.bean.AdTimeBean;
import com.acloudchina.app.callback.OnFragmentCallBack;
import com.acloudchina.app.callback.OnHttpCallBack;
import com.acloudchina.app.db.DbService;
import com.acloudchina.app.http.OkHttpManager;
import com.acloudchina.app.service.AlarmReceiver;
import com.acloudchina.app.service.BackService;
import com.acloudchina.app.service.WindowService;
import com.acloudchina.app.utils.AdManager;
import com.acloudchina.app.utils.DateUtil;
import com.acloudchina.app.utils.L;
import com.acloudchina.app.utils.NetUtils;
import com.acloudchina.app.utils.SystemPropertiesProxy;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 入口Activity
 */
public class CheckActivity extends FragmentActivity implements OnFragmentCallBack {
    private static final int OVERLAY_PERMISSION_REQ_CODE = 0x01;
    private AdFragment mAdFragment;
    private CheckFragment mCheckFragment;
    private CheckErrorFragment mCheckErrorFragment;//
    private boolean isNetWorkSetting; //设置网络
    private Fragment mFragment;
    private String errorMsg;
    private AlarmManager mAlarmManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        checkWindowPermission();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        addCheckFragment();
        pullAdInfo();
    }

    private void pullAdInfo() {
        String url = Constants.BASE_URL + "getadlist?" + "id=123";
        OkHttpManager.getInstance().getRequest(url, new OnHttpCallBack<List<AdBean>>() {
            @Override
            protected void onSuccess(Call call, Response response, List<AdBean> list) {
                //获取广告列表
                updateLocalData(list);
            }
        });
    }

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

    private void test() {
        Calendar calendar = DateUtil.calendar();
        calendar.setTime(DateUtil.now());
        calendar.add(Calendar.MINUTE, 3);
        setAlarm("2", calendar.getTimeInMillis());
    }

    /**
     * 添加广告得Fragment
     */
    private void addAdFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mAdFragment == null) {
            mAdFragment = new AdFragment();
        }
        mFragment = mAdFragment;
        transaction.replace(R.id.id_fragment, mAdFragment);
        transaction.commit();
    }

    /**
     * 添加检查失败得Fragment
     */
    private void addCheckErrorFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mCheckErrorFragment == null) {
            mCheckErrorFragment = new CheckErrorFragment();
        }
        mFragment = mCheckErrorFragment;
        mCheckErrorFragment.setErrorMsg(errorMsg);
        transaction.replace(R.id.id_fragment, mCheckErrorFragment);
        transaction.commit();
    }

    /**
     * 添加检查得Fragment
     */
    private void addCheckFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mCheckFragment == null) {
            mCheckFragment = new CheckFragment();
        }
        mFragment = mCheckFragment;
        transaction.replace(R.id.id_fragment, mCheckFragment);
        transaction.commit();
    }

    /**
     * 当广告播放完成得时候
     */
    @Override
    public void onAdPlayFinish() {
        addCheckFragment();
    }

    /**
     * 当检测网络失败得界面
     */
    @Override
    public void onCheckNetWorkError() {
        errorMsg = "网络错误";
        addCheckErrorFragment();
    }

    /**
     * 当页面重新进入得时候
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (isNetWorkSetting) {
            addCheckFragment();
        }
    }

    /**
     * 跳转到网络设置界面
     */
    @Override
    public void netWorkSetting() {
        Toast.makeText(CheckActivity.this, "跳转到网络设置界面", Toast.LENGTH_SHORT).show();
        isNetWorkSetting = true;
        Intent intent = new Intent();
        intent.setAction("com.konka.systemsetting.action.SHOW_MENU");
        intent.putExtra("menu_name", "net_lan");
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);
    }

    /**
     * 检测成功得时候
     */
    @Override
    public void onCheckSuccess() {
//        setAdAlarm();
        Toast.makeText(CheckActivity.this, "check success", Toast.LENGTH_SHORT).show();
        test();
        finish();
    }

    /**
     * 根据本地的广告列表设置广告播放的闹钟
     */
    private void setAdAlarm() {
        List<AdBean> adBeans = DbService.getInstance(CheckActivity.this).queryTypeIdAdList("1");
        if (adBeans != null) {
            for (AdBean adBean : adBeans) {
                if (AdManager.isTimeNotOut(adBean)) {
                    List<AdTimeBean> timeBeanList = AdManager.getTimeSplit(adBean.getTimeRule());
                    for (AdTimeBean adTimeBean : timeBeanList) {
                        if (AdManager.isCurTimeAdTimeBetween(adTimeBean)) {
                            setAlarm(adBean.getId(), adTimeBean.getStartTime());
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 闹钟
     *
     * @param adId
     * @param time 闹钟的时间
     */
    private void setAlarm(String adId, long time) {
        Intent intent = new Intent(CheckActivity.this, AlarmReceiver.class);
        intent.setAction(AlarmReceiver.ACTION);
        intent.putExtra("AdId", adId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(CheckActivity.this, Integer.valueOf(adId),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = DateUtil.calendar();
        calendar.setTimeInMillis(time);
        L.i(DateUtil.formatDatetime(calendar.getTime()));
        mAlarmManager.cancel(pendingIntent);
        mAlarmManager.setWindow(AlarmManager.RTC, time, 0, pendingIntent);
    }

    @Override
    public void onCheckIsNotLegal() {
        // IP不合法
        errorMsg = "IP不合法";
        addCheckErrorFragment();
    }

    @Override
    public void onCheckNotContract() {
        // 不在合约期内
        errorMsg = "不在合约期内";
        SystemPropertiesProxy.setProperty("persist.loT.charge", "false");
        finish();
    }

    /**
     * 监听错误界面得确认按键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            Toast.makeText(CheckActivity.this, "确认", Toast.LENGTH_SHORT).show();
            if (mFragment instanceof CheckErrorFragment) {
                netWorkSetting();
                mCheckErrorFragment.cancelCountDown();
            } else if (mFragment instanceof CheckFragment) {
//                SystemPropertiesProxy.setProperty("persist.loT.charge", "true");
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 屏蔽返回键
     */
    @Override
    public void onBackPressed() {

    }

    /**
     * 检测悬浮窗权限
     */
    public void checkWindowPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(CheckActivity.this)) {
                //没有悬浮窗权限,跳转申请
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            }
        }
    }

}
