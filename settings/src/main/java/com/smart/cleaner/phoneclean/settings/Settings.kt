package com.smart.cleaner.phoneclean.settings

import android.app.Application
import android.content.Context
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class Settings @Inject constructor(
    context: Application
) {

    private val sharedPreferences =
        context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)

    fun saveFirstLaunch() {
        sharedPreferences.edit().putBoolean(FIRST_LAUNCH, false).apply()
    }

    fun getFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean(FIRST_LAUNCH, true)
    }

    fun saveTimeRamBoost() {
        sharedPreferences.edit().putLong(TIME_RAM_BOOST, System.currentTimeMillis()).apply()
    }

    fun isRamBoosted(): Boolean {
        val currentTime = System.currentTimeMillis()
        val saveTime = sharedPreferences.getLong(TIME_RAM_BOOST, 0L)
        return saveTime + TimeUnit.HOURS.toMillis(2) > currentTime
    }

    fun saveTimeBatteryBoost() {
        sharedPreferences.edit().putLong(TIME_BATTERY_BOOST, System.currentTimeMillis()).apply()
    }

    fun isBatteryBoosted(): Boolean {
        val currentTime = System.currentTimeMillis()
        val saveTime = sharedPreferences.getLong(TIME_BATTERY_BOOST, 0L)
        return saveTime + TimeUnit.HOURS.toMillis(2) > currentTime
    }

    companion object {
        private const val SETTINGS = "SETTINGS"
        private const val FIRST_LAUNCH = "FIRST_LAUNCH"
        private const val TIME_RAM_BOOST = "TIME_RAM_BOOST"
        private const val TIME_BATTERY_BOOST = "TIME_BATTERY_BOOST"
    }

}