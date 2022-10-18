package com.softcleean.fastcleaner.utils

sealed class OptimizingType() {
    object Clean: OptimizingType()
    object Boost: OptimizingType()
    object Battery: OptimizingType()
    object Cooling: OptimizingType()
}