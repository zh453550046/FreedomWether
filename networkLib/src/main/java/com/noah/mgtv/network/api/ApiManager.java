package com.noah.mgtv.network.api;

import android.text.TextUtils;

import com.noah.mgtv.network.constents.RequestConstents;
import com.noah.mgtv.network.httpclient.DefaultHttpClientFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhouweinan on 2018/4/2.
 */

public class ApiManager {

    public static MGliveApi getMGliveApi() {
        return buildApi(RequestConstents.BASE_URL_MGLIVE, DefaultHttpClientFactory.getDefaultOkHttpClient(), MGliveApi.class);
    }

    public static LiveUrlApi getLiveUrlAPi() {
        return buildApi(RequestConstents.BASE_URL_LIVE, DefaultHttpClientFactory.getDefaultOkHttpClient(), LiveUrlApi.class);
    }

    public static CityUrlApi getCityUrlApi(){
        return buildApi(RequestConstents.BASE_URL_WEATHER,DefaultHttpClientFactory.getDefaultOkHttpClient(),CityUrlApi.class);
    }

    private static <T> T buildApi(String baseUrl, OkHttpClient okHttpClient, Class<T> cls) {
        if (TextUtils.isEmpty(baseUrl) || cls == null) {
            return null;
        }
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        if (okHttpClient != null) {
            retrofitBuilder.client(okHttpClient);
        }
        return retrofitBuilder.build().create(cls);
    }
}
