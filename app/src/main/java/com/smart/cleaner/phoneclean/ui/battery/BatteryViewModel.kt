package com.smart.cleaner.phoneclean.ui.battery

import androidx.lifecycle.viewModelScope
import com.softcleean.fastcleaner.domain.battery.BatteryUseCase
import com.smart.cleaner.phoneclean.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BatteryViewModel @Inject constructor(
    private val batteryUseCase: BatteryUseCase,
) : BaseViewModel<BatteryStateScreen>(BatteryStateScreen()) {

    fun getParams() {
        getIsBoostedBattery()
        getBatterySaveType()
    }

    private fun getIsBoostedBattery() {
        viewModelScope.launch {
            updateState {
                it.copy(
                    isBoostedBattery = batteryUseCase.checkBatteryDecrease()
                )
            }
        }
    }

    private fun getBatterySaveType() {
        viewModelScope.launch {
            updateState {
                it.copy(
                    batterySaveType = batteryUseCase.getBatteryType()
                )
            }
        }
    }

    fun boostBattery() {
        viewModelScope.launch {
            batteryUseCase.saveBatteryType(screenState.value.batterySaveType)
            batteryUseCase.saveTimeBatteryBoost()
        }
    }

    fun setBatterySaveType(type: String) {
        updateState {
            it.copy(
                batterySaveType = type
            )
        }
    }

    fun setWriteSettingsEnabling(isCanWrite: Boolean) {
        updateState {
            it.copy(
                isCanWriteSettings = isCanWrite
            )
        }
    }

    fun setHasBluetoothPerm(hasBluetoothPerm: Boolean) {
        updateState {
            it.copy(
                hasBluetoothPerm = hasBluetoothPerm
            )
        }
    }

}