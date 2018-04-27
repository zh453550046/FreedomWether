package com.noah.mgtv.network.application;

import android.content.Context;

import com.noah.mgtv.network.NetworkRequestFactory;

/**
 * Created by zhouweinan on 2018/4/3.
 */

public class NetworkApplication {

    public static void onCreate(Context context){
        NetworkRequestFactory.init(context);
    }
}
