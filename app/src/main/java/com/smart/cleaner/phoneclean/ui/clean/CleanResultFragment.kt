package com.smart.cleaner.phoneclean.ui.clean

import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.ui.base.BaseFragmentResult
import com.smart.cleaner.phoneclean.ui.result.FunResult
import com.smart.cleaner.phoneclean.ui.result.ResultList
import com.smart.cleaner.phoneclean.utils.OptimizingType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CleanResultFragment: BaseFragmentResult(){
    @Inject
    lateinit var resultList: ResultList

    override fun setListFun(): List<FunResult> = resultList.getList().filter { it.type != OptimizingType.Clean }

    override fun setFunName(): String = requireContext().getString(R.string.cleaning)

    override fun setMessageOfCompleteFun(): String = requireContext().getString(R.string.clean_done)
}