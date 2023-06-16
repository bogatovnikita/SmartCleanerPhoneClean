package com.smart.cleaner.phone.clean.ui.battery

import com.smart.cleaner.phone.clean.custom.ChoosingTypeBatteryBar.Companion.NORMAL

data class BatteryStateScreen(
    val isBoostedBattery: Boolean = false,
    val batterySaveType: String = NORMAL,
    val isCanWriteSettings: Boolean = false,
    val hasBluetoothPerm: Boolean = false,
    val currentBatteryType: String = NORMAL,
    val isNeedShowBtnSetBrightness: Boolean = false,
)