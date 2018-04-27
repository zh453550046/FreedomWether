package com.noah.mgtv.toolslib.application;

import android.content.Context;

import com.noah.mgtv.toolslib.sharedpreference.SpClient;

/**
 * Created by zhouweinan on 2018/4/8.
 */

public class ToolApplication {

    public static void onCreate(Context context) {
        SpClient.init(context);
    }
}
