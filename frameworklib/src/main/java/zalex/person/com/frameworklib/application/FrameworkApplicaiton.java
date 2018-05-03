package zalex.person.com.frameworklib.application;

import android.app.Application;

import zalex.person.com.frameworklib.location.LocationManager;
import zalex.person.com.frameworklib.route.RouterManager;

/**
 * Created by zhouweinan on 2018/5/2.
 */

public class FrameworkApplicaiton {

    public static void onCreate(Application context) {
        LocationManager.injectTrueLocation(context);
        RouterManager.init(context);
    }
}
