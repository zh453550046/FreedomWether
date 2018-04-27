package com.noah.mgtv.toolslib;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CharacterUtils {

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static String getBytesMd5(byte[] bytes) {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            return bytesToHexString(md.digest());
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    public static String getBytesSha1(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(bytes);
            byte[] digest = md.digest();
            return bytesToHexString(digest);
        } catch (NoSuchAlgorithmException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static ByteBuffer getByteBuffer(String str) {
        byte[] bb = str.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bb.length);
        byteBuffer.put(bb);
        return byteBuffer;
    }

    public static String getString(ByteBuffer buffer, String charset) {
        CharsetDecoder decoder = null;
        CharBuffer charBuffer = null;
        Charset cs = null;
        try {
            cs = Charset.forName(charset);
            decoder = cs.newDecoder();
            charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
            return charBuffer.toString();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (charBuffer != null) {
                charBuffer.clear();
            }
        }
        return "";
    }

    public static String getString(ByteBuffer buffer) {
        return getString(buffer, "UTF-8");
    }

    /**
     * 将map数据解析出来，并拼接成json字符串
     */
    public static String mapConverJson(Map<String, Object> map) {
        try {
            StringBuffer strBuffer = new StringBuffer();
            if (!map.isEmpty()) {
                strBuffer.append("{");
                Set<Map.Entry<String, Object>> entrySet = map.entrySet();
                for (Map.Entry<String, Object> entry : entrySet) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    strBuffer.append("\"").append(key).append("\":");
                    if (value instanceof Map<?, ?>) {
                        strBuffer.append(mapConverJson((Map<String, Object>) value)).append(",");
                    } else if (value instanceof List<?>) {
                        strBuffer.append(listConverJson((List<Map<String, Object>>) value)).append(",");
                    } else {
                        if (value != null && value instanceof Number) {
                            strBuffer.append("").append(value).append(",");
                        } else if (value == null || TextUtils.isEmpty(value.toString())) {
                            strBuffer.append("\"").append("\"").append(",");
                        } else {
                            strBuffer.append("\"").append(value).append("\"").append(",");
                        }
                    }
                }
                if (strBuffer.length() > 1) {
                    strBuffer = new StringBuffer(strBuffer.substring(0, strBuffer.length() - 1));
                }
                strBuffer.append("}");
            }
            return strBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 将单个list转成json字符串
     */
    public static String listConverJson(List<Map<String, Object>> list) {
        try {
            StringBuffer strBuffer = new StringBuffer();
            strBuffer.append("[");
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                if (i == list.size() - 1) {
                    strBuffer.append(mapConverJson(map));
                } else {
                    strBuffer.append(mapConverJson(map)).append(",");
                }
            }
            if (strBuffer.length() > 1) {
                strBuffer = new StringBuffer(strBuffer.substring(0, strBuffer.length()));
            }
            strBuffer.append("]");
            return strBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * 将十六进制 颜色代码 转换为 int
     *
     */
    public static int HextoColor(String color) {
        int c = 0;
        try {
            c = Color.parseColor(color);
        } catch (Exception e) {
            return Color.parseColor("#ffee317b");
        }
        return c;
    }
}