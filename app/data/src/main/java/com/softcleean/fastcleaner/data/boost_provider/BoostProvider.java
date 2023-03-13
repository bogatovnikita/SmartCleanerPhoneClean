package com.softcleean.fastcleaner.data.boost_provider;


import android.app.ActivityManager;
import android.content.Context;

import androidx.annotation.Keep;

@Keep
public class BoostProvider {

    static{
        System.loadLibrary("clean-lib");
    }

    public static native long getRamUsage(Context context, long ramTotal, long ramPart);

    public static native void boost(Context context);

    public static native boolean checkRamOverload(Context context);

    public static native int getOverloadedPercents(Context context);

    public static long getRamTotal(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        return mi.totalMem;
    }

    public static long getRamPart(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        return mi.availMem;
    }

}
