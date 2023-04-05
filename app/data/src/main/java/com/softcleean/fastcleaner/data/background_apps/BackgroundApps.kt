package com.softcleean.fastcleaner.data.background_apps

import android.app.Application
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import com.softcleean.fastcleaner.domain.models.BackgroundApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class BackgroundApps @Inject constructor(private val context: Application) {
    fun getRunningApps(): List<BackgroundApp> {
        return getStats()
            .map { getApp(it) }
            .filter {
                it.packageName.isNotEmpty()
                        && it.packageName != context.packageName
                        && context.packageManager.getLaunchIntentForPackage(it.packageName) != null
                        && !it.packageName.contains(".test")
            }
    }

    private fun getStats(): List<UsageStats> {
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val currentTime = System.currentTimeMillis()
        return usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            currentTime - TimeUnit.MINUTES.toMillis(1),
            currentTime
        ) ?: listOf()
    }

    private fun getApp(it: UsageStats): BackgroundApp {
        return try {
            BackgroundApp(
                packageName = it.packageName,
                name = getName(it.packageName),
                icon = getIcon(it.packageName)
            )
        } catch (ex: Exception) {
            BackgroundApp() // emptyApp
        }
    }

    private fun getName(packageName: String): String {
        return getPackageInfo(packageName).applicationInfo.loadLabel(context.packageManager)
            .toString()
    }

    private fun getIcon(packageName: String): Drawable {
        return getPackageInfo(packageName).applicationInfo.loadIcon(context.packageManager)
    }

    private fun getPackageInfo(packageName: String): PackageInfo {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(
                packageName,
                PackageManager.PackageInfoFlags.of(0)
            )
        } else {
            @Suppress("DEPRECATION") context.packageManager.getPackageInfo(packageName, 0)
        }
    }
}