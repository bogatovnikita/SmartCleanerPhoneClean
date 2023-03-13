package com.smart.cleaner.phoneclean.ui.cooling

import com.smart.cleaner.phoneclean.BuildConfig
import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.ui.base.BaseOptimizingFragment

class CoolingOptimizingFragment(
    override val keyInter: String = BuildConfig.ADMOB_INTERSTITIAL,
    override val nextScreenId: Int = R.id.action_coolingOptimizingFragment_to_coolingResultFragment
) : BaseOptimizingFragment() {

    override fun setArrayOptimization() {
        listOptions = resources.getStringArray(R.array.optimization).toMutableList()
    }

    override fun startOptimizationFun() {}

    override fun setFunName(): String = requireContext().getString(R.string.cooling_process)

}