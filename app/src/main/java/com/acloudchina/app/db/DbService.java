package com.acloudchina.app.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.acloudchina.app.bean.AdBean;
import com.acloudchina.app.bean.TvBean;
import com.acloudchina.app.utils.L;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DbService {
    private DbHelper mDbHelper;
    private static DbService mInstance;

    public static DbService getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DbService.class) {
                if (mInstance == null)
                    mInstance = new DbService(context.getApplicationContext());
            }
        }
        return mInstance;
    }

    private DbService(Context context) {
        mDbHelper = new DbHelper(context);
    }


    /**
     * 插入一条记录
     *
     * @param adBean
     */
    public void insertAdBean(AdBean adBean) {
        L.i("insertAdBean");
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        String sql = "insert into " + AdBean.TABLE_NAME + "("
                + AdBean.NAME_AD_ID + ","
                + AdBean.NAME_AD_NAME + ","
                + AdBean.NAME_AD_TYPE_ID + ","
                + AdBean.NAME_AD_CONTENT_ID + ","
                + AdBean.NAME_AD_DESC + ","
                + AdBean.NAME_VALID_BEGIN + ","
                + AdBean.NAME_VALID_END + ","
                + AdBean.NAME_AD_URL + ","
                + AdBean.NAME_AD_CIRCLE + ","
                + AdBean.NAME_AD_PLAY_TIME + ","
                + AdBean.NAME_AD_VIDEO_SAVE_PATH + ","
                + AdBean.NAME_AD_PLAY_COUNT + ","
                + AdBean.NAME_AD_TIME_RULE + ")"
                + " values(?,?,?,?,?,?,?,?,?,?,?)";
        StringBuilder builder = new StringBuilder();
        if (adBean.getTimeRule() != null) {
            for (String s : adBean.getTimeRule()) {
                builder.append(s + "&");
            }
        }
        String timeRule = builder.toString().substring(0, builder.toString().length() - 1);
        L.i("timeRule:" + timeRule);
        database.execSQL(sql, new Object[]{
                adBean.getId(),
                adBean.getName(),
                adBean.getTypeId(),
                adBean.getContentId(),
                adBean.getDescribe(),
                adBean.getValidBegin(),
                adBean.getValidEnd(),
                adBean.getUrl(),
                adBean.getCircle(),
                adBean.getPlayTime(),
                adBean.getSavePath(),
                adBean.getPlayCount(),
                timeRule
        });
        database.close();
    }

    /**
     * 删除一条记录
     *
     * @param adBean
     */
    public void deleteAdBean(AdBean adBean) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        String sql = "delete from " + AdBean.TABLE_NAME + " where " + AdBean.NAME_AD_ID + "=?";
        database.execSQL(sql, new Object[]{adBean.getId()});
    }

    /**
     * 更新数据
     *
     * @param adBean
     */
    public void updateAdBean(AdBean adBean) {
        L.i("updateAdBean");
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        String sql = "update " + AdBean.TABLE_NAME + " set "
                + AdBean.NAME_AD_ID + "=?,"
                + AdBean.NAME_AD_NAME + "=?,"
                + AdBean.NAME_AD_TYPE_ID + "=?,"
                + AdBean.NAME_AD_CONTENT_ID + "=?,"
                + AdBean.NAME_AD_DESC + "=?,"
                + AdBean.NAME_VALID_BEGIN + "=?,"
                + AdBean.NAME_VALID_END + "=?,"
                + AdBean.NAME_AD_URL + "=?,"
                + AdBean.NAME_AD_CIRCLE + "=?,"
                + AdBean.NAME_AD_PLAY_TIME + "=?,"
                + AdBean.NAME_AD_VIDEO_SAVE_PATH + "=?,"
                + AdBean.NAME_AD_PLAY_COUNT + "=?,"
                + AdBean.NAME_AD_TIME_RULE + "=?"
                + " where "
                + AdBean.NAME_AD_ID + "=?";
        StringBuilder builder = new StringBuilder();
        if (adBean.getTimeRule() != null) {
            for (String s : adBean.getTimeRule()) {
                builder.append(s + "&");
            }
        }
        String timeRule = builder.toString().substring(0, builder.toString().length() - 1);
        database.execSQL(sql, new Object[]{
                adBean.getId()
                , adBean.getName()
                , adBean.getTypeId()
                , adBean.getContentId()
                , adBean.getDescribe()
                , adBean.getValidBegin()
                , adBean.getValidEnd()
                , adBean.getUrl()
                , adBean.getCircle()
                , adBean.getPlayTime()
                , adBean.getSavePath()
                , adBean.getPlayCount()
                , timeRule
                , adBean.getId()});
        database.close();
    }

    /**
     * 查询指定songId得数据
     *
     * @param id AdID
     * @return
     */
    public AdBean queryAdBean(String id) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        String sql = "select "
                + AdBean.NAME_AD_ID + ","
                + AdBean.NAME_AD_NAME + ","
                + AdBean.NAME_AD_TYPE_ID + ","
                + AdBean.NAME_AD_CONTENT_ID + ","
                + AdBean.NAME_AD_DESC + ","
                + AdBean.NAME_VALID_BEGIN + ","
                + AdBean.NAME_VALID_END + ","
                + AdBean.NAME_AD_URL + ","
                + AdBean.NAME_AD_CIRCLE + ","
                + AdBean.NAME_AD_PLAY_TIME + ","
                + AdBean.NAME_AD_VIDEO_SAVE_PATH + ","
                + AdBean.NAME_AD_PLAY_COUNT + ","
                + AdBean.NAME_AD_TIME_RULE
                + " from "
                + AdBean.TABLE_NAME
                + " where "
                + AdBean.NAME_AD_ID
                + " = ?";
        Cursor cursor = database.rawQuery(sql, new String[]{id});
        if (cursor.moveToNext()) {
            AdBean adBean = new AdBean();
            adBean.setId(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_ID)));
            adBean.setName(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_NAME)));
            adBean.setTypeId(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_TYPE_ID)));
            adBean.setContentId(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_CONTENT_ID)));
            adBean.setDescribe(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_DESC)));
            adBean.setValidBegin(cursor.getString(cursor.getColumnIndex(AdBean.NAME_VALID_BEGIN)));
            adBean.setValidEnd(cursor.getString(cursor.getColumnIndex(AdBean.NAME_VALID_END)));
            adBean.setUrl(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_URL)));
            adBean.setCircle(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_CIRCLE)));
            adBean.setPlayTime(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_PLAY_TIME)));
            adBean.setSavePath(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_VIDEO_SAVE_PATH)));
            adBean.setPlayCount(cursor.getInt(cursor.getColumnIndex(AdBean.NAME_AD_PLAY_COUNT)));
            String timeRuler = cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_TIME_RULE));
            String[] strs = timeRuler.split("&");
            adBean.setTimeRule(Arrays.asList(strs));
            return adBean;
        }
        cursor.close();
        database.close();
        return null;
    }

    /**
     * 查询指定类型的广告
     *
     * @param typeId
     * @return
     */
    public List<AdBean> queryTypeIdAdList(String typeId) {
        List<AdBean> adList = new ArrayList<>();
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        String sql = "select "
                + AdBean.NAME_AD_ID + ","
                + AdBean.NAME_AD_NAME + ","
                + AdBean.NAME_AD_TYPE_ID + ","
                + AdBean.NAME_AD_CONTENT_ID + ","
                + AdBean.NAME_AD_DESC + ","
                + AdBean.NAME_VALID_BEGIN + ","
                + AdBean.NAME_VALID_END + ","
                + AdBean.NAME_AD_URL + ","
                + AdBean.NAME_AD_CIRCLE + ","
                + AdBean.NAME_AD_PLAY_TIME + ","
                + AdBean.NAME_AD_VIDEO_SAVE_PATH + ","
                + AdBean.NAME_AD_PLAY_COUNT + ","
                + AdBean.NAME_AD_TIME_RULE
                + " from "
                + AdBean.TABLE_NAME
                + " where "
                + AdBean.NAME_AD_TYPE_ID
                + " = ?";
        Cursor cursor = database.rawQuery(sql, new String[]{typeId});
        while (cursor.moveToNext()) {
            AdBean adBean = new AdBean();
            adBean.setId(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_ID)));
            adBean.setName(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_NAME)));
            adBean.setTypeId(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_TYPE_ID)));
            adBean.setContentId(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_CONTENT_ID)));
            adBean.setDescribe(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_DESC)));
            adBean.setValidBegin(cursor.getString(cursor.getColumnIndex(AdBean.NAME_VALID_BEGIN)));
            adBean.setValidEnd(cursor.getString(cursor.getColumnIndex(AdBean.NAME_VALID_END)));
            adBean.setUrl(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_URL)));
            adBean.setCircle(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_CIRCLE)));
            adBean.setPlayTime(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_PLAY_TIME)));
            adBean.setSavePath(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_VIDEO_SAVE_PATH)));
            adBean.setPlayCount(cursor.getInt(cursor.getColumnIndex(AdBean.NAME_AD_PLAY_COUNT)));
            String timeRuler = cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_TIME_RULE));
            String[] strs = timeRuler.split("&");
            adBean.setTimeRule(Arrays.asList(strs));
            adList.add(adBean);
        }
        cursor.close();
        database.close();
        return adList;
    }

    /**
     * 查询数据库中所有得Music
     *
     * @return
     */
    public List<AdBean> queryAllAdList() {
        List<AdBean> adList = new ArrayList<>();
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        String sql = "select * from " + AdBean.TABLE_NAME;
        Cursor cursor = database.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            AdBean adBean = new AdBean();
            adBean.setId(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_ID)));
            adBean.setName(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_NAME)));
            adBean.setTypeId(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_TYPE_ID)));
            adBean.setContentId(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_CONTENT_ID)));
            adBean.setDescribe(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_DESC)));
            adBean.setValidBegin(cursor.getString(cursor.getColumnIndex(AdBean.NAME_VALID_BEGIN)));
            adBean.setValidEnd(cursor.getString(cursor.getColumnIndex(AdBean.NAME_VALID_END)));
            adBean.setUrl(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_URL)));
            adBean.setCircle(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_CIRCLE)));
            adBean.setPlayTime(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_PLAY_TIME)));
            adBean.setSavePath(cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_VIDEO_SAVE_PATH)));
            adBean.setPlayCount(cursor.getInt(cursor.getColumnIndex(AdBean.NAME_AD_PLAY_COUNT)));
            String timeRuler = cursor.getString(cursor.getColumnIndex(AdBean.NAME_AD_TIME_RULE));
            String[] strs = timeRuler.split("&");
            adBean.setTimeRule(Arrays.asList(strs));
            adList.add(adBean);
        }
        cursor.close();
        database.close();
        return adList;
    }
}
