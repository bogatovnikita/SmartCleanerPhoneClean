package com.softcleean.fastcleaner.domain.battery

interface BatteryRepository {

    fun checkBatteryDecrease(): Boolean

    fun saveTimeBatteryBoost()

    fun getBatteryType(): String

    fun saveBatteryType(type: String)

    fun setScreenBrightness(value: Int)

    fun disableBluetooth()

    fun disableWiFi()

    suspend fun killBackgroundProcessInstalledApps()

    suspend fun killBackgroundProcessSystemApps()

}