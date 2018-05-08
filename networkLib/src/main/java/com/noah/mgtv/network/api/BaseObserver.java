package com.noah.mgtv.network.api;

import android.content.Context;
import android.text.TextUtils;

import com.noah.mgtv.datalib.BaseNetWorkModule;
import com.noah.mgtv.datalib.hefeng.HeFengModule;
import com.noah.mgtv.datalib.hefeng.HeWeather;
import com.noah.mgtv.network.NetworkCallback;
import com.noah.mgtv.network.networkevent.HandleRequestErrorInterface;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

/**
 * Created by zhouweinan on 2018/4/2.
 */

public class BaseObserver<T> implements Observer<Response<T>> {

    private NetworkCallback<T> mNetworkCallback;

    private Context mContext;

    private HandleRequestErrorInterface<T> mHandler;

    public BaseObserver(Context context, NetworkCallback<T> networkCallback, HandleRequestErrorInterface<T> handler) {
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
    public void onNext(Response<T> response) {
        T result = response.body();
//        if (result instanceof BaseNetWorkModule) {
//            BaseNetWorkModule baseNetWorkModule = (BaseNetWorkModule) result;
//            if (response.isSuccessful()) {
//                if (mNetworkCallback != null) {
//                    if (TextUtils.equals(baseNetWorkModule.getCode(), "0") || TextUtils.equals(baseNetWorkModule.getCode(), "200")) {
//                        try {
//                            mNetworkCallback.onSuccess(result);
//                        } catch (Throwable e) {
//                            if (mHandler != null) {
//                                mHandler.handlerParseErrorAfterRequest(e);
//                            }
//                        }
//                    } else {
//                        if (mHandler != null) {
//                            mHandler.handlerSuccessInErrorState(response);
//                        }
//                        mNetworkCallback.onSuccessInError(result);
//                    }
//                }
//                mNetworkCallback = null;
//            } else {
//                mHandler.handlerHttpError(response);
//            }
//        } else {
//            if (result instanceof HeFengModule) {
//                HeFengModule heFengModule = (HeFengModule) result;
//                if (response.isSuccessful()) {
//                    if (mNetworkCallback != null) {
//                        List<HeWeather> heWeatherList = heFengModule.getHeWeather6();
//                        if (heWeatherList != null && heWeatherList.size() > 0) {
//                            HeWeather heWeather = heWeatherList.get(0);
//                            if (heWeather != null && TextUtils.equals(heWeather.getStatus(), "ok")) {
//                                try {
//                                    mNetworkCallback.onSuccess(result);
//                                } catch (Throwable e) {
//                                    if (mHandler != null) {
//                                        mHandler.handlerParseErrorAfterRequest(e);
//                                    }
//                                }
//                            } else {
//                                if (mHandler != null) {
//                                    mHandler.handlerSuccessInErrorState(response);
//                                }
//                                mNetworkCallback.onSuccessInError(result);
//                            }
//                        }
//                    }
//                    mNetworkCallback = null;
//                } else {
//                    mHandler.handlerHttpError(response);
//                }
//            }
//        }
        if (response.isSuccessful()) {
            if (mNetworkCallback != null) {
                if (isResultOk(result)) {
                    try {
                        mNetworkCallback.onSuccess(result);
                    } catch (Throwable e) {
                        if (mHandler != null) {
                            mHandler.handlerParseErrorAfterRequest(e);
                        }
                    }
                } else {
                    if (mHandler != null) {
                        mHandler.handlerSuccessInErrorState(response);
                    }
                    mNetworkCallback.onSuccessInError(result);
                }
                mNetworkCallback = null;
            }
        } else {
            mHandler.handlerHttpError(response);
        }

    }

    private boolean isResultOk(T result) {
        if (result instanceof BaseNetWorkModule) {
            BaseNetWorkModule baseNetWorkModule = (BaseNetWorkModule) result;
            return TextUtils.equals(baseNetWorkModule.getCode(), "0") || TextUtils.equals(baseNetWorkModule.getCode(), "200");
        } else if (result instanceof HeFengModule) {
            HeFengModule heFengModule = (HeFengModule) result;
            List<HeWeather> heWeatherList = heFengModule.getHeWeather6();
            if (heWeatherList != null && heWeatherList.size() > 0) {
                HeWeather heWeather = heWeatherList.get(0);
                return heWeather != null && TextUtils.equals(heWeather.getStatus(), "ok");
            }
        }

        return false;
    }

    @Override
    public void onError(Throwable e) {
        mHandler.handlerNetworkError(e);
        if (mNetworkCallback != null) {
            mNetworkCallback.onFaile(e);
            mNetworkCallback = null;
        }
    }

    @Override
    public void onComplete() {

    }

}
