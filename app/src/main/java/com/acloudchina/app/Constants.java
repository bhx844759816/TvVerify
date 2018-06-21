package com.acloudchina.app;

import android.os.Environment;

import java.io.File;

public class Constants {

    public static final String BASE_URL = "http://101.200.63.58:3100/app/";

    public static final String AD_SAVE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "TvVerify" + File.separator + "Video";

}
