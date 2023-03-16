package com.smart.cleaner.phoneclean.ui.boost

import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.databinding.FragmentBoostResultBinding
import com.smart.cleaner.phoneclean.ui.base.BaseFragmentResult
import com.smart.cleaner.phoneclean.ui.result.FunResult
import com.smart.cleaner.phoneclean.ui.result.ResultList
import com.smart.cleaner.phoneclean.utils.OptimizingType
import com.softcleean.fastcleaner.domain.boost.BoostUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BoostResultFragment: BaseFragmentResult(R.layout.fragment_boost_result) {

    private val binding: FragmentBoostResultBinding by viewBinding()

    @Inject
    lateinit var resultList: ResultList

    @Inject
     lateinit var boostUseCase: BoostUseCase

    override fun setListFun(): List<FunResult> = resultList.getList().filter { it.type != OptimizingType.Boost }

    override fun setRecyclerView(): RecyclerView = binding.recyclerView


}