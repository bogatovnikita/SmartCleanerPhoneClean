package com.entertainment.event.ssearch.data.cooling_provider;

import android.content.Context;

import androidx.annotation.Keep;

@Keep
public class CoolingProvider {

    static{
        System.loadLibrary("clean-lib");
    }

    public static native int calculateTemperature(Context context, int temp);

    public static native void cpu(Context context);

    public static native int getOverheatedApps(Context context);

}
