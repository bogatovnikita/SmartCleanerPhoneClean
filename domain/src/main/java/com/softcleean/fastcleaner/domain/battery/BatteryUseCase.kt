package com.softcleean.fastcleaner.domain.battery

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BatteryUseCase @Inject constructor(
    private val batteryRepository: BatteryRepository
) {
    fun checkBatteryDecrease(): Boolean = batteryRepository.checkBatteryDecrease()

    fun savePowerLowType() = batteryRepository.savePowerLowType()

    fun savePowerMediumType() = batteryRepository.savePowerMediumType()

    fun savePowerHighType() = batteryRepository.savePowerHighType()

    fun getBatteryType(): String = batteryRepository.getBatteryType()

    fun saveBatteryType(type: String) = batteryRepository.saveBatteryType(type)

    fun setScreenBrightness(value: Int) = batteryRepository.setScreenBrightness(value)

    fun disableBluetooth() = batteryRepository.disableBluetooth()

    fun disableWiFi() = batteryRepository.disableWiFi()

    fun turnOffAutoBrightness() = batteryRepository.turnOffAutoBrightness()

    fun calculateWorkingTime(percent: Int): Int = batteryRepository.calculateWorkingTime(percent)

    fun getBatteryTemperature(): Flow<Int> = batteryRepository.getBatteryTemperature()

    fun getBatteryPercent(): Flow<Int> = batteryRepository.getBatteryPercent()
}