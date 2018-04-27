package fw.android.com.freedomweather.application;

import android.app.Application;

import com.noah.mgtv.imagelib.ImageApplication;
import com.noah.mgtv.network.application.NetworkApplication;
import com.noah.mgtv.toolslib.application.ToolApplication;

/**
 * Created by zhouweinan on 2018/4/27.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ToolApplication.onCreate(this);
        ImageApplication.onCreate(this);
        NetworkApplication.onCreate(this);
    }
}
