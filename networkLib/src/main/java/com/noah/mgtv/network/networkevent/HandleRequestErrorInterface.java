package com.noah.mgtv.network.networkevent;


import android.content.Context;

import com.noah.mgtv.datalib.BaseNetWorkModule;

import retrofit2.Response;

/**
 * Created by zhouweinan on 2018/4/3.
 */

public interface HandleRequestErrorInterface<T> {

    boolean checkNetworkAvailableBeforConnect(Context context);

    void handlerSuccessInErrorState(Response<T> response);

    void handlerHttpError(Response<T> response);

    void handlerNetworkError(Throwable e);

    void handlerParseErrorAfterRequest(Throwable e);
}
