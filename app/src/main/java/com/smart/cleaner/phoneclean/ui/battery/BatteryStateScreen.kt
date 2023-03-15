package com.smart.cleaner.phoneclean.ui.battery

import com.smart.cleaner.phoneclean.custom.ChoosingTypeBatteryBar.Companion.NORMAL

data class BatteryStateScreen(
    val isBoostedBattery: Boolean = false,
    val batterySaveType: String = NORMAL,
    val isCanWriteSettings: Boolean = false,
    val hasBluetoothPerm: Boolean = false,
)