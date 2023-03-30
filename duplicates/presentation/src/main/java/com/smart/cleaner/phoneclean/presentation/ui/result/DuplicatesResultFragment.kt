package com.smart.cleaner.phoneclean.presentation.ui.result

import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phoneclean.presentation.R
import com.smart.cleaner.phoneclean.presentation.databinding.FragmentDuplicatesResultBinding
import com.smart.cleaner.phoneclean.ui_core.adapters.base.BaseFragmentResult
import com.smart.cleaner.phoneclean.ui_core.adapters.models.OptimizingType

class DuplicatesResultFragment(
    override val typeResult: OptimizingType = OptimizingType.Duplicates
) : BaseFragmentResult(R.layout.fragment_duplicates_result) {

    private val binding: FragmentDuplicatesResultBinding by viewBinding()

    override fun setRecyclerView(): RecyclerView = binding.recyclerView

}