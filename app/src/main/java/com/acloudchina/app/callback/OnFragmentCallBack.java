package com.acloudchina.app.callback;

public interface OnFragmentCallBack {

    void onAdPlayFinish();

    void onCheckNetWorkError();

    void netWorkSetting();


    void onCheckSuccess();

    /**
     * 检测不合法
     */
    void onCheckIsNotLegal();

    /**
     * 合约期外
     */
    void onCheckNotContract();

}
