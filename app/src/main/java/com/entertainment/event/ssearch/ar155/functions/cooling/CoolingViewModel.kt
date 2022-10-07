package com.entertainment.event.ssearch.ar155.functions.cooling

import androidx.lifecycle.viewModelScope
import com.entertainment.event.ssearch.domain.battery.BatteryUseCase
import com.entertainment.event.ssearch.domain.cooling.CoolingUseCase
import com.entertainment.event.ssearch.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoolingViewModel @Inject constructor(
    private val coolingUseCase: CoolingUseCase,
    private val batteryUseCase: BatteryUseCase,
): BaseViewModel<CoolingScreenState>(CoolingScreenState()) {

    fun getTemperature() {
        viewModelScope.launch(Dispatchers.Default) {
            batteryUseCase.getBatteryTemperature().collect { temperature ->
                updateState {
                    it.copy(
                        temperature = coolingUseCase.calculateTemperature(temperature),
                        isCoolingDone = coolingUseCase.getCoolingDone()
                    )
                }
            }
        }
    }

    fun cooling() {
        coolingUseCase.cpu()
    }

}