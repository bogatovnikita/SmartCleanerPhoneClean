package com.softcleean.fastcleaner.data.battery_provider

import android.app.Application
import com.smart.cleaner.phoneclean.settings.Settings
import com.softcleean.fastcleaner.data.shared_pref.BatterySharedPreferences
import com.softcleean.fastcleaner.domain.battery.BatteryRepository
import javax.inject.Inject

class BatteryRepositoryImpl @Inject constructor(
    private val settings: Settings,
    private val context: Application,
    private val realBatteryProvider: RealBatteryProvider,
    private val batterySharedPreferences: BatterySharedPreferences,
) : BatteryRepository {

    override fun isBatteryBoosted() = settings.isBatteryBoosted()

    override fun saveTimeBatteryBoost() = settings.saveTimeBatteryBoost()

    override fun setScreenBrightness(value: Int) = realBatteryProvider.setScreenBrightness(value)

    override fun disableBluetooth() = realBatteryProvider.disableBluetooth(context)

    override fun disableWiFi() = realBatteryProvider.disableWiFi()

    override suspend fun killBackgroundProcessInstalledApps() =
        realBatteryProvider.killBackgroundProcessInstalledApps()

    override suspend fun killBackgroundProcessSystemApps() =
        realBatteryProvider.killBackgroundProcessSystemApps()

    override fun getBatteryType(): String = batterySharedPreferences.getBatteryType()

    override fun saveBatteryType(type: String) = batterySharedPreferences.saveBatteryType(type)

}