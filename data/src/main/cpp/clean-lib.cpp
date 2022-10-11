#include <jni.h>
#include <stdlib.h>

jint garbageSize = 0;
jint appsCausing = 0;
jlong usedRam = 0;
jlong temperature = 0;
jint overloadedPercens = 0;

bool junkSizeCountCreated = false;
jintArray junkSizeCountArray = jintArray();
jint junk_one = 0;
jint junk_two = 0;
jint junk_three = 0;
jint junk_four = 0;

bool junkItemsCountCreated = false;
jintArray junkItemsCountArray = jintArray();
jint files_junk_one = 0;
jint files_junk_two = 0;
jint files_junk_three = 0;
jint files_junk_four = 0;

jstring BOOST(JNIEnv *env) {
    return env->NewStringUTF("boost");
}

jstring POWER_LOW(JNIEnv *env) {
    return env->NewStringUTF("power_low");
}

jstring POWER_MEDIUM(JNIEnv *env) {
    return env->NewStringUTF("power_medium");
}

jstring POWER_HIGH(JNIEnv *env) {
    return env->NewStringUTF("power_high");
}

jstring CPU(JNIEnv *env) {
    return env->NewStringUTF("cpu");
}

jstring JUNK(JNIEnv *env) {
    return env->NewStringUTF("junk");
}

jobject getPreferences(JNIEnv *env, jobject context) {
    jclass contextClass = env->FindClass("android/content/Context");
    jmethodID getSharedPreferences = env->GetMethodID(contextClass, "getSharedPreferences",
                                                      "(Ljava/lang/String;I)Landroid/content/SharedPreferences;");
    jfieldID modePrivateFieldId = env->GetStaticFieldID(contextClass, "MODE_PRIVATE", "I");

    jint modePrivate = env->GetStaticIntField(contextClass, modePrivateFieldId);
    return env->CallObjectMethod(context, getSharedPreferences,
                                 env->NewStringUTF("APP_PREFERENCES"), modePrivate);
}

jlong getLongFromPreferences(JNIEnv *env, jobject context, jstring fieldName, jlong defaultValue) {
    jclass preferencesClass = env->FindClass("android/content/SharedPreferences");
    jobject preferences = getPreferences(env, context);

    jmethodID getIntMethodId = env->GetMethodID(preferencesClass, "getLong",
                                                "(Ljava/lang/String;J)J");

    return env->CallLongMethod(preferences, getIntMethodId, fieldName, defaultValue);
}

jlong getCurrentTime(JNIEnv *env) {
    jclass system = env->FindClass("java/lang/System");
    jmethodID currentTime = env->GetStaticMethodID(system, "currentTimeMillis", "()J");
    return env->CallStaticLongMethod(system, currentTime);
}

jboolean checkTimeExpired(JNIEnv *env, jobject context, jstring field) {
    jlong currentTime = getCurrentTime(env);
    jlong def = 0;
    jlong doneTime = getLongFromPreferences(env, context, field, def);
    return (currentTime - doneTime) < 1000 * 60 * 60 * 2;
}

extern "C"
JNIEXPORT jobjectArray JNICALL
Java_com_entertainment_event_ssearch_data_clean_1provider_CleanProvider_getFolders(JNIEnv *env,
                                                         jclass clazz) {
    jobjectArray ret;
    int i;

    char *message[50] = {"/apex/com.android.tethering/priv-app/TetheringGoogle/TetheringGoogle.apk",
                         "/system/app/FilterProvider/FilterProvider.apk",
                         "/data/app/~~bdjQFK06A3fQ_MpGON9WzA==/com.antapps.skyphclean-GJo_Pf0vT0iou1c36S_f_g==/base.apk",
                         "/system/app/AutomationTest_FB/AutomationTest_FB.apk",
                         "/system/priv-app/SmartSwitchAssistant/SmartSwitchAssistant.apk",
                         "/system/app/SetupWizardLegalProvider/SetupWizardLegalProvider.apk",
                         "/system/priv-app/Finder/Finder.apk",
                         "/system/priv-app/NSFusedLocation_v5.3/NSFusedLocation_v5.3.apk",
                         "/system/app/ChromeCustomizations/ChromeCustomizations.apk",
                         "/system/priv-app/AODService_v60/AODService_v60.apk",
                         "/system/priv-app/CocktailBarService_v3.2/CocktailBarService_v3.2.apk",
                         "/apex/com.android.extservices/priv-app/GoogleExtServices/GoogleExtServices.apk",
                         "/system/priv-app/SecTelephonyProvider/SecTelephonyProvider.apk",
                         "/data/app/~~DIKEQ6QinBDjzDQlbHn1KA==/com.sec.android.app.ve.vebgm-XWmxPEE7j026NBKXKGRoBg==/base.apk",
                         "/system/app/DRParser/DRParser.apk",
                         "/system/priv-app/DynamicSystemInstallationService/DynamicSystemInstallationService.apk",
                         "/product/app/NetworkStackOverlay/NetworkStackOverlay.apk",
                         "/data/app/~~EaVT6DHvcdtOhd-e_-KWUg==/com.samsung.android.calendar-XO6DPMPgrZRvWwi7HwC7qw==/base.apk",
                         "/apex/com.android.cellbroadcast/priv-app/GoogleCellBroadcastServiceModule/GoogleCellBroadcastServiceModule.apk",
                         "/system/priv-app/SamsungCalendarProvider/SamsungCalendarProvider.apk",
                         "/data/app/~~edFOXh3WpKerJY1-X8f1EQ==/com.osp.app.signin-JYsMLk_l87iQYtheqkroGw==/base.apk",
                         "/system/priv-app/AREmoji/AREmoji.apk",
                         "/system/app/TetheringAutomation/TetheringAutomation.apk",
                         "/system/priv-app/MediaProviderLegacy/MediaProviderLegacy.apk",
                         "/system/priv-app/SamsungSocial/SamsungSocial.apk",
                         "/system/system_ext/priv-app/WallpaperCropper/WallpaperCropper.apk",
                         "/system/priv-app/wallpaper-res/wallpaper-res.apk",
                         "/system/app/SmartMirroring/SmartMirroring.apk",
                         "/system/app/MAPSAgent/MAPSAgent.apk",
                         "/system/priv-app/SendHelpMessage/SendHelpMessage.apk",
                         "/system/priv-app/SamsungInCallUI/SamsungInCallUI.apk",
                         "/system/app/FactoryCameraFB/FactoryCameraFB.apk",
                         "/system/app/USBSettings/USBSettings.apk",
                         "/data/app/~~N7czJw18VjEgh1OiKAHZOw==/com.samsung.android.easysetup-Vwy8CcD2EwY6AmdP6IFaQQ==/base.apk",
                         "/system/priv-app/ExternalStorageProvider/ExternalStorageProvider.apk",
                         "/system/app/AllShareAware/AllShareAware.apk",
                         "/system/app/EasyOneHand3/EasyOneHand3.apk",
                         "/system/priv-app/DeviceTest/DeviceTest.apk",
                         "/system/app/SecHTMLViewer/SecHTMLViewer.apk",
                         "/system/app/CompanionDeviceManager/CompanionDeviceManager.apk",
                         "/system/priv-app/MmsService/MmsService.apk",
                         "/data/app/~~Qgm5KQL77yoefzGyKZxdDA==/com.samsung.android.rubin.app-3tGQL_nA4Hcy0pfqlkbXEA==/base.apk",
                         "/system/priv-app/SecDownloadProvider/SecDownloadProvider.apk",
                         "/system/app/SmartSwitchAgent/SmartSwitchAgent.apk",
                         "/vendor/overlay/TetheringOverlay/TetheringOverlay.apk",
                         "/data/app/~~FYxuh_m6lZDt87GbuoitVg==/com.samsung.android.mdx.quickboard-Lj7BEftNTLVQaapt4Q5YoQ==/base.apk",
                         "/system/priv-app/OmaCP/OmaCP.apk",
                         "/system/priv-app/FaceService/FaceService.apk",
                         "/system/priv-app/GpuWatchApp/GpuWatchApp.apk",
                         "/system/priv-app/MtpApplication/MtpApplication.apk"};

    ret = (jobjectArray) env->NewObjectArray(50,
                                             env->FindClass("java/lang/String"),
                                             env->NewStringUTF(""));

    for (i = 0; i < 50; i++) {
        env->SetObjectArrayElement(
                ret, i, env->NewStringUTF(message[i]));
    }
    return (ret);
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_entertainment_event_ssearch_data_cooling_1provider_CoolingProvider_getOverheatedApps(
        JNIEnv *env, jclass clazz, jobject context) {
    if (checkTimeExpired(env, context, CPU(env))) {
        return 0;
    } else if (appsCausing == 0) {
        appsCausing = (jint) (rand() % 30) + 10;
    }
    return appsCausing;
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_ronmobgroup_ronclenaer_utils_NativeProvider_getGarbageSize(
        JNIEnv *env, jclass clazz, jobject context) {
    if (!checkTimeExpired(env, context, JUNK(env))) {
        return 0;
    }
    if (garbageSize == 0) {
        garbageSize = (jint) (rand() % 1000) + 1000;
    }
    return garbageSize;
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_entertainment_event_ssearch_data_battery_1provider_BatteryProvider_calculateWorkingMinutes(
        JNIEnv *env, jclass clazz, jobject context, jint percent) {
    if (!checkTimeExpired(env, context, POWER_HIGH(env))) {
        return percent * 16 + ((jint) (rand() % 30));
    } else if (!checkTimeExpired(env, context, POWER_MEDIUM(env))) {
        return percent * 14 + ((jint) (rand() % 30));
    } else if (!checkTimeExpired(env, context, POWER_LOW(env))) {
        return percent * 12 + ((jint) (rand() % 30));
    } else {
        return percent * 9 + ((jint) (rand() % 30));
    }
}


void putLongToPreferences(JNIEnv *env, jobject context, jstring fieldName, jlong value) {
    jclass providerClass = env->FindClass(
            "com/entertainment/event/ssearch/data/shared_pref/SharedPrefProvider");
    jmethodID putStringMethodId = env->GetStaticMethodID(providerClass, "saveToPreferences",
                                                         "(Ljava/lang/String;J)V");
    env->CallStaticVoidMethod(providerClass, putStringMethodId, fieldName, value);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_entertainment_event_ssearch_data_boost_1provider_BoostProvider_boost(JNIEnv *env,
                                                    jclass clazz,
                                                    jobject context) {
    putLongToPreferences(env, context, BOOST(env), getCurrentTime(env));
}

extern "C"
JNIEXPORT void JNICALL
Java_com_entertainment_event_ssearch_data_clean_1provider_CleanProvider_clean(JNIEnv *env,
                                                   jclass clazz,
                                                   jobject context) {
    putLongToPreferences(env, context, JUNK(env), getCurrentTime(env));
}
extern "C"
JNIEXPORT void JNICALL
Java_com_entertainment_event_ssearch_data_cooling_1provider_CoolingProvider_cpu(JNIEnv *env,
                                                  jclass clazz,
                                                  jobject context) {
    putLongToPreferences(env, context, CPU(env), getCurrentTime(env));
    appsCausing = 0;
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_entertainment_event_ssearch_data_boost_1provider_BoostProvider_getRamUsage(JNIEnv *env,
                                                          jclass clazz,
                                                          jobject context,
                                                          jlong ram_total,
                                                          jlong ram_part) {
    jlong ramFree = ram_part * 0.7;
    if (checkTimeExpired(env, context, BOOST(env))) {
        return ram_total - ram_part;
    } else {
        if (usedRam == 0) {
            usedRam = ram_total - ram_part + (rand() % ramFree);
        }
        return usedRam;
    }
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_entertainment_event_ssearch_data_boost_1provider_BoostProvider_checkRamOverload(
        JNIEnv *env, jclass clazz, jobject instance) {
    return checkTimeExpired(env, instance, BOOST(env));
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_entertainment_event_ssearch_data_cooling_1provider_CoolingProvider_calculateTemperature(
        JNIEnv *env, jclass clazz, jobject context, jint temp) {
    if (!checkTimeExpired(env, context, CPU(env))) {
        if(temperature == 0){
            temperature = ((rand() % 3) + 3);
        }
        return temp / 10 + temperature;
    } else {
        return temp / 10;
    }
}
extern "C"
JNIEXPORT void JNICALL
Java_com_entertainment_event_ssearch_data_battery_1provider_BatteryProvider_savePowerLowType(JNIEnv *env,
                                                       jclass clazz,
                                                       jobject context) {
    putLongToPreferences(env, context, POWER_LOW(env), getCurrentTime(env));

}
extern "C"
JNIEXPORT void JNICALL
Java_com_entertainment_event_ssearch_data_battery_1provider_BatteryProvider_savePowerMediumType(JNIEnv *env,
                                                          jclass clazz,
                                                          jobject context) {
    putLongToPreferences(env, context, POWER_MEDIUM(env), getCurrentTime(env));
}
extern "C"
JNIEXPORT void JNICALL
Java_com_entertainment_event_ssearch_data_battery_1provider_BatteryProvider_savePowerHighType(JNIEnv *env,
                                                        jclass clazz,
                                                        jobject context) {
    putLongToPreferences(env, context, POWER_HIGH(env), getCurrentTime(env));
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_entertainment_event_ssearch_data_battery_1provider_BatteryProvider_checkBatteryDecrease(
        JNIEnv *env, jclass clazz, jobject context) {
    return checkTimeExpired(env, context, POWER_LOW(env))
           || checkTimeExpired(env, context, POWER_MEDIUM(env))
           || checkTimeExpired(env, context, POWER_HIGH(env));

}
extern "C"
JNIEXPORT jint JNICALL
Java_com_entertainment_event_ssearch_data_boost_1provider_BoostProvider_getOverloadedPercents(JNIEnv *env, jclass clazz,
                                                                    jobject context) {
    overloadedPercens = (rand() % 10) + 2;
    return overloadedPercens;
}

jintArray createJunkSizeArray(JNIEnv *env) {
    jintArray result;
    result = (*env).NewIntArray(4);
    jint fill[4];
    fill[0] = junk_one;
    fill[1] = junk_two;
    fill[2] = junk_three;
    fill[3] = junk_four;
    (*env).SetIntArrayRegion(result, 0, 4, fill);
    return result;
}

jintArray createJunkFilesArray(JNIEnv *env) {
    jintArray result;
    result = (*env).NewIntArray(4);
    jint fill[4];
    fill[0] = files_junk_one;
    fill[1] = files_junk_two;
    fill[2] = files_junk_three;
    fill[3] = files_junk_four;
    (*env).SetIntArrayRegion(result, 0, 4, fill);
    return result;
}

extern "C"
JNIEXPORT jintArray JNICALL
Java_com_entertainment_event_ssearch_data_clean_1provider_CleanProvider_getGarbageFilesCount(JNIEnv *env, jclass clazz,
                                                                   jobject context) {
    if (!checkTimeExpired(env, context, JUNK(env))) {
        files_junk_one = 0;
        files_junk_two = 0;
        files_junk_three = 0;
        files_junk_four = 0;
    } else {
        if (!junkItemsCountCreated) {
            files_junk_one = (jint) (rand() % 10) + 10;
            files_junk_two = (jint) (rand() % 10) + 10;
            files_junk_three = (jint) (rand() % 10) + 10;
            files_junk_four = (jint) (rand() % 10) + 10;
            junkItemsCountCreated = true;
        }
    }
    junkItemsCountArray = createJunkFilesArray(env);
    return junkItemsCountArray;
}

extern "C"
JNIEXPORT jintArray JNICALL
Java_com_entertainment_event_ssearch_data_clean_1provider_CleanProvider_getGarbageSizeArray(JNIEnv *env, jclass clazz,
                                                                  jobject context) {
    if (checkTimeExpired(env, context, JUNK(env))) {
        junk_one = 0;
        junk_two = 0;
        junk_three = 0;
        junk_four = 0;
    } else {
        if (!junkSizeCountCreated) {
            junk_one = (jint) (rand() % 200) + 100;
            junk_two = (jint) (rand() % 200) + 100;
            junk_three = (jint) (rand() % 200) + 100;
            junk_four = (jint) (rand() % 200) + 100;
            junkSizeCountCreated = true;
        }
    }
    junkSizeCountArray = createJunkSizeArray(env);
    return junkSizeCountArray;
}