package com.softcleean.fastcleaner.domain.boost

import com.softcleean.fastcleaner.domain.models.BackgroundApp

interface BoostUseCase {

    fun getCachedApps(): List<BackgroundApp>

    fun getTotalRam(): Long

    fun getRamUsage(): Long

    fun saveTimeRamBoost()

    fun isRamBoosted(): Boolean

    suspend fun killBackgroundProcessInstalledApps()

    suspend fun killBackgroundProcessSystemApps()

    fun getRunningApps(): List<BackgroundApp>

}