package com.noah.mgtv.network;

/**
 * Created by zhouweinan on 2018/4/2.
 */

public interface NetworkCallback<T> {

    public void onSuccess(T module);

    public void onSuccessInError(T module);

    public void onFaile(Throwable e);

}
