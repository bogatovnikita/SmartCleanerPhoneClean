package com.softcleean.fastcleaner.functions.home

import com.softcleean.fastcleaner.adapters.ItemHomeFun

data class HomeScreenState(
    val usedRam: Double = 0.0,
    val totalRam: Double = 0.0,
    val freeRam: Double = 0.0,
    val ramPercent: Int = 60,
    val usedMemory: Double = 0.0,
    val totalMemory: Double = 0.0,
    val freeMemory: Double = 0.0,
    val memoryPercent: Int = 60,
    val funList: List<ItemHomeFun> = emptyList(),
)