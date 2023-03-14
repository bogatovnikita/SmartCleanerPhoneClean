package com.smart.cleaner.phoneclean.ui.result

import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.utils.OptimizingType
import com.softcleean.fastcleaner.domain.battery.BatteryUseCase
import com.softcleean.fastcleaner.domain.boost.BoostUseCase
import javax.inject.Inject

class ResultList @Inject constructor(
    private val batteryUseCase: BatteryUseCase,
    private val boostUseCase: BoostUseCase,
) {

    fun getList(): List<FunResult> = listOf(
        FunResult(
            isOptimized = boostUseCase.checkRamOverload(),
            funName = R.string.boosting,
            funDangerDescription = R.string.boost_danger_desc,
            icon = if (boostUseCase.checkRamOverload())
                R.drawable.ic_rocket_danger_off
            else
                R.drawable.ic_boost_danger,
            type = OptimizingType.Boost,
        ),
        FunResult(
            isOptimized = batteryUseCase.checkBatteryDecrease(),
            funName = R.string.battery_title,
            funDangerDescription = R.string.battery_danger_desc,
            icon = if (batteryUseCase.checkBatteryDecrease())
                R.drawable.ic_battery_danger_off
            else
                R.drawable.ic_battery_danger,
            type = OptimizingType.Battery,
        ),
    )

}

