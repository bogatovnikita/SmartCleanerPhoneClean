package com.softcleean.fastcleaner.data.battery_provider

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.softcleean.fastcleaner.data.shared_pref.UtilsProviderForCLibrary.getContentResolver
import java.lang.ref.WeakReference
import javax.inject.Inject

class RealBatteryProvider @Inject constructor(
    application: Application
) {

    private val contextWeakRef = WeakReference(application.applicationContext)
    private val context: Context
        get() = contextWeakRef.get()!!

    fun setScreenBrightness(value: Int) {
        try {
            Settings.System.putInt(
                getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,
                value
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    fun disableBluetooth() {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter() ?: return
        if (bluetoothAdapter.isEnabled) {
            bluetoothAdapter.disable()
        }
    }

    // c 10 версии это делать нельзя
    fun disableWiFi() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            val wifiManager =
                context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            if (wifiManager.isWifiEnabled) {
                wifiManager.isWifiEnabled = false
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun turnOffAutoBrightness() {
        if (Settings.System.canWrite(context)) {
            Settings.System.putInt(
                context.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
            )
        }
    }

}