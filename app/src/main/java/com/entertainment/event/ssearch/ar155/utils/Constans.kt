package com.entertainment.event.ssearch.ar155.utils

sealed class OptimizingType() {
    object Clean: OptimizingType()
    object Boost: OptimizingType()
    object Battery: OptimizingType()
    object Cooling: OptimizingType()
}