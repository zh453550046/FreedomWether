package com.noah.mgtv.toolslib.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhouweinan on 2018/4/8.
 */

class SpUtil {

    private SharedPreferences mPreference;

    private static final String FILE_NAME = "data_lib";

    private SpUtil() {
    }

    static SpUtil newUtil(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SpUtil spUtil = new SpUtil();
        spUtil.mPreference = preferences;
        return spUtil;
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public String getString(String key, String defValue) {
        return mPreference.getString(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mPreference.getBoolean(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return mPreference.getFloat(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return mPreference.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return mPreference.getLong(key, defValue);
    }


}
