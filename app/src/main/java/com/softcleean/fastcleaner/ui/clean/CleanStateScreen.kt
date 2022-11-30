package com.softcleean.fastcleaner.ui.clean

data class CleanStateScreen(
    val isCleared: Boolean = false,
    val totalGarbageSize: Int = 0,
    val usedMemory: Double = 0.0,
    val totalMemory: Double = 0.0,
    val freeMemory: Double = 0.0,
    val memoryPercent: Int = 60,
)
