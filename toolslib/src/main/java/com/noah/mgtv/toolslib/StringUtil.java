/**
 *
 */
package com.noah.mgtv.toolslib;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import com.noah.mgtv.toolslib.logger.Logger;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

/**
 * String 工具类
 *
 * @author QiuDa
 */
public class StringUtil {
    private static final String TAG = "StringUtil";

    /**
     * 删除BOM字符
     *
     * @param data
     * @return
     */
    public static String removeBOM(String data) {
        if (isNullorEmpty(data)) {
            return data;
        }

        if (data.startsWith("\ufeff")) {
            return data.substring(1);
        } else {
            return data;
        }
    }

    public static boolean isNullorEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {

        // 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
        // 联通：130、131、132、152、155、156、185、186 \n 电信：133、153、180、189、（1349卫通）
        // 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
        String telRegex = "[1][34578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }
    }

    /**
     * [\u4e00-\u9fa5] //匹配中文字符^[1-9]\d*$ //匹配正整数^[A-Za-z]+$ //匹配由26个英文字母组成的字符串^[A-Z]+$ //匹配由26个英文字母的大写组成的字符串^[a-z]+$
     * //匹配由26个英文字母的小写组成的字符串^[A-Za-z0-9]+$ //匹配由数字和26个英文字母组成的字符串
     *
     * @return false 不符合 true 符合
     * 描述 验证输入密码是否符合规范
     */
    public static boolean isPwdlegally(String mPassWord) {
        String pwdRegex = "[A-Za-z0-9]*";
        if (mPassWord.matches(pwdRegex)) {
            return true;
        }
        return false;

    }

    /**
     * 判断目标时间是否大于当前时间
     *
     * @param dateStr 目标时间
     * @return boolean
     */
    public static boolean greaterThanCurDate(String dateStr) {
        long time = System.currentTimeMillis();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
        try {
            Date date = formater.parse(dateStr);
            if (time < date.getTime()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
        return false;
    }

    /**
     * 将手机号码中间4位改为*号
     *
     * @param mobile 手机号码
     * @return String
     */
    public static String encryptMobile(String mobile) {
        if (!StringUtil.isNullorEmpty(mobile) && mobile.length() > 4) {
            int length = mobile.length();
            String encryMobileEnd = mobile.substring(length - 4, length);
            String encryMobileStart = "";
            if (length - 8 > 0) {
                encryMobileStart = mobile.substring(0, length - 8);
            }
            return encryMobileStart + "****" + encryMobileEnd;
        }
        return mobile;
    }


    public static String getFormatNum(long fansCount) {
        String result;
        if (fansCount > 999999) {
            result = String.format(Locale.CHINESE,"%.1f", fansCount / 10000.0f) + "万";
        } else {
            result = fansCount + "";
        }
        return result;
    }

    public static String getFormatNum(String fansCountstr) {
        String result;
        if (!isNullorEmpty(fansCountstr)) {
            try {
                long fanscount = Long.parseLong(fansCountstr);
                result = getFormatNum(fanscount);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                result = "0";
            }
        } else {
            result = "0";
        }

        return result;
    }

    /**
     * url拼接
     *
     * @param url url
     * @param param 参数
     * @return 拼接后的url
     */
    public static String urlJoint(String url, Map<String, Object> param) {
        String resultUlr = url;
        if (param != null) {
            StringBuilder paramStr = new StringBuilder("?");
            for (String key : param.keySet()) {
                if (!paramStr.toString().equals("?")) {
                    paramStr.append("&");
                }
                paramStr.append(key).append("=").append(param.get(key));
            }
            if (!paramStr.toString().equals("?")) {
                resultUlr += paramStr;
            }
        }
        return resultUlr;
    }

    /**
     * 将十六进制 颜色代码 转换为 int
     *
     * @return int
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


    public static String getUrlHost(String url) {
        if (url == null || url.trim().equals("")) {
            return "";
        }
        String host = "";
        Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            host = matcher.group();
        }
        return host;
    }

    public static byte[] decompressZlib(byte[] data) {
        byte[] output = new byte[0];
        Inflater decompresser = new Inflater();
        decompresser.reset();
        decompresser.setInput(data);
        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!decompresser.finished()) {
                int i = decompresser.inflate(buf);
                o.write(buf, 0, i);
            }
            output = o.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        } finally {
            IoUtils.close(o);
        }
        decompresser.end();
        return output;
    }
}
