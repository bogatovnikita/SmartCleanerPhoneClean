package com.softcleean.fastcleaner.data.boost_provider

import com.softcleean.fastcleaner.data.kill_background.KillBackgroundProvider
import com.softcleean.fastcleaner.domain.boost.BoostRepository
import javax.inject.Inject

class BoostRepositoryImpl @Inject constructor(
    private val boostRealProvider: BoostRealProvider,
    private val killBackgroundProvider: KillBackgroundProvider,
) : BoostRepository {

    override fun getTotalRam(): Long = boostRealProvider.getRamTotal()

    override fun getRamUsage(): Long = boostRealProvider.getRamUsage()

    override fun boost() = boostRealProvider.boost()

    override fun isRamBoosted(): Boolean = boostRealProvider.isRamBoosted()

    override suspend fun killBackgroundProcessInstalledApps() =
        killBackgroundProvider.killBackgroundProcessInstalledApps()


    override suspend fun killBackgroundProcessSystemApps() =
        killBackgroundProvider.killBackgroundProcessSystemApps()
}