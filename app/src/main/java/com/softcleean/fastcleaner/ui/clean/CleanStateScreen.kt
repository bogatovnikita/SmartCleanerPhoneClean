package com.softcleean.fastcleaner.ui.clean

import com.softcleean.fastcleaner.adapters.ItemGarbage

data class CleanStateScreen(
    val isCleared: Boolean = false,
    val totalGarbageSize: Int = 0,
    val garbageList: List<ItemGarbage> = emptyList(),
    val usedMemory: Double = 0.0,
    val totalMemory: Double = 0.0,
    val freeMemory: Double = 0.0,
    val memoryPercent: Int = 60,
)
