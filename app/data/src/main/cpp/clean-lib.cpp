#include <jni.h>
#include <stdlib.h>

jlong usedRam = 0;
jint overloadedPercens = 0;

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

void putLongToPreferences(JNIEnv *env, jobject context, jstring fieldName, jlong value) {
    jclass providerClass = env->FindClass(
            "com/softcleean/fastcleaner/data/shared_pref/SharedPrefProvider");
    jmethodID putStringMethodId = env->GetStaticMethodID(providerClass, "saveToPreferences",
                                                         "(Ljava/lang/String;J)V");
    env->CallStaticVoidMethod(providerClass, putStringMethodId, fieldName, value);
}


extern "C"
JNIEXPORT void JNICALL
Java_com_softcleean_fastcleaner_data_boost_1provider_BoostProvider_boost(JNIEnv *env,
                                                    jclass clazz,
                                                    jobject context) {
    putLongToPreferences(env, context, BOOST(env), getCurrentTime(env));
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_softcleean_fastcleaner_data_boost_1provider_BoostProvider_getRamUsage(JNIEnv *env,
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
Java_com_softcleean_fastcleaner_data_boost_1provider_BoostProvider_checkRamOverload(
        JNIEnv *env, jclass clazz, jobject instance) {
    return checkTimeExpired(env, instance, BOOST(env));
}
extern "C"
JNIEXPORT void JNICALL
Java_com_softcleean_fastcleaner_data_battery_1provider_BatteryPrefsProvider_savePowerLowType(JNIEnv *env,
                                                                                             jclass clazz,
                                                                                             jobject context) {
    putLongToPreferences(env, context, POWER_LOW(env), getCurrentTime(env));

}
extern "C"
JNIEXPORT void JNICALL
Java_com_softcleean_fastcleaner_data_battery_1provider_BatteryPrefsProvider_savePowerMediumType(JNIEnv *env,
                                                                                                jclass clazz,
                                                                                                jobject context) {
    putLongToPreferences(env, context, POWER_MEDIUM(env), getCurrentTime(env));
}
extern "C"
JNIEXPORT void JNICALL
Java_com_softcleean_fastcleaner_data_battery_1provider_BatteryPrefsProvider_savePowerHighType(JNIEnv *env,
                                                                                              jclass clazz,
                                                                                              jobject context) {
    putLongToPreferences(env, context, POWER_HIGH(env), getCurrentTime(env));
}
extern "C"
JNIEXPORT jboolean JNICALL
Java_com_softcleean_fastcleaner_data_battery_1provider_BatteryPrefsProvider_checkBatteryDecrease(
        JNIEnv *env, jclass clazz, jobject context) {
    return checkTimeExpired(env, context, POWER_LOW(env))
           || checkTimeExpired(env, context, POWER_MEDIUM(env))
           || checkTimeExpired(env, context, POWER_HIGH(env));

}
extern "C"
JNIEXPORT jint JNICALL
Java_com_softcleean_fastcleaner_data_boost_1provider_BoostProvider_getOverloadedPercents(JNIEnv *env, jclass clazz,
                                                                    jobject context) {
    overloadedPercens = (rand() % 10) + 2;
    return overloadedPercens;
}