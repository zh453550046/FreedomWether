package com.noah.mgtv.network.rxjava;

import android.content.Context;

import com.noah.mgtv.datalib.BaseNetWorkModule;
import com.noah.mgtv.network.NetworkCallback;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by zhouweinan on 2018/4/3.
 */

public interface RxClientInterface {

    void init(Context context);

    void sendRequestByRxJava(Observable<Response<BaseNetWorkModule>> observable, NetworkCallback callBack);
}
