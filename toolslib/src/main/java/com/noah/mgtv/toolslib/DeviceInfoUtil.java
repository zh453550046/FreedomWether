package com.noah.mgtv.toolslib;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.WindowManager;


import com.noah.mgtv.toolslib.logger.Logger;
import com.noah.mgtv.toolslib.sharedpreference.SpClient;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.UUID;


/**
 * qiansong
 */
public class DeviceInfoUtil {

    private static final String TAG = "DeviceInfo";
    private static final String DEFAULT_FRAMEWORK_LIB_LIST = "0000000000";
    private static final int UNIQUE_ID_LENGTH = 17;

    private static String mUniqueID;
    private static int width;
    private static int height;

    /**
     * 获取versioncode
     *
     * @param context
     * @return int
     */
    public static int getVersionCode(Context context) {
        return AppUtils.getAppVersionCode(context);
    }

    /**
     * 获取versionName
     *
     * @param context
     * @return String
     */
    public static String getVersionName(Context context) {
        return AppUtils.getAppVersionName(context);
    }

    /**
     * 获取Cpu利用信息
     */
    public static int getCpuUsedMsg() {
        String result;
        BufferedReader br = null;
        try {
            Process process = Runtime.getRuntime().exec("top -n 1");
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((result = br.readLine()) != null) {
                if (result.trim().length() < 1) {
                    continue;
                } else {
                    String[] cupStr = result.split("%");
                    String[] cpuUsage = cupStr[0].split("User");
                    String[] sysUsage = cupStr[1].split("System");
                    try {
                        int userUse = Integer.parseInt(cpuUsage[1].trim());
                        int sysUse = Integer.parseInt(sysUsage[1].trim());
                        return userUse + sysUse;
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                    }
                    break;
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            IoUtils.close(br);
        }
        return 0;
    }

    /**
     * 获取分辨率
     *
     * @param context
     * @return String
     */
    public static String getResolution(Context context) {
        String resolution = "";
        try {
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            window.getDefaultDisplay().getMetrics(metrics);
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            int temp;
            if (width > height) {
                temp = width;
                width = height;
                height = temp;
            }
            resolution = String.valueOf(width) + "," + String.valueOf(height);
        } catch (Exception e2) {
            Logger.e(TAG, e2);

        }
        return resolution;
    }

    /**
     * 获取分辨率
     *
     * @param context
     * @return String
     */
    public static String getResolution2(Context context) {
        String resolution = "";
        try {
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            window.getDefaultDisplay().getMetrics(metrics);
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            int temp;
            if (width > height) {
                temp = width;
                width = height;
                height = temp;
            }
            resolution = String.valueOf(width) + "*" + String.valueOf(height);
        } catch (Exception e2) {
            Logger.e(TAG, e2);

        }
        return resolution;
    }

    /**
     * 获取分辨率
     *
     * @param context
     * @return int
     */
    public static int getResolutionWidth(Context context) {
        if (width >= 0) {
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            window.getDefaultDisplay().getMetrics(metrics);
            width = metrics.widthPixels;
            height = metrics.heightPixels;
            int temp;
            if (width > height) {
                temp = width;
                width = height;
                height = temp;
            }
        }
        return width;
    }

    /**
     * 获取分辨率
     *
     * @param context
     * @return int
     */
    public static int getResolutionHeight(Context context) {
        if (width >= 0) {
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            window.getDefaultDisplay().getMetrics(metrics);
            width = metrics.widthPixels;
            height = metrics.heightPixels;
            int temp;
            if (width > height) {
                temp = width;
                width = height;
                height = temp;
            }
        }
        return height;
    }

    /**
     * @return cpu info
     */
    public static String getCpuInfo() {
        String str = null;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader("/proc/cpuinfo");
            try {
                bufferedReader = new BufferedReader(fileReader);
                str = bufferedReader.readLine();
                return str.trim();
            } catch (IOException ex) {
                Logger.d("读CPU信息失败");
            } finally {
                IoUtils.close(bufferedReader);
                IoUtils.close(fileReader);
            }
        } catch (FileNotFoundException fileEx) {
            Logger.d("读CPU信息失败");
        }
        return "";
    }


    public static String getPhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = "";
        try {
            if (PackageUtils.checkPermission(context, "android.permission.READ_PHONE_STATE")) {
                phoneNumber = telephonyManager.getLine1Number();
                if (phoneNumber.startsWith("+86")) {
                    phoneNumber = phoneNumber.substring(3);
                }
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
        return phoneNumber;
    }

    public static String getImsi(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = "";
        try {
            if (PackageUtils.checkPermission(context, "android.permission.READ_PHONE_STATE")) {
                imsi = telephonyManager.getSimSerialNumber();
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
        return imsi;
    }

    /**
     * @param context 上下文
     * @return 设备ID
     */
    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = "";
        try {
            if (PackageUtils.checkPermission(context, "android.permission.READ_PHONE_STATE")) {
                deviceId = telephonyManager.getDeviceId();
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }
        if (TextUtils.isEmpty(deviceId)) {
            Logger.d(TAG, "No IMEI.");
            deviceId = getMacAddress(context);
            if (deviceId == null) {
                Logger.e(TAG, "Failed to take mac as IMEI.");
            }
        }
        if (StringUtil.isNullorEmpty(deviceId)) {
            deviceId = getUniquePsuedoID();
        }
        if (StringUtil.isNullorEmpty(deviceId)) {
            deviceId = "unknown";
        }
        return deviceId;
    }

    public static String getUniquePsuedoID() {
        String serial;

        String m_szDevIDShort = "35" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; // 13 位

        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            // API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        // 使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    /**
     * @param paramContext paramContext
     * @return String
     *
     */
    public static String getMacAddress(Context paramContext) {
        String result = null;
        try {
            WifiManager wifiManager = (WifiManager) paramContext.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            result = wifiInfo.getMacAddress();
        } catch (Exception ex) {
            Logger.d(TAG, "不能读mac地址");
        }
        return result;
    }

    // 0=iOS 1=android
    public static String getOSType() {
        return "1";
    }

    public static String getEndType() {
        return "phone";
    }

    public static String getDevice() {
        return Build.MODEL;
    }

    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static String getSimOperatorInfo(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operatorString = telephonyManager.getSimOperator();
        if (operatorString == null) {
            return "04";
        }
        if (operatorString.equals("46000") || operatorString.equals("46002")) {
            // 中国移动
            return "00";
        } else if (operatorString.equals("46001")) {
            // 中国联通
            return "01";
        } else if (operatorString.equals("46003")) {
            // 中国电信
            return "03";
        }
        // error
        return "04";
    }

    public static String getSimCountryMCC(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String operatorString = telephonyManager.getSimOperator();
        if (operatorString != null && operatorString.length() > 3) {
            return operatorString.substring(0, 3);
        }
        return "";
    }

    /**
     * 获取APP所占内存
     *
     * @return 内存大小MB
     */
    public static int getAppMemory(Context mContext) {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        android.os.Debug.MemoryInfo[] processMemoryInfo =
                activityManager.getProcessMemoryInfo(new int[]{android.os.Process.myPid()});
        android.os.Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
        int totalPrivateDirty = memoryInfo.getTotalPrivateDirty() / 1024; // MB
        return totalPrivateDirty;

    }

    /**
     * 获取设备可用内存
     *
     * @return 可用内存大小MB
     */
    public static long getAvailMemory(Context mContext) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem / 1024;
    }

    /**
     * 获取当前屏幕旋转角度
     *
     * @param activity
     * @return 0表示是竖屏; 90表示是左横屏; 180表示是反向竖屏; 270表示是右横屏
     */
    public static int getDisplayRotation(Activity activity) {
        if (activity == null)
            return 0;

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

    /**
     * 返回运营商 需要加入权限 <uses-permission android:name="android.permission.READ_PHONE_STATE"/> <BR>
     *
     * @return 1, 代表中国移动，2，代表中国联通，3，代表中国电信，0，代表未知
     * @author youzc@yiche.com
     */
    public static String getOperators(Context context) {
        // 移动设备网络代码（英语：Mobile Network Code，MNC）是与移动设备国家代码（Mobile Country Code，MCC）（也称为“MCC /
        // MNC”）相结合, 例如46000，前三位是MCC，后两位是MNC 获取手机服务商信息
        String OperatorsName = "";
        String IMSI = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
        // IMSI号前面3位460是国家，紧接着后面2位00 运营商代码
        System.out.println(IMSI);
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
            OperatorsName = "cmcc";
        } else if (IMSI.startsWith("46001") || IMSI.startsWith("46006")) {
            OperatorsName = "cucc";
        } else if (IMSI.startsWith("46003") || IMSI.startsWith("46005")) {
            OperatorsName = "ctcc";
        }
        return OperatorsName;
    }

    /*** 获取 随机的一个 数字或者大小写英文字母 */
    private static char randomDigitOrLetter() {
        // 48～57 为 0~9 十个阿拉伯数字 
        // 65～90 为 26个 大写英文字母 
        // 97～122 为 26个 小写英文字母  
        Random random = new Random();
        int type = random.nextInt(3);
        if (0 == type) {
            // 阿拉伯数字 
            return (char) (48 + random.nextInt(10));
        } else if (1 == type) {
            // 大写英文字母 
            return (char) (65 + random.nextInt(26));
        } else {
            // 小写英文字母 
            return (char) (97 + random.nextInt(26));
        }
    }

    /*** 是否为 数字 或者 大小写英文字母 */
    private static boolean isDigitOrLetter(char c) {
        return (c >= 48 && c <= 57)
                || (c >= 65 && c <= 90)
                || (c >= 97 && c <= 122);
    }

    /*** 获取 设备唯一 ID。(标识 设备 唯一性)  前缀带 i，17位字符串*/
    public static @NonNull
    String getUniqueID(Context context) {
        if (!TextUtils.isEmpty(mUniqueID)) {
            return mUniqueID;
        }
        mUniqueID = SpClient.getUniqueId();
        if (!TextUtils.isEmpty(mUniqueID)) {
            return mUniqueID;
        }
        String uniqueID = null;
        // 1. 系统API 获取 设备ID
        try {
            TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            uniqueID = telephonyMgr.getDeviceId();
            if (BuildConfig.DEBUG)
                Log.e(TAG, "DeviceId(" + uniqueID + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 2. 获取 ANDROID_ID
        // 存在系统BUG 可能为恒值 "9774d56d682e549c"
        if (TextUtils.isEmpty(uniqueID)) {
            try {
                uniqueID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                if (TextUtils.equals("9774d56d682e549c", uniqueID)) {
                    uniqueID = null;
                }
                if (BuildConfig.DEBUG)
                    Log.e(TAG, "AndroidID(" + uniqueID + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // 3. 生成随机 UUID
        if (TextUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            if (BuildConfig.DEBUG)
                Log.e(TAG, "UUID(" + uniqueID + ")");
        }
        if (uniqueID == null) {
            uniqueID = "";
        }
        uniqueID = "i".concat(uniqueID);
        // 限定位数
        int length = uniqueID.length();
        if (length > UNIQUE_ID_LENGTH) {
            uniqueID = uniqueID.substring(0, UNIQUE_ID_LENGTH);
        }

        // v5.4.0: 不再补齐位数，避免 大数据 那边 数据紊乱
        /*
        else if (length < UNIQUE_ID_LENGTH) {
            int count = UNIQUE_ID_LENGTH - length;
            for (int i = 0; i < count; ++i) {
                uniqueID = uniqueID.concat(String.valueOf(randomDigitOrLetter())); 
            }
        } */
        int len = uniqueID.length();
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < len; ++i) {
            char c = uniqueID.charAt(i);
            if (isDigitOrLetter(c)) {
                strBuilder.append(c);
            } else {
                strBuilder.append(randomDigitOrLetter());
            }
        }
        mUniqueID = strBuilder.toString();
//        PreferencesUtil.putString(PreferencesUtil.PREF_DEVICE_INFO_UNIQUE_ID, mUniqueID);
        SpClient.putUniqueId(mUniqueID);
        if (BuildConfig.DEBUG)
            Logger.e(TAG, "RESULT(" + mUniqueID + ")");

        return mUniqueID;
    }
}
