package com.smart.cleaner.phoneclean.ui.boost

import androidx.lifecycle.viewModelScope
import com.softcleean.fastcleaner.domain.boost.BoostUseCase
import com.smart.cleaner.phoneclean.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoostViewModel @Inject constructor(
    private val boostUseCase: BoostUseCase,
) : BaseViewModel<BoostScreenState>(BoostScreenState()) {

    fun getParams() {
        viewModelScope.launch(Dispatchers.IO) {
            val usedRam = toGb(boostUseCase.getRamUsage())
            val totalRam = toGb(boostUseCase.getTotalRam())
            val freeRam = totalRam - usedRam
            updateState {
                it.copy(
                    usedRam = usedRam,
                    totalRam = totalRam,
                    freeRam = freeRam,
                    ramPercent = 100 - (freeRam * 100 / totalRam).toInt(),
                    isRamBoosted = boostUseCase.isRamBoosted(),
                )
            }
        }
    }

    private fun toGb(size: Long): Double = size / 1000.0 / 1000.0 / 1000
}