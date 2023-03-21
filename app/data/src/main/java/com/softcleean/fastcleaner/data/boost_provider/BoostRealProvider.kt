package com.softcleean.fastcleaner.data.boost_provider

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import com.softcleean.fastcleaner.data.shared_pref.SharedPreferencesProvider
import javax.inject.Inject

class BoostRealProvider @Inject constructor(
    private val context: Application,
    private val sharedPreferencesProvider: SharedPreferencesProvider
) {

    fun getRamTotal(): Long {
        val mi = ActivityManager.MemoryInfo()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)
        return mi.totalMem
    }

    private fun getRamPart(): Long {
        val mi = ActivityManager.MemoryInfo()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)
        return mi.availMem
    }

    fun getRamUsage(): Long {
        return getRamTotal() - getRamPart()
    }

    fun boost() {
        sharedPreferencesProvider.saveBoost()
    }

    fun checkRamOverload() = sharedPreferencesProvider.getBoost()

}