package com.smart.cleaner.phoneclean.ui.cooling

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.smart.cleaner.phoneclean.R
import com.smart.cleaner.phoneclean.databinding.FragmentCoolingBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CoolingFragment : Fragment(R.layout.fragment_cooling) {

    private val binding: FragmentCoolingBinding by viewBinding()

    private val viewModel: CoolingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTemperature()
        initScreenStateObserver()
        setBtnListeners()
    }

    private fun initScreenStateObserver() {
        lifecycleScope.launchWhenResumed {
            viewModel.screenState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(stateScreen: CoolingScreenState) {
        with(stateScreen) {
            renderBtnBoostingBattery(isCoolingDone)
            renderCircularProgress(isCoolingDone, temperature)
            with(binding) {
                tvDegree.text = getString(R.string.degree, temperature)
                if (isCoolingDone) {
                    tvDangerDescriptionOff.isVisible = true
                    tvDangerDescription.isVisible = false
                    ivDegree.setImageDrawable(resources.getDrawable(R.drawable.ic_thermometer_blue))
                } else {
                    tvDangerDescriptionOff.isVisible = false
                    tvDangerDescription.isVisible = true
                    ivDegree.setImageDrawable(resources.getDrawable(R.drawable.ic_thermometer_red))
                }
            }
        }
    }

    private fun renderCircularProgress(isBoosted: Boolean, temperature: Int) {
        binding.circularProgressTemperaturePercent.indicator.color =
            if (isBoosted)
                resources.getColor(R.color.blue)
            else
                resources.getColor(R.color.red)
        val percentOfTemperature = if (temperature / 0.7f <= 100) temperature / 0.7f else 100f
        binding.circularProgressTemperaturePercent.indicator.progress = percentOfTemperature
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
            viewModel.cooling()
            findNavController().navigate(R.id.action_coolingFragment_to_coolingOptimizingFragment)
        }
    }

}