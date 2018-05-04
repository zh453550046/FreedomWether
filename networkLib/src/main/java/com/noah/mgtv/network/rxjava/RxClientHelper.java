package com.noah.mgtv.network.rxjava;

import android.content.Context;

/**
 * Created by zhouweinan on 2018/5/4.
 */

public class RxClientHelper implements RxClientHelperInterface {

    private Context mContext;

    @Override
    public void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public Context getNetworkContext() {
        return mContext;
    }
}
