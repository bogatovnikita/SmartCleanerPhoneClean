package com.entertainment.event.ssearch.ar155.functions.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.ar155.R
import com.entertainment.event.ssearch.ar155.adapters.HomeFunAdapter
import com.entertainment.event.ssearch.ar155.adapters.ItemHomeFun
import com.entertainment.event.ssearch.ar155.adapters.ItemHomeFun.Companion.BATTERY
import com.entertainment.event.ssearch.ar155.adapters.ItemHomeFun.Companion.BOOST
import com.entertainment.event.ssearch.ar155.adapters.ItemHomeFun.Companion.CLEAN
import com.entertainment.event.ssearch.ar155.adapters.ItemHomeFun.Companion.COOLING
import com.entertainment.event.ssearch.ar155.databinding.FragmentHomeBinding
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
        Log.e("!!!", state.toString())
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
                    BOOST -> findNavController().navigate(R.id.action_homeFragment_to_boostFragment)
                    CLEAN -> findNavController().navigate(R.id.action_homeFragment_to_cleanFragment)
                    COOLING -> findNavController().navigate(R.id.action_homeFragment_to_coolingFragment)
                    BATTERY -> findNavController().navigate(R.id.action_homeFragment_to_batteryFragment)
                }
            }
        })
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
    }
}