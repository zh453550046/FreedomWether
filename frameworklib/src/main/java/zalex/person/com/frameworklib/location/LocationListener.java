package zalex.person.com.frameworklib.location;

/**
 * Created by zhouweinan on 2018/5/2.
 */

public interface LocationListener {

    public void onLocationRecieve(Location location);

    public void onError();
}
