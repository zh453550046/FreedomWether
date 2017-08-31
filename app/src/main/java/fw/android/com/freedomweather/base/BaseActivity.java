package fw.android.com.freedomweather.base;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import fw.android.com.freedomweather.R;

/**
 * Created by zhouweinan on 2017/8/30.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }

    public abstract void handleMessage(Message message);
}
