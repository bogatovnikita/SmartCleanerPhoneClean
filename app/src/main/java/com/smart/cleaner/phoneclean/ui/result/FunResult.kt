package com.smart.cleaner.phoneclean.ui.result

import com.smart.cleaner.phoneclean.utils.OptimizingType

data class FunResult(
    val isOptimized: Boolean = false,
    val funName: Int = 0,
    val funDangerDescription: Int = 0,
    val icon: Int = 0,
    val type: OptimizingType = OptimizingType.Boost,
)