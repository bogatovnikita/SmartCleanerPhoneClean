package com.smart.cleaner.phone.clean.ui.battery

import androidx.lifecycle.viewModelScope
import com.softcleean.fastcleaner.domain.battery.BatteryUseCase
import com.smart.cleaner.phone.clean.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BatteryViewModel @Inject constructor(
    private val batteryUseCase: BatteryUseCase,
) : BaseViewModel<BatteryStateScreen>(BatteryStateScreen()) {

    fun getParams() {
        getIsBoostedBattery()
    }

    private fun getIsBoostedBattery() {
        viewModelScope.launch {
            updateState {
                it.copy(
                    isBoostedBattery = batteryUseCase.isBatteryBoosted(),
                    batterySaveType = batteryUseCase.getBatteryType()
                )
            }
        }
    }

    fun boostBattery() {
        viewModelScope.launch {
            batteryUseCase.saveBatteryType(screenState.value.currentBatteryType)
            batteryUseCase.saveTimeBatteryBoost()
            updateState {
                it.copy(
                    isNeedShowBtnSetBrightness = true
                )
            }
        }
    }

    fun setCurrentBatteryType(type: String) {
        updateState {
            it.copy(
                currentBatteryType = type
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

    fun setBrightnessTo80() {
        batteryUseCase.setScreenBrightness(204)
        updateState {
            it.copy(
                isNeedShowBtnSetBrightness = false
            )
        }
    }

}