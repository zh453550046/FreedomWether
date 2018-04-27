package com.noah.mgtv.toolslib.logger;

import android.util.Log;

/**
 * 日志类
 */
public class Logger {

    public static final int VERBOSE = Log.VERBOSE;

    public static final int DEBUG = Log.DEBUG;

    public static final int INFO = Log.INFO;

    public static final int WARN = Log.WARN;

    public static final int ERROR = Log.ERROR;

    public static final int ASSERT = Log.ASSERT;

    private static final LoggerDelegate DEFAULT_DELEGATE = new DefaultLoggerDelegate();

    private static LoggerDelegate sLoggerDelegate = DEFAULT_DELEGATE;

    public static void setDefaultTag(String defaultTag) {
        sLoggerDelegate.setDefalutTag(defaultTag);
    }

    public static void setMiniLoggerLevel(int level) {
        sLoggerDelegate.setMiniLoggerLevel(level);
    }

    public static void closeDebug() {
        sLoggerDelegate.setMiniLoggerLevel(ASSERT);
    }

    public static boolean isDebug() {
        return !sLoggerDelegate.isClose();
    }

    public static void setLoggerDelegate(LoggerDelegate loggerDelegate) {
        sLoggerDelegate = loggerDelegate;
    }

    public static void v(String msg) {
        v(getTag(null), msg);
    }

    public static void d(String msg) {
        d(getTag(null), msg);
    }

    public static void e(String msg) {
        e(getTag(null), msg);
    }

    public static void i(String msg) {
        i(getTag(null), msg);
    }

    public static void w(String msg) {
        w(getTag(null), msg);
    }

    public static void v(Throwable throwable) {
        v(getTag(null), throwable);
    }

    public static void d(Throwable throwable) {
        d(getTag(null), throwable);
    }

    public static void e(Throwable throwable) {
        e(getTag(null), throwable);
    }

    public static void i(Throwable throwable) {
        i(getTag(null), throwable);
    }

    public static void w(Throwable throwable) {
        w(getTag(null), throwable);
    }

    public static void v(Object tag, Throwable throwable) {
        v(getTag(tag.getClass()), throwable);
    }

    public static void d(Object tag, Throwable throwable) {
        d(getTag(tag.getClass()), throwable);
    }

    public static void e(Object tag, Throwable throwable) {
        e(getTag(tag.getClass()), throwable);
    }

    public static void i(Object tag, Throwable throwable) {
        i(getTag(tag.getClass()), throwable);
    }

    public static void w(Object tag, Throwable throwable) {
        w(getTag(tag.getClass()), throwable);
    }

    public static void d(Object tag, String msg) {
        d(getTag(tag.getClass()), msg);
    }

    public static void v(Object tag, String msg) {
        v(getTag(tag.getClass()), msg);
    }

    public static void e(Object tag, String msg) {
        e(getTag(tag.getClass()), msg);
    }

    public static void i(Object tag, String msg) {
        i(getTag(tag.getClass()), msg);
    }

    public static void w(Object tag, String msg) {
        w(getTag(tag.getClass()), msg);
    }

    public static void v(String tag, String msg) {
        if (sLoggerDelegate.isLogger(VERBOSE)) {
            sLoggerDelegate.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (sLoggerDelegate.isLogger(DEBUG)) {
            sLoggerDelegate.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (sLoggerDelegate.isLogger(INFO)) {
            sLoggerDelegate.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (sLoggerDelegate.isLogger(WARN)) {
            sLoggerDelegate.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (sLoggerDelegate.isLogger(ERROR)) {
            sLoggerDelegate.e(tag, msg);
        }
    }

    public static void v(String tag, Throwable throwable) {
        if (sLoggerDelegate.isLogger(VERBOSE)) {
            sLoggerDelegate.v(tag, null, throwable);
        }
    }

    public static void d(String tag, Throwable throwable) {
        if (sLoggerDelegate.isLogger(DEBUG)) {
            sLoggerDelegate.d(tag, null, throwable);
        }
    }

    public static void i(String tag, Throwable throwable) {
        if (sLoggerDelegate.isLogger(INFO)) {
            sLoggerDelegate.i(tag, null, throwable);
        }
    }

    public static void e(String tag, Throwable throwable) {
        if (sLoggerDelegate.isLogger(ERROR)) {
            sLoggerDelegate.e(tag, null, throwable);
        }
    }

    public static void w(String tag, Throwable throwable) {
        if (sLoggerDelegate.isLogger(WARN)) {
            sLoggerDelegate.w(tag, null, throwable);
        }
    }

    public static void v(String tag, String msg, Throwable throwable) {
        if (sLoggerDelegate.isLogger(VERBOSE)) {
            sLoggerDelegate.v(tag, msg, throwable);
        }
    }

    public static void d(String tag, String msg, Throwable throwable) {
        if (sLoggerDelegate.isLogger(DEBUG)) {
            sLoggerDelegate.d(tag, msg, throwable);
        }
    }

    public static void i(String tag, String msg, Throwable throwable) {
        if (sLoggerDelegate.isLogger(INFO)) {
            sLoggerDelegate.i(tag, msg, throwable);
        }
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (sLoggerDelegate.isLogger(ERROR)) {
            sLoggerDelegate.e(tag, msg, throwable);
        }
    }

    public static void w(String tag, String msg, Throwable throwable) {
        if (sLoggerDelegate.isLogger(WARN)) {
            sLoggerDelegate.w(tag, msg, throwable);
        }
    }

    public static void d(Class<?> cls, Throwable throwable) {
        d(getTag(cls), throwable);
    }

    public static void i(Class<?> cls, Throwable throwable) {
        i(getTag(cls), throwable);
    }

    public static void e(Class<?> cls, Throwable throwable) {
        e(getTag(cls), throwable);
    }

    public static void w(Class<?> cls, Throwable throwable) {
        w(getTag(cls), throwable);
    }

    public static void v(Class<?> cls, String msg) {
        v(getTag(cls), msg);
    }

    public static void d(Class<?> cls, String msg) {
        d(getTag(cls), msg);
    }

    public static void i(Class<?> cls, String msg) {
        i(getTag(cls), msg);
    }

    public static void w(Class<?> cls, String msg) {
        w(getTag(cls), msg);
    }

    public static void e(Class<?> cls, String msg) {
        e(getTag(cls), msg);
    }

    public static void writeLogger(String fileName, String msg) {
        sLoggerDelegate.writeLogger(fileName, msg);
    }

    private static String getTag(Class<?> cls) {
        if (cls == null) {
            return null;
        } else {
            return cls.getSimpleName();
        }
    }
}
