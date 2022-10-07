package com.entertainment.event.ssearch.data.battery_provider;

import android.content.Context;
import android.provider.Settings;
import androidx.annotation.Keep;
import com.entertainment.event.ssearch.data.shared_pref.UtilsProviderForCLibrary;

@Keep
public class BatteryProvider {

    private static final String BATTERY_TYPE = "BATTERY_TYPE";

    static{
        System.loadLibrary("clean-lib");
    }

    public static native boolean checkBatteryDecrease(Context context);

    public static native int calculateWorkingMinutes(Context context, int percent);

    public static native void savePowerLowType(Context context);

    public static native void savePowerMediumType(Context context);

    public static native void savePowerHighType(Context context);

    public static void setScreenBrightness(Integer value){
        try{
            Settings.System.putInt(UtilsProviderForCLibrary.INSTANCE.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, value);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void saveBatteryType(String type) {
        UtilsProviderForCLibrary.INSTANCE.getSharedPreferencesProvider().edit().putString(BATTERY_TYPE, type).apply();
    }

    public static String getBatteryType() {
       return UtilsProviderForCLibrary.INSTANCE.getSharedPreferencesProvider().getString(BATTERY_TYPE, "");
    }
}
