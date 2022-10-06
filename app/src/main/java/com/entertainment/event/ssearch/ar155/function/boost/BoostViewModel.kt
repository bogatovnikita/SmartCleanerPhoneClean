package com.entertainment.event.ssearch.ar155.function.boost

import androidx.lifecycle.viewModelScope
import com.entertainment.event.ssearch.domain.boost.BoostUseCase
import com.entertainment.event.ssearch.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoostViewModel @Inject constructor(
    private val boostUseCase: BoostUseCase
): BaseViewModel<BoostStateScreen>(BoostStateScreen()) {

    init {
        getParams()
    }

    private fun getParams() {
        viewModelScope.launch(Dispatchers.Default) {
            updateState {
                it.copy(
                    totalRam = boostUseCase.getTotalRam() / 1024.0 / 1024.0 / 1024.0,
                    usedRam = boostUseCase.getRamUsage() / 1024.0 / 1024.0 / 1024.0,
                    boostPercent = boostUseCase.calculatePercentAvail(),
                    isBoosted = boostUseCase.checkRamOverload(),
                    boostingPercent = boostUseCase.getOverloadedPercents()
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