package com.softcleean.fastcleaner.ui.boost

import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.ui.base.BaseOptimizingFragment

class BoostOptimizingFragment(
//    override val keyInter: String = "",
    override val nextScreenId: Int = R.id.action_boostOptimizingFragment_to_boostResultFragment
) : BaseOptimizingFragment() {

    override fun startOptimizationFun() {}

    override fun setArrayOptimization() {
        listOptions = resources.getStringArray(R.array.optimization).toMutableList()
    }

    override fun setFunName(): String = requireContext().getString(R.string.boosting)

}