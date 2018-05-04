package com.noah.mgtv.datalib.hefeng;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhouweinan on 2018/5/4.
 */

public class HeWeather implements Serializable{

    private List<Basic> basic;

    private String status;

    public List<Basic> getBasic() {
        return basic;
    }

    public void setBasic(List<Basic> basic) {
        this.basic = basic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
