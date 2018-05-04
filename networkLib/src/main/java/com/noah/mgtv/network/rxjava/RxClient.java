package com.noah.mgtv.network.rxjava;

import android.content.Context;
import com.noah.mgtv.network.NetworkCallback;
import com.noah.mgtv.network.api.BaseObserver;
import com.noah.mgtv.network.networkevent.HandlerRequestErrorUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by zhouweinan on 2018/4/3.
 */

public class RxClient<T> implements RxClientInterface<T> {

    @Override
    public void sendRequestByRxJava(Context context,Observable<Response<T>> observable, NetworkCallback<T> callBack) {
           observable.subscribeOn(Schedulers.io()) // 在子线程中进行Http访问
                   .observeOn(AndroidSchedulers.mainThread()) // UI线程处理返回接口
                   .subscribe(new BaseObserver<>(context,callBack,new HandlerRequestErrorUtil<T>()));
    }
}
