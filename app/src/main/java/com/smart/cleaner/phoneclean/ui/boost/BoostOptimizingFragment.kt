package com.smart.cleaner.phoneclean.ui.boost

import com.smart.cleaner.phoneclean.BuildConfig
import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.ui.base.BaseOptimizingFragment

class BoostOptimizingFragment(
    override val nextScreenId: Int = R.id.action_boostOptimizingFragment_to_boostResultFragment
) : BaseOptimizingFragment() {

    override fun startOptimizationFun() {}

    override fun getArrayOptimization() {
        listOptions = resources.getStringArray(R.array.optimization).toMutableList()
    }

    override fun getFunName(): String = requireContext().getString(R.string.boosting)

}