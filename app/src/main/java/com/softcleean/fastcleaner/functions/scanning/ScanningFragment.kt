package com.softcleean.fastcleaner.functions.scanning

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.ads.library.SubscriptionProvider
import com.softcleean.fastcleaner.BuildConfig
import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.databinding.FragmentScanningBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import preloadInterstitial
import showInterstitial

class ScanningFragment : Fragment(R.layout.fragment_scanning) {

    private val binding: FragmentScanningBinding by viewBinding()

    private var isDoneOptimization = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startOptimization()
    }

    private fun startOptimization() {
        binding.ivBoosting.setImageDrawable(resources.getDrawable(R.drawable.ic_scanning_splash))
        lifecycleScope.launch {
            repeat(101) { percent ->
                val delay = if
                        (SubscriptionProvider.getInstance(requireActivity()).checkHasSubscription()) 30L
                                                                                                       else 80L
                delay(delay)
                setPercents(percent)
                if (percent == 100) {
                    optimizationIsDone()
                    delay(100)
                    showInterstitial(
                        onClosed = {
                            findNavController().navigate(R.id.action_scanningFragment_to_homeFragment)
                        }
                    )
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, true
        ) {
        }
    }

    private fun setPercents(percents: Int) {
        if (percents == 50)
            binding.tvProcess.text = getString(R.string.collect_data)
        if (percents == 75)
            binding.tvProcess.text = getString(R.string.check_device)
        binding.linearProgressIndicator.progress = percents
        binding.tvProgressPercents.text = getString(R.string.value_percents, percents)

    }

    private fun optimizationIsDone() {
        isDoneOptimization = true
        with(binding) {
            tvProgressPercents.text = getString(R.string.ready)
            tvProcess.isVisible = false
            ivBoosting.setImageDrawable(resources.getDrawable(R.drawable.ic_optimization_done))
        }
    }

    override fun onResume() {
        super.onResume()
        if (isDoneOptimization)
            findNavController().navigate(R.id.action_scanningFragment_to_homeFragment)
    }
}