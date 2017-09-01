package fw.android.com.freedomweather;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import fw.android.com.freedomweather.base.BaseActivity;
import fw.android.com.freedomweather.util.ScreenUtil;

public class MainActivity extends BaseActivity {

    private NavigationView navigationView;

    private LinearLayout ll_weather_tab;

    private long backTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setViewSize();

    }

    private void initView() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        ll_weather_tab = (LinearLayout) findViewById(R.id.ll_weather_tab);
    }

    private void setViewSize() {
        int screenWidth = ScreenUtil.getScreenWidth(this);
        ViewGroup.LayoutParams navigationLayoutParams = navigationView.getLayoutParams();
        navigationLayoutParams.width = screenWidth / 3 * 2;
        navigationView.setLayoutParams(navigationLayoutParams);

        ViewGroup.LayoutParams tabLayoutParams = ll_weather_tab.getLayoutParams();
        tabLayoutParams.height = (int) (screenWidth * 0.3);
        ll_weather_tab.setLayoutParams(tabLayoutParams);
    }

    @Override
    public void handleMessage(Message message) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - backTime > 2000) {
                backTime = currentTime;
                Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
