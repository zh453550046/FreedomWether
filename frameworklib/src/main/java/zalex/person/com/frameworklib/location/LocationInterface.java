package zalex.person.com.frameworklib.location;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;

/**
 * Created by zhouweinan on 2018/5/2.
 */

public abstract class LocationInterface extends BDAbstractLocationListener {

    public abstract void init(Context context);

    public abstract void bindListener(LocationListener locationListener);

    public abstract void unbindListener();
}
