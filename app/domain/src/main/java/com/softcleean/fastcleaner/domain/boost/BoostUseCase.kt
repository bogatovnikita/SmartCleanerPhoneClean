package com.softcleean.fastcleaner.domain.boost

import javax.inject.Inject

class BoostUseCase @Inject constructor(
    private val boostRepository: BoostRepository
) {

    fun getTotalRam(): Long = boostRepository.getTotalRam()

    fun getRamUsage(): Long = boostRepository.getRamUsage()

    fun boost() = boostRepository.boost()

    fun checkRamOverload(): Boolean = boostRepository.checkRamOverload()

    suspend fun killBackgroundProcessInstalledApps() =
        boostRepository.killBackgroundProcessInstalledApps()

    suspend fun killBackgroundProcessSystemApps() =
        boostRepository.killBackgroundProcessSystemApps()
}