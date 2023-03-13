package com.smart.cleaner.phoneclean.ui.cooling

import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.ui.base.BaseFragmentResult
import com.smart.cleaner.phoneclean.ui.result.FunResult
import com.smart.cleaner.phoneclean.ui.result.ResultList
import com.smart.cleaner.phoneclean.utils.OptimizingType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CoolingResultFragment: BaseFragmentResult(){

    @Inject
    lateinit var resultList: ResultList

    override fun setListFun(): List<FunResult> = resultList.getList().filter { it.type != OptimizingType.Cooling }

    override fun setFunName(): String = requireContext().getString(R.string.cooling_process)

    override fun setMessageOfCompleteFun(): String = requireContext().getString(R.string.coolling_done)

}