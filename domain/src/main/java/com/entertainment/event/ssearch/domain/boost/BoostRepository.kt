package com.entertainment.event.ssearch.domain.boost

interface BoostRepository {

    fun getTotalRam(): Long

    fun getRamUsage(): Long

    fun getOverloadedPercents(): Int

    fun boost()

    fun checkRamOverload(): Boolean

}