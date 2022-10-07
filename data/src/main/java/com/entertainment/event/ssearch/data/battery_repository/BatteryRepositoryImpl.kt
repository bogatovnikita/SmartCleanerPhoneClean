package com.entertainment.event.ssearch.data.battery_repository

import android.app.Application
import com.entertainment.event.ssearch.data.battery_provider.BatteryChargeReceiver
import com.entertainment.event.ssearch.data.battery_provider.BatteryProvider
import com.entertainment.event.ssearch.domain.battery.BatteryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BatteryRepositoryImpl @Inject constructor(
    private val application: Application,
    private val batteryChargeReceiver: BatteryChargeReceiver,
): BatteryRepository {

    override fun checkBatteryDecrease(): Boolean = BatteryProvider.checkBatteryDecrease(application)

    override fun calculateWorkingTime(percent: Int): Int = BatteryProvider.calculateWorkingMinutes(application, percent)

    override fun savePowerLowType() = BatteryProvider.savePowerLowType(application)

    override fun savePowerMediumType() = BatteryProvider.savePowerMediumType(application)

    override fun savePowerHighType() = BatteryProvider.savePowerHighType(application)

    override fun getBatteryType(): String = BatteryProvider.getBatteryType()

    override fun saveBatteryType(type: String) = BatteryProvider.saveBatteryType(type)

    override fun setScreenBrightness(value: Int) = BatteryProvider.setScreenBrightness(value)

    override fun getBatteryTemperature(): Flow<Int> = batteryChargeReceiver.temperature

    override fun getBatteryPercent(): Flow<Int> = batteryChargeReceiver.batteryPercent

}