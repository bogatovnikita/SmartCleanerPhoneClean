package com.softcleean.fastcleaner.data.battery_provider

import android.app.Application
import com.softcleean.fastcleaner.data.shared_pref.SharedPreferencesProvider
import com.softcleean.fastcleaner.domain.battery.BatteryRepository
import javax.inject.Inject

class BatteryRepositoryImpl @Inject constructor(
    private val context: Application,
    private val realBatteryProvider: RealBatteryProvider,
    private val sharedPreferencesProvider: SharedPreferencesProvider
) : BatteryRepository {

    override fun checkBatteryDecrease() = sharedPreferencesProvider.checkBatteryDecrease()

    override fun savePowerLowType() = sharedPreferencesProvider.savePowerLowType()

    override fun savePowerMediumType() = sharedPreferencesProvider.savePowerMediumType()

    override fun savePowerHighType() = sharedPreferencesProvider.savePowerHighType()

    override fun setScreenBrightness(value: Int) = realBatteryProvider.setScreenBrightness(value)

    override fun disableBluetooth() = realBatteryProvider.disableBluetooth(context)

    override fun disableWiFi() = realBatteryProvider.disableWiFi()

    override suspend fun killBackgroundProcessInstalledApps() =
        realBatteryProvider.killBackgroundProcessInstalledApps()

    override suspend fun killBackgroundProcessSystemApps() =
        realBatteryProvider.killBackgroundProcessSystemApps()

    override fun getBatteryType(): String = sharedPreferencesProvider.getBatteryType()

    override fun saveBatteryType(type: String) = sharedPreferencesProvider.saveBatteryType(type)

}