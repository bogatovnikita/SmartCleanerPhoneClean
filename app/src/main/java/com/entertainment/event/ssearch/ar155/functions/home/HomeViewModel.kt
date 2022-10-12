package com.entertainment.event.ssearch.ar155.functions.home

import androidx.lifecycle.viewModelScope
import com.entertainment.event.ssearch.ar155.R
import com.entertainment.event.ssearch.ar155.adapters.ItemHomeFun
import com.entertainment.event.ssearch.ar155.adapters.ItemHomeFun.Companion.BATTERY
import com.entertainment.event.ssearch.ar155.adapters.ItemHomeFun.Companion.BOOST
import com.entertainment.event.ssearch.ar155.adapters.ItemHomeFun.Companion.CLEAN
import com.entertainment.event.ssearch.ar155.adapters.ItemHomeFun.Companion.COOLING
import com.entertainment.event.ssearch.domain.battery.BatteryUseCase
import com.entertainment.event.ssearch.domain.boost.BoostUseCase
import com.entertainment.event.ssearch.domain.clean.CleanUseCase
import com.entertainment.event.ssearch.domain.cooling.CoolingUseCase
import com.entertainment.event.ssearch.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val batteryUseCase: BatteryUseCase,
    private val coolingUseCase: CoolingUseCase,
    private val cleanUseCase: CleanUseCase,
    private val boostUseCase: BoostUseCase,
) : BaseViewModel<HomeScreenState>(HomeScreenState()) {

    fun getParams() {
        viewModelScope.launch(Dispatchers.IO) {
            val usedRam = toGb(boostUseCase.getRamUsage())
            val totalRam = toGb(boostUseCase.getTotalRam())
            val freeRam = totalRam - usedRam
            val usedMemory = toGb(cleanUseCase.getUsedSizeMemory())
            val totalMemory = toGb(cleanUseCase.getTotalSizeMemory())
            val freeMemory = totalMemory - usedMemory
            updateState {
                it.copy(
                    usedRam = usedRam,
                    totalRam = totalRam,
                    freeRam = freeRam,
                    ramPercent = 100 - (freeRam * 100 / totalRam).toInt(),
                    totalMemory = totalMemory,
                    usedMemory = usedMemory,
                    freeMemory = freeMemory,
                    memoryPercent = (100 - (freeMemory * 100 / totalMemory)).toInt(),
                    funList = generateFunList()
                    )
            }
        }
    }

    private fun generateFunList(): List<ItemHomeFun> = listOf(
        ItemHomeFun(
            isOptimized = boostUseCase.checkRamOverload(),
            funName = R.string.boosting,
            funDangerDescription = R.string.boost_danger_desc,
            icon = if (boostUseCase.checkRamOverload())
                R.drawable.ic_rocket_danger_off
            else
                R.drawable.ic_rocket_danger,
            type = BOOST,
            btnText = R.string.boost
        ),
        ItemHomeFun(
            isOptimized = batteryUseCase.checkBatteryDecrease(),
            funName = R.string.battery_title,
            funDangerDescription = R.string.battery_danger_desc,
            icon = if (batteryUseCase.checkBatteryDecrease())
                R.drawable.ic_battery_danger_off
            else
                R.drawable.ic_battery_danger,
            type = BATTERY,
            btnText = R.string.battery_boost
        ),
        ItemHomeFun(
            isOptimized = coolingUseCase.getCoolingDone(),
            funName = R.string.cooling_process,
            funDangerDescription = R.string.cooling_danger_desc,
            icon = if (coolingUseCase.getCoolingDone())
                R.drawable.ic_cooling_danger_off
            else
                R.drawable.ic_cooling_danger,
            type = COOLING,
            btnText = R.string.cooling
        ),
        ItemHomeFun(
            isOptimized = cleanUseCase.isGarbageCleared(),
            funName = R.string.cleaning,
            funDangerDescription = R.string.clean_danger_desc,
            icon = if (cleanUseCase.isGarbageCleared())
                R.drawable.ic_clean_danger_off
            else
                R.drawable.ic_clean_danger,
            type = CLEAN,
            btnText = R.string.clean
        )
    )

    private fun toGb(size: Long): Double = size / 1024.0 / 1024.0 / 1024

}