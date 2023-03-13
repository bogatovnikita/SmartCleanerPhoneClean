package com.smart.cleaner.phoneclean.ui.boost

import androidx.lifecycle.viewModelScope
import com.softcleean.fastcleaner.domain.battery.BatteryUseCase
import com.softcleean.fastcleaner.domain.boost.BoostUseCase
import com.softcleean.fastcleaner.domain.clean.CleanUseCase
import com.softcleean.fastcleaner.domain.cooling.CoolingUseCase
import com.smart.cleaner.phoneclean.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoostViewModel @Inject constructor(
    private val batteryUseCase: BatteryUseCase,
    private val coolingUseCase: CoolingUseCase,
    private val cleanUseCase: CleanUseCase,
    private val boostUseCase: BoostUseCase,
) : BaseViewModel<BoostScreenState>(BoostScreenState()) {

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
                    isMemoryBoosted = cleanUseCase.isGarbageCleared(),
                    isRamBoosted = boostUseCase.checkRamOverload(),
                    overloadPercent = boostUseCase.getOverloadedPercents()
                    )
            }
        }
    }

    private fun toGb(size: Long): Double = size / 1024.0 / 1024.0 / 1024

    fun boost() {
        viewModelScope.launch {
            boostUseCase.boost()
        }
    }

}