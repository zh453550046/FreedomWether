package zalex.person.com.frameworklib.location;

import android.content.Context;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.noah.mgtv.toolslib.sharedpreference.SpClient;

/**
 * Created by zhouweinan on 2018/5/2.
 */

public class BaiDuLocationIm extends LocationInterface {

    private LocationClient mLocationClient;

    private LocationListener mLocationListener;

    @Override
    public void init(Context context) {
        mLocationClient = new LocationClient(context.getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true
        option.setIsNeedAddress(true);
        option.setIgnoreKillProcess(false);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void bindListener(LocationListener locationListener) {
        if (mLocationClient != null) {
            mLocationListener = locationListener;
            mLocationClient.registerLocationListener(this);
            if (!mLocationClient.isStarted()) {
                mLocationClient.start();
            } else {
                mLocationClient.requestLocation();
            }
        }
    }

    @Override
    public void unbindListener() {
        mLocationListener = null;
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (mLocationListener != null) {
            String city = bdLocation.getCity();    //获取城市
            String district = bdLocation.getDistrict();    //获取区县
            int type = bdLocation.getLocType();
            if (TextUtils.isEmpty(city) || TextUtils.isEmpty(district) || type == 62 || type == 63 || type == 68 || type == 167) {
                mLocationListener.onError();
            } else {
                Location location = new Location();
                location.setCity(city);
                location.setDistrict(district);
                mLocationListener.onLocationRecieve(location);
                SpClient.putDistrict(location.getDistrict());
                SpClient.putCity(location.getCity());
            }
        }
    }
}
