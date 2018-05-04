package com.noah.mgtv.network;

import android.content.Context;

import com.noah.mgtv.datalib.BaseNetWorkModule;
import com.noah.mgtv.datalib.hefeng.HeFengModule;
import com.noah.mgtv.network.api.ApiManager;
import com.noah.mgtv.network.api.CityUrlApi;
import com.noah.mgtv.network.api.LiveUrlApi;
import com.noah.mgtv.network.api.MGliveApi;
import com.noah.mgtv.network.publicparams.HeaderUtil;
import com.noah.mgtv.network.rxjava.RxClient;
import com.noah.mgtv.network.rxjava.RxClientHelper;
import com.noah.mgtv.network.rxjava.RxClientHelperInterface;
import com.noah.mgtv.network.rxjava.RxClientInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by zhouweinan on 2018/4/2.
 */

public class NetworkRequestFactory {

    private static MGliveApi mMGliveApi;

    private static LiveUrlApi mLivewApi;

    private static CityUrlApi mCityApi;

    private static final RxClientHelperInterface mRxClientHelper = new RxClientHelper();

    public static void init(Context context) {
        mMGliveApi = ApiManager.getMGliveApi();
        mLivewApi = ApiManager.getLiveUrlAPi();
        mCityApi = ApiManager.getCityUrlApi();
        mRxClientHelper.init(context);
    }


    public static void getMGLiveMenu(NetworkRequest networkRequest, NetworkCallback<BaseNetWorkModule> networkCallback) {
        if (mMGliveApi == null || networkRequest == null) {
            return;
        }

        sendRequestByRxJava(mMGliveApi.getMenu(networkRequest.getQeryMap(), networkRequest.getHeaderMap()), networkCallback);
    }

    public static void postLiveUrl(NetworkRequest networkRequest, NetworkCallback<BaseNetWorkModule> networkCallback) {
        if (mLivewApi == null || networkRequest == null) {
            return;
        }

        sendRequestByRxJava(mLivewApi.postLiveUrl(networkRequest.getQeryMap(), networkRequest.getHeaderMap()), networkCallback);
    }

    public static void getHotCities(NetworkRequest networkRequest, NetworkCallback<HeFengModule> networkCallback) {
        if (mCityApi == null || networkRequest == null) {
            return;
        }

        sendRequestByRxJava(mCityApi.getHotCities(networkRequest.getQeryMap(), networkRequest.getHeaderMap()), networkCallback);
    }


    private static <T> void sendRequestByRxJava(Observable<Response<T>> observable, NetworkCallback<T> callBack) {
        RxClientInterface<T> rxClient = new RxClient<>();
        rxClient.sendRequestByRxJava(mRxClientHelper.getNetworkContext(), observable, callBack);
    }

}
