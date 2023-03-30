package com.smart.cleaner.phoneclean.ui_core.adapters.models

sealed class OptimizingType() {
    object Clean: OptimizingType()
    object Boost: OptimizingType()
    object Battery: OptimizingType()
    object Duplicates: OptimizingType()
}