package com.noah.mgtv.imagelib;

import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by zhouweinan on 2018/4/17.
 */

public class ImageApplication {

    public static void onCreate(Context context){
        Fresco.initialize(context);
    }
}
