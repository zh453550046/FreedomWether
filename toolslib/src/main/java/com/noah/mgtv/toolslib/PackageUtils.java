package com.noah.mgtv.toolslib;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PackageUtils {

    public static PackageInfo getPackageInfo(Context context, String pkgName) {
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(pkgName, PackageManager.GET_DISABLED_COMPONENTS | PackageManager.GET_SIGNATURES);
        } catch (NameNotFoundException ex) {
            try {
                return pm.getPackageInfo(pkgName, PackageManager.GET_DISABLED_COMPONENTS
                        | PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_SIGNATURES);
            } catch (NameNotFoundException ex1) {
                ex1.printStackTrace();
            }
        }
        return null;
    }

    public static ApplicationInfo getAppInfo(Context context, String pkgName) {
        PackageInfo info = getPackageInfo(context, pkgName);
        if (info != null) {
            return info.applicationInfo;
        }
        return null;
    }

    public static Drawable getAppIcon(Context context, String apkFilepath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageArchiveInfo(apkFilepath, 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (pkgInfo == null) {
            return null;
        }

        ApplicationInfo appInfo = pkgInfo.applicationInfo;
        if (Build.VERSION.SDK_INT >= 8) {
            appInfo.sourceDir = apkFilepath;
            appInfo.publicSourceDir = apkFilepath;
        }

        return pm.getApplicationIcon(appInfo);
    }

    public static String getPackageLabel(Context ctx, String pkgName) {
        PackageInfo info = getPackageInfo(ctx, pkgName);
        if (info != null) {
            return (String) info.applicationInfo.loadLabel(ctx.getPackageManager());
        }
        return null;
    }

    public static Drawable getPackageIcon(Context ctx, String pkgName) {
        PackageInfo info = getPackageInfo(ctx, pkgName);
        if (info != null) {
            return info.applicationInfo.loadIcon(ctx.getPackageManager());
        }
        return null;
    }

    public static boolean isPkgInstalled(Context context, String pkgName) {
        return getPackageInfo(context, pkgName) != null;
    }

    public static boolean isPackageEnabled(Context ctx, String pkgName) {
        PackageManager pm = ctx.getPackageManager();
        final int enable = pm.getApplicationEnabledSetting(pkgName);
        return enable == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
                || enable == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
    }

    public static boolean isSystemApp(Context ctx, String pkgName) {
        try {
            PackageInfo info = getPackageInfo(ctx, pkgName);
            if (info != null) {
                return isSystemApp(info.applicationInfo.flags);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    private static boolean isSystemApp(int appInfoFlags) {
        if ((ApplicationInfo.FLAG_SYSTEM & appInfoFlags) != 0) {
            return true;
        }
        return false;
    }

    public static boolean isSystemAppNotUpdated(Context ctx, String pkgName) {
        try {
            PackageInfo info = getPackageInfo(ctx, pkgName);
            if (info != null) {
                return isSystemAppNotUpdated(info.applicationInfo.flags);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public static boolean isSystemAppNotUpdated(int appInfoFlags) {
        return (appInfoFlags & ApplicationInfo.FLAG_SYSTEM) != 0
                && (appInfoFlags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0;
    }

    public static boolean isOnExternalStorage(Context ctx, String pkgName) {
        try {
            PackageInfo info = getPackageInfo(ctx, pkgName);
            if (info != null) {
                return (info.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void killPackage(Context context, String pkgName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        killPackage(am, pkgName);
    }

    public static void killPackage(ActivityManager am, String pkgName) {
        if (Build.VERSION.SDK_INT >= 8) {
            am.killBackgroundProcesses(pkgName);
        }
        am.restartPackage(pkgName);
    }

    public static void killService(Context context, ComponentName serviceComponentName) {
        try {
            context.stopService(new Intent().setComponent(serviceComponentName));
        } catch (Exception ex) {
            ex.printStackTrace();
            killPackage(context, serviceComponentName.getPackageName());
        }
    }

    public static String getMainActivity(Context cxt, String pkgName) {
        try {
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            resolveIntent.setPackage(pkgName);
            ResolveInfo result = cxt.getPackageManager().resolveActivity(resolveIntent, 0);
            if (result == null) {
                resolveIntent.removeCategory(Intent.CATEGORY_LAUNCHER);
                resolveIntent.addCategory(Intent.CATEGORY_HOME);
                result = cxt.getPackageManager().resolveActivity(resolveIntent, 0);
            }
            if (result != null) {
                return result.activityInfo.name;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static boolean startupApp(Context context, String pkgName) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                context.startActivity(intent);
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void openInstaller(Context cxt, String filepath) {
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE, Uri.fromFile(new File(filepath)));

        if (!isActivityAvailable(cxt, intent)) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(filepath)), "application/vnd.android.package-archive");
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        cxt.startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean openUninstaller(Context context, String pkgName, boolean newTask) {
        Uri pkgUri = Uri.parse("package:" + pkgName);
        Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, pkgUri);

        if (!isActivityAvailable(context, uninstallIntent)) {
            uninstallIntent.setAction(Intent.ACTION_DELETE);
        }
        if (newTask) {
            uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (isActivityAvailable(context, uninstallIntent)) {
            context.startActivity(uninstallIntent);
            return true;
        } else {
            return false;
        }
    }

    public static boolean hasMismatchSignature(Context ctx, String pkgName, PackageInfo pkgInfo) {
        try {
            PackageInfo localInfo = getPackageInfo(ctx, pkgName);
            if (localInfo != null) {
                Signature sign1 = pkgInfo.signatures[0];
                return !sign1.equals(localInfo.signatures[0]);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void installApp(Context cxt, String filepath) {
        openInstaller(cxt, filepath);
    }

    public static void uninstallApps(final Context cxt, final ArrayList<String> pkgsNameList) {
        for (String pkgName : pkgsNameList) {
            openUninstaller(cxt, pkgName, true);
        }
    }

    public static List<PackageInfo> queryInstalledNotSystemApps(Context context) {
        List<PackageInfo> packageInfo = new ArrayList<PackageInfo>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> pakageinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo pi : pakageinfos) {
            if (!isSystemApp(pi.applicationInfo.flags)) {
                packageInfo.add(pi);
            }
        }
        return packageInfo;
    }

    public static List<PackageInfo> queryInstalledSystemApps(Context context) {
        List<PackageInfo> packageInfo = new ArrayList<PackageInfo>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> pakageinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo pi : pakageinfos) {
            if (isSystemApp(pi.applicationInfo.flags)) {
                packageInfo.add(pi);
            }
        }
        return packageInfo;
    }

    public static String[] queryRunningTopActivity(ActivityManager activityManager) {
        String[] result = new String[2];
        if (Build.VERSION.SDK_INT > 20) {
            List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        && processInfo.importanceReasonCode == 0) {
                    Object object = ReflectUtils.getDeclaredField(processInfo, "processState");
                    if (object != null) {
                        int state = (Integer) object;
                        if (state == 2) {
                            if (processInfo.pkgList != null && processInfo.pkgList.length > 0) {
                                result[0] = processInfo.pkgList[0];
                                result[1] = "unknown";
                                return result;
                            }
                        }
                    }
                }
            }
        }

        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
        if (tasks != null && tasks.size() > 0) {
            ComponentName topActivity = tasks.get(0).topActivity;

            if (topActivity != null) {
                result[0] = topActivity.getPackageName();
                result[1] = topActivity.getClassName();
            }
        }
        if (result[0] == null) {
            result[0] = "unknown";
            result[1] = "unknown";
        }

        return result;
    }

    public static boolean isActivityAvailable(Context cxt, Intent intent) {
        List<ResolveInfo> list = cxt.getPackageManager().queryIntentActivities(intent, 0);
        return list != null && list.size() > 0;
    }

    public static List<ResolveInfo> queryIntentActivities(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        return packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
    }

    public static boolean checkPermission(Context context, String key) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.checkPermission(key, context.getPackageName()) == 0;
    }


}
