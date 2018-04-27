package com.noah.mgtv.toolslib;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class StorageUtils {
    private static final String TAG = "SdcardUtil";
    private static final String MOUNTS_PATH = "/proc/mounts";
    private static final String VOLD_PATH = "/system/etc/vold.fstab";

    private static final int TYPE_ALL = 3;
    private static final int TYPE_INTERN = 1;
    private static final int TYPE_EXTERN = 2;

    /**
     * 检查内置的sd卡是否可用
     *
     */
    public static boolean checkSdCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }



    /**
     * 通过读取系统文件获取
     * 
     */
    private static List<String> findByReadFile(int type) {
        String internPath = Environment.getExternalStorageDirectory().getPath();
        if (type == TYPE_INTERN) {
            List<String> paths = new ArrayList<String>();
            paths.add(internPath);
            filterValidPaths(paths);
            return paths;
        }

        String filterPath = type == TYPE_EXTERN ? internPath : null;

        List<String> paths = new ArrayList<String>();
        List<String> voldPaths = readVoldFile(filterPath);
        List<String> mountPaths = readMountsFile(filterPath);

        if (voldPaths == null || voldPaths.size() < 1 || mountPaths == null || mountPaths.size() < 1) {
            return null;
        }

        for (String path : voldPaths) {
            if (mountPaths.contains(path)) {
                paths.add(path);
            }
        }

        filterValidPaths(paths);
        return paths;
    }

    /**
     * 获取内置sd卡的路径,如果没有挂载则返回空
     *
     */
    public static String getSdPath() {
        return getSdPath(true);
    }

    /**
     * 获取内置sd卡的路径,如果没有挂载则返回空
     *
     */
    public static String getSdPath(boolean needMounted) {
        if (needMounted) {
            if (checkSdCard()) {
                return Environment.getExternalStorageDirectory().toString();
            } else {
                return null;
            }
        } else {
            return Environment.getExternalStorageDirectory().getPath();
        }
    }



    public static void filterSamePath(List<String> list, String path) {
        if (list == null || path == null) {
            return;
        }
        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String str = it.next();
            String subStr = str.endsWith("/") ? str.substring(0, str.length() - 1) : str;
            if (subStr.equals(path)) {
                it.remove();
            }
        }
    }

    /**
     * 读取系统文件vold.fstab获取挂载的SD卡
     */
    private static List<String> readVoldFile(String filterPath) {
        File file = new File(VOLD_PATH);
        if (!file.exists()) {
            return null;
        }
        Scanner scanner = null;
        List<String> paths = null;
        try {
            scanner = new Scanner(file);
            paths = new ArrayList<String>();
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.startsWith("dev_mount")) {
                    String[] lineElements = line.split(" ");
                    String element = lineElements[2];
                    if (element.contains(":")) {
                        element = element.substring(0, element.indexOf(":"));
                    }

                    if (element.contains("usb")) {
                        continue;
                    }

                    // don't add the default vold path
                    // it's already in the list.
                    if (!element.equals(filterPath) && !paths.contains(element)) {
                        paths.add(element);
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return paths;
    }

    /**
     * 读取系统文件mounts获取挂载的SD卡
     *
     */
    private static List<String> readMountsFile(String filterPath) {
        File file = new File(MOUNTS_PATH);
        if (!file.exists()) {
            return null;
        }

        Scanner scanner = null;
        List<String> paths = null;
        try {
            scanner = new Scanner(file);
            paths = new ArrayList<String>();
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.startsWith("/dev/block/vold/")) {
                    String[] lineElements = line.split(" ");
                    String element = lineElements[1];
                    // don't add the default mount path
                    // it's already in the list.
                    if (!element.equals(filterPath) && !paths.contains(element)) {
                        paths.add(element);
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
        return paths;
    }


    /**
     * 将不存在的sd卡过滤掉；
     *
     */
    private static void filterValidPaths(List<String> paths) {
        filterValidPaths(paths, null);
    }

    /**
     * 将不存在的SD卡和需要过滤的路径过滤掉
     */
    private static void filterValidPaths(List<String> paths, String filterPath) {
        if (paths == null || paths.size() < 1) {
            return;
        }

        Iterator<String> it = paths.iterator();
        while (it.hasNext()) {
            String path = it.next();
            if (path != null && path.equals(filterPath)) {
                File file = new File(path);
                if (!file.exists() || !file.isDirectory() || !file.canWrite()) {
                    it.remove();
                }
            }
        }
    }

    /**
     * 获取可用空间
     *
     */
    public static long getAvailaleSize(String pathStr) {
        File path = new File(pathStr); // 取得sdcard文件路径
        if (!path.exists()) {
            return -1;
        }
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     *
     * 获取sd卡的大小
     *
     * @param pathStr sd卡的路径
     * @return sd卡的大小，单位byte
     */
    public static long getSdCardSize(String pathStr) {
        File path = new File(pathStr);
        if (!path.exists()) {
            return -1;
        }
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }


}