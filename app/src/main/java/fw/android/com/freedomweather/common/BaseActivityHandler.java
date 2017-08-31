package fw.android.com.freedomweather.common;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

import fw.android.com.freedomweather.base.BaseActivity;

/**
 * Created by zhouweinan on 2017/8/30.
 */

public class BaseActivityHandler extends Handler {
    private WeakReference<BaseActivity> target;

    public BaseActivityHandler(BaseActivity baseActivity) {
        target = new WeakReference<>(baseActivity);
    }

    public BaseActivityHandler(Looper looper, BaseActivity baseActivity) {
        super(looper);
        target = new WeakReference<>(baseActivity);
    }

    protected BaseActivity getTarget() {
        return target != null ? target.get() : null;
    }

    @Override
    public void handleMessage(Message msg) {
        BaseActivity activity = getTarget();
        if (activity == null || activity.isFinishing()) {
            target = null;
            removeCallbacksAndMessages(null);
        } else {
            activity.handleMessage(msg);
        }
    }
}
