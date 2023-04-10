package com.softcleean.fastcleaner.data.shared_pref

import android.app.Application
import android.content.Context
import javax.inject.Inject

class BatterySharedPreferences @Inject constructor(private val application: Application) {

    private val sharedPreferences =
        application.getSharedPreferences(BATTERY_SETTINGS, Context.MODE_PRIVATE)

    fun saveBatteryType(type: String) {
        sharedPreferences.edit().putString(BATTERY_TYPE, type).apply()
    }

    fun getBatteryType(): String {
        return sharedPreferences.getString(BATTERY_TYPE, "NORMAL") ?: "NORMAL"
    }

    companion object {
        private const val BATTERY_SETTINGS = "APPLICATION_NAME"
        private const val BATTERY_TYPE = "BATTERY_TYPE"
    }
}