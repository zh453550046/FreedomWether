package zalex.person.com.main_builder;

import android.app.Application;

import fw.android.com.freedomweather.application.ModuleApplication;


/**
 * Created by zhouweinan on 2018/4/27.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ModuleApplication.onCreate(this);
    }
}
