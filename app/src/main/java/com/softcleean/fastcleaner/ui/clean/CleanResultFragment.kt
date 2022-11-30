package com.softcleean.fastcleaner.ui.clean

import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.ui.base.BaseFragmentResult
import com.softcleean.fastcleaner.ui.result.FunResult
import com.softcleean.fastcleaner.ui.result.ResultList
import com.softcleean.fastcleaner.utils.OptimizingType
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