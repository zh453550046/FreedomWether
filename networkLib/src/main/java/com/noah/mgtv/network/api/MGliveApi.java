package com.noah.mgtv.network.api;

import com.noah.mgtv.datalib.BaseNetWorkModule;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.QueryMap;

/**
 * Created by zhouweinan on 2018/4/2.
 */

public interface MGliveApi {

    @GET(ApiPaths.MGLIV_PATH)
    Observable<Response<BaseNetWorkModule>> getMenu(@QueryMap Map<String, Object> queryMap, @HeaderMap Map<String, Object> headerMap);
}
