package com.noah.mgtv.network;

import android.content.Context;

import com.noah.mgtv.datalib.BaseNetWorkModule;
import com.noah.mgtv.network.api.ApiManager;
import com.noah.mgtv.network.api.LiveUrlApi;
import com.noah.mgtv.network.api.MGliveApi;
import com.noah.mgtv.network.publicparams.HeaderUtil;
import com.noah.mgtv.network.rxjava.RxClient;
import com.noah.mgtv.network.rxjava.RxClientInterface;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by zhouweinan on 2018/4/2.
 */

public class NetworkRequestFactory {

    private static MGliveApi mMGliveApi;

    private static LiveUrlApi mLivewApi;

    private final static RxClientInterface mClient = new RxClient();


    public static void init(Context context) {
        mMGliveApi = ApiManager.getMGliveApi();
        mLivewApi = ApiManager.getLiveUrlAPi();
        mClient.init(context);
    }


    public static void getMGLiveMenu(NetworkRequest networkRequest, NetworkCallback networkCallback) {
        if (mMGliveApi == null || networkRequest == null) {
            return;
        }

        sendRequestByRxJava(mMGliveApi.getMenu(networkRequest.getQeryMap(), networkRequest.getHeaderMap()), networkCallback);
    }

    public static void postLiveUrl(NetworkRequest networkRequest, NetworkCallback networkCallback) {
        if (mLivewApi == null || networkRequest == null) {
            return;
        }

        sendRequestByRxJava(mLivewApi.postLiveUrl(networkRequest.getQeryMap(), networkRequest.getHeaderMap()), networkCallback);
    }


    private static void sendRequestByRxJava(Observable<Response<BaseNetWorkModule>> observable, NetworkCallback callBack) {
        mClient.sendRequestByRxJava(observable, callBack);
    }


}
