package com.smart.cleaner.phone.clean.ui.boost

import com.smart.cleaner.phone.clean.models.BackgroundApp

data class BoostScreenState(
    val usedRam: Double = 0.0,
    val totalRam: Double = 0.0,
    val isRamBoosted: Boolean = false,
    val permissionGiven: Boolean = false,
    val listBackgroundApp: List<BackgroundApp> = emptyList(),
    val isLoadUseCase: Boolean = false
)