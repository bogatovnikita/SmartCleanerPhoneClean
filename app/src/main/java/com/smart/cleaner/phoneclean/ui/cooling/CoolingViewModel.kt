package com.smart.cleaner.phoneclean.ui.cooling

import androidx.lifecycle.viewModelScope
import com.softcleean.fastcleaner.domain.battery.BatteryUseCase
import com.softcleean.fastcleaner.domain.cooling.CoolingUseCase
import com.smart.cleaner.phoneclean.ui.base.BaseViewModel
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