package com.softcleean.fastcleaner.ui.battery

import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.custom.ChoosingTypeBatteryBar
import com.softcleean.fastcleaner.domain.battery.BatteryUseCase
import com.softcleean.fastcleaner.ui.base.BaseFragmentResult
import com.softcleean.fastcleaner.ui.result.FunResult
import com.softcleean.fastcleaner.ui.result.ResultList
import com.softcleean.fastcleaner.utils.OptimizingType
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