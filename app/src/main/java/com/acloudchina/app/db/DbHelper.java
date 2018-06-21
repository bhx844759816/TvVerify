package com.acloudchina.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.acloudchina.app.bean.AdBean;
import com.acloudchina.app.utils.L;

/**
 * Created by Administrator on 2018/3/23.
 */
public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "tv_verify.db";//数据库得名字
    public static final int DB_VERSION = 1;

    private String createTableSql = "create table " + AdBean.TABLE_NAME + "(" +
            "_id Integer primary key autoincrement, " +
            AdBean.NAME_AD_ID + " text not null, " +
            AdBean.NAME_AD_NAME + " text, " +
            AdBean.NAME_AD_TYPE_ID + " text, " +
            AdBean.NAME_AD_CONTENT_ID + " text, " +
            AdBean.NAME_AD_DESC + " text, " +
            AdBean.NAME_VALID_BEGIN + " text, " +
            AdBean.NAME_VALID_END + " text, " +
            AdBean.NAME_AD_URL + " text, " +
            AdBean.NAME_AD_CIRCLE + " text, " +
            AdBean.NAME_AD_PLAY_TIME + " text, " +
            AdBean.NAME_VIDEO_SAVE_PATH + " text, " +
            AdBean.NAME_PLAY_COUNT + " integer, " +
            AdBean.NAME_TIME_RULE + " text)";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        L.i("db onCreate");
        L.i("db sql:" + createTableSql);
        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
