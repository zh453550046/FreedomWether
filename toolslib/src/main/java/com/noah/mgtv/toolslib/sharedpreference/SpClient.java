package com.noah.mgtv.toolslib.sharedpreference;

import android.content.Context;

/**
 * Created by zhouweinan on 2018/4/8.
 */

public class SpClient {

    private static SpUtil mSpUtil;

    private static final String APP_NAME_KEY = "app";

    private static final String UNIQUE_ID = "uniqueId";

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
}
