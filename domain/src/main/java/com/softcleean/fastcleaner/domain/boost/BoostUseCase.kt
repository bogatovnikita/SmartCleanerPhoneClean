package com.softcleean.fastcleaner.domain.boost

import javax.inject.Inject

class BoostUseCase @Inject constructor(
    private val boostRepository: BoostRepository
) {

    fun getTotalRam(): Long = boostRepository.getTotalRam()

    fun getRamUsage(): Long = boostRepository.getRamUsage()

    fun getOverloadedPercents(): Int = boostRepository.getOverloadedPercents()

    fun boost() = boostRepository.boost()

    fun checkRamOverload(): Boolean = boostRepository.checkRamOverload()

}