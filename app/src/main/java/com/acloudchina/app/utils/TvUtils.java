package com.acloudchina.app.utils;

import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;

public class TvUtils {
    /**
     * 获取TvId
     * @return
     */
    public static String getSerialNum() {
        boolean b_Serial = false; //是否有串号标志
        String mSerialNum = null;
        byte[] sn = new byte[30];
        try {
            //CustomerSetImpl.getInstance().getSerialNumber();
            FileInputStream fis = new FileInputStream("/data/serialnum.bin");
            fis.read(sn, 0, 30);
            fis.close();
            //判断是否有烧录串号
            for (int i = 0; i < 30; i++) {

                if (sn[i] == -1) {
                    b_Serial = false;
                } else {
                    b_Serial = true;

                    break;
                }
            }
            mSerialNum = new String(sn, "UTF-8");
            L.i("getSerialNumber() =" + mSerialNum + "==");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (b_Serial) {
            return mSerialNum;
        } else {
            return null;
        }
    }
}
