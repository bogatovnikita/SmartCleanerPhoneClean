package com.smart.cleaner.phoneclean.ui.boost

import androidx.lifecycle.lifecycleScope
import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.ui_core.adapters.base.BaseOptimizingFragment
import com.smart.cleaner.phoneclean.ui_core.adapters.models.BoostOptimizingItem
import com.smart.cleaner.phoneclean.ui_core.adapters.models.OptimizingItem
import com.softcleean.fastcleaner.domain.boost.BoostUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BoostOptimizingFragment(
    override val nextScreenId: Int = R.id.action_boostOptimizingFragment_to_boostResultFragment
) : BaseOptimizingFragment() {

    @Inject
    lateinit var boostUseCase: BoostUseCase

    override fun startOptimizationFun() {
        lifecycleScope.launch(Dispatchers.IO) {
            boostUseCase.killBackgroundProcessSystemApps()
        }
        lifecycleScope.launch(Dispatchers.IO) {
            boostUseCase.killBackgroundProcessInstalledApps()
        }
        boostUseCase.saveTimeRamBoost()
    }

    override fun getArrayOptimization() {
        listOptions = getBoostOptimizingItem()
    }

    override fun getFunName(): String = requireContext().getString(R.string.optimization)


    private fun getBoostOptimizingItem(): MutableList<OptimizingItem> {
        return resources.getStringArray(R.array.optimization).map {
            BoostOptimizingItem(
                name = it,
                icon = R.drawable.ic_duplicate_danger
            )
        }.toMutableList()
    }

}