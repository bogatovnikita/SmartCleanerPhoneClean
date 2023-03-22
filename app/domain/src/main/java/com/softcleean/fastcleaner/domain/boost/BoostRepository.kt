package com.softcleean.fastcleaner.domain.boost

interface BoostRepository {

    fun getTotalRam(): Long

    fun getRamUsage(): Long

    fun boost()

    fun checkRamOverload(): Boolean

    suspend fun killBackgroundProcessInstalledApps()

    suspend fun killBackgroundProcessSystemApps()

}