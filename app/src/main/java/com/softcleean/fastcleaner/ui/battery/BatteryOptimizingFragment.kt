package com.softcleean.fastcleaner.ui.battery

import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.custom.ChoosingTypeBatteryBar
import com.softcleean.fastcleaner.domain.battery.BatteryUseCase
import com.softcleean.fastcleaner.ui.base.BaseOptimizingFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BatteryOptimizingFragment(
//    override val keyInter: String = BuildConfig.ADMOB_INTERSTITIAL3,
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