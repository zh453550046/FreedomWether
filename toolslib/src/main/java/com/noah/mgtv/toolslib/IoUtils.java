package com.noah.mgtv.toolslib;

import android.content.Context;
import android.database.Cursor;

import com.noah.mgtv.toolslib.logger.Logger;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipFile;

public class IoUtils {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    private static final int EOF = -1;

    public static void writeStringToFile(Context context, String filename, String data) {
        FileOutputStream fos = null;
        FileChannel fc = null;
        ByteBuffer bf = null;

        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fc = fos.getChannel();
            bf = getByteBuffer(data);
            bf.flip();
            fc.write(bf);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            close(fc);
            close(fos);
        }
    }

    public static void appendStringToFile(Context context, String filename, String data) {
        FileOutputStream fos = null;
        FileChannel fc = null;
        ByteBuffer bf = null;

        try {
            fos = context.openFileOutput(filename, Context.MODE_APPEND);
            fc = fos.getChannel();
            bf = getByteBuffer(data);
            bf.flip();
            fc.write(bf);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            close(fc);
            close(fos);
        }
    }

    public static String readStringFromStream(InputStream inputStream, String charset) {
        String str = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (inputStream == null) {
            return null;
        }
        try {
            byte[] buff = new byte[1024];
            int readed = -1;
            while ((readed = inputStream.read(buff)) != -1) {
                baos.write(buff, 0, readed);
            }
            byte[] result = baos.toByteArray();
            if (result == null) {
                return null;
            }
            str = new String(result, charset);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            close(baos);
        }
        return str;
    }

    public static String readStringFromStream(InputStream inputStream) {
        return readStringFromStream(inputStream, "UTF-8");
    }

    public static String readStringFromStream(InputStream inputStream, long limit) {
        return readStringFromStream(inputStream, "UTF-8", limit);

    }

    public static String readStringFromStream(InputStream inputStream, String charset, long limit) {
        String str = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (inputStream == null) {
            return null;
        }
        try {
            byte[] buff = new byte[1024];
            int readed = -1;
            while ((readed = inputStream.read(buff)) != -1) {
                baos.write(buff, 0, readed);
                if (baos.size() > limit) {
                    break;
                }
            }
            byte[] result = baos.toByteArray();
            if (result == null) {
                return null;
            }
            str = new String(result, charset);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return str;
    }

    public static String readStringFromAsset(Context context, String fileName, String charset) {
        String str = null;
        InputStream is = null;
        GZIPInputStream gzipIs = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if (android.text.TextUtils.isEmpty(fileName) || context == null) {
            return null;
        }

        try {
            byte[] buff = new byte[1024];
            int readed = -1;

            is = context.getAssets().open(fileName);
            boolean isGzipped = isGzipped(is);
            if (isGzipped) {
                gzipIs = new GZIPInputStream(is);

                while ((readed = gzipIs.read(buff)) != -1) {
                    baos.write(buff, 0, readed);
                }
            } else {
                while ((readed = is.read(buff)) != -1) {
                    baos.write(buff, 0, readed);
                }
            }

            byte[] result = baos.toByteArray();
            if (result == null) {
                return null;
            }
            str = new String(result, "utf-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            close(is);
            close(baos);
            close(gzipIs);
        }
        return str;
    }

    public static String readStringFromAsset(Context context, String fileName) {
        return readStringFromAsset(context, fileName, "UTF-8");
    }

    public static boolean isGzipped(InputStream in) {
        if (!in.markSupported()) {
            in = new BufferedInputStream(in);
        }
        in.mark(2);
        int magic = 0;
        try {
            magic = in.read() & 0xff | ((in.read() << 8) & 0xff00);
            in.reset();
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return magic == GZIPInputStream.GZIP_MAGIC;
    }

    public static boolean writeStringToFile(String path, String text, boolean append) {
        FileOutputStream fos = null;
        FileChannel fc = null;
        ByteBuffer bf = null;

        try {
            File file = new File(path);
            fos = new FileOutputStream(file, append);
            fc = fos.getChannel();
            bf = getByteBuffer(text);
            bf.flip();
            fc.write(bf);
            return true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            close(fc);
            close(fos);
        }
    }

    public static String readStringFromFile(Context context, String filename) {
        if (!FileUtils.isFileExist(context, filename)) {
            return "";
        }
        FileInputStream fis = null;
        FileChannel fc = null;
        ByteBuffer bf = ByteBuffer.allocate(1024);
        StringBuilder sb = new StringBuilder();

        try {
            fis = context.openFileInput(filename);
            fc = fis.getChannel();
            while (fc.read(bf) != -1) {
                bf.flip();
                sb.append(CharacterUtils.getString(bf));
                bf.clear();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            close(fc);
            close(fis);
        }
        return sb.toString();
    }

    public static boolean writeStringToFile(String path, String text) {
        return writeStringToFile(path, text, false);
    }

    public static String readStringFromFile(String path) {
        if (android.text.TextUtils.isEmpty(path)) {
            return "";
        }
        File file = new File(path);
        if (!file.exists()) {
            return "";
        }
        FileInputStream fis = null;
        FileChannel fc = null;
        ByteBuffer bf = ByteBuffer.allocate(1024);
        StringBuilder sb = new StringBuilder();

        try {
            fis = new FileInputStream(file);
            fc = fis.getChannel();
            while (fc.read(bf) != -1) {
                bf.flip();
                sb.append(CharacterUtils.getString(bf));
                bf.clear();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            close(fc);
            close(fis);
        }
        return sb.toString();
    }

    public static void decompressAssetsFile(Context context, String assetsFile, String desFile) {
        InputStream is = null;
        FileOutputStream fos = null;
        GZIPInputStream gzpis = null;
        try {
            is = context.getAssets().open(assetsFile);
            fos = context.openFileOutput(desFile, Context.MODE_PRIVATE);
            gzpis = new GZIPInputStream(is);
            byte[] buffer = new byte[4096];
            int readbytes;
            while ((readbytes = gzpis.read(buffer)) > 0) {
                fos.write(buffer, 0, readbytes);
            }
            fos.flush();
            buffer = null;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close(gzpis);
            close(fos);
            close(is);
        }
    }

    private static ByteBuffer getByteBuffer(String str) {
        return CharacterUtils.getByteBuffer(str);
    }

    public static long copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        return count;
    }

    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        return copyLarge(input, output, new byte[DEFAULT_BUFFER_SIZE]);
    }

    public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long count = 0;
        int position = 0;
        while (EOF != (position = input.read(buffer))) {
            output.write(buffer, 0, position);
            count += position;
        }
        return count;
    }

    public static void copyUriToFile(URL source, File destination, int connectionTimeout, int readTimeout,
                                     long limitContentLength) throws IOException {
        URLConnection connection = source.openConnection();
        connection.setConnectTimeout(connectionTimeout);
        connection.setReadTimeout(readTimeout);
        InputStream input = connection.getInputStream();
        int contentLength = connection.getContentLength();
        if (limitContentLength > 0 && contentLength > limitContentLength) {
            throw new IOException("content length too big, limited:" + limitContentLength + " actual:" + contentLength);
        }
        copyInputStreamToFile(input, destination);
    }

    public static boolean copyInputStreamToFile(InputStream source, File destination) throws IOException {
        FileOutputStream output = null;
        try {
            output = openOutputStream(destination, false);
            copy(source, output);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            close(source);
            close(output);
        }
        return false;
    }

    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                Logger.d("File '" + file + "' exists but is a directory");
            }
            if (!file.canWrite()) {
                Logger.d("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    Logger.d("Directory '" + parent + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file, append);
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void close(Cursor target) {
        try {
            if (target != null) {
                target.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void close(ZipFile zipFile) {
        try {
            if (zipFile != null) {
                zipFile.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取 Stream MD5.
     */
    public static String getStreamMD5(InputStream stream) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return "";
        }

        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while ((len = stream.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            close(stream);
        }
        BigInteger integer = new BigInteger(1, digest.digest());
        return integer.toString(16);
    }

    public static byte[] toByteArray(File file) throws IOException {
        if (file != null && file.exists()) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
            BufferedInputStream in = null;
            try {
                in = new BufferedInputStream(new FileInputStream(file));
                int bufSize = 1024;
                byte[] buffer = new byte[bufSize];
                int len = 0;
                while (-1 != (len = in.read(buffer, 0, bufSize))) {
                    bos.write(buffer, 0, len);
                }
                return bos.toByteArray();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                close(in);
                close(bos);
            }
        }
        return null;
    }
}
