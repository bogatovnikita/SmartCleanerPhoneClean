package com.softcleean.fastcleaner.ui.boost

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.databinding.FragmentBoostResultBinding
import com.softcleean.fastcleaner.ui.result.BaseResultFragment
import com.softcleean.fastcleaner.ui.result.ResultList
import com.softcleean.fastcleaner.utils.OptimizingType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BoostResultFragment :
    BaseResultFragment<FragmentBoostResultBinding>(FragmentBoostResultBinding::inflate) {

    private val viewModel: BoostViewModel by viewModels()

    @Inject
    lateinit var resultList: ResultList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter(recyclerView = binding.recyclerView)
        viewModel.getParams()
        initScreenStateObserver()
        setBtnListeners(btnGoBack = binding.btnGoBack, btnGoMain = binding.btnGoMain)
    }

    private fun initScreenStateObserver() {
        lifecycleScope.launchWhenResumed {
            viewModel.screenState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(stateScreen: BoostStateScreen) {
        with(stateScreen) {
            adapter.submitList(resultList.getList().filter {
                OptimizingType.Boost != it.type
            })
            with(binding) {
                tvRamPercents.text = getString(R.string.value_percents, boostPercent)
                circularProgressRamPercent.progress = boostPercent.toFloat()
                renderCircularProgress(isBoosted)
                tvTotalRam.text = getString(R.string.gb_fraction, totalRam)
                tvUsedRam.text = getString(R.string.gb, usedRam)
                tvFreeRam.text = getString(R.string.gb, totalRam - usedRam)
                tvDangerDescriptionOff.text = getString(R.string.danger_boost_off, freeRam, overloadPercents)
            }
        }
    }

    private fun renderCircularProgress(isBoosted: Boolean) {
        binding.circularProgressRamPercent.indicator.color =
            if (isBoosted)
                resources.getColor(R.color.blue)
            else
                resources.getColor(R.color.orange)
    }
}