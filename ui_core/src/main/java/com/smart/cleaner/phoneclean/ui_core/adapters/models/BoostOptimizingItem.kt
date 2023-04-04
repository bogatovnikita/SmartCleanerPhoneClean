package com.smart.cleaner.phoneclean.ui_core.adapters.models

import android.graphics.drawable.Drawable

data class BoostOptimizingItem(
    override val name: String,
    val icon: Drawable?,
) : OptimizingItem