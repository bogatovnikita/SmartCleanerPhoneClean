package com.smart.cleaner.phoneclean.ui_core.adapters.models


data class FunResult(
    val isOptimized: Boolean = false,
    val funName: Int = 0,
    val funDangerDescription: Int = 0,
    val icon: Int = 0,
    val type: OptimizingType = OptimizingType.Boost,
)