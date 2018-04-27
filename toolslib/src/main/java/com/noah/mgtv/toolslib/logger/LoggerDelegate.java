package com.noah.mgtv.toolslib.logger;

import android.util.Log;


public abstract class LoggerDelegate {

    private int mMiniLeve = Log.VERBOSE;

    protected String mDefaultTag;

    void setDefalutTag(String defalutTag) {
        mDefaultTag = defalutTag;
    }

    void setMiniLoggerLevel(int level) {
        mMiniLeve = level;
    }

    boolean isLogger(int level) {
        return mMiniLeve <= level;
    }

    boolean isClose() {
        return mMiniLeve >= Log.ASSERT;
    }

    void d(String tag, String msg) {
        println(Log.DEBUG, tag, msg);
    }

    void v(String tag, String msg) {
        println(Log.VERBOSE, tag, msg);
    }

    void e(String tag, String msg) {
        println(Log.ERROR, tag, msg);
    }

    void i(String tag, String msg) {
        println(Log.INFO, tag, msg);
    }

    void w(String tag, String msg) {
        println(Log.WARN, tag, msg);
    }

    void d(String tag, String msg, Throwable throwable) {
        println(Log.DEBUG, tag, msg, throwable);
    }

    void v(String tag, String msg, Throwable throwable) {
        println(Log.VERBOSE, tag, msg, throwable);
    }

    void e(String tag, String msg, Throwable throwable) {
        println(Log.ERROR, tag, msg, throwable);
    }

    void i(String tag, String msg, Throwable throwable) {
        println(Log.INFO, tag, msg, throwable);
    }

    void w(String tag, String msg, Throwable throwable) {
        println(Log.WARN, tag, msg, throwable);
    }


    public abstract void writeLogger(String fileName, String msg);

    public abstract void println(int priority, String tag, String msg);

    public abstract void println(int priority, String tag, String msg, Throwable tr);
}
