package com.entertainment.event.ssearch.ar155.function.boost

import android.os.Bundle
import android.util.Log
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
        goToBoostingListener()
    }

    private fun initScreenStateObserver() {
        lifecycleScope.launch {
            viewModel.screenState.collect { state ->
                renderState(state)
                Log.e("!!!", state.toString())
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
                } else {
                    tvDangerDescriptionOff.isVisible = false
                    tvDangerDescription.isVisible = true
                }
            }
        }
    }
    private fun goToBoostingListener() {
        binding.btnBoostBattery.setOnClickListener {
            viewModel.boost()
            findNavController().navigate(R.id.action_boostFragment_to_boostOptimizingFragment)
        }
    }
}