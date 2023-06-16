package com.smart.cleaner.phoneclean.settings

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class Settings @Inject constructor(
    @ApplicationContext context: Context
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
        return saveTime + TimeUnit.MINUTES.toMillis(30) > currentTime
    }

    fun saveTimeBatteryBoost() {
        sharedPreferences.edit().putLong(TIME_BATTERY_BOOST, System.currentTimeMillis()).apply()
    }

    fun isBatteryBoosted(): Boolean {
        val saveTime = sharedPreferences.getLong(TIME_BATTERY_BOOST, 0L)
        return isCanBoostAgain(saveTime, TimeUnit.HOURS.toMillis(2))
    }

    fun saveTimeDuplicateDelete() {
        sharedPreferences.edit().putLong(TIME_DUPLICATES_DELETE, System.currentTimeMillis()).apply()
    }

    fun isDuplicateDelete(): Boolean {
        val saveTime = sharedPreferences.getLong(TIME_DUPLICATES_DELETE, 0L)
        return isBoostedToday(saveTime)
    }

    private fun isCanBoostAgain(saveTime: Long, repeatPeriod: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        return saveTime + repeatPeriod > currentTime
    }

    private fun isBoostedToday(time: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        val day = TimeUnit.DAYS.toMillis(1)
        return currentTime / day == time / day
    }

    fun getOpenInformationDialog(): Boolean {
        return sharedPreferences.getBoolean(OPEN_INFORMATION_DIALOG, false)
    }

    fun saveOpenInformation(state: Boolean) {
        sharedPreferences.edit().putBoolean(OPEN_INFORMATION_DIALOG, state).apply()
    }

    fun saveTimeJunkClean() {
        sharedPreferences.edit().putLong(JUNK_CLEAN_BOOST, System.currentTimeMillis()).apply()
    }

    fun isJunkCleanBoosted(): Boolean {
        val saveTime = sharedPreferences.getLong(JUNK_CLEAN_BOOST, 0L)
        val currentTime = System.currentTimeMillis()
        return saveTime + TimeUnit.DAYS.toMillis(1) > currentTime
    }

    companion object {
        private const val SETTINGS = "SETTINGS"
        private const val FIRST_LAUNCH = "FIRST_LAUNCH"
        private const val TIME_RAM_BOOST = "TIME_RAM_BOOST"
        private const val TIME_DUPLICATES_DELETE = "TIME_DUPLICATES_DELETE"
        private const val TIME_BATTERY_BOOST = "TIME_BATTERY_BOOST"
        private const val OPEN_INFORMATION_DIALOG = "OPEN_INFORMATION_DIALOG"
        private const val JUNK_CLEAN_BOOST = "JUNK_CLEAN_BOOST"
    }

}