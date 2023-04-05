package com.softcleean.fastcleaner.data.boost_provider

import com.softcleean.fastcleaner.data.background_apps.BackgroundApps
import com.softcleean.fastcleaner.data.kill_background.KillBackgroundProvider
import com.softcleean.fastcleaner.domain.boost.BoostRepository
import com.softcleean.fastcleaner.domain.models.BackgroundApp
import javax.inject.Inject

class BoostRepositoryImpl @Inject constructor(
    private val boostRealProvider: BoostRealProvider,
    private val killBackgroundProvider: KillBackgroundProvider,
    private val backgroundApps: BackgroundApps
) : BoostRepository {

    override fun getTotalRam(): Long = boostRealProvider.getRamTotal()

    override fun getRamUsage(): Long = boostRealProvider.getRamUsage()

    override fun saveTimeRamBoost() = boostRealProvider.saveTimeRamBoost()

    override fun isRamBoosted(): Boolean = boostRealProvider.isRamBoosted()

    override suspend fun killBackgroundProcessInstalledApps() =
        killBackgroundProvider.killBackgroundProcessInstalledApps()


    override suspend fun killBackgroundProcessSystemApps() =
        killBackgroundProvider.killBackgroundProcessSystemApps()

    override fun getRunningApps(): List<BackgroundApp> = backgroundApps.getRunningApps()

}