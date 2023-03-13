package com.smart.cleaner.phoneclean.ui.battery

import com.smart.cleaner.phoneclean.BuildConfig
import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.custom.ChoosingTypeBatteryBar
import com.smart.cleaner.phoneclean.ui.base.BaseOptimizingFragment
import com.softcleean.fastcleaner.domain.battery.BatteryUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BatteryOptimizingFragment(
    override val keyInter: String = BuildConfig.ADMOB_INTERSTITIAL3,
    override val nextScreenId: Int = R.id.action_batteryOptimizingFragment_to_batteryResultFragment
) : BaseOptimizingFragment() {

    @Inject
    lateinit var batteryUseCase: BatteryUseCase

    override fun setArrayOptimization() {
        listOptions = when (batteryUseCase.getBatteryType()) {
            ChoosingTypeBatteryBar.NORMAL -> resources.getStringArray(R.array.battery_normal)
                .toMutableList()
            ChoosingTypeBatteryBar.ULTRA -> resources.getStringArray(R.array.battery_ultra)
                .toMutableList()
            ChoosingTypeBatteryBar.EXTRA -> resources.getStringArray(R.array.battery_extra)
                .toMutableList()
            else -> mutableListOf()
        }
    }

    override fun startOptimizationFun() {
        when (batteryUseCase.getBatteryType()) {
            ChoosingTypeBatteryBar.NORMAL -> {
                batteryUseCase.setScreenBrightness(5)
                batteryUseCase.turnOffAutoBrightness()
            }
            ChoosingTypeBatteryBar.ULTRA -> {
                batteryUseCase.setScreenBrightness(5)
                batteryUseCase.turnOffAutoBrightness()
                batteryUseCase.disableWiFi()
            }
            ChoosingTypeBatteryBar.EXTRA -> {
                batteryUseCase.setScreenBrightness(5)
                batteryUseCase.turnOffAutoBrightness()
                batteryUseCase.disableWiFi()
                batteryUseCase.disableBluetooth()
            }
        }
    }

    override fun setFunName(): String = requireContext().getString(R.string.battery_title)

}