package com.entertainment.event.ssearch.ar155.functions.clean

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.ar155.R
import com.entertainment.event.ssearch.ar155.adapters.CleanAdapter
import com.entertainment.event.ssearch.ar155.databinding.FragmentCleanBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import yinkio.android.customView.progressBar.ArcCircleProgressBar

@AndroidEntryPoint
class CleanFragment : Fragment(R.layout.fragment_clean) {

    private val binding: FragmentCleanBinding by viewBinding()

    private val viewModel: CleanViewModel by viewModels()

    private lateinit var adapter: CleanAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getGarbageInfo()
        initAdapter()
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

    private fun renderState(stateScreen: CleanStateScreen) {
        with(stateScreen) {
            adapter.submitList(garbageList)
            renderBtnClean(isCleared)
            with(binding) {
                if (isCleared) {
                    groupCoolingDone.isVisible = true
                    groupNeedCooling.isVisible = false
                    circularProgressStoragePercent.progress = memoryPercent.toFloat()
                    renderCircularProgress(memoryPercent)
                    tvStoragePercents.text = getString(R.string.value_percents, memoryPercent)
                    tvUsedStorage.text = getString(R.string.gb, usedMemory)
                    tvTotalStorage.text = getString(R.string.gb_fraction, totalMemory)
                    tvFreeStorage.text = getString(R.string.gb, freeMemory)
                } else {
                    groupCoolingDone.isVisible = false
                    groupNeedCooling.isVisible = true
                    tvDegree.text = getString(R.string.mb, totalGarbageSize)
                }
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

    private fun renderCircularProgress(percent: Int) {
        binding.circularProgressStoragePercent.indicator.color =
            if (percent > 85)
                resources.getColor(R.color.red)
            else if (percent > 60)
                resources.getColor(R.color.orange)
            else
                resources.getColor(R.color.blue)
    }

    private fun setBtnListeners() {
        binding.btnClean.setOnClickListener {
            viewModel.cleanGarbage()
            findNavController().navigate(R.id.action_cleanFragment_to_cleanOptimizingFragment)
        }
        binding.btnGoBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initAdapter() {
        adapter = CleanAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}
