package com.softcleean.fastcleaner.utils

sealed class OptimizingType() {
    object Clean: OptimizingType()
    object Boost: OptimizingType()
    object Battery: OptimizingType()
    object Cooling: OptimizingType()
}

const val LOW_LEVEL = 85
const val MEDIUM_LEVEL = 65