package com.chenlibrary.utils;

import android.util.Log;

/**
 * 版权：chenxiaozong 版权所有
 * -------------------------
 * 作者：chenxiaozong
 * 邮箱：chenxzong@qq.com
 * 代码：https://gitee.com/chenxiaozong
 * 版本：1.0
 * 日期：2020-02-14 21:51
 * 描述：com.chen.utilsdemo.utils/ChenLog10.java
 */
public class ChenLog {
    private static final String TAG = "ChenLog";
    public static String tagPrefix = TAG; //日志前缀

    public static String APPVERSION = "Version:";
    public static String VersionCode = "1.0";
    public static String LOGSPLIT = "|";
    public static String LOG_Pre = APPVERSION + VersionCode;

    public static boolean showV = true;
    public static boolean showD = true;
    public static boolean showI = true;
    public static boolean showW = true;
    public static boolean showE = true;
    public static boolean showWTF = true;
    private static int MAX_LOG_LENGTH = 1024 * 3;

    /**
     * 得到tag（所在类.方法（L:行））
     *
     * @return
     */
    private static String generateTag() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        String callerClazzName = stackTraceElement.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        String tag = "%s.%s(L:%d)";
        tag = String.format(tag, new Object[]{callerClazzName, stackTraceElement.getMethodName(), Integer.valueOf(stackTraceElement.getLineNumber())});
        //给tag设置前缀
//        tag = TextUtils.isEmpty(tagPrefix) ? tag : tagPrefix + ":" + tag;
        tag = new StringBuilder(LOG_Pre).append(LOGSPLIT).append(tagPrefix).append(LOGSPLIT).append(tag).toString();

        return tag;
    }

    public static void v(String msg) {
        if (showV) {
            String tag = generateTag();
            Log.v(tag, msg);
        }
    }

    public static void v(String msg, Throwable tr) {
        if (showV) {
            String tag = generateTag();
            Log.v(tag, msg, tr);
        }
    }

    public static void d(String msg) {
        if (showD) {
            String tag = generateTag();
            Log.d(tag, msg);
        }
    }

    public static void d(String msg, Throwable tr) {
        if (showD) {
            String tag = generateTag();
            Log.d(tag, msg, tr);
        }
    }

    public static void i(String msg) {
        if (!showI) {
            return;
        }
        String temTag = new StringBuilder().append(generateTag()).toString();
        String text = msg;
        for (int len = text.length(); len >= MAX_LOG_LENGTH; len = text.length()) {
            Log.i(temTag, text.substring(0, MAX_LOG_LENGTH));
            text = text.substring(MAX_LOG_LENGTH);
        }

        if (text.length() > 0) {
            Log.i(temTag, text);
        }
    }

    public static void i(String TAG, String msg) {
        if (!showI) {
            return;
        }
        String temTag = new StringBuilder().append(generateTag()).append(">").append(TAG).toString();
        String text = msg;
        for (int len = text.length(); len >= MAX_LOG_LENGTH; len = text.length()) {
            Log.i(temTag, text.substring(0, MAX_LOG_LENGTH));
            text = text.substring(MAX_LOG_LENGTH);
        }

        if (text.length() > 0) {
            Log.i(temTag, text);
        }
    }


    public static void i(String msg, Throwable tr) {
        if (showI) {
            String tag = generateTag();
            Log.i(tag, msg, tr);
        }
    }

    public static void w(String msg) {
        if (showW) {
            String tag = generateTag();
            Log.w(tag, msg);
        }
    }

    public static void w(String msg, Throwable tr) {
        if (showW) {
            String tag = generateTag();
            Log.w(tag, msg, tr);
        }
    }

    public static void e(String msg) {
        if (showE) {
            String tag = generateTag();
            Log.e(tag, msg);
        }
    }

    public static void e(String msg, Throwable tr) {
        if (showE) {
            String tag = generateTag();
            Log.e(tag, msg, tr);
        }
    }


    public static void e(Object... msg) {
        if (!showE) {
            return;
        }
        String temTag = new StringBuilder().append(generateTag()).toString();
        String text = buildMessage(msg);
        for (int len = text.length(); len >= MAX_LOG_LENGTH; len = text.length()) {
            Log.e(temTag, text.substring(0, MAX_LOG_LENGTH));
            text = text.substring(MAX_LOG_LENGTH);
        }
        if (text.length() > 0) {
            Log.e(temTag, text);
        }
    }

    public static void wtf(String msg) {
        if (showWTF) {
            String tag = generateTag();
            Log.wtf(tag, msg);
        }
    }

    public static void wtf(String msg, Throwable tr) {
        if (showWTF) {
            String tag = generateTag();
            Log.wtf(tag, msg, tr);
        }
    }


    public static void i(Object... msg) {
        if (!showI) {
            return;
        }
        String temTag = new StringBuilder().append(generateTag()).toString();
        String text = buildMessage(msg);
        for (int len = text.length(); len >= MAX_LOG_LENGTH; len = text.length()) {
            Log.i(temTag, text.substring(0, MAX_LOG_LENGTH));
            text = text.substring(MAX_LOG_LENGTH);
        }

        if (text.length() > 0) {
            Log.i(temTag, text);
        }
    }


    private static String buildMessage(Object[] msg) {
        try {

            if (msg == null || msg.length == 0) {
                return null;
            }

            StringBuilder sb = new StringBuilder();
            for (Object m : msg) {
                sb.append(m);
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error--> " + e.getMessage());
            return null;
        }
    }

}
