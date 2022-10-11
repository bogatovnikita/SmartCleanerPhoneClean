package com.entertainment.event.ssearch.data.battery_provider

import android.app.Application
import android.content.Context
import com.entertainment.event.ssearch.data.battery_provider.BatteryChargeReceiver
import com.entertainment.event.ssearch.data.battery_provider.BatteryProvider
import com.entertainment.event.ssearch.domain.battery.BatteryRepository
import kotlinx.coroutines.flow.Flow
import java.lang.ref.WeakReference
import javax.inject.Inject

class BatteryRepositoryImpl @Inject constructor(
    private val application: Application,
    private val batteryChargeReceiver: BatteryChargeReceiver,
): BatteryRepository {

    private val contextWeakRef = WeakReference(application.applicationContext)
    private val context: Context
        get() = contextWeakRef.get()!!

    override fun checkBatteryDecrease(): Boolean = BatteryProvider.checkBatteryDecrease(context)

    override fun calculateWorkingTime(percent: Int): Int = BatteryProvider.calculateWorkingMinutes(context, percent)

    override fun savePowerLowType() = BatteryProvider.savePowerLowType(context)

    override fun savePowerMediumType() = BatteryProvider.savePowerMediumType(context)

    override fun savePowerHighType() = BatteryProvider.savePowerHighType(context)

    override fun getBatteryType(): String = BatteryProvider.getBatteryType()

    override fun saveBatteryType(type: String) = BatteryProvider.saveBatteryType(type)

    override fun setScreenBrightness(value: Int) = BatteryProvider.setScreenBrightness(value)

    override fun getBatteryTemperature(): Flow<Int> = batteryChargeReceiver.temperature

    override fun getBatteryPercent(): Flow<Int> = batteryChargeReceiver.batteryPercent

}