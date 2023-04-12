package com.smart.cleaner.phone.clean.ui.boost

import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phone.clean.R
import com.smart.cleaner.phone.clean.databinding.FragmentBoostResultBinding
import com.smart.cleaner.phoneclean.ui_core.adapters.base.BaseFragmentResult
import com.smart.cleaner.phoneclean.ui_core.adapters.models.OptimizingType

class BoostResultFragment(
    override val typeResult: OptimizingType = OptimizingType.Boost
) : BaseFragmentResult(R.layout.fragment_boost_result) {

    private val binding: FragmentBoostResultBinding by viewBinding()

    override fun setRecyclerView(): RecyclerView = binding.recyclerView

}