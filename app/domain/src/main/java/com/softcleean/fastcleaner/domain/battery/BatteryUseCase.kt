package com.softcleean.fastcleaner.domain.battery

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

    suspend fun killBackgroundProcessInstalledApps() = batteryRepository.killBackgroundProcessInstalledApps()

    suspend fun killBackgroundProcessSystemApps() = batteryRepository.killBackgroundProcessSystemApps()
}