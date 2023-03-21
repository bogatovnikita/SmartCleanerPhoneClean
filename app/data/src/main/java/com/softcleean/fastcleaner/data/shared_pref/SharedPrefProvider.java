package com.softcleean.fastcleaner.data.shared_pref;

import androidx.annotation.Keep;

@Keep
public class SharedPrefProvider {

    static{
        System.loadLibrary("clean-lib");
    }

    //Применяется для сохранения времени в сишной библиотеке
//    public static void saveToPreferences(String fieldName, long time) {
//        UtilsProviderForCLibrary.INSTANCE
//                .getSharedPreferencesProvider()
//                .edit()
//                .putLong(fieldName, time)
//                .apply();
//    }

}
