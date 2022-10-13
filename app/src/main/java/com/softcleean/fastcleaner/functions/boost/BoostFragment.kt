package com.softcleean.fastcleaner.functions.boost

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
import com.softcleean.fastcleaner.utils.LOW_LEVEL
import com.softcleean.fastcleaner.utils.MEDIUM_LEVEL
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BoostFragment : Fragment(R.layout.fragment_boost) {

    private val binding: FragmentBoostBinding by viewBinding()

    private val viewModel: BoostViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initScreenStateObserver()
        viewModel.getParams()
        setBtnListeners()
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
            with(binding) {
                tvRamPercents.text = getString(R.string.value_percents, boostPercent)
                circularProgressRamPercent.progress = boostPercent.toFloat()
                renderCircularProgress(boostPercent)
                tvTotalRam.text = getString(R.string.gb_fraction, totalRam)
                tvUsedRam.text = getString(R.string.gb, usedRam)
                tvFreeRam.text = getString(R.string.gb, totalRam - usedRam)
                if (isBoosted) {
                    tvDangerDescriptionOff.isVisible = true
                    tvDangerDescription.isVisible = false
                    tvDangerDescriptionOff.text = getString(R.string.danger_boost_off, freeRam, overloadPercents)
                    ivRocketDanger.setImageDrawable(resources.getDrawable(R.drawable.ic_rocket_danger_off))
                    tvDangerReason.text = getString(R.string.boost_reason_danger_off)
                    tvTypeSaveBatteryTitle.text = getString(R.string.boost_danger_description_off)
                } else {
                    tvDangerDescriptionOff.isVisible = false
                    tvDangerDescription.isVisible = true
                    ivRocketDanger.setImageDrawable(resources.getDrawable(R.drawable.ic_rocket_danger))
                }
                renderBtnBoostingBattery(isBoosted)
            }
        }
    }

    private fun renderCircularProgress(percent: Int) {
        binding.circularProgressRamPercent.indicator.color =
            if (percent > LOW_LEVEL)
                resources.getColor(R.color.red)
            else if (percent > MEDIUM_LEVEL)
                resources.getColor(R.color.orange)
            else
                resources.getColor(R.color.blue)
    }

    private fun renderBtnBoostingBattery(isBoostedBattery: Boolean) {
        if (isBoostedBattery) {
            binding.btnBoostBattery.isClickable = false
            binding.btnBoostBattery.background = resources.getDrawable(R.drawable.bg_button_boost_off)
        } else {
            binding.btnBoostBattery.isClickable = true
            binding.btnBoostBattery.background = resources.getDrawable(R.drawable.bg_button_boost_on)
        }
    }

    private fun setBtnListeners() {
        binding.btnBoostBattery.setOnClickListener {
            viewModel.boost()
            findNavController().navigate(R.id.action_boostFragment_to_boostOptimizingFragment)
        }
        binding.btnGoBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}