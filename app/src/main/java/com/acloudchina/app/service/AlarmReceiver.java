package com.acloudchina.app.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.acloudchina.app.bean.AdBean;
import com.acloudchina.app.bean.AdTimeBean;
import com.acloudchina.app.db.DbService;
import com.acloudchina.app.utils.AdManager;
import com.acloudchina.app.utils.DateUtil;
import com.acloudchina.app.utils.L;

import java.io.File;
import java.util.Calendar;
import java.util.List;

/**
 * 闹钟的广播
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static final String ACTION = "com.acloudchina.app.alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        L.i("AlarmReceiver onReceive");
        if (!ACTION.equals(intent.getAction())) {
            return;
        }
        String adId = intent.getStringExtra("AdId");
        AdBean adBean = DbService.getInstance(context).queryAdBean(adId);
        if (adBean != null) {
            L.i("AdBean not null");
            Intent intent_ = new Intent(context, WindowService.class);
            intent_.putExtra("AdBean", adBean);
            context.startService(intent_);
        }else {
            L.i("AdBean is null");
        }
//        // 当不为null且也在
//        if (adBean != null && AdManager.isTimeNotOut(adBean)) {
//            List<String> timeRulers = adBean.getTimeRule();
//            boolean isBetween = AdManager.isCurTimeAdTimeBetween(AdManager.getTimeSplit(timeRulers));
//            if (isBetween) {
//                setAlarm(context, adId, Integer.valueOf(adBean.getCircle()) * 60 * 1000);
//                Intent intent_ = new Intent(context, WindowService.class);
//                intent_.putExtra("AdBean", adBean);
//                context.startService(intent_);
//            }
//        }

    }

    /**
     * 设置隔多长时间在发一次闹钟
     *
     * @param context
     * @param time
     */
    private void setAlarm(Context context, String adId, long time) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction(ACTION);
        intent.putExtra("AdId", adId);
        PendingIntent pi = PendingIntent.getBroadcast(context, Integer.valueOf(adId), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (manager != null) {
            manager.cancel(pi);
            manager.setWindow(AlarmManager.RTC, System.currentTimeMillis(), time, pi);
        }
    }
}
