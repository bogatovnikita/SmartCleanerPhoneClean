package com.softcleean.fastcleaner.domain.boost

import com.softcleean.fastcleaner.domain.models.BackgroundApp
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoostUseCaseImpl @Inject constructor(
    private val boostRepository: BoostRepository
): BoostUseCase {

    private var apps = emptyList<BackgroundApp>()

    override fun getCachedApps(): List<BackgroundApp> = apps

    override fun getTotalRam(): Long = boostRepository.getTotalRam()

    override fun getRamUsage(): Long = boostRepository.getRamUsage()

    override fun saveTimeRamBoost() = boostRepository.saveTimeRamBoost()

    override fun isRamBoosted(): Boolean = boostRepository.isRamBoosted()

    override suspend fun killBackgroundProcessInstalledApps() =
        boostRepository.killBackgroundProcessInstalledApps()

    override suspend fun killBackgroundProcessSystemApps() =
        boostRepository.killBackgroundProcessSystemApps()

    override fun getRunningApps(): List<BackgroundApp>{
        val newList =  boostRepository.getRunningApps()
        apps = newList
        return newList
    }

}