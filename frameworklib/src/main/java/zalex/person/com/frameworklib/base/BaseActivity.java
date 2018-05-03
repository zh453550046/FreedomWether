package zalex.person.com.frameworklib.base;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import zalex.person.com.frameworklib.R;
import zalex.person.com.frameworklib.common.BaseActivityHandler;


/**
 * Created by zhouweinan on 2017/8/30.
 */

public class BaseActivity extends AppCompatActivity {

    private BaseActivityHandler mHandler = new BaseActivityHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }

    protected void sendMessage(int what, long delay) {
        mHandler.sendEmptyMessageDelayed(what, delay);
    }

    public void handleMessage(Message message) {

    }
}
