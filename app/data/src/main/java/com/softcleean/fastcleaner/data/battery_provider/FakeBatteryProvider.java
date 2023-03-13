package com.softcleean.fastcleaner.data.battery_provider;

import android.content.Context;
import androidx.annotation.Keep;
import com.softcleean.fastcleaner.data.shared_pref.UtilsProviderForCLibrary;

@Keep
public class FakeBatteryProvider {

    private static final String BATTERY_TYPE = "BATTERY_TYPE";

    static{
        System.loadLibrary("clean-lib");
    }

    public static native boolean checkBatteryDecrease(Context context);

    public static native int calculateWorkingMinutes(Context context, int percent);

    public static native void savePowerLowType(Context context);

    public static native void savePowerMediumType(Context context);

    public static native void savePowerHighType(Context context);

    public static void saveBatteryType(String type) {
        UtilsProviderForCLibrary.INSTANCE.getSharedPreferencesProvider().edit().putString(BATTERY_TYPE, type).apply();
    }

    public static String getBatteryType() {
       return UtilsProviderForCLibrary.INSTANCE.getSharedPreferencesProvider().getString(BATTERY_TYPE, "NORMAL");
    }
}
