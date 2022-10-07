package com.entertainment.event.ssearch.ar155.functions.boost

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.ar155.R
import com.entertainment.event.ssearch.ar155.databinding.FragmentBoostBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BoostFragment : Fragment(R.layout.fragment_boost) {

    private val binding: FragmentBoostBinding by viewBinding()

    private val viewModel: BoostViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initScreenStateObserver()
        viewModel.getParams()
        goToBoostingListener()
    }

    private fun initScreenStateObserver() {
        lifecycleScope.launch {
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
                tvTotalRam.text = getString(R.string.gb_fraction, totalRam)
                tvUsedRam.text = getString(R.string.gb, usedRam)
                tvFreeRam.text = getString(R.string.gb, totalRam - usedRam)
                if (isBoosted) {
                    tvDangerDescriptionOff.isVisible = true
                    tvDangerDescription.isVisible = false
                    tvDangerDescriptionOff.text = getString(R.string.danger_boost_off, freedRam, overloadPercents)
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

    private fun renderBtnBoostingBattery(isBoostedBattery: Boolean) {
        if (isBoostedBattery) {
            binding.btnBoostBattery.isClickable = false
            binding.btnBoostBattery.background = resources.getDrawable(R.drawable.bg_button_boost_off)
        } else {
            binding.btnBoostBattery.isClickable = true
            binding.btnBoostBattery.background = resources.getDrawable(R.drawable.bg_button_boost_on)
        }
    }

    private fun goToBoostingListener() {
        binding.btnBoostBattery.setOnClickListener {
            viewModel.boost()
            findNavController().navigate(R.id.action_boostFragment_to_boostOptimizingFragment)
        }
    }
}