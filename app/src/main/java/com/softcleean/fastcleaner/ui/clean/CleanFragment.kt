package com.softcleean.fastcleaner.ui.clean

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.databinding.FragmentCleanBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CleanFragment : Fragment(R.layout.fragment_clean) {

    private val binding: FragmentCleanBinding by viewBinding()

    private val viewModel: CleanViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getGarbageInfo()
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

    private fun renderState(stateScreen: CleanStateScreen) {
        with(stateScreen) {
            renderBtnClean(isCleared)
            renderCircularProgress(isCleared)
            with(binding) {
                if (isCleared) {
                    groupCleaningDone.isVisible = true
                    groupNeedCleaning.isVisible = false
                    tvFreeMemory.text = getString(R.string.free_memory_size, freeMemory)
                } else {
                    groupCleaningDone.isVisible = false
                    groupNeedCleaning.isVisible = true
                }
                tvDegree.text = getString(R.string.mb, totalGarbageSize)
                circularProgressStoragePercent.progress = memoryPercent.toFloat()
            }
        }
    }

    private fun renderBtnClean(isBoostedBattery: Boolean) {
        if (isBoostedBattery) {
            binding.btnClean.isClickable = false
            binding.btnClean.background = resources.getDrawable(R.drawable.bg_button_boost_off)
        } else {
            binding.btnClean.isClickable = true
            binding.btnClean.background = resources.getDrawable(R.drawable.bg_button_boost_on)
        }
    }

    private fun renderCircularProgress(isBoosted: Boolean) {
        binding.circularProgressStoragePercent.indicator.color =
            if (isBoosted)
                resources.getColor(R.color.blue)
            else
                resources.getColor(R.color.red)
    }

    private fun setBtnListeners() {
        binding.btnClean.setOnClickListener {
            viewModel.cleanGarbage()
            findNavController().navigate(R.id.action_cleanFragment_to_cleanOptimizingFragment)
        }
    }

}
