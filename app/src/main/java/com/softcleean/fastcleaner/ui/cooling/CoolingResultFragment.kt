package com.softcleean.fastcleaner.ui.cooling

import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.ui.base.BaseFragmentResult
import com.softcleean.fastcleaner.ui.result.FunResult
import com.softcleean.fastcleaner.ui.result.ResultList
import com.softcleean.fastcleaner.utils.OptimizingType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CoolingResultFragment: BaseFragmentResult(){

    @Inject
    lateinit var resultList: ResultList

    override fun setListFun(): List<FunResult> = resultList.getList().filter { it.type != OptimizingType.Cooling }

    override fun setFunName(): String = requireContext().getString(R.string.cooling)

    override fun setMessageOfCompleteFun(): String = requireContext().getString(R.string.coolling_done)

}