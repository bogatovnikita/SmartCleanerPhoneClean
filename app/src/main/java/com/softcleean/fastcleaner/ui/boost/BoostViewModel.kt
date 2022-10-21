package com.softcleean.fastcleaner.ui.boost

import androidx.lifecycle.viewModelScope
import com.softcleean.fastcleaner.domain.boost.BoostUseCase
import com.softcleean.fastcleaner.ssearch.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoostViewModel @Inject constructor(
    private val boostUseCase: BoostUseCase
): BaseViewModel<BoostStateScreen>(BoostStateScreen()) {

    fun getParams() {
        viewModelScope.launch(Dispatchers.Default) {
            val totalRam = boostUseCase.getTotalRam() / 1024.0 / 1024.0 / 1024.0
            val usedRam = boostUseCase.getRamUsage() / 1024.0 / 1024.0 / 1024.0
            val overloadedPercents = boostUseCase.getOverloadedPercents()
            updateState {
                it.copy(
                    totalRam = totalRam,
                    usedRam = usedRam,
                    boostPercent = 100 - ((totalRam - usedRam) * 100 / totalRam).toInt(),
                    isBoosted = boostUseCase.checkRamOverload(),
                    overloadPercents = overloadedPercents,
                    freeRam = totalRam * overloadedPercents / 100.0
                )
            }
        }
    }

    fun boost() {
        viewModelScope.launch {
            boostUseCase.boost()
        }
    }

}