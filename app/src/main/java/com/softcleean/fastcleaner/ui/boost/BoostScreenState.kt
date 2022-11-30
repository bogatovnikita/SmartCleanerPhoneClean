package com.softcleean.fastcleaner.ui.boost

data class BoostScreenState(
    val usedRam: Double = 0.0,
    val totalRam: Double = 0.0,
    val freeRam: Double = 0.0,
    val ramPercent: Int = 60,
    val isRamBoosted: Boolean = false,
    val usedMemory: Double = 0.0,
    val totalMemory: Double = 0.0,
    val freeMemory: Double = 0.0,
    val memoryPercent: Int = 60,
    val isMemoryBoosted: Boolean = false,
    val overloadPercent: Int = 0,
)