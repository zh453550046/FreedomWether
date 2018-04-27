package com.noah.mgtv.network.api;

import android.content.Context;
import android.text.TextUtils;

import com.noah.mgtv.datalib.BaseNetWorkModule;
import com.noah.mgtv.network.NetworkCallback;
import com.noah.mgtv.network.networkevent.HandleRequestErrorInterface;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

/**
 * Created by zhouweinan on 2018/4/2.
 */

public class BaseObserver implements Observer<Response<BaseNetWorkModule>> {

    private NetworkCallback mNetworkCallback;

    private Context mContext;

    private HandleRequestErrorInterface mHandler;

    public BaseObserver(Context context, NetworkCallback networkCallback, HandleRequestErrorInterface handler) {
        this.mNetworkCallback = networkCallback;
        mContext = context;
        mHandler = handler;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (mHandler != null && !mHandler.checkNetworkAvailableBeforConnect(mContext)) {
            d.dispose();
        }
    }

    @Override
    public void onNext(Response<BaseNetWorkModule> response) {
        BaseNetWorkModule baseNetWorkModule = response.body();
        if (response.isSuccessful()) {
            if (baseNetWorkModule != null && mNetworkCallback != null) {
                if (TextUtils.equals(baseNetWorkModule.getCode(), "0") || TextUtils.equals(baseNetWorkModule.getCode(), "200")) {
                    try {
                        mNetworkCallback.onSuccess(baseNetWorkModule);
                    } catch (Throwable e) {
                        if (mHandler != null) {
                            mHandler.handlerParseErrorAfterRequest(e);
                        }
                    }
                } else {
                    if (mHandler != null) {
                        mHandler.handlerSuccessInErrorState(response);
                    }
                    mNetworkCallback.onSuccessInError(baseNetWorkModule);
                }
            }
        } else {
            mHandler.handlerHttpError(response);
        }
    }

    @Override
    public void onError(Throwable e) {
        mHandler.handlerNetworkError(e);
        if (mNetworkCallback != null) {
            mNetworkCallback.onFaile(e);
        }
    }

    @Override
    public void onComplete() {

    }

}
