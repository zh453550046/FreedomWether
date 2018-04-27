package com.noah.mgtv.network;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * Created by zhouweinan on 2018/4/2.
 */
public class NetworkRequest {

    private Map<String, Object> mQeryMap;

    private Map<String, Object> mHeaderMap;

    private NetworkRequest() {
    }

    public Map<String, Object> getQeryMap() {
        return mQeryMap;
    }

    public Map<String, Object> getHeaderMap() {
        return mHeaderMap;
    }

    public static class Builder {

        private Map<String, Object> mQeryMap = new HashMap<>();

        private Map<String, Object> mHeaderMap = new HashMap<>();


        public Builder query(String key, String value) {
            mQeryMap.put(key, value);
            return this;
        }

        public Builder querys(Map<String, Object> qeryMap) {
            mQeryMap.putAll(qeryMap);
            return this;
        }

        public Builder header(String key, String value) {
            mHeaderMap.put(key, value);
            return this;
        }

        public Builder headers(Map<String, Object> headerMap) {
            mHeaderMap.putAll(headerMap);
            return this;
        }

        public NetworkRequest build() {
            NetworkRequest networkRequest = new NetworkRequest();
            networkRequest.mQeryMap = mQeryMap;
            networkRequest.mHeaderMap = mHeaderMap;
            return networkRequest;
        }
    }


}
