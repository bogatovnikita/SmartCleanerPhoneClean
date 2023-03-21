package com.softcleean.fastcleaner.data.kill_background

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import com.softcleean.fastcleaner.data.apps_provider.AppsProvider
import javax.inject.Inject

class KillBackgroundProvider @Inject constructor(
    private val context: Application,
    private val appsProvider: AppsProvider
) {

    suspend fun killBackgroundProcessInstalledApps() {
        val am = context.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
        appsProvider.getInstalledApp().forEach { app ->
            am.killBackgroundProcesses(app.packageName)
        }
    }

    suspend fun killBackgroundProcessSystemApps() {
        val am = context.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
        appsProvider.getSystemApp().forEach { app ->
            am.killBackgroundProcesses(app.packageName)
        }
    }
}