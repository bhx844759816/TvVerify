package com.acloudchina.app.callback;

import com.acloudchina.app.bean.TvBean;
import com.acloudchina.app.http.BaseCallBack;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public abstract class OnTvBeanCallBack<T> extends BaseCallBack<T> {
    @Override
    protected void OnRequestBefore(Request request) {

    }

    @Override
    protected void onFailure(Call call, IOException e) {

    }

    @Override
    protected void onSuccess(Call call, Response response, T t) {

    }

    @Override
    protected void onResponse(Response response) {

    }

    @Override
    protected void onEror(Call call, int statusCode, Exception e) {

    }

    @Override
    protected void inProgress(int progress, long total, int id) {

    }
}
