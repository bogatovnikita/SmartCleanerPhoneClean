package com.softcleean.fastcleaner.domain.battery

interface BatteryRepository {

    fun checkBatteryDecrease(): Boolean

    fun savePowerLowType()

    fun savePowerMediumType()

    fun savePowerHighType()

    fun getBatteryType(): String

    fun saveBatteryType(type: String)

    fun setScreenBrightness(value: Int)

    fun disableBluetooth()

    fun disableWiFi()

    suspend fun killBackgroundProcessInstalledApps()

    suspend fun killBackgroundProcessSystemApps()

}