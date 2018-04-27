package com.noah.mgtv.toolslib;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class AppUtils {

    private static final String CHANNEL_APP_KEY = "UV1Aep1c6m";

    public static int getAppVersionCode(Context context) {

        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            if (packInfo != null) {
                return packInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static String getAppVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            if (packInfo != null) {
                return packInfo.versionName;
            }
        } catch (PackageManager.NameNotFoundException exception) {
            exception.printStackTrace();
        }
        return "";
    }

    public static String getApplicationMetaData(Context context, String name) {

        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            if (ai.metaData != null) {
                return ai.metaData.getString(name);
            }
        } catch (PackageManager.NameNotFoundException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * 应用是否运行在当前界面
     *
     * @param context 上下文环境
     * @return 返回布尔值
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String[] param = PackageUtils.queryRunningTopActivity(am);
        String pkgName = param[0];
        return !TextUtils.isEmpty(pkgName) && context.getPackageName().equals(pkgName);
    }

    public static void setLayerTypeSoftware(View view) {
        if (Build.VERSION.SDK_INT > 14) {
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    public static void setFullScreen(Window window, boolean fullScreen) {
        if (fullScreen) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    /**
     * 获取app MD5值
     *
     * @param ctx 上下文环境
     * @return 返回app签名值
     */
    public static String getAppSignMd5(Context ctx) {
        PackageInfo info = PackageUtils.getPackageInfo(ctx, ctx.getPackageName());
        Signature signature = null;
        if (info != null) {
            signature = info.signatures[0];
        }
        if (signature != null) {
            return signature.toCharsString();
        }
        return null;
    }

    /**
     * date 创建时间 16/8/26
     * @author liushu 重启应用
     */
    public static boolean restartApp(Context ctx) {
        Process.killProcess(Process.myPid());
        return PackageUtils.startupApp(ctx, ctx.getPackageName());
    }

    public static String getPackageName(Context ctx) {
        return ctx.getPackageName();
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (mActivityManager != null) {
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        }
        return null;
    }

    // 返回首页接口所需的appkey
    public static String getChannelAppkey() {
        return CHANNEL_APP_KEY;
    }
}
