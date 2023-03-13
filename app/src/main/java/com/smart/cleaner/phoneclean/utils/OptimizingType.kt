package com.smart.cleaner.phoneclean.utils

sealed class OptimizingType() {
    object Clean: OptimizingType()
    object Boost: OptimizingType()
    object Battery: OptimizingType()
    object Cooling: OptimizingType()
}