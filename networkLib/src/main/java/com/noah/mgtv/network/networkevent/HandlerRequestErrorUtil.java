package com.noah.mgtv.network.networkevent;

import android.content.Context;
import android.widget.Toast;
import com.noah.mgtv.datalib.BaseNetWorkModule;
import com.noah.mgtv.toolslib.NetworkUtils;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by zhouweinan on 2018/4/3.
 */

public class HandlerRequestErrorUtil<T> implements HandleRequestErrorInterface<T> {
    @Override
    public boolean checkNetworkAvailableBeforConnect(Context context) {
        if (!NetworkUtils.isNormalNetWork(context)) {
            Toast.makeText(context, "当前网络不可用，请检查网络情况", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void handlerSuccessInErrorState(Response<T> response) {
        // do report, this error belongs to logic error.
    }

    @Override
    public void handlerHttpError(Response<T> response) {
        // do report, this error's http code are not between 200 and 300.
    }

    @Override
    public void handlerNetworkError(Throwable e) {
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            int httpCode = httpException.code();
            String url = httpException.response().raw().request().url().toString();
            String errorMsg = httpException.message();
        } else {
            if (e instanceof Error) {
                System.gc();
            }
            String errorMsg = e.getMessage();
        }
    }

    @Override
    public void handlerParseErrorAfterRequest(Throwable e) {
        if (e instanceof Error) {
            System.gc();
        }
        String errorMsg = e.getMessage();
    }
}
