package com.noah.mgtv.toolslib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkUtils {

    public static final String NET_TYPE_WIFI = "WIFI";
    public static final String NET_TYPE_LTE = "LTE";
    public static final String NET_TYPE_3G = "3G";
    public static final String NET_TYPE_2G = "2G";
    public static final String NET_TYPE_MOBILE_UNKNOW = "MOBLE_UNKOWN";
    public static final String NET_TYPE_NO_NETWORK = "NONETWORK";
    public static final String NET_TYPE_UNKNOWN = "UNKNOWN";

    public static final int NET_VALUE_WIFI = 1;
    public static final int NET_VALUE_LTE_4G = 2;
    public static final int NET_VALUE_3G = 3;
    public static final int NET_VALUE_2G = 4;
    public static final int NET_VALUE_NO_NETWORK = 5;
    public static final int NET_VALUE_UNKNOWN = 6;

    public static final int ALLOW_NONE = 0;
    public static final int ALLOW_WIFI = 1;
    public static final int ALLOW_LTE_4G = 1 << 1;
    public static final int ALLOW_3G = 1 << 2;
    public static final int ALLOW_2G = 1 << 3;
    public static final int ALLOW_NO_NETWORK = 1 << 4;
    public static final int ALLOW_UNKNOWN = 1 << 5;

    public static final int ALLOW_MOBILE = ALLOW_LTE_4G | ALLOW_3G | ALLOW_2G;
    public static final int ALLOW_ANY = ALLOW_WIFI | ALLOW_LTE_4G | ALLOW_3G | ALLOW_2G | ALLOW_UNKNOWN;
    public static final int ALLOW_ALL =
            ALLOW_WIFI | ALLOW_LTE_4G | ALLOW_3G | ALLOW_2G | ALLOW_NO_NETWORK | ALLOW_UNKNOWN;

    private static final String HOST_PATTERN = "(?<=//|)((\\w)+\\.)+\\w+";

    public static boolean isWifiNetwork(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        return info != null && info.getType() == ConnectivityManager.TYPE_WIFI;
    }

    private static boolean isNetworkConnected(Context context) {
        if (context == null) {
            return false;
        }

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        return !(networkInfo == null || !networkInfo.isConnected());
    }

    public static boolean isUrl(String url) {
        if (url == null || TextUtils.isEmpty(url)) {
            return false;
        }
        Uri uri = Uri.parse(url);
        if (uri != null) {
            String scheme = uri.getScheme();
            if (scheme != null && (scheme.equals("http") || scheme.equals("https"))) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getDnsServers() {
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            Method method = systemProperties.getMethod("get", new Class[]{String.class});
            ArrayList<String> servers = new ArrayList<String>();
            for (String name : new String[]{"net.dns1", "net.dns2", "net.dns3", "net.dns4"}) {
                String value = (String) method.invoke(null, name);
                if (value != null && !"".equals(value) && !servers.contains(value)) {
                    servers.add(value);
                }
            }
            return servers;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static boolean isMobileNetwork(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        return info != null && info.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    public static String getHostAddressByUrl(String urlStr) {
        if (urlStr == null || urlStr.trim().equals("")) {
            return "";
        }
        String host = "";
        Pattern p = Pattern.compile(HOST_PATTERN);
        Matcher matcher = p.matcher(urlStr);
        if (matcher.find()) {
            host = matcher.group();
        }
        return host;
    }

    public static String getCurrentNetworkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return NET_TYPE_WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

                if ("TD-SCDMA".equals(networkInfo.getSubtypeName())) {
                    return NET_TYPE_3G;
                }
                return translateMobileType(networkInfo.getSubtype());
            } else {
                return NET_TYPE_UNKNOWN;
            }
        } else {
            return NET_TYPE_NO_NETWORK;
        }
    }

    public static String translateMobileType(int type) {
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return NET_TYPE_UNKNOWN;

            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_IDEN:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return NET_TYPE_2G;

            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return NET_TYPE_3G;

            case TelephonyManager.NETWORK_TYPE_LTE:
                return NET_TYPE_LTE;

            default:
                return NET_TYPE_MOBILE_UNKNOW;
        }
    }

    public static int translateNetworkTypeValue(String strNetType) {
        int netType = NET_VALUE_NO_NETWORK;
        if (TextUtils.equals(strNetType, NET_TYPE_WIFI)) {
            netType = NET_VALUE_WIFI;
        } else if (TextUtils.equals(strNetType, NET_TYPE_LTE)) {
            netType = NET_VALUE_LTE_4G;
        } else if (TextUtils.equals(strNetType, NET_TYPE_3G)) {
            netType = NET_VALUE_3G;
        } else if (TextUtils.equals(strNetType, NET_TYPE_2G)) {
            netType = NET_VALUE_2G;
        } else if (TextUtils.equals(strNetType, NET_TYPE_NO_NETWORK)) {
            netType = NET_VALUE_NO_NETWORK;
        } else if (TextUtils.equals(strNetType, NET_TYPE_UNKNOWN)) {

            netType = NET_VALUE_NO_NETWORK;
        }

        return netType;
    }

    public static int getCurrentNetworkTypeValue(Context context) {
        String strNetType = getCurrentNetworkType(context);
        return translateNetworkTypeValue(strNetType);
    }

    public static int translateAllowNetType(int currentTypeValue) {
        switch (currentTypeValue) {
            case NET_VALUE_WIFI:
                return ALLOW_WIFI;
            case NET_VALUE_LTE_4G:
                return ALLOW_LTE_4G;
            case NET_VALUE_3G:
                return ALLOW_3G;
            case NET_VALUE_2G:
                return ALLOW_2G;
            case NET_VALUE_NO_NETWORK:
                return ALLOW_NO_NETWORK;
            case NET_VALUE_UNKNOWN:
                return ALLOW_UNKNOWN;
            default:
                return ALLOW_NONE;
        }
    }

    public static int parseAllowNetType(String netTypes, int defaultAllow) {
        int allowNetType = defaultAllow;
        if (!TextUtils.isEmpty(netTypes)) {
            String[] items = netTypes.split(",");
            int len = items.length;
            for (int i = 0; i < len; i++) {
                String netType = items[i].trim();

                try {
                    int value = Integer.parseInt(netType);
                    switch (value) {
                        case 1:
                            allowNetType |= ALLOW_WIFI;
                            break;
                        case 2:
                            allowNetType |= ALLOW_LTE_4G;
                            break;
                        case 3:
                            allowNetType |= ALLOW_3G;
                            break;
                        case 4:
                            allowNetType |= ALLOW_2G;
                            break;
                        default:
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return allowNetType;
    }

    public static boolean allowCurrentNetworkType(Context context, int allowNetTypes) {
        int current = getCurrentNetworkTypeValue(context);
        int allow = translateAllowNetType(current);
        return (allow & allowNetTypes) != 0;
    }

    public static String buildParms(Map<String, Object> params) {
        StringBuilder strParm = new StringBuilder();
        if (params == null) {
            return strParm.toString();
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (strParm.length() > 0) {
                strParm.append("&");
            }
            strParm.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return strParm.toString();
    }

    public static String buildUrl(String url, Map<String, Object> params) {
        if (!isUrl(url)) {
            return "";
        }
        if (params == null || params.isEmpty()) {
            return url;
        }
        return url + "?" +
                buildParms(params);

    }

    private static boolean isConnectionFast(int type, int subType) {
        Set<Integer> netSlowNetTypeSet = new HashSet<>();
        netSlowNetTypeSet.add(TelephonyManager.NETWORK_TYPE_1xRTT); // ~ 50-100 kbps, 2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡,
        netSlowNetTypeSet.add(TelephonyManager.NETWORK_TYPE_CDMA);  // ~ 14-64 kbps, 2G 电信 Code Division Multiple Access 码分多址
        netSlowNetTypeSet.add(TelephonyManager.NETWORK_TYPE_EDGE);  // ~ 50-100 kbps, 2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps
        netSlowNetTypeSet.add(TelephonyManager.NETWORK_TYPE_GPRS);  // ~ 100 kbps, 2G(2.5) General Packet Radia Service 114kbps
        netSlowNetTypeSet.add(TelephonyManager.NETWORK_TYPE_IDEN);  // ~25 kbps, 2G Integrated Dispatch Enhanced Networks 集成数字增强型网络
        /** Current network is GSM {@hide} */
        //public static final int NETWORK_TYPE_GSM = 16;
        netSlowNetTypeSet.add(16);  // 2G

        Set<Integer> netFastNetTypeSet = new HashSet<>();
        netFastNetTypeSet.add(TelephonyManager.NETWORK_TYPE_EVDO_0); // ~ 400-1000 kbps, 3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps 属于3G
        netFastNetTypeSet.add(TelephonyManager.NETWORK_TYPE_EVDO_A); // ~ 600-1400 kbps, 3G 1.8mbps - 3.1mbps 属于3G过渡，3.5G
        netFastNetTypeSet.add(TelephonyManager.NETWORK_TYPE_EVDO_B); // ~ 5 Mbps, 3G EV-DO Rev.B 14.7Mbps 下行 3.5G
        netFastNetTypeSet.add(TelephonyManager.NETWORK_TYPE_HSDPA);  // ~ 2-14 Mbps, 3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps
        netFastNetTypeSet.add(TelephonyManager.NETWORK_TYPE_HSPA);   // ~ 700-1700 kbps, 3G (分HSDPA,HSUPA) High Speed Packet Access
        netFastNetTypeSet.add(TelephonyManager.NETWORK_TYPE_HSUPA);  // ~ 1-23 Mbps, 3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps
        netFastNetTypeSet.add(TelephonyManager.NETWORK_TYPE_UMTS);   // ~ 400-7000 kbps, 3G WCDMA 联通3G Universal MOBILE Telecommunication System 完整的3G移动通信技术标准
        netFastNetTypeSet.add(TelephonyManager.NETWORK_TYPE_EHRPD);  // ~ 1-2 Mbps, 3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data HRPD的升级
        netFastNetTypeSet.add(TelephonyManager.NETWORK_TYPE_HSPAP);  // ~ 10-20 Mbps, 3G HSPAP 比 HSDPA 快些
        netFastNetTypeSet.add(TelephonyManager.NETWORK_TYPE_LTE);    // ~ 10+ Mbps, 4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE Advanced 才是4G
        /* Current network is TD_SCDMA {@hide} */
        //public static final int NETWORK_TYPE_TD_SCDMA = 17;
        netFastNetTypeSet.add(17); // 中移动使用的3G
        /* Current network is IWLAN {@hide} */
        //public static final int NETWORK_TYPE_IWLAN = 18;
        netFastNetTypeSet.add(18); // 综合无线接入网

        if (type == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            if (netSlowNetTypeSet.contains(subType)) {
                return false;
            } else if (netFastNetTypeSet.contains(subType)) {
                return true;
            } else {
                return true; //其他未知移动网络返回true
            }
        } else {
            return false; //其他未知网络返回false
        }
    }

    private static boolean isConnectedFast(Context context) {
        Context appCtx = context.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager =
                (ConnectivityManager) appCtx.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && isConnectionFast(networkInfo.getType(), networkInfo.getSubtype());
        }
    }

    /**
     * 检查当前网络是否可用
     */
    @Deprecated
    public static boolean isNetworkAvailable(Context activity) {
        return isNetworkConnected(activity);
    }

    // 获取解决的host+path拼装 http://www.baidu.com/hello
    public static String getHostAndPath(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        Uri uri = Uri.parse(url);
        return uri.getScheme() + "://" + uri.getHost() + "/" + uri.getPath();
    }


    public static boolean isNormalNetWork(Context context) {
        return !(!NetworkUtils.isNetworkConnected(context) || !NetworkUtils.isConnectedFast(context));
    }

}
