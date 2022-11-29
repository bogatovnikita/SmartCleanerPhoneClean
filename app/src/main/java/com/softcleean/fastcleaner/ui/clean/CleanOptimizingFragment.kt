package com.softcleean.fastcleaner.ui.clean

import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.domain.clean.CleanUseCase
import com.softcleean.fastcleaner.ui.base.BaseOptimizingFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class CleanOptimizingFragment(
//    override val keyInter: String = "",
    override val nextScreenId: Int = R.id.action_cleanOptimizingFragment_to_cleanResultFragment
) : BaseOptimizingFragment() {

    @Inject
    lateinit var cleanUseCase: CleanUseCase

    override fun startOptimizationFun() {}

    override fun setArrayOptimization() {
        val randomType = Random.nextInt(1, 3)
        listOptions = cleanUseCase.getFolders().filter { path ->
            when (randomType) {
                1 -> path.length % 2 == 0
                2 -> path.length % 2 != 0
                3 -> path.length % 4 == 0
                else -> path.length % 3 == 0
            }
        }.toMutableList()
    }

}