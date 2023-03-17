package com.softcleean.fastcleaner.data.battery_provider

import android.app.Application
import android.content.Context
import com.softcleean.fastcleaner.domain.battery.BatteryRepository
import java.lang.ref.WeakReference
import javax.inject.Inject

class BatteryRepositoryImpl @Inject constructor(
    private val application: Application,
    private val realBatteryProvider: RealBatteryProvider,
): BatteryRepository {

    private val contextWeakRef = WeakReference(application.applicationContext)
    private val context: Context
        get() = contextWeakRef.get()!!

    override fun checkBatteryDecrease(): Boolean = BatteryPrefsProvider.checkBatteryDecrease(context)

    override fun savePowerLowType() = BatteryPrefsProvider.savePowerLowType(context)

    override fun savePowerMediumType() = BatteryPrefsProvider.savePowerMediumType(context)

    override fun savePowerHighType() = BatteryPrefsProvider.savePowerHighType(context)

    override fun getBatteryType(): String = BatteryPrefsProvider.getBatteryType()

    override fun saveBatteryType(type: String) = BatteryPrefsProvider.saveBatteryType(type)

    override fun setScreenBrightness(value: Int) = realBatteryProvider.setScreenBrightness(value)

    override fun disableBluetooth() = realBatteryProvider.disableBluetooth(context)

    override fun disableWiFi() = realBatteryProvider.disableWiFi()

    override suspend fun killBackgroundProcessInstalledApps() = realBatteryProvider.killBackgroundProcessInstalledApps()

    override suspend fun killBackgroundProcessSystemApps() = realBatteryProvider.killBackgroundProcessSystemApps()

}