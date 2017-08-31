package fw.android.com.freedomweather;

import android.os.Bundle;
import android.os.Message;

import fw.android.com.freedomweather.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void handleMessage(Message message) {

    }
}
