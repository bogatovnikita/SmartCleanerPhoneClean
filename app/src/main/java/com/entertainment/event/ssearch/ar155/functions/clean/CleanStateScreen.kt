package com.entertainment.event.ssearch.ar155.functions.clean

import com.entertainment.event.ssearch.ar155.adapters.ItemGarbage

data class CleanStateScreen(
    val isCleared: Boolean = false,
    val totalGarbageSize: Int = 0,
    val garbageList: List<ItemGarbage> = emptyList(),
    val totalSizeMemory: Long = 0,
    val usedSizeMemory: Long = 0,
)
