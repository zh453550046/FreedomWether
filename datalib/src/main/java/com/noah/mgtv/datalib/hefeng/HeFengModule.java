package com.noah.mgtv.datalib.hefeng;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhouweinan on 2018/5/4.
 */

public class HeFengModule implements Serializable {

    private List<HeWeather> HeWeather6;

    public List<HeWeather> getHeWeather6() {
        return HeWeather6;
    }

    public void setHeWeather6(List<HeWeather> heWeather6) {
        HeWeather6 = heWeather6;
    }
}
