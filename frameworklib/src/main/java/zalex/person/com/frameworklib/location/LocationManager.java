package zalex.person.com.frameworklib.location;

import android.content.Context;

/**
 * Created by zhouweinan on 2018/5/2.
 */

public class LocationManager {

    private static LocationInterface mLocation;

    public static void injectTrueLocation(Context context) {
        mLocation = new BaiDuLocationIm();
        mLocation.init(context.getApplicationContext());
    }

    public static void bindListener(LocationListener locationListener) {
        if (mLocation == null) {
            return;
        }
        mLocation.bindListener(locationListener);
    }

    public static void unBindListener() {
        if (mLocation == null) {
            return;
        }
        mLocation.unbindListener();
    }
}
