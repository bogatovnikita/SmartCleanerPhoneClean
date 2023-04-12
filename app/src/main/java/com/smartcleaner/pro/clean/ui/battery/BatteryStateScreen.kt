package com.smartcleaner.pro.clean.ui.battery

import com.smartcleaner.pro.clean.custom.ChoosingTypeBatteryBar.Companion.NORMAL

data class BatteryStateScreen(
    val isBoostedBattery: Boolean = false,
    val batterySaveType: String = NORMAL,
    val isCanWriteSettings: Boolean = false,
    val hasBluetoothPerm: Boolean = false,
    val currentBatteryType: String = NORMAL
)