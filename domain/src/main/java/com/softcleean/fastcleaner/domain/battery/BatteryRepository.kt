package com.softcleean.fastcleaner.domain.battery

import kotlinx.coroutines.flow.Flow

interface BatteryRepository {

    fun checkBatteryDecrease(): Boolean

    fun calculateWorkingTime(percent: Int): Int

    fun savePowerLowType()

    fun savePowerMediumType()

    fun savePowerHighType()

    fun getBatteryType(): String

    fun saveBatteryType(type: String)

    fun setScreenBrightness(value: Int)

    fun getBatteryTemperature(): Flow<Int>

    fun getBatteryPercent(): Flow<Int>

}