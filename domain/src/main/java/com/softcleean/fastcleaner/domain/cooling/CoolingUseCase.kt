package com.softcleean.fastcleaner.domain.cooling

import javax.inject.Inject

class CoolingUseCase @Inject constructor(
    private val coolingRepository: CoolingRepository
) {

    fun calculateTemperature(temperature: Int): Int = coolingRepository.calculateTemperature(temperature)

    fun getCoolingDone(): Boolean = coolingRepository.getCoolingDone()

    fun cpu() = coolingRepository.cpu()

}