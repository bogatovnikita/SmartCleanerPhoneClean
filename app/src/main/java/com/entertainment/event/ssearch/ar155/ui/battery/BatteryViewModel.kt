package com.entertainment.event.ssearch.ar155.ui.battery

import androidx.lifecycle.viewModelScope
import com.entertainment.event.ssearch.ar155.ui.custom.ChoosingTypeBatteryBar
import com.entertainment.event.ssearch.ar155.ui.custom.ChoosingTypeBatteryBar.Companion.EXTRA
import com.entertainment.event.ssearch.ar155.ui.custom.ChoosingTypeBatteryBar.Companion.NORMAL
import com.entertainment.event.ssearch.ar155.ui.custom.ChoosingTypeBatteryBar.Companion.ULTRA
import com.entertainment.event.ssearch.domain.battery.BatteryUseCase
import com.entertainment.event.ssearch.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BatteryViewModel @Inject constructor(
    private val batteryUseCase: BatteryUseCase,
) : BaseViewModel<BatteryStateScreen>(BatteryStateScreen()) {

    init {
        getBatteryParams()
    }

    private fun getBatteryParams() {
        viewModelScope.launch(Dispatchers.Default) {
            batteryUseCase.getBatteryPercent().collect { percent ->
                calculateRemainingBatteryLife(percent)
                updateState {
                    it.copy(
                        batteryPercents = percent,
                    )
                }
            }
                updateState {
                    it.copy(
                        batterySaveType = batteryUseCase.getBatteryType(),
                        isBoostedBattery = batteryUseCase.checkBatteryDecrease()
                    )
                }
        }
    }

    private fun calculateRemainingBatteryLife(percent: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            val time = batteryUseCase.calculateWorkingTime(percent)
            updateState {
                it.copy(
                    batteryWorkingTime = arrayOf(time / 60, time % 60)
                )
            }
        }
    }

    fun boostBattery() {
        viewModelScope.launch {
            batteryUseCase.saveBatteryType(screenState.value.batterySaveType)
            when (screenState.value.batterySaveType) {
                NORMAL -> batteryUseCase.savePowerLowType()
                ULTRA -> batteryUseCase.savePowerMediumType()
                EXTRA -> batteryUseCase.savePowerHighType()
            }
        }
    }

    fun modePercentBoost(): Int {
        return when (batteryUseCase.getBatteryType()) {
            NORMAL -> 10
            ULTRA -> 30
            EXTRA -> 60
            else -> {10}
        }
    }

    fun setBatterySaveType(type: String) {
        updateState {
            it.copy(
                batterySaveType = type
            )
        }
    }

}