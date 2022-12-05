package com.softcleean.fastcleaner.ui.cooling

import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.ui.base.BaseOptimizingFragment


class CoolingOptimizingFragment(
//    override val keyInter: String = BuildConfig.ADMOB_INTERSTITIAL4,
    override val nextScreenId: Int = R.id.action_coolingOptimizingFragment_to_coolingResultFragment
) : BaseOptimizingFragment() {

    override fun setArrayOptimization() {
        listOptions = resources.getStringArray(R.array.optimization).toMutableList()
    }

    override fun startOptimizationFun() {}

    override fun setFunName(): String = requireContext().getString(R.string.cooling_process)

}