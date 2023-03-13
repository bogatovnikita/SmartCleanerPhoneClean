package com.smart.cleaner.phoneclean.ui.boost

import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.ui.base.BaseFragmentResult
import com.smart.cleaner.phoneclean.ui.result.FunResult
import com.smart.cleaner.phoneclean.ui.result.ResultList
import com.smart.cleaner.phoneclean.utils.OptimizingType
import com.softcleean.fastcleaner.domain.boost.BoostUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BoostResultFragment: BaseFragmentResult() {

    @Inject
    lateinit var resultList: ResultList

    @Inject
     lateinit var boostUseCase: BoostUseCase

    override fun setListFun(): List<FunResult> = resultList.getList().filter { it.type != OptimizingType.Boost }

    override fun setFunName(): String = requireContext().getString(R.string.boosting)

    override fun setMessageOfCompleteFun(): String {
        val freePercents = boostUseCase.getOverloadedPercents()
        val freeRam = (boostUseCase.getTotalRam() / 1024.0 / 1024.0 / 1024.0) * freePercents / 100
        return requireContext().getString(R.string.danger_boost_off, freeRam, freePercents)
    }


}