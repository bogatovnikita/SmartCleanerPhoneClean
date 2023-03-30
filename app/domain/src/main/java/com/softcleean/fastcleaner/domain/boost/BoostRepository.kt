package com.softcleean.fastcleaner.domain.boost

interface BoostRepository {

    fun getTotalRam(): Long

    fun getRamUsage(): Long

    fun saveTimeRamBoost()

    fun isRamBoosted(): Boolean

    suspend fun killBackgroundProcessInstalledApps()

    suspend fun killBackgroundProcessSystemApps()

}