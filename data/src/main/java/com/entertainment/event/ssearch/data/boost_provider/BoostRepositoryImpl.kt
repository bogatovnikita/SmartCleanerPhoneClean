package com.entertainment.event.ssearch.data.boost_provider

import android.app.Application
import com.entertainment.event.ssearch.domain.boost.BoostRepository
import javax.inject.Inject


class BoostRepositoryImpl @Inject constructor(
    private val application: Application
) : BoostRepository {

    override fun getTotalRam(): Long = BoostProvider.getRamTotal(application)

    override fun getRamUsage(): Long =
        BoostProvider.getRamUsage(
            application,
            BoostProvider.getRamTotal(application),
            BoostProvider.getRamPart(application)
        )

    override fun calculatePercentAvail(): Int = BoostProvider.calculatePercentAvail(application)

    override fun getOverloadedPercents(): Int = BoostProvider.getOverloadedPercents(application)

    override fun boost() = BoostProvider.boost(application)

    override fun checkRamOverload(): Boolean = BoostProvider.checkRamOverload(application)

}