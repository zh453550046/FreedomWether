package com.noah.mgtv.toolslib.sharedpreference;

import android.content.Context;

/**
 * Created by zhouweinan on 2018/4/8.
 */

public class SpClient {

    private static SpUtil mSpUtil;

    private static final String APP_NAME_KEY = "app";

    private static final String UNIQUE_ID = "uniqueId";

    private static final String CITY_KEY = "city";

    private static final String DISTRICT_KEY = "district";

    public static void init(Context context) {
        mSpUtil = SpUtil.newUtil(context.getApplicationContext());
    }

    public static void putAppName(String name) {
        mSpUtil.putString(APP_NAME_KEY, name);
    }

    public static String getAppName() {
        return mSpUtil.getString(APP_NAME_KEY, "");
    }

    public static void putUniqueId(String uniqueId) {
        mSpUtil.putString(UNIQUE_ID, uniqueId);
    }

    public static String getUniqueId() {
        return mSpUtil.getString(UNIQUE_ID, "");
    }

    public static void putCity(String city) {
        mSpUtil.putString(CITY_KEY, city);
    }

    public static String getCity() {
        return mSpUtil.getString(CITY_KEY, "");
    }

    public static void putDistrict(String district) {
        mSpUtil.putString(DISTRICT_KEY, district);
    }

    public static String getDistrict() {
        return mSpUtil.getString(DISTRICT_KEY, "");
    }
}
