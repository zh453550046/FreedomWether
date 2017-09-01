package fw.android.com.freedomweather.splash;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import fw.android.com.freedomweather.MainActivity;
import fw.android.com.freedomweather.R;
import fw.android.com.freedomweather.base.BaseActivity;
import fw.android.com.freedomweather.common.BaseActivityHandler;

public class SplashActivity extends BaseActivity {

    private ImageView iv_logo;

    private TextView tv_tip;

    private BaseActivityHandler handler = new BaseActivityHandler(this);

    private final int WHAT_GO_MAIN = 1;

    private final int WHAT_FINISH = 2;

    private final int WHAT_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        tv_tip.animate().setDuration(3000).alpha(1).start();

        handler.sendEmptyMessageDelayed(WHAT_GO_MAIN, WHAT_DELAY);

    }

    @Override
    public void handleMessage(Message message) {
        int what = message.what;
        if (what == WHAT_GO_MAIN) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent, ActivityOptions
                    .makeSceneTransitionAnimation(this, iv_logo, getResources().getString(R.string.transition_name))
                    .toBundle());
            handler.sendEmptyMessageDelayed(WHAT_FINISH, 1000);
        } else if (what == WHAT_FINISH) {
            finish();
        }
    }

}
