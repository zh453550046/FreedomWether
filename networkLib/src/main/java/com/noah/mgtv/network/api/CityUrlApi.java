package com.noah.mgtv.network.api;
import com.noah.mgtv.datalib.hefeng.HeFengModule;

import java.util.Map;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.QueryMap;

/**
 * Created by zhouweinan on 2018/5/4.
 */

public interface CityUrlApi {

    @GET(ApiPaths.HOT_CITIES_PATH)
    Observable<Response<HeFengModule>> getHotCities(@QueryMap Map<String, Object> queryMap, @HeaderMap Map<String, Object> headerMap);
}
