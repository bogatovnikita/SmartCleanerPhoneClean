package com.entertainment.event.ssearch.data.battery_provider

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.os.BatteryManager
import android.content.Intent
import android.content.IntentFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BatteryChargeReceiver @Inject constructor(): BroadcastReceiver() {

    private val _temperature: MutableStateFlow<Int> = MutableStateFlow(0)
    val temperature = _temperature.asStateFlow()

    private val _batteryPercent: MutableStateFlow<Int> = MutableStateFlow(0)
    val batteryPercent = _batteryPercent.asStateFlow()

    override fun onReceive(ctxt: Context?, intent: Intent) {
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val batteryPct = level * 100 / scale.toFloat()
        _temperature.value = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
        _batteryPercent.value = batteryPct.toInt()
    }

    //Почему сразу мы не подписываемся на значение, чтобы нам приходило последнее значение при подписке
//    fun updateInfo() {
//        batteryTemperature.postValue(BatteryProvider.calculateTemperature(application, temp))
//    }

    //Регистрируем при входе в приложение (в main activity (ты же пишешь все в single activity?))
    fun registerReceiver(activity: Activity) {
        activity.registerReceiver(
            this,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
    }

    //Отписываемся от подписки при выходе из приложения
    fun unregisterReceiver(activity: Activity) {
        try {
            activity.unregisterReceiver(this)
        } catch (e: Exception){}
    }
}