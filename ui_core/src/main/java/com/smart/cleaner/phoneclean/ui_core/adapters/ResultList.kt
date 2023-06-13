package com.smart.cleaner.phoneclean.ui_core.adapters

import android.content.Context
import com.smart.cleaner.phoneclean.settings.Settings
import com.smart.cleaner.phoneclean.ui_core.R
import com.smart.cleaner.phoneclean.ui_core.adapters.models.FunResult
import com.smart.cleaner.phoneclean.ui_core.adapters.models.OptimizingType

class ResultList constructor(
    private val context: Context,
) {

    private val settings: Settings by lazy { Settings(context) }

    fun getList(typeResult: OptimizingType): List<FunResult> {
        return when (typeResult) {
            OptimizingType.Boost -> getList().filter { it.type != OptimizingType.Boost }
            OptimizingType.Clean -> getList().filter { it.type != OptimizingType.Clean }
            OptimizingType.Duplicates -> getList().filter { it.type != OptimizingType.Duplicates }
            OptimizingType.Battery -> getList().filter { it.type != OptimizingType.Battery }
        }
    }

    private fun getList(): List<FunResult> = listOf(
        FunResult(
            isOptimized = settings.isRamBoosted(),
            funName = general.R.string.boosting,
            funDangerDescription = general.R.string.boost_danger_desc,
            icon = general.R.drawable.ic_boost_danger,
            type = OptimizingType.Boost,
        ),
        FunResult(
            isOptimized = settings.isBatteryBoosted(),
            funName = general.R.string.battery_title,
            funDangerDescription = general.R.string.battery_danger_desc,
            icon = general.R.drawable.ic_battery_danger,
            type = OptimizingType.Battery,
        ),
        FunResult(
            isOptimized = settings.isDuplicateDelete(),
            funName = general.R.string.duplicate_title,
            funDangerDescription = general.R.string.duplicates_danger_desc,
            icon = general.R.drawable.ic_duplicate_danger,
            type = OptimizingType.Duplicates,
        ),
        FunResult(
            isOptimized = settings.isJunkCleanBoosted(),
            funName = general.R.string.junk_clean_title,
            funDangerDescription = general.R.string.junk_clean_desc,
            icon = general.R.drawable.ic_clean_danger,
            type = OptimizingType.Clean
        )
    )

}

