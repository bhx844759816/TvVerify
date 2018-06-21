package com.acloudchina.app.utils;

import android.view.WindowManager;

import com.acloudchina.app.bean.AdBean;
import com.acloudchina.app.bean.AdTimeBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 通过广告Id展示广告内容
 */
public class AdManager {

    /**
     * 判断当前的日期是否过期
     *
     * @param adBean
     * @return
     */
    public static boolean isTimeNotOut(AdBean adBean) {
        Date startData = DateUtil.str2Date(adBean.getValidBegin(), "yyyy-MM-dd HH:mm:ss");
        Date endData = DateUtil.str2Date(adBean.getValidEnd(), "yyyy-MM-dd HH:mm:ss");
        return DateUtil.between(startData, endData, DateUtil.now());
    }

    /**
     * 获取指定的时间片段[09 00 12 00]
     *
     * @return
     */
    public static List<AdTimeBean> getTimeSplit(List<String> times) {
        List<AdTimeBean> list = new ArrayList<>();
        Calendar calendar = DateUtil.calendar();
        calendar.setTime(DateUtil.now());
        for (int i = 0; i < times.size(); i++) {
            AdTimeBean adTimeBean = new AdTimeBean();
            String[] timeSplit = times.get(i).split("-");
            calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(timeSplit[0].substring(0, 2)));
            calendar.set(Calendar.MINUTE, Integer.valueOf(timeSplit[0].substring(2, timeSplit[0].length())));
            adTimeBean.setStartTime(calendar.getTimeInMillis());
            calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(timeSplit[1].substring(0, 2)));
            calendar.set(Calendar.MINUTE, Integer.valueOf(timeSplit[1].substring(2, timeSplit[0].length())));
            adTimeBean.setEndTime(calendar.getTimeInMillis());
        }
        return list;
    }


    public static boolean isCurTimeAdTimeBetween(List<AdTimeBean> list) {
        for (AdTimeBean adTimeBean : list) {
            if (DateUtil.between(DateUtil.timeToDate(adTimeBean.getStartTime()), new Date(adTimeBean.getEndTime()), DateUtil.now())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCurTimeAdTimeBetween(AdTimeBean adTimeBean) {
        return DateUtil.between(DateUtil.timeToDate(adTimeBean.getStartTime()), new Date(adTimeBean.getEndTime()), DateUtil.now());
    }
}
