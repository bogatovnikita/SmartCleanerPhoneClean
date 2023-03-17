package com.smart.cleaner.phoneclean.ui.battery

import androidx.lifecycle.lifecycleScope
import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.custom.ChoosingTypeBatteryBar
import com.smart.cleaner.phoneclean.ui.base.BaseOptimizingFragment
import com.softcleean.fastcleaner.domain.battery.BatteryUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BatteryOptimizingFragment(
    override val nextScreenId: Int = R.id.action_batteryOptimizingFragment_to_batteryResultFragment
) : BaseOptimizingFragment() {

    @Inject
    lateinit var batteryUseCase: BatteryUseCase

    override fun getArrayOptimization() {
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
                batteryUseCase.setScreenBrightness(77)
            }
            ChoosingTypeBatteryBar.ULTRA -> {
                batteryUseCase.setScreenBrightness(51)
                killBackgroundProcess()
            }
            ChoosingTypeBatteryBar.EXTRA -> {
                batteryUseCase.setScreenBrightness(26)
                batteryUseCase.disableWiFi()
                batteryUseCase.disableBluetooth()
                killBackgroundProcess()
            }
        }
    }

    private fun killBackgroundProcess() {
        lifecycleScope.launch(Dispatchers.IO) {
            batteryUseCase.killBackgroundProcessInstalledApps()
        }
        lifecycleScope.launch(Dispatchers.IO) {
            batteryUseCase.killBackgroundProcessSystemApps()
        }
    }

    override fun getFunName(): String = requireContext().getString(R.string.optimization)

}