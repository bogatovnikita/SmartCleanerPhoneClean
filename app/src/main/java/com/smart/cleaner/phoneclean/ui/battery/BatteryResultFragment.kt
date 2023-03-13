package com.smart.cleaner.phoneclean.ui.battery

import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.custom.ChoosingTypeBatteryBar
import com.smart.cleaner.phoneclean.ui.base.BaseFragmentResult
import com.smart.cleaner.phoneclean.ui.result.FunResult
import com.smart.cleaner.phoneclean.ui.result.ResultList
import com.smart.cleaner.phoneclean.utils.OptimizingType
import com.softcleean.fastcleaner.domain.battery.BatteryUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BatteryResultFragment: BaseFragmentResult(){

    @Inject
    lateinit var resultList: ResultList

    @Inject
    lateinit var batteryUseCase: BatteryUseCase

    override fun setListFun(): List<FunResult> = resultList.getList().filter { it.type != OptimizingType.Battery }

    override fun setFunName(): String = requireContext().getString(R.string.battery_title)

    override fun setMessageOfCompleteFun(): String = requireContext().getString(R.string.improve_working_time_by_percent, modePercentBoost())

    private fun modePercentBoost(): Int {
        return when (batteryUseCase.getBatteryType()) {
            ChoosingTypeBatteryBar.NORMAL -> 10
            ChoosingTypeBatteryBar.ULTRA -> 30
            ChoosingTypeBatteryBar.EXTRA -> 60
            else -> {
                10
            }
        }
    }

}