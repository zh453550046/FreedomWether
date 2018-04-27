package com.noah.mgtv.network;

import com.noah.mgtv.datalib.BaseNetWorkModule;

/**
 * Created by zhouweinan on 2018/4/2.
 */

public interface NetworkCallback {

    public void onSuccess(BaseNetWorkModule baseNetWorkModule);

    public void onSuccessInError(BaseNetWorkModule baseNetWorkModule);

    public void onFaile(Throwable e);

}
