package com.softcleean.fastcleaner.ui.boost

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.databinding.FragmentBoostBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import yinkio.android.customView.progressBar.ArcCircleProgressBar

@AndroidEntryPoint
class BoostFragment : Fragment(R.layout.fragment_boost) {

    private val binding: FragmentBoostBinding by viewBinding()

    private val viewModel: BoostViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getParams()
        initScreenStateObserver()
        setBtnListeners()
    }

    private fun initScreenStateObserver() {
        lifecycleScope.launch {
            viewModel.screenState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(state: BoostScreenState) {
        with(state) {
            renderBtnBoostingBattery(isRamBoosted)
            with(binding) {
                circularProgressRamPercent.progress = ramPercent.toFloat()
                circularProgressRamPercentDuplicate?.progress = ramPercent.toFloat()
                renderCircularProgress(isRamBoosted, circularProgressRamPercent)
                tvRamPercents.text = getString(R.string.value_percents, ramPercent)
                tvRamPercentsDuplicate?.text = getString(R.string.value_percents, ramPercent)
                circularProgressStoragePercent.progress = memoryPercent.toFloat()
                renderCircularProgress(isMemoryBoosted, circularProgressStoragePercent)
                tvStoragePercents.text = getString(R.string.value_percents, memoryPercent)
                tvUsedStorage.text = getString(R.string.gb, usedMemory)
                tvTotalStorage.text = getString(R.string.gb_fraction, totalMemory)
                tvFreeStorage.text = getString(R.string.gb, freeMemory)
                tvUsedRam.text = getString(R.string.gb, usedRam)
                tvTotalRam.text = getString(R.string.gb_fraction, totalRam)
                tvFreeRam.text = getString(R.string.gb, freeRam)
                tvDangerDescription?.isVisible = !isRamBoosted
            }
        }
    }

    private fun renderCircularProgress(isBoosted: Boolean, view: ArcCircleProgressBar) {
        view.indicator.color =
            if (isBoosted)
                resources.getColor(R.color.blue)
            else
                resources.getColor(R.color.orange)
    }

    private fun renderBtnBoostingBattery(isBoostedBattery: Boolean) {
        if (isBoostedBattery) {
            binding.btnBoostBattery?.isClickable = false
            binding.btnBoostBattery?.background = resources.getDrawable(R.drawable.bg_button_boost_off)
        } else {
            binding.btnBoostBattery?.isClickable = true
            binding.btnBoostBattery?.background = resources.getDrawable(R.drawable.bg_button_boost_on)
        }
    }

    private fun setBtnListeners() {
        binding.btnBoostBattery?.setOnClickListener {
            viewModel.boost()
            findNavController().navigate(R.id.action_homeFragment_to_boostOptimizingFragment)
        }
    }
}