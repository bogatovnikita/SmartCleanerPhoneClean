package com.entertainment.event.ssearch.ar155.functions.battery

import com.entertainment.event.ssearch.ar155.functions.custom.ChoosingTypeBatteryBar.Companion.NORMAL

data class BatteryStateScreen(
    val batteryPercents: Int = 80,
    val batteryWorkingTime: Array<Int> = arrayOf(8, 30),
    val isBoostedBattery: Boolean = false,
    val batterySaveType: String = NORMAL,
)