package com.softcleean.fastcleaner.data.boost_provider

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import com.smart.cleaner.phoneclean.settings.Settings
import javax.inject.Inject

class BoostRealProvider @Inject constructor(
    private val settings: Settings,
    private val context: Application,
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

    fun saveTimeRamBoost() {
        settings.saveTimeRamBoost()
    }

    fun isRamBoosted() = settings.isRamBoosted()

}