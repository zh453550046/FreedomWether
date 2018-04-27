package com.noah.mgtv.toolslib.logger;

import android.text.TextUtils;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

public class DefaultLoggerDelegate extends LoggerDelegate {

    private static final String UNKNOWN = "unknown";

    @Override
    public void println(int priority, String tag, String msg) {
        if (msg == null) {
            msg = "";
        }
        if (mDefaultTag == null && TextUtils.isEmpty(tag)) {
            tag = UNKNOWN;
        } else if (TextUtils.isEmpty(tag)) {
            tag = mDefaultTag;
        }
        Log.println(priority, tag, msg);
    }

    @Override
    public void println(int priority, String tag, String msg, Throwable tr) {
        println(priority, tag, msg + "" + getStackTraceString(tr));
    }

    @Override
    public void writeLogger(String fileName, String msg) {

    }

    private String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }
}
