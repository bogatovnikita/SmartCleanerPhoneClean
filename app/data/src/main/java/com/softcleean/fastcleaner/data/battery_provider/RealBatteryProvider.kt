package com.softcleean.fastcleaner.data.battery_provider

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Context.BLUETOOTH_SERVICE
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import com.softcleean.fastcleaner.data.shared_pref.UtilsProviderForCLibrary.getContentResolver
import com.softcleean.fastcleaner.data.apps_provider.AppsProvider
import java.lang.ref.WeakReference
import javax.inject.Inject

class RealBatteryProvider @Inject constructor(
    application: Application,
    private val appsProvider: AppsProvider
) {

    private val contextWeakRef = WeakReference(application.applicationContext)
    private val context: Context
        get() = contextWeakRef.get()!!

    fun setScreenBrightness(value: Int) {
        try {
            Settings.System.putInt(
                getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS,
                value
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    fun disableBluetooth(context: Context) {
        val bluetoothAdapter =
            (context.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter ?: return
        if (bluetoothAdapter.isEnabled) {
            bluetoothAdapter.disable()
        }
    }

    fun disableWiFi() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val wifiManager =
                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            if (wifiManager.isWifiEnabled) {
                wifiManager.isWifiEnabled = false
            }
        }
    }

    suspend fun killBackgroundProcessInstalledApps() {
        val am =
            context.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
            appsProvider.getInstalledApp().forEach { app ->
                am.killBackgroundProcesses(app.packageName)
            }
    }

    suspend fun killBackgroundProcessSystemApps() {
        val am =
            context.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
        appsProvider.getSystemApp().forEach { app ->
            am.killBackgroundProcesses(app.packageName)
        }
    }

}