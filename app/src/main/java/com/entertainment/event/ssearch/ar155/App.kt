package com.entertainment.event.ssearch.ar155

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import com.entertainment.event.ssearch.data.shared_pref.UtilsProviderForCLibrary
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
//        val config = YandexMetricaConfig.newConfigBuilder(BuildConfig.APP_METRICA_KEY).build() TODO
//        YandexMetrica.activate(applicationContext, config)
//        YandexMetrica.enableActivityAutoTracking(this)
        if (isMainProcess()) {
            UtilsProviderForCLibrary.initUtilsProviderForCLibrary(this)
        }
    }

    private fun isMainProcess(): Boolean {
        return packageName == getCurrentProcessName()
    }

    private fun getCurrentProcessName(): String? {
        val mypid = Process.myPid()
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val infos = manager.runningAppProcesses
        for (info in infos) {
            if (info.pid == mypid) {
                return info.processName
            }
        }
        return null
    }
}