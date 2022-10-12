package com.entertainment.event.ssearch.ar155.functions.clean

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.entertainment.event.ssearch.ar155.R
import com.entertainment.event.ssearch.ar155.adapters.ResultFunAdapter
import com.entertainment.event.ssearch.ar155.databinding.FragmentCleanResultBinding
import com.entertainment.event.ssearch.ar155.functions.result.FunResult
import com.entertainment.event.ssearch.ar155.functions.result.ResultList
import com.entertainment.event.ssearch.ar155.utils.OptimizingType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CleanResultFragment : Fragment(R.layout.fragment_clean_result) {

    private val binding: FragmentCleanResultBinding by viewBinding()

    private val viewModel: CleanViewModel by viewModels()

    private lateinit var adapter: ResultFunAdapter

    @Inject
    lateinit var resultList: ResultList

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
            adapter.submitList(resultList.getList().filter {
                OptimizingType.Clean != it.type
            })
            with(binding) {
                circularProgressStoragePercent.progress = memoryPercent.toFloat()
                renderCircularProgress(memoryPercent)
                tvStoragePercents.text = getString(R.string.value_percents, memoryPercent)
                tvUsedStorage.text = getString(R.string.gb, usedMemory)
                tvTotalStorage.text = getString(R.string.gb_fraction, totalMemory)
                tvFreeStorage.text = getString(R.string.gb, freeMemory)
            }
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
            findNavController().navigate(R.id.action_to_homeFragment)
        }
        binding.btnGoBack.setOnClickListener {
            findNavController().navigate(R.id.action_to_homeFragment)
        }
    }

    private fun initAdapter() {
        adapter = ResultFunAdapter(object : ResultFunAdapter.ClickOnFunResultListener {
            override fun onFunClick(item: FunResult) {
                when (item.type) {
                    OptimizingType.Boost -> findNavController().navigate(R.id.action_to_boostFragment)
                    OptimizingType.Clean -> findNavController().navigate(R.id.action_to_cleanFragment)
                    OptimizingType.Cooling -> findNavController().navigate(R.id.action_to_coolingFragment)
                    OptimizingType.Battery -> findNavController().navigate(R.id.action_to_batteryFragment)
                }
            }

        })
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}