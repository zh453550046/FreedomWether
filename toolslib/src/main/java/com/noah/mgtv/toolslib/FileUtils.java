package com.noah.mgtv.toolslib;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    public static boolean deleteFile(String path) {
        try {
            if (!TextUtils.isEmpty(path)) {
                File file = new File(path);
                return file.delete();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public static void deleteFileOrDirectory(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.deleteOnExit();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File file1 : files) {
                        deleteFileOrDirectory(file1);
                    }
                }
                file.deleteOnExit();
            }
        }
    }

    public static boolean isFileExist(String path) {
        try {
            if (!TextUtils.isEmpty(path)) {
                File file = new File(path);
                return file.exists();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public static long getFileSize(Context context, String filename) {
        long size = 0;
        FileInputStream ios = null;
        try {
            ios = context.openFileInput(filename);
            size = ios.available();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            IoUtils.close(ios);
        }
        return size;
    }

    public static long getFileSize(File file) {
        long size = 0;
        FileInputStream ios = null;
        try {
            ios = new FileInputStream(file);
            size = ios.available();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            IoUtils.close(ios);
        }
        return size;
    }

    public static boolean isFileExist(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        return isFileExist(path);
    }

    public static long getFolderSize(File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        long size = 0;
        File[] fileList = file.listFiles();
        if (fileList == null) {
            return 0;
        }
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i] == null) {
                continue;
            }
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            } else {
                size = size + fileList[i].length();
            }
        }
        return size;
    }

    public static File createUniqueFile(File directory, String filename) throws IOException {
        File file = new File(directory, filename);
        if (file.createNewFile()) {
            return file;
        }
        int index = filename.lastIndexOf('.');
        String format;
        if (index != -1) {
            String name = filename.substring(0, index);
            String extension = filename.substring(index);
            format = name + "-%d" + extension;
        } else {
            format = filename + "-%d";
        }
        for (int i = 2; i < Integer.MAX_VALUE; i++) {
            file = new File(directory, String.format(format, i));
            if (file.createNewFile()) {
                return file;
            }
        }
        return null;
    }

    public static File getExternalStorageDownloadsDirectory(String fileName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        }
        return null;
    }

    public static String getFileNameFromUrl(String url, String fallbackName) {
        if (!NetworkUtils.isUrl(url)) {
            return "";
        }
        if (!NetworkUtils.isUrl(url)) {
            return "";
        }
        int slashIndex = url.lastIndexOf('/');
        String filename = url.substring(slashIndex + 1);
        if (filename.length() == 0) {
            filename = fallbackName;
        }
        return filename;
    }

    public static void zip(String src, String dest) throws IOException {
        ZipOutputStream out = null;
        try {
            File outFile = new File(dest);
            File fileOrDirectory = new File(src);
            out = new ZipOutputStream(new FileOutputStream(outFile));
            if (fileOrDirectory.isFile()) {
                zipFileOrDirectory(out, fileOrDirectory, "");
            } else {
                File[] entries = fileOrDirectory.listFiles();
                if (entries != null) {
                    for (File entry : entries) {
                        zipFileOrDirectory(out, entry, "");
                    }
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            IoUtils.close(out);
        }
    }

    private static void zipFileOrDirectory(ZipOutputStream out, File fileOrDirectory, String curPath)
            throws IOException {
        FileInputStream in = null;
        try {
            if (!fileOrDirectory.isDirectory()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                in = new FileInputStream(fileOrDirectory);
                ZipEntry entry = new ZipEntry(curPath + fileOrDirectory.getName());
                out.putNextEntry(entry);
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.closeEntry();
            } else {
                File[] entries = fileOrDirectory.listFiles();
                if (entries != null) {
                    for (File entry : entries) {
                        zipFileOrDirectory(out, entry, curPath + fileOrDirectory.getName() + "/");
                    }
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();

        } finally {
            IoUtils.close(in);
        }
    }

    public static void unzip(String zipFileName, String outputDirectory) throws IOException {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(zipFileName);
            Enumeration enumeration = zipFile.entries();
            ZipEntry zipEntry = null;
            File dest = new File(outputDirectory);
            dest.mkdirs();
            while (enumeration.hasMoreElements()) {
                zipEntry = (ZipEntry) enumeration.nextElement();
                String entryName = zipEntry.getName();
                InputStream in = null;
                FileOutputStream out = null;
                try {
                    if (zipEntry.isDirectory()) {
                        String name = zipEntry.getName();
                        name = name.substring(0, name.length() - 1);
                        File file = new File(outputDirectory + File.separator + name);
                        file.mkdirs();
                    } else {
                        int index = entryName.lastIndexOf("\\");
                        if (index != -1) {
                            File df = new File(outputDirectory + File.separator + entryName.substring(0, index));
                            df.mkdirs();
                        }
                        index = entryName.lastIndexOf("/");
                        if (index != -1) {
                            File df = new File(outputDirectory + File.separator + entryName.substring(0, index));
                            df.mkdirs();
                        }
                        File file = new File(outputDirectory + File.separator + zipEntry.getName());

                        in = zipFile.getInputStream(zipEntry);
                        out = new FileOutputStream(file);
                        int count;
                        byte[] by = new byte[1024];
                        while ((count = in.read(by)) != -1) {
                            out.write(by, 0, count);
                        }
                        out.flush();
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                } finally {
                    IoUtils.close(in);
                    IoUtils.close(out);
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            IoUtils.close(zipFile);
        }

    }

    public static String getTempPath(Context ctx) {
        String tempPath = null;
        if (StorageUtils.checkSdCard()) {
            String packageName = ctx.getPackageName();
            tempPath = StorageUtils.getSdPath() + "tmp/" + packageName + "/";

        } else {
            tempPath = getAppFilePath(ctx) + "tmp/";
        }
        createPaths(tempPath);
        return tempPath;
    }

    public static String getAppFilePath(Context ctx) {
        String appFilePath = ctx.getFilesDir().getPath() + "/";
        createPaths(appFilePath);
        return appFilePath;
    }

    public static boolean createPaths(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        if (isFileExist(filePath)) {
            return true;
        }
        File file = new File(filePath);
        return file.mkdirs();
    }

    public static boolean createFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return createFile(file);
    }

    public static boolean createFile(File file) {
        if (!file.exists()) {
            try {
                return file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static boolean copyFile(String srcPath, String destPath) {
        File strPath = new File(srcPath);
        if (!strPath.exists()) {
            return false;
        }
        try {
            return IoUtils.copyInputStreamToFile(new FileInputStream(new File(srcPath)), new File(destPath));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 获取大小文本描述.
     */
    public static String getSizeTextDescription(long sizeBytes) {
        String desc = null;
        final long oneKb = 1024;
        final long oneMb = 1024 * oneKb;
        final long oneGb = 1024 * oneMb;
        if (sizeBytes >= oneGb) {
            desc = String.format(Locale.CHINESE,"%.1f GB", (float) sizeBytes / (float) oneGb);
        } else if (sizeBytes >= oneMb) {
            desc = String.format(Locale.CHINESE,"%.1f MB", (float) sizeBytes / (float) oneMb);
        } else if (sizeBytes >= oneKb) {
            desc = String.format(Locale.CHINESE,"%.1f KB", (float) sizeBytes / (float) oneKb);
        } else {
            desc = String.format(Locale.CHINESE,"%.1f B", (float) sizeBytes);
        }
        return desc;
    }

    /**
     * 获取文件 MD5.
     */
    public static String getFileMD5(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return "";
        }
        try {
            FileInputStream stream = new FileInputStream(file);
            return IoUtils.getStreamMD5(stream);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * date 创建时间 16/8/29
     * @author liushu 先判断是否存在外部存储cache,然后在检测内部
     */
    public static File getAppCacheFile(Context ctx) {
        File externalDir = ctx.getExternalCacheDir();
        if (externalDir != null) {
            return externalDir;
        } else {
            return ctx.getCacheDir();
        }

    }

}
