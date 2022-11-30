package com.softcleean.fastcleaner.ui.boost

import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.domain.boost.BoostUseCase
import com.softcleean.fastcleaner.ui.base.BaseFragmentResult
import com.softcleean.fastcleaner.ui.result.FunResult
import com.softcleean.fastcleaner.ui.result.ResultList
import com.softcleean.fastcleaner.utils.OptimizingType
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