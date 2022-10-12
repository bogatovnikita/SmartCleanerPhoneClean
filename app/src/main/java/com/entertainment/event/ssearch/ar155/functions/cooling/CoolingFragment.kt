package com.entertainment.event.ssearch.ar155.functions.cooling

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.ar155.R
import com.entertainment.event.ssearch.ar155.databinding.FragmentCoolingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


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
        lifecycleScope.launch {
            viewModel.screenState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(stateScreen: CoolingScreenState) {
        with(stateScreen) {
            with(binding) {
                tvDegree.text = getString(R.string.degree, temperature)
                if (isCoolingDone) {
                    tvDangerDescriptionOff.isVisible = true
                    tvDangerDescription.isVisible = false
                    ivRocketDanger.setImageDrawable(resources.getDrawable(R.drawable.ic_cooling_danger_off))
                    ivDegree.setImageDrawable(resources.getDrawable(R.drawable.ic_thermometer_blue))
                    tvDangerReason.text = getString(R.string.cooling_done_description)
                } else {
                    tvDangerDescriptionOff.isVisible = false
                    tvDangerDescription.isVisible = true
                    ivRocketDanger.setImageDrawable(resources.getDrawable(R.drawable.ic_cooling_danger))
                    ivDegree.setImageDrawable(resources.getDrawable(R.drawable.ic_thermometer_red))
                }
                renderBtnBoostingBattery(isCoolingDone)
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

    private fun setBtnListeners() {
        binding.btnBoostBattery.setOnClickListener {
            viewModel.cooling()
            findNavController().navigate(R.id.action_coolingFragment_to_coolingOptimizingFragment)
        }
        binding.btnGoBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}