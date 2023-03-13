package com.softcleean.fastcleaner.domain.cooling

interface CoolingRepository {

    fun calculateTemperature(temperature: Int): Int

    fun getCoolingDone(): Boolean

    fun cpu()

}