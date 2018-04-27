package com.noah.mgtv.network.publicparams;


import android.content.Context;
import android.text.TextUtils;

import com.noah.mgtv.toolslib.AppUtils;
import com.noah.mgtv.toolslib.DeviceInfoUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouweinan on 2018/3/30.
 */

public class HeaderUtil {
    public static Map<String, Object> defaultHeader(Context context) {
        Map<String, Object> defaultHeader = new HashMap<>();
        defaultHeader.put("noah", "1");
        String osVersion = DeviceInfoUtil.getOSVersion();
        if (!TextUtils.isEmpty(osVersion)) {
            defaultHeader.put("osVersion", osVersion);
        }
        String ostype = DeviceInfoUtil.getOSType();
        if (!TextUtils.isEmpty(ostype)) {
            defaultHeader.put("osType", ostype);
        }
        String device = DeviceInfoUtil.getDevice();
        if (!TextUtils.isEmpty(device)) {
            defaultHeader.put("device", device);
        }
        String manufacturer = DeviceInfoUtil.getManufacturer();
        if (!TextUtils.isEmpty(manufacturer)) {
            defaultHeader.put("manufacturer", manufacturer);
        }
        String endType = DeviceInfoUtil.getEndType();
        if (!TextUtils.isEmpty(endType)) {
            defaultHeader.put("endType", endType);
        }
        String appVersion = DeviceInfoUtil.getVersionName(context);
        if (!TextUtils.isEmpty(appVersion)) {
            defaultHeader.put("appVersion", appVersion + getReportVersionSuffix());
        }
        String deviceId = DeviceInfoUtil.getDeviceId(context);
        if (!TextUtils.isEmpty(deviceId)) {
            defaultHeader.put("deviceId", deviceId);
        }
        String did = DeviceInfoUtil.getUniqueID(context);
        defaultHeader.put("did", did);
        defaultHeader.put("appKey", "UV1Aep1c6m");
        defaultHeader.put("Content-Type", "application/json");
        defaultHeader.put("charset", "UTF-8");
        String build = AppUtils.getApplicationMetaData(context, "SDK_VERSION");
        if (!TextUtils.isEmpty(build)) {
            defaultHeader.put("build", build);
        }

        return defaultHeader;
    }

    private static String getReportVersionSuffix() {
        return "";
    }


}
