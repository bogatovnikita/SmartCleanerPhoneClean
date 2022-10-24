package com.softcleean.fastcleaner.ui.battery

import com.softcleean.fastcleaner.custom.ChoosingTypeBatteryBar.Companion.NORMAL

data class BatteryStateScreen(
    val batteryPercents: Int = 80,
    val batteryWorkingTime: Array<Int> = arrayOf(8, 30),
    val isBoostedBattery: Boolean = false,
    val batterySaveType: String = NORMAL,
    val isCanWriteSettings: Boolean = false,
    val hasBluetoothPerm: Boolean = false,
)