package com.softcleean.fastcleaner.data.battery_provider

import android.app.Application
import android.content.Context
import com.softcleean.fastcleaner.domain.battery.BatteryRepository
import kotlinx.coroutines.flow.Flow
import java.lang.ref.WeakReference
import javax.inject.Inject

class BatteryRepositoryImpl @Inject constructor(
    private val application: Application,
    private val batteryChargeReceiver: BatteryChargeReceiver,
    private val realBatteryProvider: RealBatteryProvider,
): BatteryRepository {

    private val contextWeakRef = WeakReference(application.applicationContext)
    private val context: Context
        get() = contextWeakRef.get()!!

    override fun checkBatteryDecrease(): Boolean = FakeBatteryProvider.checkBatteryDecrease(context)

    override fun calculateWorkingTime(percent: Int): Int = FakeBatteryProvider.calculateWorkingMinutes(context, percent)

    override fun savePowerLowType() = FakeBatteryProvider.savePowerLowType(context)

    override fun savePowerMediumType() = FakeBatteryProvider.savePowerMediumType(context)

    override fun savePowerHighType() = FakeBatteryProvider.savePowerHighType(context)

    override fun getBatteryType(): String = FakeBatteryProvider.getBatteryType()

    override fun saveBatteryType(type: String) = FakeBatteryProvider.saveBatteryType(type)

    override fun setScreenBrightness(value: Int) = realBatteryProvider.setScreenBrightness(value)

    override fun disableBluetooth() = realBatteryProvider.disableBluetooth(context)

    override fun disableWiFi() = realBatteryProvider.disableWiFi()

    override fun turnOffAutoBrightness() = realBatteryProvider.turnOffAutoBrightness()

    override fun getBatteryTemperature(): Flow<Int> = batteryChargeReceiver.temperature

    override fun getBatteryPercent(): Flow<Int> = batteryChargeReceiver.batteryPercent

}