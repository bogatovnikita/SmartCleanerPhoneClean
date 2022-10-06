package com.entertainment.event.ssearch.domain.boost

interface BoostRepository {

    fun getTotalRam(): Long

    fun getRamUsage(): Long

    fun calculatePercentAvail(): Int

    fun getOverloadedPercents(): Int

    fun boost()

    fun checkRamOverload(): Boolean

}