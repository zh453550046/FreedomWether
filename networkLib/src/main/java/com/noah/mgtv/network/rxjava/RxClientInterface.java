package com.noah.mgtv.network.rxjava;

import android.content.Context;

import com.noah.mgtv.datalib.BaseNetWorkModule;
import com.noah.mgtv.network.NetworkCallback;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by zhouweinan on 2018/4/3.
 */

public interface RxClientInterface<T> {

    void sendRequestByRxJava(Context context,Observable<Response<T>> observable, NetworkCallback<T> callBack);
}
