package com.entertainment.event.ssearch.domain.cooling

interface CoolingRepository {

    fun calculateTemperature(temperature: Int): Int

    fun getCoolingDone(): Boolean

    fun cpu()

}