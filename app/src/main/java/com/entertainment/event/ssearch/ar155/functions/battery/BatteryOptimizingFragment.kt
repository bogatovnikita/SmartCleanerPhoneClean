package com.entertainment.event.ssearch.ar155.functions.battery

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.ar155.R
import com.entertainment.event.ssearch.ar155.databinding.FragmentBatteryOptimizingBinding
import com.entertainment.event.ssearch.ar155.adapters.HintDecoration
import com.entertainment.event.ssearch.ar155.adapters.OptimizingRecyclerAdapter
import com.entertainment.event.ssearch.ar155.functions.custom.ChoosingTypeBatteryBar
import com.entertainment.event.ssearch.domain.battery.BatteryUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class BatteryOptimizingFragment : Fragment(R.layout.fragment_battery_optimizing) {

    private val binding: FragmentBatteryOptimizingBinding by viewBinding()

    private lateinit var adapter: OptimizingRecyclerAdapter

    private var listSize = 0
    private var listOptions = mutableListOf<String>()

    @Inject
    lateinit var batteryUseCase: BatteryUseCase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        setArrayOptionBoosting()
        startOptimization()
    }

    private fun startOptimization() {
        binding.ivBoosting.setImageDrawable(resources.getDrawable(R.drawable.ic_optimization_battery))
        lifecycleScope.launch {
            repeat(101) { percent ->
                delay(80)
                binding.tvProgressPercents.text = getString(R.string.value_percents, percent)
                binding.linearProgressIndicator.progress = percent
                withContext(Dispatchers.Main) {
                    updateList(progress = percent)
                }
                if (percent == 100) {
                    optimizationIsDone()
                    delay(500)
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun optimizationIsDone() {
        with(binding) {
            tvProgressPercents.text = getString(R.string.ready)
            recyclerView.isVisible = false
            ivBoosting.setImageDrawable(resources.getDrawable(R.drawable.ic_optimization_done))
        }
    }

    private fun setArrayOptionBoosting() {
        when (batteryUseCase.getBatteryType()) {
            ChoosingTypeBatteryBar.NORMAL ->  {
                val list = resources.getStringArray(R.array.battery_normal).toList()
                adapter.submitList(list)
                listSize = list.size
                listOptions = list.toMutableList()
            }
            ChoosingTypeBatteryBar.ULTRA -> {
                val list = resources.getStringArray(R.array.battery_ultra).toList()
                adapter.submitList(list)
                listSize = list.size
                listOptions = list.toMutableList()
            }
            ChoosingTypeBatteryBar.EXTRA -> {
                val list = resources.getStringArray(R.array.battery_extra).toList()
                adapter.submitList(list)
                listSize = list.size
                listOptions = list.toMutableList()
            }
        }
    }

    private fun initAdapter() {
        adapter = OptimizingRecyclerAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.addItemDecoration(HintDecoration())
    }

    private fun updateList(
        valueToDelete: Int = 100 / listSize,
        progress: Int,
    ) {
        if (progress != 0 && progress % valueToDelete == 0 && listOptions.isNotEmpty()) {
            val list = mutableListOf<String>()
            listOptions.forEachIndexed { index, s ->
                if (index != 0) list.add(s)
            }
            listOptions = list
            adapter.submitList(list)
        }
    }
}