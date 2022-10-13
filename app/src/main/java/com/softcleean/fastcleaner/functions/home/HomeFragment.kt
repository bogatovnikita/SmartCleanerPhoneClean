package com.softcleean.fastcleaner.functions.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.softcleean.fastcleaner.R
import com.softcleean.fastcleaner.adapters.HomeFunAdapter
import com.softcleean.fastcleaner.adapters.ItemHomeFun
import com.softcleean.fastcleaner.databinding.FragmentHomeBinding
import com.softcleean.fastcleaner.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import yinkio.android.customView.progressBar.ArcCircleProgressBar

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var adapter: HomeFunAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getParams()
        initAdapter()
        initScreenStateObserver()
    }

    private fun initScreenStateObserver() {
        lifecycleScope.launch {
            viewModel.screenState.collect { state ->
                renderState(state)
            }
        }
    }

    private fun renderState(state: HomeScreenState) {
        with(state) {
            adapter.submitList(funList)
            with(binding) {
                circularProgressRamPercent.progress = ramPercent.toFloat()
                renderCircularProgress(ramPercent, circularProgressRamPercent)
                tvRamPercents.text = getString(R.string.value_percents, ramPercent)
                circularProgressStoragePercent.progress = memoryPercent.toFloat()
                renderCircularProgress(memoryPercent, circularProgressStoragePercent)
                tvStoragePercents.text = getString(R.string.value_percents, memoryPercent)
                tvUsedStorage.text = getString(R.string.gb, usedMemory)
                tvTotalStorage.text = getString(R.string.gb_fraction, totalMemory)
                tvFreeStorage.text = getString(R.string.gb, freeMemory)
                tvUsedRam.text = getString(R.string.gb, usedRam)
                tvTotalRam.text = getString(R.string.gb_fraction, totalRam)
                tvFreeRam.text = getString(R.string.gb, freeRam)
            }
        }
    }

    private fun renderCircularProgress(percent: Int, view: ArcCircleProgressBar) {
        view.indicator.color =
            if (percent > 85)
                resources.getColor(R.color.red)
            else if (percent > 60)
                resources.getColor(R.color.orange)
            else
                resources.getColor(R.color.blue)
    }

    private fun initAdapter() {
        adapter = HomeFunAdapter(object: HomeFunAdapter.ClickOnFunListener {
            override fun onFunClick(item: ItemHomeFun) {
                when(item.type) {
                    OptimizingType.Boost -> findNavController().navigate(R.id.action_to_boostFragment)
                    OptimizingType.Clean -> findNavController().navigate(R.id.action_to_cleanFragment)
                    OptimizingType.Cooling -> findNavController().navigate(R.id.action_to_coolingFragment)
                    OptimizingType.Battery -> findNavController().navigate(R.id.action_to_batteryFragment)
                }
            }
        })
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
    }
}