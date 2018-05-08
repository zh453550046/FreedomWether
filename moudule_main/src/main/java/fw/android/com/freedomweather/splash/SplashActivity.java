package fw.android.com.freedomweather.splash;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.noah.mgtv.imagelib.ImageDrawView;
import com.noah.mgtv.toolslib.sharedpreference.SpClient;

import fw.android.com.freedomweather.MainActivity;
import fw.android.com.freedomweather.R;
import zalex.person.com.frameworklib.base.BaseActivity;
import zalex.person.com.frameworklib.location.Location;
import zalex.person.com.frameworklib.location.LocationListener;
import zalex.person.com.frameworklib.location.LocationManager;
import zalex.person.com.frameworklib.route.RouterManager;
import zalex.person.com.frameworklib.route.RouterSchema;

public class SplashActivity extends BaseActivity {
    private ImageDrawView splashBg;
    private ImageView iv_logo;
    private TextView tv_city;

    private final int WHAT_GO_MAIN = 1;
    private static final int WHAT_FINISH = 2;
    private static final int WHAT_DELAY = 3000;
    private LocationListener mListener = new LocationListener() {
        @Override
        public void onLocationRecieve(Location location) {
            if (tv_city != null) {
                tv_city.setAlpha(0);
                tv_city.setText(location.getCity() + location.getDistrict());
                tv_city.animate().setDuration(1000).alpha(1).start();
                sendMessage(WHAT_GO_MAIN, WHAT_DELAY);
            }
        }

        @Override
        public void onError() {
            Toast.makeText(SplashActivity.this, getString(R.string.slash_erro_locaion), Toast.LENGTH_SHORT).show();
            RouterManager.buildRouter().uri(RouterSchema.SCHEMA_SELECT_CITIES).build();
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashBg = (ImageDrawView) findViewById(R.id.splash_bg);
        tv_city = (TextView) findViewById(R.id.tv_city);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        splashBg.setImageURI(Uri.parse("res://fw.android.com.freedomweather/" + R.mipmap.splash_bg));
        LocationManager.bindListener(mListener);
    }

    @Override
    public void handleMessage(Message message) {
        int what = message.what;
        if (what == WHAT_GO_MAIN) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent, ActivityOptions
                    .makeSceneTransitionAnimation(this, iv_logo, getResources().getString(R.string.transition_name))
                    .toBundle());
            sendMessage(WHAT_FINISH, 1000);
        } else if (what == WHAT_FINISH) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationManager.unBindListener();
    }
}
