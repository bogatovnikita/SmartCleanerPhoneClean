package com.softcleean.fastcleaner.domain.boost

import javax.inject.Inject

class BoostUseCase @Inject constructor(
    private val boostRepository: BoostRepository
) {

    fun getTotalRam(): Long = boostRepository.getTotalRam()

    fun getRamUsage(): Long = boostRepository.getRamUsage()

    fun saveTimeRamBoost() = boostRepository.saveTimeRamBoost()

    fun isRamBoosted(): Boolean = boostRepository.isRamBoosted()

    suspend fun killBackgroundProcessInstalledApps() =
        boostRepository.killBackgroundProcessInstalledApps()

    suspend fun killBackgroundProcessSystemApps() =
        boostRepository.killBackgroundProcessSystemApps()
}