package com.example.base.util;

import android.util.Log;

import com.example.base.BuildConfig;


/**
 * @author : sklyand
 * @email : zhengdengyao@51yryc.com
 * @time : 2019/7/18 15:41
 * @describe ：
 */
public class LogUtils {
    /**
     * 是否开启debug
     */
    public static boolean isDebug = BuildConfig.DEBUG;
    private static final String TAG = "LogUtils";
    /**
     * 错误
     */

    public static void e(String msg) {
        if (isDebug) {
            Log.e(TAG,msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag,msg);
        }
    }

    /**
     * 调试
     */
    public static void d(String msg) {
        if (isDebug) {
            Log.d(TAG,msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag,msg);
        }
    }

    /**
     * 信息
     */

    public static void i(String msg) {
        if (isDebug) {
            Log.i(TAG,msg);
        }

    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag,msg);
        }
    }
}
