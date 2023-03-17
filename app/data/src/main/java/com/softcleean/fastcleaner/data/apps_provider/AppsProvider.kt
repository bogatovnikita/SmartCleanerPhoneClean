package com.softcleean.fastcleaner.data.apps_provider

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import javax.inject.Inject

class AppsProvider @Inject constructor(
    private val context: Application
) {

    suspend fun getInstalledApp() : List<PackageInfo> {
        return context.packageManager.getInstalledPackages(0).filter {
            it.applicationInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP == 0 &&
                    it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0 &&
                    !it.packageName.contains(".test")
        }
    }

    suspend fun getSystemApp() : List<PackageInfo> {
        return context.packageManager.getInstalledPackages(0).filter {
            it.applicationInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0 &&
                    it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0 &&
                    !it.packageName.contains(".test")
        }
    }

}