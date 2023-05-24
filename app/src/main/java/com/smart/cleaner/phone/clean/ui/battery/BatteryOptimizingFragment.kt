package com.smart.cleaner.phone.clean.ui.battery

import androidx.lifecycle.lifecycleScope
import com.smart.cleaner.phone.clean.R
import com.smart.cleaner.phoneclean.ui_core.adapters.base.BaseOptimizingFragment
import com.smart.cleaner.phoneclean.ui_core.adapters.models.GeneralOptimizingItem
import com.smart.cleaner.phoneclean.ui_core.adapters.models.OptimizingItem
import com.smart.cleaner.phone.clean.custom.ChoosingTypeBatteryBar
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
            ChoosingTypeBatteryBar.NORMAL -> toGeneralOptimizingItemList(resources.getStringArray(R.array.battery_normal))
            ChoosingTypeBatteryBar.ULTRA -> toGeneralOptimizingItemList(resources.getStringArray(R.array.battery_ultra))
            ChoosingTypeBatteryBar.EXTRA -> toGeneralOptimizingItemList(resources.getStringArray(R.array.battery_extra))
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

    override fun getFunName(): String = requireContext().getString(R.string.activation)

    private fun toGeneralOptimizingItemList(list: Array<String>): MutableList<OptimizingItem> {
        val newList = mutableListOf<OptimizingItem>()
        list.forEach {
            newList.add(GeneralOptimizingItem(it))
        }
        return newList
    }

}