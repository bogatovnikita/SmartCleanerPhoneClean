package com.entertainment.event.ssearch.ar155.functions.boost

data class BoostStateScreen(
    val usedRam: Double = 3.4,
    val totalRam: Double = 4.0,
    val boostPercent: Int = 60,
    val isBoosted: Boolean = false,
    val overloadPercents: Int = 0,
    val freedRam: Double = 0.0,
)