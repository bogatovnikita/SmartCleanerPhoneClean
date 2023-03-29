package com.softcleean.fastcleaner.data.shared_pref

import android.app.Application
import android.content.Context
import javax.inject.Inject

class SharedPreferencesProvider @Inject constructor(private val application: Application) {

    private val sharedPreferences =
        application.getSharedPreferences(APPLICATION_NAME, Context.MODE_PRIVATE)

    fun saveFirstLaunch() {
        sharedPreferences.edit().putBoolean(FIRST_LAUNCH, false).apply()
    }

    fun getFirstLaunch(): Boolean {
        return sharedPreferences.getBoolean(FIRST_LAUNCH, true)
    }

    fun saveBoost() {
        sharedPreferences.edit().putLong(SAVE_BOOST_TIME, System.currentTimeMillis()).apply()
    }

    fun isRamBoosted(): Boolean {
        val currentTime = System.currentTimeMillis()
        val saveTime = sharedPreferences.getLong(SAVE_BOOST_TIME, 0L)
        return saveTime + TIME_NEED_BOOST > currentTime
    }

    fun saveBatteryType(type: String) {
        sharedPreferences.edit().putString(BATTERY_TYPE, type).apply()
    }

    fun getBatteryType(): String {
        return sharedPreferences.getString(BATTERY_TYPE, "") ?: ""
    }

    fun savePowerLowType() {
        sharedPreferences.edit().putLong(POWER_LOW_TYPE, System.currentTimeMillis()).apply()
    }

    fun savePowerMediumType() {
        sharedPreferences.edit().putLong(POWER_MEDIUM_TYPE, System.currentTimeMillis()).apply()
    }

    fun savePowerHighType() {
        sharedPreferences.edit().putLong(POWER_HIGH_TYPE, System.currentTimeMillis()).apply()
    }

    fun checkBatteryDecrease(): Boolean {
        return checkTimeDecrease(POWER_LOW_TYPE) || checkTimeDecrease(POWER_MEDIUM_TYPE) || checkTimeDecrease(
            POWER_HIGH_TYPE
        )
    }

    private fun checkTimeDecrease(type: String): Boolean {
        val currentTime = System.currentTimeMillis()
        return sharedPreferences.getLong(type, 0L) + TIME_NEED_BATTERY_SAVE > currentTime
    }

    companion object {
        private const val APPLICATION_NAME = "APPLICATION_NAME"
        private const val FIRST_LAUNCH = "FIRST_LAUNCH"
        private const val SAVE_BOOST_TIME = "SAVE_BOOST_TIME"
        private const val TIME_NEED_BOOST = 900_000L
        private const val BATTERY_TYPE = "BATTERY_TYPE"
        private const val POWER_LOW_TYPE = "POWER_LOW_TYPE"
        private const val POWER_MEDIUM_TYPE = "POWER_MEDIUM_TYPE"
        private const val POWER_HIGH_TYPE = "POWER_HIGH_TYPE"
        private const val TIME_NEED_BATTERY_SAVE = 720_000L
    }
}