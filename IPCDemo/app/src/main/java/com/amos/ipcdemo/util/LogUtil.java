package com.amos.ipcdemo.util;

import android.util.Log;

/**
 * author: amos
 * date: 18/5/1 19:36
 */

public class LogUtil {
    private static final String TAG_MESSENGER = "LOG_MESSENGER";

    public static void logMessenger(String msg) {
        Log.d(TAG_MESSENGER, msg);
    }
}
