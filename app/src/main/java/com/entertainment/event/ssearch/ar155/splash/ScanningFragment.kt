package com.entertainment.event.ssearch.ar155.splash

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.ar155.R
import com.entertainment.event.ssearch.ar155.databinding.FragmentScanningBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ScanningFragment : Fragment(R.layout.fragment_scanning) {

    private val binding: FragmentScanningBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startOptimization()
    }

    private fun startOptimization() {
        binding.ivBoosting.setImageDrawable(resources.getDrawable(R.drawable.ic_optimization_clean))
        lifecycleScope.launch {
            repeat(101) { percent ->
                delay(80)
                setPercents(percent)
                if (percent == 100) {
                    optimizationIsDone()
                    delay(500)
//                    findNavController().popBackStack()
                }
            }
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
        with(binding) {
            tvProgressPercents.text = getString(R.string.ready)
            tvProcess.isVisible = false
            ivBoosting.setImageDrawable(resources.getDrawable(R.drawable.ic_optimization_done))
        }
    }
}